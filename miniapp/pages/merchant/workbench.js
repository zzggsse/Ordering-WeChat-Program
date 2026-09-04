const api = require('../../utils/api')

function fmtTime(iso) {
  if (!iso) return ''
  const t = String(iso)
  const idx = t.indexOf('T')
  const tail = idx >= 0 ? t.slice(idx + 1) : t
  return tail.slice(0, 5)
}

const ACT = {
  PAID: { label: '接单', key: 'accept', statusText: '待接单' },
  MAKING: { label: '出杯', key: 'ready', statusText: '制作中' },
  READY: { label: '', key: '', statusText: '待取餐·等顾客' }
}

Page({
  data: {
    loggedIn: false,
    role: '',
    isOwner: false,
    activeTab: 'orders',
    loading: false,
    searchKw: '',
    stats: [],
    orders: [],
    users: [],
    products: [],
    inventory: []
  },

  onShow() { this.init() },

  init() {
    if (!wx.getStorageSync('token')) {
      this.setData({ loggedIn: false })
      return
    }
    this.setData({ loggedIn: true })
    api.me()
      .then((me) => {
        const isOwner = me && me.role === '店长'
        this.setData({ role: (me && me.role) || '', isOwner: !!isOwner })
        if (!isOwner && this.data.activeTab === 'staff') {
          this.setData({ activeTab: 'orders' })
        }
        this.loadTab()
      })
      .catch(() => wx.showToast({ title: '加载失败', icon: 'none' }))
  },

  goLogin() { wx.navigateTo({ url: '/pages/login/login' }) },

  switchTab(e) {
    const key = e.currentTarget.dataset.tab
    if (key === this.data.activeTab) return
    this.setData({ activeTab: key })
    this.loadTab()
  },

  loadTab() {
    const t = this.data.activeTab
    if (t === 'staff') this.loadUsers()
    else if (t === 'inventory') this.loadInventory()
    else this.loadOrders()
  },

  loadOrders() {
    this.setData({ loading: true })
    Promise.all([api.stats(), api.orders({ status: 'PAID,MAKING,READY' })])
      .then(([stats, paged]) => {
        const s = stats || {}
        this.setData({
          stats: [
            { label: '今日营收', value: '¥' + (s.todayRevenue != null ? s.todayRevenue : 0) },
            { label: '进行中', value: s.pendingOrders != null ? s.pendingOrders : 0 },
            { label: '今日订单', value: s.todayOrders != null ? s.todayOrders : 0 }
          ],
          orders: ((paged && paged.records) || []).map((r) => {
            const act = ACT[r.status] || { label: '', key: '', statusText: r.statusLabel || r.status }
            return {
              id: r.id, no: r.orderNo, time: fmtTime(r.createdAt),
              items: (r.items || []).map((i) => (i.name + ' ×' + i.quantity)).join(' · ') || '—',
              amount: r.totalAmount, queue: r.pickupNo,
              statusText: act.statusText, action: act.key, actionLabel: act.label
            }
          })
        })
      })
      .catch((err) => wx.showToast({ title: err.message || '加载失败', icon: 'none' }))
      .finally(() => this.setData({ loading: false }))
  },

  loadInventory() {
    this.setData({ loading: true })
    Promise.all([api.products(), api.inventoryList()])
      .then(([pr, inv]) => {
        this.setData({
          products: ((pr && pr.records) || []).map((p) => ({
            id: p.id, name: p.name, price: p.price,
            status: p.status, soldout: p.soldout, sales: p.sales, stock: p.stock
          })),
          inventory: (inv || []).map((it) => ({
            id: it.id, name: it.name, spec: it.spec || '', stock: it.stock,
            threshold: it.threshold, unit: it.unit || ''
          }))
        })
      })
      .catch((err) => wx.showToast({ title: err.message || '加载失败', icon: 'none' }))
      .finally(() => this.setData({ loading: false }))
  },

  toggleShelf(e) {
    const { id, status } = e.currentTarget.dataset
    const next = status === 1 ? 0 : 1
    const tip = next === 1 ? '上架' : '下架'
    api.setProductStatus(id, { status: next })
      .then(() => {
        wx.showToast({ title: '已' + tip, icon: 'success' })
        this.loadInventory()
      })
      .catch((err) => wx.showToast({ title: err.message || '操作失败', icon: 'none' }))
  },

  toggleSoldout(e) {
    const { id, soldout } = e.currentTarget.dataset
    const next = soldout === 1 ? 0 : 1
    const tip = next === 1 ? '设为售罄' : '恢复在售'
    api.setProductStatus(id, { soldout: next })
      .then(() => {
        wx.showToast({ title: tip, icon: 'success' })
        this.loadInventory()
      })
      .catch((err) => wx.showToast({ title: err.message || '操作失败', icon: 'none' }))
  },

  onProductStock(e) {
    const idx = e.currentTarget.dataset.index
    this.setData({ ['products[' + idx + '].stock']: e.detail.value })
  },

  saveProductStock(e) {
    const idx = e.currentTarget.dataset.index
    const it = this.data.products[idx]
    if (!it) return
    const stock = parseInt(it.stock, 10)
    if (isNaN(stock) || stock < 0) {
      wx.showToast({ title: '请输入正确数量', icon: 'none' })
      return
    }
    wx.showLoading({ title: '保存中...' })
    api.updateProductStock(it.id, stock)
      .then(() => {
        wx.hideLoading()
        wx.showToast({ title: '库存已更新', icon: 'success' })
        this.loadInventory()
      })
      .catch((err) => {
        wx.hideLoading()
        wx.showToast({ title: err.message || '保存失败', icon: 'none' })
      })
  },

  onInvStock(e) {
    const idx = e.currentTarget.dataset.index
    this.setData({ ['inventory[' + idx + '].stock']: e.detail.value })
  },

  updateInvStock(e) {
    const idx = e.currentTarget.dataset.index
    const it = this.data.inventory[idx]
    if (!it) return
    const stock = parseInt(it.stock, 10)
    if (isNaN(stock) || stock < 0) {
      wx.showToast({ title: '请输入正确数量', icon: 'none' })
      return
    }
    wx.showLoading({ title: '保存中...' })
    api.inventoryUpdate(it.id, { name: it.name, spec: it.spec, stock: stock, threshold: it.threshold, unit: it.unit })
      .then(() => {
        wx.hideLoading()
        wx.showToast({ title: '库存已更新', icon: 'success' })
      })
      .catch((err) => {
        wx.hideLoading()
        wx.showToast({ title: err.message || '保存失败', icon: 'none' })
      })
  },

  loadUsers() {
    this.setData({ loading: true })
    api.users(this.data.searchKw.trim() || '')
      .then((users) => this.setData({ users: users || [] }))
      .catch((err) => wx.showToast({ title: err.message || '加载失败', icon: 'none' }))
      .finally(() => this.setData({ loading: false }))
  },

  onSearchInput(e) { this.setData({ searchKw: e.detail.value }) },
  searchUsers() { this.loadUsers() },
  clearSearch() {
    this.setData({ searchKw: '' })
    this.loadUsers()
  },

  doAction(e) {
    const { id, act } = e.currentTarget.dataset
    if (!id || !act) return
    const labels = { accept: '接单', ready: '出杯' }
    const fn = { accept: api.accept, ready: api.ready }[act]
    if (!fn) return
    wx.showLoading({ title: labels[act] + '中...' })
    fn(id)
      .then(() => {
        wx.hideLoading()
        wx.showToast({ title: '已' + labels[act], icon: 'success' })
        this.loadOrders()
      })
      .catch((err) => {
        wx.hideLoading()
        wx.showToast({ title: err.message || '操作失败', icon: 'none' })
      })
  },

  setUserRole(e) {
    const { id, role } = e.currentTarget.dataset
    const next = role === '店员' ? '客户' : '店员'
    const tip = next === '店员' ? '设为店员' : '收回店员权限'
    wx.showModal({
      title: tip,
      content: '确认' + tip + '该用户的权限吗？',
      success: (r) => {
        if (!r.confirm) return
        api.setUserRole(id, next)
          .then(() => {
            wx.showToast({ title: '已' + tip, icon: 'success' })
            this.loadUsers()
          })
          .catch((err) => wx.showToast({ title: err.message || '操作失败', icon: 'none' }))
      }
    })
  }
})
