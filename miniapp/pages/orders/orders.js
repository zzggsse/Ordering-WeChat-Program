const api = require('../../utils/api')

const TIP_MAP = {
  UNPAID: '待支付',
  PAID: '待接单',
  MAKING: '门店正在制作',
  READY: '已出杯，叫号中',
  DONE: '已取餐',
  CANCELED: '已取消',
  REFUNDING: '退款处理中',
  REFUNDED: '已退款'
}

Page({
  data: {
    active: 'today',
    tabs: [
      { key: 'today', label: '今日订单' },
      { key: 'history', label: '历史订单' }
    ],
    orders: [],
    loggedIn: false,
    loading: false
  },

  onShow() {
    this.loadOrders()
  },

  switchTab(e) {
    const key = e.currentTarget.dataset.key
    if (key === this.data.active) return
    this.setData({ active: key })
    this.loadOrders()
  },

  loadOrders() {
    const token = wx.getStorageSync('token')
    if (!token) {
      this.setData({ orders: [], loggedIn: false })
      return
    }
    this.setData({ loading: true, loggedIn: true })
    api.mineOrders(this.data.active)
      .then((d) => {
        const records = (d && d.records) ? d.records : []
        this.setData({ orders: records.map((r) => this.toDisplay(r)) })
      })
      .catch((err) => {
        this.setData({ orders: [] })
        if (err.message === '请先登录') {
          this.setData({ loggedIn: false })
        } else {
          wx.showToast({ title: err.message, icon: 'none' })
        }
      })
      .finally(() => this.setData({ loading: false }))
  },

  toDisplay(o) {
    const items = (o.items || [])
      .map((i) => (i.name + ' ×' + i.quantity))
      .join(' · ')
    return {
      id: o.id,
      no: o.orderNo,
      time: o.createdAt || '',
      items: items || '—',
      amount: o.totalAmount,
      queue: o.pickupNo,
      status: (o.status || '').toLowerCase(),
      statusText: o.statusLabel || o.status,
      tip: TIP_MAP[o.status]
    }
  },

  goLogin() { wx.navigateTo({ url: '/pages/login/login' }) },

  goDetail(e) {
    const id = e.currentTarget.dataset.id
    wx.navigateTo({ url: '/pages/order/detail?id=' + (id || '') })
  },

  receive(e) {
    const id = e.currentTarget.dataset.id
    wx.showLoading({ title: '确认中...' })
    api.receiveOrder(id)
      .then(() => {
        wx.hideLoading()
        wx.showToast({ title: '已取餐，祝您愉快 🧋', icon: 'none' })
        this.loadOrders()
      })
      .catch((err) => {
        wx.hideLoading()
        wx.showToast({ title: err.message || '操作失败', icon: 'none' })
      })
  }
})
