const api = require('../../utils/api')

const STAFF_ROLES = ['店员', '店长']

Page({
  data: { loggedIn: false, nickname: '', level: '', avatar: '', phone: '', points: 0, isStaff: false },

  onShow() {
    const token = wx.getStorageSync('token')
    if (!token) {
      this.setData({ loggedIn: false, isStaff: false })
      return
    }
    api.me()
      .then((u) => {
        getApp().globalData.user = u
        this.setData({
          loggedIn: true,
          nickname: u.nickname || '会员',
          level: u.level || '普通会员',
          avatar: u.avatar || '',
          phone: u.phone || '',
          points: u.points || 0,
          isStaff: STAFF_ROLES.indexOf(u.role) >= 0
        })
      })
      .catch(() => this.setData({ loggedIn: false, isStaff: false }))
  },

  goLogin() { wx.navigateTo({ url: '/pages/login/login' }) },

  goOrders() { wx.switchTab({ url: '/pages/orders/orders' }) },

  goProfile() { wx.navigateTo({ url: '/pages/profile/profile' }) },

  goMall() { wx.navigateTo({ url: '/pages/mall/mall' }) },

  goMerchant() { wx.navigateTo({ url: '/pages/merchant/workbench' }) },

  logout() {
    wx.removeStorageSync('token')
    wx.removeStorageSync('user')
    getApp().globalData.token = ''
    getApp().globalData.user = null
    this.setData({ loggedIn: false, isStaff: false })
    wx.showToast({ title: '已退出登录', icon: 'none' })
  }
})
