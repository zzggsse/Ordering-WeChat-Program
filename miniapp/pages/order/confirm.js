const api = require('../../utils/api')

Page({
  data: { items: [], total: 0, type: '自取', queueNo: '', pay: false },

  onLoad() {
    const cart = getApp().globalData.cart
    const total = cart.reduce((s, i) => s + i.price * i.count, 0)
    this.setData({ items: cart, total })
  },

  pickType(e) { this.setData({ type: e.currentTarget.dataset.t }) },

  submit() {
    if (this.data.pay) return

    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '请先登录',
        content: '登录后才能下单',
        confirmText: '去登录',
        success: (r) => { if (r.confirm) wx.navigateTo({ url: '/pages/login/login' }) }
      })
      return
    }

    const cart = getApp().globalData.cart
    if (!cart.length) { wx.showToast({ title: '购物车为空', icon: 'none' }); return }

    this.setData({ pay: true })
    wx.showLoading({ title: '支付中...' })

    const orderData = {
      pickupType: this.data.type,
      items: cart.map((i) => ({
        productId: i.id,
        quantity: i.count,
        sugar: '全糖',
        temp: '正常冰',
        cupSize: '中杯'
      }))
    }

    api.createOrder(orderData)
      .then((order) => api.payOrder(order.id))
      .then((order) => {
        wx.hideLoading()
        getApp().globalData.cart = []
        this.setData({ items: [], total: 0, queueNo: order.pickupNo || '', pay: false })
        wx.showModal({
          title: '支付成功 🎉',
          content: '订单号：' + order.orderNo + '\n取餐号：' + (order.pickupNo || '—') + '\n请留意叫号',
          showCancel: false,
          confirmText: '查看订单',
          success: () => wx.switchTab({ url: '/pages/orders/orders' })
        })
      })
      .catch((err) => {
        wx.hideLoading()
        this.setData({ pay: false })
        wx.showToast({ title: err.message || '下单失败', icon: 'none' })
      })
  }
})
