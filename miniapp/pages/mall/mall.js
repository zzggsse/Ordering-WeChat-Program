const api = require('../../utils/api')

Page({
  data: {
    points: 0,
    balance: 0,
    level: '',
    amount: '',
    savingRecharge: false,
    coupons: [],
    myCoupons: []
  },

  onShow() {
    if (!this.ensureLogin()) return
    this.load()
  },

  ensureLogin() {
    if (!wx.getStorageSync('token')) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      wx.navigateTo({ url: '/pages/login/login' })
      return false
    }
    return true
  },

  load() {
    api.me()
      .then((u) => {
        this.setData({
          points: u.points || 0,
          balance: u.balance != null ? u.balance : 0,
          level: u.level || '普通会员'
        })
      })
      .catch(() => {})
    api.coupons().then((list) => this.setData({ coupons: list || [] })).catch(() => {})
    api.myCoupons().then((list) => this.setData({ myCoupons: list || [] })).catch(() => {})
  },

  onAmount(e) { this.setData({ amount: e.detail.value }) },

  recharge() {
    if (!this.ensureLogin()) return
    const amount = parseFloat(this.data.amount)
    if (!amount || amount <= 0) {
      wx.showToast({ title: '请输入充值金额', icon: 'none' })
      return
    }
    if (this.data.savingRecharge) return
    this.setData({ savingRecharge: true })
    wx.showLoading({ title: '充值中...' })
    api.recharge(amount)
      .then((u) => {
        wx.hideLoading()
        this.setData({ savingRecharge: false, amount: '' })
        wx.showToast({ title: '充值成功，送 ' + (u.points - this.data.points) + ' 积分', icon: 'none' })
        this.load()
      })
      .catch((err) => {
        wx.hideLoading()
        this.setData({ savingRecharge: false })
        wx.showToast({ title: err.message || '充值失败', icon: 'none' })
      })
  },

  redeem(e) {
    const { id, cost, name } = e.currentTarget.dataset
    wx.showModal({
      title: '积分兑换',
      content: `用 ${cost} 积分兑换「${name}」，确定吗？`,
      success: (r) => {
        if (!r.confirm) return
        api.redeem(id)
          .then(() => {
            wx.showToast({ title: '兑换成功', icon: 'success' })
            this.load()
          })
          .catch((err) => wx.showToast({ title: err.message || '兑换失败', icon: 'none' }))
      }
    })
  }
})
