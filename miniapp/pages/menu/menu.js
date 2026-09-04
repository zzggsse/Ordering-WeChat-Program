const api = require('../../utils/api')

const CAT_ICONS = ['🧋', '🍊', '🧀', '🥛', '☕', '🧋', '🍵', '🍓', '🫧']
Page({
  data: {
    categories: [
      { key: 'all', label: '推荐', icon: '🔥' }
    ],
    activeKey: 'all',
    products: [],
    shown: [],
    kw: '',
    cartCount: 0,
    loading: true
  },
  onShow() {
    this.loadData()
    const cart = getApp().globalData.cart
    const n = cart.reduce((s, i) => s + i.count, 0)
    this.setData({ cartCount: n })
  },
  loadData() {
    this.setData({ loading: true })
    Promise.all([api.categories(), api.menuProducts()])
      .then(([cats, paged]) => {
        const list = (paged && paged.records) || []
        const mapped = list.map((p) => Object.assign({}, p, {
          image: (p.image || '').replace(/^.*\//, '/images/')
        }))
        const categories = [{ key: 'all', label: '推荐', icon: '🔥' }]
          .concat((cats || []).map((c, i) => ({
            key: c.name, label: c.name, icon: CAT_ICONS[i] || '🧋'
          })))
        this.setData({ categories, products: mapped }, () => this.applyFilter())
      })
      .catch((err) => wx.showToast({ title: err.message || '加载失败', icon: 'none' }))
      .finally(() => this.setData({ loading: false }))
  },
  applyFilter() {
    const kw = this.data.kw.trim()
    let shown = this.data.activeKey === 'all'
      ? this.data.products
      : this.data.products.filter(p => p.category === this.data.activeKey)
    if (kw) shown = shown.filter(p => p.name.includes(kw))
    this.setData({ shown })
  },
  switchCat(e) {
    this.setData({ activeKey: e.currentTarget.dataset.key }, () => this.applyFilter())
  },
  onSearch(e) {
    this.setData({ kw: e.detail.value.trim() }, () => this.applyFilter())
  },
  addToCart(e) {
    const id = e.currentTarget.dataset.id
    const p = this.data.products.find(x => x.id === id)
    if (!p) return
    if (p.soldout) { wx.showToast({ title: '暂时售罄', icon: 'none' }); return }
    const cart = getApp().globalData.cart
    const hit = cart.find(i => i.id === id)
    if (hit) hit.count++
    else cart.push({ ...p, count: 1 })
    const n = cart.reduce((s, i) => s + i.count, 0)
    this.setData({ cartCount: n })
    wx.showToast({ title: '已加入', icon: 'none' })
  },
  goCart() { wx.switchTab({ url: '/pages/cart/cart' }) }
})