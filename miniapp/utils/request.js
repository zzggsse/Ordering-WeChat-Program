const config = require('./config')

/**
 * 统一请求封装：自动拼接 /api/v1 前缀并带登录凭证
 * opts.auth:
 *   - false  : 不需要登录
 *   - 'admin': 使用管理员凭证（商家工作台）
 *   - 其他    : 使用顾客 token（默认）
 * @returns {Promise<any>} 返回 data 字段
 */
function request(opts) {
  const url = config.baseUrl + '/api/v1' + opts.url
  const method = opts.method || 'GET'
  const data = opts.data || {}
  const withAuth = opts.auth !== false
  const asAdmin = opts.auth === 'admin'

  return new Promise((resolve, reject) => {
    const header = { 'Content-Type': 'application/json' }
    const token = wx.getStorageSync(asAdmin ? 'adminToken' : 'token')
    if (asAdmin && !token) {
      reject(new Error('AUTH_REQUIRED'))
      return
    }
    if (withAuth && token) header.Authorization = 'Bearer ' + token

    wx.request({
      url,
      method,
      data,
      header,
      success(res) {
        const body = res.data
        if (body && body.code === 0) {
          resolve(body.data)
        } else if (body && (body.code === 401 || body.code === 403)) {
          wx.removeStorageSync(asAdmin ? 'adminToken' : 'token')
          reject(new Error('AUTH_REQUIRED'))
        } else {
          reject(new Error((body && body.message) || '请求失败'))
        }
      },
      fail() {
        reject(new Error('网络不可用，请检查后端服务'))
      }
    })
  })
}

module.exports = request
