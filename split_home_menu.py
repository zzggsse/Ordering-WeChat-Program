# -*- coding: utf-8 -*-
import os, shutil
root = r'D:\Order\miniapp'

# 1) 更新 app.json：加 home 与 menu 页面，tabBar 5 项
appjson = '''{
  "pages": [
    "pages/home/home",
    "pages/menu/menu",
    "pages/cart/cart",
    "pages/orders/orders",
    "pages/order/confirm",
    "pages/order/detail",
    "pages/mine/mine",
    "pages/merchant/workbench"
  ],
  "window": {
    "navigationBarBackgroundColor": "#ffffff",
    "navigationBarTextStyle": "black",
    "navigationBarTitleText": "古茗",
    "backgroundColor": "#f7f6f2",
    "backgroundTextStyle": "dark"
  },
  "tabBar": {
    "color": "#9b9b9b",
    "selectedColor": "#e8332d",
    "backgroundColor": "#ffffff",
    "borderStyle": "black",
    "list": [
      { "pagePath": "pages/home/home", "text": "首页" },
      { "pagePath": "pages/menu/menu", "text": "点单" },
      { "pagePath": "pages/cart/cart", "text": "购物车" },
      { "pagePath": "pages/orders/orders", "text": "订单" },
      { "pagePath": "pages/mine/mine", "text": "我的" }
    ]
  },
  "style": "v2",
  "sitemapLocation": "sitemap.json"
}
'''
open(os.path.join(root,'app.json'),'w',encoding='utf-8',newline='\n').write(appjson)

# 2) 删除旧 index 页面目录
old = os.path.join(root,'pages','index')
if os.path.isdir(old):
    shutil.rmtree(old)
    print('removed old index page')

# 3) 创建 home 与 menu 目录
for p in ('home','menu'):
    os.makedirs(os.path.join(root,'pages',p), exist_ok=True)

# ---------- 首页 home ----------
hjs = '''const { products } = require('../../data.js')
Page({
  data: { hot: products.filter(p => p.recommended || p.sales > 700).slice(0, 4) },
  goMenu() { wx.switchTab({ url: '/pages/menu/menu' }) },
  goStore() { this.goMenu() },
  goCoupon() { wx.showToast({ title: '优惠券活动（演示）', icon: 'none' }) }
})
'''
hwxml = '''<view class="page">
  <!-- 品牌 hero -->
  <view class="hero">
    <view class="logo">🍹 古茗</view>
    <view class="slogan">每天一杯 · 好喝不贵</view>
  </view>

  <!-- 门店信息条 -->
  <view class="store" bindtap="goStore">
    <view class="addr">📍 中心店 · 距你 1.2km</view>
    <view class="open">营业中 ›</view>
  </view>

  <!-- 活动 banner -->
  <view class="banner" bindtap="goCoupon">
    <view class="bt">新人专享</view>
    <view class="bc">首杯立减 3 元</view>
    <view class="bg">🪙</view>
  </view>

  <!-- 快捷入口 -->
  <view class="entry" bindtap="goMenu">
    <view class="ei">🧋</view><view class="et">在线点单</view>
  </view>

  <!-- 热门推荐 -->
  <view class="sec">
    <view class="sttl">人气必喝 <text class="more" bindtap="goMenu">去点单 ›</text></view>
    <scroll-view scroll-x class="hrow">
      <view class="hcard" wx:for="{{hot}}" wx:key="id" bindtap="goMenu">
        <image class="him" src="{{item.image}}" mode="aspectFill" />
        <view class="hn">{{item.name}}</view>
        <view class="hp price">¥{{item.price}}</view>
      </view>
    </scroll-view>
  </view>

  <!-- 去点单 CTA -->
  <view class="cta" bindtap="goMenu">立即点单</view>
</view>
'''
hwxss = '''.page { padding: 20rpx 20rpx 40rpx; }
.hero { background: linear-gradient(135deg,#e8332d,#c0251f); color: #fff; border-radius: 24rpx; padding: 50rpx 34rpx; }
.logo { font-size: 52rpx; font-weight: 800; }
.slogan { font-size: 26rpx; opacity: .9; margin-top: 12rpx; }
.store { margin-top: -16rpx; background: #fff; border-radius: 18rpx; padding: 24rpx 28rpx; margin-left: 16rpx; margin-right: 16rpx; box-shadow: 0 6rpx 20rpx rgba(0,0,0,.08); display: flex; justify-content: space-between; }
.addr { font-size: 28rpx; font-weight: 600; }
.open { color: #e8332d; font-size: 26rpx; font-weight: 600; }
.banner { margin-top: 24rpx; background: #fff0ef; border-radius: 20rpx; padding: 30rpx; display: flex; align-items: center; gap: 12rpx; border: 2rpx solid #f6ccc7; }
.bt { font-size: 22rpx; color: #e8332d; background: #fff; padding: 4rpx 14rpx; border-radius: 20rpx; }
.bc { font-size: 30rpx; font-weight: 700; color: #333; }
.bg { margin-left: auto; font-size: 48rpx; }
.entry { margin-top: 24rpx; background: #fff; border-radius: 20rpx; padding: 30rpx; display: flex; align-items: center; gap: 16rpx; box-shadow: 0 4rpx 16rpx rgba(0,0,0,.05); }
.ei { font-size: 44rpx; }
.et { font-size: 30rpx; font-weight: 600; }
.sec { margin-top: 30rpx; }
.sttl { font-size: 34rpx; font-weight: 800; display: flex; justify-content: space-between; align-items: center; }
.more { font-size: 26rpx; color: #e8332d; font-weight: 600; }
.hrow { white-space: nowrap; margin-top: 16rpx; }
.hcard { display: inline-block; width: 200rpx; background: #fff; border-radius: 18rpx; padding: 14rpx; margin-right: 16rpx; box-shadow: 0 4rpx 14rpx rgba(0,0,0,.05); }
.him { width: 100%; height: 170rpx; border-radius: 12rpx; }
.hn { font-size: 26rpx; font-weight: 600; text-overflow: ellipsis; overflow: hidden; white-space: nowrap; margin-top: 10rpx; }
.hp { font-size: 30rpx; margin-top: 6rpx; }
.cta { margin-top: 36rpx; background: #e8332d; color: #fff; text-align: center; padding: 26rpx 0; border-radius: 48rpx; font-weight: 700; font-size: 32rpx; box-shadow: 0 10rpx 26rpx rgba(232,51,45,.35); }
'''
open(os.path.join(root,'pages','home','home.js'),'w',encoding='utf-8',newline='\n').write(hjs)
open(os.path.join(root,'pages','home','home.wxml'),'w',encoding='utf-8',newline='\n').write(hwxml)
open(os.path.join(root,'pages','home','home.wxss'),'w',encoding='utf-8',newline='\n').write(hwxss)

