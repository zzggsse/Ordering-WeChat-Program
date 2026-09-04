const api = require('../../utils/api')

Page({
  data: {
    tab: 'phone',
    phone: '',
    code: '',
    counting: false,
    countdown: 0,
    sending: false,
    loggingIn: false
  },

  switchTab(e) {
    this.setData({ tab: e.currentTarget.dataset.tab })
  },

  onPhone(e) { this.setData({ phone: e.detail.value }) },
  onCode(e) { this.setData({ code: e.detail.value }) },

  isPhone(p) { return /^1[3-9]\d{9}$/.test(p) },

  sendSms() {
    if (!this.isPhone(this.data.phone)) {
      wx.showToast({ title: '请输入正确的手机号', icon: 'none' })
      return
    }
    if (this.data.sending || this.data.counting) return
    this.setData({ sending: true })
    api.sendSms(this.data.phone)
      .then((res) => {
        const tip = (res && res.mock && res.code) ? ('验证码：' + res.code) : '验证码已发送'
        wx.showToast({ title: tip, icon: 'none', duration: 2000 })
        this.startCountdown(60)
      })
      .catch((err) => wx.showToast({ title: err.message, icon: 'none' }))
      .finally(() => this.setData({ sending: false }))
  },

  startCountdown(n) {
    this.setData({ counting: true, countdown: n })
    const timer = setInterval(() => {
      if (this.data.countdown <= 1) {
        clearInterval(timer)
        this.setData({ counting: false, countdown: 0 })
      } else {
        this.setData({ countdown: this.data.countdown - 1 })
      }
    }, 1000)
  },

  phoneLogin() {
    const { phone, code } = this.data
    if (!this.isPhone(phone)) { wx.showToast({ title: '请输入正确的手机号', icon: 'none' }); return }
    if (!code) { wx.showToast({ title: '请输入验证码', icon: 'none' }); return }
    this.setData({ loggingIn: true })
    api.phoneLogin({ phone, code })
      .then((u) => this.finish(u))
      .catch((err) => wx.showToast({ title: err.message, icon: 'none' }))
      .finally(() => this.setData({ loggingIn: false }))
  },

  wechatLogin() {
    wx.login({
      success: (res) => {
        const data = { code: res.code || 'wx_miniapp' }
        wx.getUserProfile({
          desc: '用于展示会员昵称与头像',
          success: (p) => {
            data.nickname = p.userInfo.nickName
            data.avatar = p.userInfo.avatarUrl
            this.reqWechat(data)
          },
          fail: () => this.reqWechat(data)
        })
      },
      fail: () => wx.showToast({ title: '微信登录失败', icon: 'none' })
    })
  },

  reqWechat(data) {
    api.wechatLogin(data)
      .then((u) => this.finish(u))
      .catch((err) => wx.showToast({ title: err.message, icon: 'none' }))
  },

  finish(user) {
    wx.setStorageSync('token', user.token)
    wx.setStorageSync('user', user)
    wx.showToast({ title: '登录成功', icon: 'success' })
    setTimeout(() => wx.navigateBack(), 600)
  }
})

