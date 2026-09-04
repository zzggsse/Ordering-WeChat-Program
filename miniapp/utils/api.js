const request = require('./request')
const config = require('./config')

const auth = {
  wechatLogin(data) { return request({ url: '/auth/wechat/login', method: 'POST', data, auth: false }) },
  sendSms(phone) { return request({ url: '/auth/sms/send', method: 'POST', data: { phone }, auth: false }) },
  phoneLogin(data) { return request({ url: '/auth/phone/login', method: 'POST', data, auth: false }) },
  adminLogin(data) { return request({ url: '/auth/login', method: 'POST', data, auth: false }) },
  me() { return request({ url: '/auth/me' }) },
  mineOrders(scope) { return request({ url: '/orders/mine?scope=' + (scope || 'today'), auth: true }) },
  updateProfile(data) { return request({ url: '/auth/profile', method: 'PUT', data }) },
  uploadAvatar(filePath) {
    return new Promise((resolve, reject) => {
      const token = wx.getStorageSync('token')
      wx.uploadFile({
        url: config.baseUrl + '/api/v1/upload',
        filePath,
        name: 'file',
        header: token ? { Authorization: 'Bearer ' + token } : {},
        success(res) {
          try {
            const body = JSON.parse(res.data)
            if (body && body.code === 0) resolve(body.data.url)
            else reject(new Error((body && body.message) || '上传失败'))
          } catch (e) { reject(new Error('上传失败')) }
        },
        fail() { reject(new Error('上传失败，请检查后端服务')) }
      })
    })
  }
}

const order = {
  createOrder(data) { return request({ url: '/orders', method: 'POST', data }) },
  payOrder(id) { return request({ url: '/orders/' + id + '/pay', method: 'POST', data: {} }) },
  receiveOrder(id) { return request({ url: '/orders/' + id + '/receive', method: 'POST', data: {} }) },
  orderDetail(id) { return request({ url: '/orders/' + id }) },
  cancelOrder(id) { return request({ url: '/orders/' + id + '/cancel', method: 'POST', data: {} }) }
}

// 顾客端：分类 / 上架商品（后端默认只返回在售商品）
const catalog = {
  categories() { return request({ url: '/categories' }) },
  menuProducts() { return request({ url: '/products?size=200' }) }
}

// 商家工作台：店员/店长 用本人登录凭证即可（后端按角色放行）
const workbench = {
  stats() { return request({ url: '/dashboard/stats' }) },
  orders(options) { return request({ url: '/admin/orders?status=' + ((options && options.status) || 'PAID') }) },
  accept(id) { return request({ url: '/admin/orders/' + id + '/accept', method: 'POST', data: {} }) },
  ready(id) { return request({ url: '/admin/orders/' + id + '/ready', method: 'POST', data: {} }) },
  done(id) { return request({ url: '/admin/orders/' + id + '/done', method: 'POST', data: {} }) }
}

// 员工管理：仅店长可用
const manage = {
  users(kw) { return request({ url: '/manage/users' + (kw ? '?kw=' + encodeURIComponent(kw) : '') }) },
  setUserRole(id, role) { return request({ url: '/manage/users/' + id + '/role', method: 'PUT', data: { role } }) }
}

// 库存 / 商品上下架（店员以上可用）
const stock = {
  products() { return request({ url: '/admin/products?size=200' }) },
  updateProductStock(id, stockVal) { return request({ url: '/products/' + id + '/stock?stock=' + stockVal, method: 'PATCH', data: {} }) },
  setProductStatus(id, opts) {
    const qs = []
    if (opts.status != null) qs.push('status=' + opts.status)
    if (opts.soldout != null) qs.push('soldout=' + opts.soldout)
    return request({ url: '/products/' + id + '/status' + (qs.length ? '?' + qs.join('&') : ''), method: 'PATCH', data: {} })
  },
  inventoryList() { return request({ url: '/inventory' }) },
  inventoryUpdate(id, data) { return request({ url: '/inventory/' + id, method: 'PUT', data }) }
}

// 会员中心：充值 / 积分兑换（顾客端）
const member = {
  recharge(amount) { return request({ url: '/member/recharge', method: 'POST', data: { amount } }) },
  coupons() { return request({ url: '/member/coupons' }) },
  redeem(couponId) { return request({ url: '/member/redeem', method: 'POST', data: { couponId } }) },
  myCoupons() { return request({ url: '/member/my-coupons' }) }
}

module.exports = { ...auth, ...catalog, ...order, ...workbench, ...manage, ...stock, ...member }
