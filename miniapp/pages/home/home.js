const api = require('../../utils/api')
Page({
  data: { hot: [] },
  onShow() {
    this.loadData()
  },
  loadData() {
    api.menuProducts()
      .then((paged) => {
        const list = ((paged && paged.records) || [])
          .filter((p) => p.recommended === 1 || (p.sales || 0) > 700)
          .slice(0, 4)
          .map((p) => Object.assign({}, p, {
            image: (p.image || '').replace(/^.*\//, '/images/')
          }))
        this.setData({ hot: list })
      })
      .catch(() => {})
  },
  goMenu() { wx.switchTab({ url: '/pages/menu/menu' }) },
  goStore() { this.goMenu() },
  goCoupon() { wx.showToast({ title: '优惠券活动（演示）', icon: 'none' }) }
})