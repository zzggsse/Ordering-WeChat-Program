Page({
  data: { cart: [], total: 0, checked: 0 },
  onShow() { this.refresh() },
  refresh() {
    const cart = getApp().globalData.cart
    const total = cart.reduce((s, i) => s + i.price * i.count, 0)
    const checked = cart.reduce((s, i) => s + i.count, 0)
    this.setData({ cart, total, checked })
  },
  step(id, d) {
    const cart = getApp().globalData.cart
    const it = cart.find(i => i.id === id)
    if (!it) return
    it.count += d
    if (it.count <= 0) {
      const idx = cart.findIndex(i => i.id === id)
      if (idx > -1) cart.splice(idx, 1)
    }
    this.refresh()
  },
  inc(e) { this.step(e.currentTarget.dataset.id, 1) },
  dec(e) { this.step(e.currentTarget.dataset.id, -1) },
  remove(e) {
    const id = e.currentTarget.dataset.id
    const cart = getApp().globalData.cart
    const idx = cart.findIndex(i => i.id === id)
    if (idx > -1) cart.splice(idx, 1)
    this.refresh()
  },
  checkout() {
    if (!this.data.total) { wx.showToast({ title: '购物车是空的', icon: 'none' }); return }
    wx.navigateTo({ url: '/pages/order/confirm' })
  }
})