# ---------- 点单 menu ----------
mjs = '''const { categories, products } = require('../../data.js')
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
mwxml = '''<view class="page">
  <view class="srch">
    <input placeholder="搜索奶茶 / 口味" confirm-type="search" bindinput="onSearch" />
    <text>🔍</text>
  </view>

  <view class="cats">
    <view class="cat {{activeKey===c.key?'on':''}}"
          wx:for="{{categories}}" wx:key="key"
          bindtap="switchCat" data-key="{{c.key}}">
      <text class="ci">{{c.icon}}</text>
      <text>{{c.label}}</text>
    </view>
  </view>

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

  <view class="cartbar" wx:if="{{cartCount>0}}" bindtap="goCart">
    <view class="cart-l"><text class="cart-icon">🛒</text><text>购物车 {{cartCount}} 件</text></view>
    <view class="cart-go">去结算 ›</view>
  </view>
</view>
'''
mwxss = '''.page { padding-bottom: 40rpx; }
.srch { display: flex; align-items: center; background: #fff; border-radius: 40rpx; padding: 16rpx 26rpx; margin: 20rpx; box-shadow: 0 2rpx 8rpx rgba(0,0,0,.04); }
.srch input { flex: 1; font-size: 26rpx; }
.cats { display: flex; overflow-x: auto; padding: 0 16rpx 16rpx; gap: 12rpx; }
.cat { flex-shrink: 0; display: flex; align-items: center; gap: 6rpx; padding: 12rpx 24rpx; background: #fff; border-radius: 40rpx; border: 2rpx solid #eee5dc; color: #7a7a7a; font-size: 26rpx; }
.cat.on { background: #fff0ef; border-color: #e8332d; color: #e8332d; font-weight: 600; }
.ci { font-size: 24rpx; }
.grid { display: flex; flex-wrap: wrap; padding: 8rpx 16rpx; }
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
.cartbar { position: fixed; left: 20rpx; right: 20rpx; bottom: 32rpx; display: flex; justify-content: space-between; align-items: center; background: #e8332d; color: #fff; padding: 24rpx 30rpx; border-radius: 48rpx; box-shadow: 0 10rpx 30rpx rgba(232,51,45,.4); }
.cart-l { display: flex; align-items: center; gap: 10rpx; font-weight: 600; }
.cart-icon { font-size: 34rpx; }
.cart-go { background: #fff; color: #e8332d; font-weight: 700; padding: 10rpx 34rpx; border-radius: 40rpx; }
'''
open(os.path.join(root,'pages','menu','menu.js'),'w',encoding='utf-8',newline='\n').write(mjs)
open(os.path.join(root,'pages','menu','menu.wxml'),'w',encoding='utf-8',newline='\n').write(mwxml)
open(os.path.join(root,'pages','menu','menu.wxss'),'w',encoding='utf-8',newline='\n').write(mwxss)

print('home + menu pages created, app.json updated, old index removed')
