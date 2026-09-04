App({
  globalData: {
    storeName: '中心店 · 示例',
    cart: [],
    isMerchantMode: false,
    token: wx.getStorageSync('token') || '',
    user: wx.getStorageSync('user') || null
  },
  onLaunch() {
    console.log('奶茶点单小程序启动')
  }
})
