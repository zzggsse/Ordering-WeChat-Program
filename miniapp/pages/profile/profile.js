const api = require('../../utils/api')
const baseUrl = require('../../utils/config').baseUrl

Page({
  data: {
    avatar: '',      // 存储用（/uploads/xxx）
    avatarSrc: '',   // 展示用（完整 URL）
    nickname: '',
    saving: false,
    uploading: false
  },

  onLoad() { this.load() },
  onShow() { this.load() },

  load() {
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      setTimeout(() => wx.navigateBack(), 600)
      return
    }
    api.me()
      .then((u) => {
        this.applyAvatar(u.avatar)
        this.setData({ nickname: u.nickname || '' })
      })
      .catch((err) => wx.showToast({ title: err.message, icon: 'none' }))
  },

  resolveSrc(src) {
    if (!src) return ''
    return src.indexOf('/') === 0 ? baseUrl + src : src
  },
  applyAvatar(src) {
    this.setData({ avatar: src || '', avatarSrc: this.resolveSrc(src) })
  },

  onNickname(e) { this.setData({ nickname: e.detail.value }) },



  // 相册 / 拍照选图
  chooseAvatar() {
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const temp = res.tempFiles[0].tempFilePath
        this.applyAvatar(temp)
        this.upload(temp)
      }
    })
  },

  upload(temp) {
    this.setData({ uploading: true })
    api.uploadAvatar(temp)
      .then((url) => this.applyAvatar(url))
      .catch((err) => wx.showToast({ title: '上传失败：' + err.message, icon: 'none' }))
      .finally(() => this.setData({ uploading: false }))
  },

  save() {
    const name = (this.data.nickname || '').trim()
    if (!name) { wx.showToast({ title: '请输入昵称', icon: 'none' }); return }
    this.setData({ saving: true })
    api.updateProfile({ nickname: name, avatar: this.data.avatar })
      .then(() => {
        wx.showToast({ title: '已保存', icon: 'success' })
        setTimeout(() => wx.navigateBack(), 600)
      })
      .catch((err) => wx.showToast({ title: err.message, icon: 'none' }))
      .finally(() => this.setData({ saving: false }))
  }
})


