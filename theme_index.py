# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp\pages\index\index'
js = '''const { products } = require('../../data.js')

Page({
  data: {
    categories: [
      { key: 'all', label: '推荐', icon: '🔥' },
      { key: '经典奶茶', label: '经典奶茶', icon: '🧋' },
      { key: '果茶', label: '鲜果茶', icon: '🍊' },
      { key: '芝士奶盖', label: '芝士奶盖', icon: '🧀' },
      { key: '鲜奶', label: '鲜奶', icon: '🥛' },
      { key: '咖啡', label: '咖啡', icon: '☕' }
    ],
    activeKey: 'all',
    products,
    shown: [],
    kw: '',
    cartCount: 0
  },
  onLoad() { this.applyFilter() },
  onShow() {
    const cart = getApp().globalData.cart
    const n = cart.reduce((s, i) => s + i.count, 0)
    this.setData({ cartCount: n })
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
'''
wxml = '''<view class="page">
  <!-- 品牌头部 -->
  <view class="head">
    <view class="store">
      <view class="brand">🍹 古茗 · 点单</view>
      <view class="placed">中心店 · 距你 1.2km <text class="open">营业中</text></view>
    </view>
    <view class="search">
      <input placeholder="搜索奶茶 / 口味" confirm-type="search" bindinput="onSearch" />
      <text class="s-icon">🔍</text>
    </view>
  </view>

  <!-- 分类 -->
  <view class="cats">
    <view class="cat {{activeKey===c.key?'on':''}}"
          wx:for="{{categories}}" wx:key="key"
          bindtap="switchCat" data-key="{{c.key}}">
      <text class="ci">{{c.icon}}</text>
      <text>{{c.label}}</text>
    </view>
  </view>

  <!-- 商品卡片 -->
  <view class="grid">
    <view class="card" wx:for="{{shown}}" wx:key="id">
      <view class="imgbox">
        <image src="{{item.image}}" mode="aspectFill" />
        <view class="reco" wx:if="{{item.recommended}}">热销</view>
        <view class="sold" wx:if="{{item.soldout}}">已售罄</view>
      </view>
      <view class="body">
        <view class="name">{{item.name}}</view>
        <view class="sub">月售 {{item.sales}}</view>
        <view class="brow">
          <view class="price">¥{{item.price}}</view>
          <view class="add {{item.soldout?'off':''}}" bindtap="addToCart" data-id="{{item.id}}">＋</view>
        </view>
      </view>
    </view>
  </view>

  <view class="empty" wx:if="{{!shown.length}}">没有找到相关商品</view>

  <!-- 底部购物车栏 -->
  <view class="cartbar" wx:if="{{cartCount>0}}" bindtap="goCart">
    <view class="cart-l"><text class="cart-icon">🛒</text><text>购物车 {{cartCount}} 件</text></view>
    <view class="cart-go">去结算 ›</view>
  </view>
</view>
'''
wxss = '''.page { padding-bottom: 40rpx; }
/* 品牌头部 */
.head { background: #e8332d; padding: 24rpx 24rpx 0; color: #fff; }
.store { margin-bottom: 16rpx; }
.brand { font-size: 36rpx; font-weight: 800; letter-spacing: 1rpx; }
.placed { font-size: 24rpx; opacity: .95; margin-top: 6rpx; }
.open { background: rgba(255,255,255,.25); padding: 2rpx 14rpx; border-radius: 20rpx; margin-left: 10rpx; font-size: 22rpx; }
.search { display: flex; align-items: center; background: #fff; border-radius: 40rpx; padding: 14rpx 24rpx; color: #333; margin-bottom: 20rpx; }
.search input { flex: 1; font-size: 26rpx; }
.s-icon { font-size: 26rpx; }
/* 分类 */
.cats { display: flex; overflow-x: auto; padding: 20rpx 16rpx 4rpx; background: #f7f6f2; gap: 12rpx; }
.cat { flex-shrink: 0; display: flex; align-items: center; gap: 6rpx; padding: 12rpx 24rpx; background: #fff; border-radius: 40rpx; border: 2rpx solid #eee5dc; color: #7a7a7a; font-size: 26rpx; }
.cat.on { background: #fff0ef; border-color: #e8332d; color: #e8332d; font-weight: 600; }
.ci { font-size: 24rpx; }
/* 商品 */
.grid { display: flex; flex-wrap: wrap; padding: 16rpx; }
.card { width: calc(50% - 16rpx); margin: 8rpx; background: #fff; border-radius: 20rpx; overflow: hidden; box-shadow: 0 6rpx 20rpx rgba(0,0,0,.05); }
.imgbox { position: relative; height: 260rpx; background: #f3efe9; }
.imgbox image { width: 100%; height: 100%; }
.reco { position: absolute; top: 12rpx; left: 12rpx; background: rgba(232,51,45,.92); color: #fff; font-size: 20rpx; padding: 4rpx 14rpx; border-radius: 20rpx; }
.sold { position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,.4); color: #fff; display: flex; align-items: center; justify-content: center; font-size: 30rpx; }
.body { padding: 16rpx 18rpx 18rpx; }
.name { font-size: 28rpx; font-weight: 600; text-overflow: ellipsis; overflow: hidden; white-space: nowrap; }
.sub { font-size: 22rpx; color: #9b9b9b; margin: 6rpx 0 10rpx; }
.brow { display: flex; justify-content: space-between; align-items: center; }
.price { font-size: 32rpx; color: #e8332d; font-weight: 700; }
.add { width: 56rpx; height: 56rpx; border-radius: 50%; background: #e8332d; color: #fff; font-size: 40rpx; text-align: center; line-height: 50rpx; font-weight: 700; box-shadow: 0 4rpx 12rpx rgba(232,51,45,.4); }
.add.off { background: #ccc; box-shadow: none; }
.empty { text-align: center; color: #9b9b9b; padding: 80rpx 0; }
/* 底部购物车 */
.cartbar { position: fixed; left: 20rpx; right: 20rpx; bottom: 32rpx; display: flex; justify-content: space-between; align-items: center; background: #e8332d; color: #fff; padding: 24rpx 30rpx; border-radius: 48rpx; box-shadow: 0 10rpx 30rpx rgba(232,51,45,.4); }
.cart-l { display: flex; align-items: center; gap: 10rpx; font-weight: 600; }
.cart-icon { font-size: 34rpx; }
.cart-go { background: #fff; color: #e8332d; font-weight: 700; padding: 10rpx 34rpx; border-radius: 40rpx; }
'''
open(base + '.js', 'w', encoding='utf-8', newline='\n').write(js)
open(base + '.wxml', 'w', encoding='utf-8', newline='\n').write(wxml)
open(base + '.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
print('index page themed')
