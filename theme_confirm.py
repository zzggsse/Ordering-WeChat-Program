# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp\pages\order\confirm'
js = '''Page({
  data: { items: [], total: 0, type: '自取', queueNo: '', pay: false },
  onLoad() {
    const cart = getApp().globalData.cart
    const total = cart.reduce((s, i) => s + i.price * i.count, 0)
    this.setData({ items: cart, total })
  },
  pickType(e) { this.setData({ type: e.currentTarget.dataset.t }) },
  submit() {
    if (this.data.pay) return
    this.setData({ pay: true })
    wx.showLoading({ title: '支付中...' })
    setTimeout(() => {
      wx.hideLoading()
      const q = 'A' + Math.floor(100 + Math.random() * 900)
      getApp().globalData.cart = []
      this.setData({ queueNo: q, items: [], total: 0, pay: false })
      wx.showModal({
        title: '支付成功 🎉',
        content: '取餐号：' + q + '\\n订单已推送到门店，请留意叫号',
        showCancel: false,
        confirmText: '查看订单',
        success: () => wx.switchTab({ url: '/pages/orders/orders' })
      })
    }, 900)
  }
})
'''
wxml = '''<view class="page">
  <!-- 门店信息 -->
  <view class="store">
    <text class="dot">📍</text>
    <view class="stext">
      <view class="sn">中心店</view>
      <view class="sa">XX 路 88 号 · 距你 1.2km</view>
    </view>
    <text class="open">营业中</text>
  </view>

  <view class="block" wx:if="{{items.length}}">
    <view class="bt">取餐方式</view>
    <view class="types">
      <view class="tp {{type==='自取'?'on':''}}" bindtap="pickType" data-t="自取">到店自取</view>
      <view class="tp {{type==='外带'?'on':''}}" bindtap="pickType" data-t="外带">外带</view>
    </view>
  </view>

  <view class="block">
    <view class="bt">商品明细</view>
    <view class="line" wx:for="{{items}}" wx:key="id">
      <view class="liname">{{item.name}} × {{item.count}}</view>
      <view class="price">¥{{item.price * item.count}}</view>
    </view>
  </view>

  <view class="block" wx:if="{{!items.length}}">
    <view class="ok">订单已提交 ✅</view>
    <view class="ok-sub">取餐号 {{queueNo}}</view>
  </view>

  <view class="foot" wx:if="{{items.length}}" bindtap="submit">
    <view class="amt">合计 <text class="big">¥{{total}}</text></view>
    <view class="paybtn">{{pay ? '支付中...' : '支付 ¥' + total}}</view>
  </view>
</view>
'''
wxss = '''.page { padding: 16rpx 16rpx 150rpx; }
.store { display: flex; align-items: center; background: #fff; border-radius: 20rpx; padding: 26rpx; margin-bottom: 16rpx; box-shadow: 0 4rpx 16rpx rgba(0,0,0,.05); }
.dot { font-size: 44rpx; margin-right: 16rpx; }
.stext { flex: 1; }
.sn { font-size: 32rpx; font-weight: 700; }
.sa { font-size: 22rpx; color: #9b9b9b; margin-top: 6rpx; }
.open { color: #e8332d; font-size: 24rpx; font-weight: 600; }
.block { background: #fff; border-radius: 20rpx; padding: 26rpx; margin-bottom: 16rpx; box-shadow: 0 4rpx 16rpx rgba(0,0,0,.05); }
.bt { font-weight: 700; font-size: 30rpx; margin-bottom: 20rpx; }
.types { display: flex; gap: 20rpx; }
.tp { flex: 1; text-align: center; padding: 22rpx 0; border-radius: 14rpx; border: 2rpx solid #eee5dc; color: #7a7a7a; }
.tp.on { border-color: #e8332d; color: #e8332d; background: #fff0ef; font-weight: 600; }
.line { display: flex; justify-content: space-between; padding: 14rpx 0; color: #555; font-size: 28rpx; }
.liname { color: #333; }
.price { color: #333; }
.ok { font-size: 32rpx; font-weight: 700; text-align: center; padding: 20rpx 0 6rpx; }
.ok-sub { text-align: center; color: #e8332d; font-size: 40rpx; font-weight: 700; padding-bottom: 20rpx; }
.foot { position: fixed; left: 0; right: 0; bottom: 0; background: #fff; display: flex; justify-content: space-between; align-items: center; padding: 24rpx 30rpx; box-shadow: 0 -4rpx 16rpx rgba(0,0,0,.07); }
.amt { font-size: 26rpx; color: #7a7a7a; }
.big { font-size: 40rpx; color: #333; font-weight: 700; }
.paybtn { background: #e8332d; color: #fff; padding: 20rpx 56rpx; border-radius: 48rpx; font-weight: 700; }
'''
open(base + '.js', 'w', encoding='utf-8', newline='\n').write(js)
open(base + '.wxml', 'w', encoding='utf-8', newline='\n').write(wxml)
open(base + '.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
print('confirm page themed')
