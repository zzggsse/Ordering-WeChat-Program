# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp\pages\orders\orders'
js = '''const demoOrders = [
  { no: '20260831-0003', time: '10:37', items: '四季春芝士 ×2', amount: 32, queue: 'A108', status: 'ready', statusText: '待取餐', tip: '已出杯，叫号中' },
  { no: '20260831-0002', time: '10:31', items: '茉莉奶绿 ×1 · 椰奶拿铁 ×1', amount: 30, queue: 'A106', status: 'making', statusText: '制作中', tip: '门店正在制作' },
  { no: '20260831-0001', time: '10:24', items: '珍珠奶茶 ×2 · 杨枝甘露 ×1', amount: 42, queue: 'A102', status: 'done', statusText: '已完成', tip: '已取餐' }
]
Page({
  data: {
    orders: demoOrders,
    active: 'all',
    tabs: [
      { key: 'all', label: '全部' },
      { key: 'making', label: '制作中' },
      { key: 'ready', label: '待取餐' },
      { key: 'done', label: '已完成' }
    ]
  },
  switchTab(e) { this.setData({ active: e.currentTarget.dataset.key }) },
  goDetail() { wx.navigateTo({ url: '/pages/order/detail' }) }
})
'''
wxml = '''<view class="page">
  <view class="tabs">
    <view class="tab {{active===t.key?'on':''}}" wx:for="{{tabs}}" wx:key="key"
          bindtap="switchTab" data-key="{{t.key}}">{{t.label}}</view>
  </view>

  <view class="order" wx:for="{{orders}}" wx:key="no" bindtap="goDetail">
    <view class="oh">
      <view class="ono">{{item.no}}</view>
      <view class="st st-{{item.status}}">{{item.statusText}}</view>
    </view>
    <view class="oi">{{item.items}}</view>
    <view class="of">
      <view class="queue" wx:if="{{item.queue}}">取餐号 <text class="qn">{{item.queue}}</text></view>
      <view class="price">¥{{item.amount}}</view>
    </view>
    <view class="tip" wx:if="{{item.tip}}">{{item.tip}}</view>
  </view>

  <view class="empty" wx:if="{{!orders.length}}">
    <view class="e-icon">📦</view>
    <view>暂无{{active==='all'?'':'对应'}}订单</view>
  </view>
</view>
'''
wxss = '''.page { padding: 16rpx; }
.tabs { display: flex; background: #fff; border-radius: 12rpx; padding: 10rpx; margin-bottom: 20rpx; }
.tab { flex: 1; text-align: center; padding: 16rpx 0; color: #7a7a7a; border-radius: 10rpx; font-size: 26rpx; }
.tab.on { background: #ffefeD; color: #e8332d; font-weight: 700; }
.order { background: #fff; border-radius: 20rpx; padding: 26rpx; margin-bottom: 16rpx; box-shadow: 0 4rpx 16rpx rgba(0,0,0,.05); }
.oh { display: flex; justify-content: space-between; align-items: center; }
.ono { font-size: 24rpx; color: #9b9b9b; }
.st { font-size: 28rpx; font-weight: 700; }
.st-ready { color: #e8332d; }
.st-making { color: #409eff; }
.st-done { color: #67c23a; }
.oi { margin: 20rpx 0 16rpx; font-size: 30rpx; font-weight: 600; }
.of { display: flex; justify-content: space-between; align-items: center; }
.queue { font-size: 26rpx; color: #7a7a7a; }
.qn { font-size: 40rpx; color: #e8332d; font-weight: 800; margin-left: 8rpx; }
.price { font-size: 34rpx; font-weight: 700; color: #333; }
.tip { margin-top: 16rpx; font-size: 22rpx; color: #9b9b9b; border-top: 1rpx solid #f6f2ec; padding-top: 14rpx; }
.empty { text-align: center; color: #9b9b9b; padding: 140rpx 0; }
.e-icon { font-size: 80rpx; margin-bottom: 20rpx; }
'''
open(base + '.js', 'w', encoding='utf-8', newline='\n').write(js)
open(base + '.wxml', 'w', encoding='utf-8', newline='\n').write(wxml)
open(base + '.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
print('orders page themed')
