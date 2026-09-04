# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp\pages\cart\cart'
js = '''Page({
  data: { cart: [], total: 0, checked: 0 },
  onShow() { this.refresh() },
  refresh() {
    const cart = getApp().globalData.cart
    const total = cart.reduce((s, i) => s + i.price * i.count, 0)
    const checked = cart.reduce((s, i) => s + i.count, 0)
    this.setData({ cart, total, checked })
  },
  step(id, d) {
    const cart = getApp().globalData.cart
    const it = cart.find(i => i.id === id)
    if (!it) return
    it.count += d
    if (it.count <= 0) {
      const idx = cart.findIndex(i => i.id === id)
      if (idx > -1) cart.splice(idx, 1)
    }
    this.refresh()
  },
  inc(e) { this.step(e.currentTarget.dataset.id, 1) },
  dec(e) { this.step(e.currentTarget.dataset.id, -1) },
  remove(e) {
    const id = e.currentTarget.dataset.id
    const cart = getApp().globalData.cart
    const idx = cart.findIndex(i => i.id === id)
    if (idx > -1) cart.splice(idx, 1)
    this.refresh()
  },
  checkout() {
    if (!this.data.total) { wx.showToast({ title: '购物车是空的', icon: 'none' }); return }
    wx.navigateTo({ url: '/pages/order/confirm' })
  }
})
'''
wxml = '''<view class="page">
  <view class="empty" wx:if="{{!cart.length}}">
    <view class="e-icon">🧋</view>
    <view>购物车空空的，去点一杯吧</view>
    <view class="e-btn" bindtap="goHome">去点单</view>
  </view>

  <view class="item" wx:for="{{cart}}" wx:key="id">
    <image class="pic" src="{{item.image}}" mode="aspectFill" />
    <view class="info">
      <view class="nm">{{item.name}}</view>
      <view class="opt">已选规格 · 正常冰 全糖</view>
      <view class="price">¥{{item.price}}</view>
    </view>
    <view class="right">
      <view class="stepper">
        <view class="btn min" bindtap="dec" data-id="{{item.id}}">−</view>
        <view class="num">{{item.count}}</view>
        <view class="btn plus" bindtap="inc" data-id="{{item.id}}">＋</view>
      </view>
      <view class="del" bindtap="remove" data-id="{{item.id}}">删除</view>
    </view>
  </view>

  <view class="foot" wx:if="{{cart.length}}">
    <view class="f-l">合计 <text class="price fs">¥{{total}}</text></view>
    <view class="checkout" bindtap="checkout">去结算 ({{checked}})</view>
  </view>
</view>
'''
wxss = '''.page { padding: 16rpx 16rpx 150rpx; }
.empty { text-align: center; padding: 160rpx 0; color: #9b9b9b; }
.e-icon { font-size: 90rpx; margin-bottom: 20rpx; }
.e-btn { display: inline-block; margin-top: 30rpx; background: #e8332d; color: #fff; padding: 16rpx 60rpx; border-radius: 40rpx; }
.item { display: flex; background: #fff; border-radius: 20rpx; padding: 22rpx; margin-bottom: 16rpx; box-shadow: 0 4rpx 16rpx rgba(0,0,0,.05); }
.pic { width: 150rpx; height: 150rpx; border-radius: 16rpx; flex-shrink: 0; }
.info { flex: 1; margin-left: 22rpx; }
.nm { font-size: 30rpx; font-weight: 600; }
.opt { font-size: 22rpx; color: #9b9b9b; margin: 8rpx 0; }
.price { font-size: 30rpx; color: #e8332d; font-weight: 700; }
.right { display: flex; flex-direction: column; justify-content: space-between; align-items: flex-end; }
.stepper { display: flex; align-items: center; gap: 14rpx; }
.btn { width: 48rpx; height: 48rpx; border-radius: 50%; text-align: center; line-height: 46rpx; font-weight: 700; }
.min { background: #fff0ef; color: #e8332d; border: 2rpx solid #f6ccc7; }
.plus { background: #e8332d; color: #fff; box-shadow: 0 4rpx 10rpx rgba(232,51,45,.35); }
.num { min-width: 40rpx; text-align: center; font-size: 28rpx; }
.del { color: #c0c0c0; font-size: 24rpx; }
.foot { position: fixed; left: 0; right: 0; bottom: 0; background: #fff; display: flex; justify-content: space-between; align-items: center; padding: 24rpx 30rpx; box-shadow: 0 -4rpx 16rpx rgba(0,0,0,.07); }
.checkout { background: #e8332d; color: #fff; padding: 18rpx 50rpx; border-radius: 48rpx; font-weight: 700; }
.fs { font-size: 40rpx; }
'''
open(base + '.js', 'w', encoding='utf-8', newline='\n').write(js)
open(base + '.wxml', 'w', encoding='utf-8', newline='\n').write(wxml)
open(base + '.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
