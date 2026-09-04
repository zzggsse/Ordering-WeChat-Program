# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp\pages\orders\orders'
js = '''const demoOrders = [
  { no: '20260831-0003', time: '10:37', items: '四季春芝士 ×2', amount: 32, queue: 'A108', status: 'ready', statusText: '已出杯，叫号中' },
  { no: '20260831-0002', time: '10:31', items: '茉莉奶绿 ×1 · 椰奶拿铁 ×1', amount: 30, queue: 'A106', status: 'making', statusText: '门店正在制作' },
  { no: '20260831-0001', time: '10:24', items: '珍珠奶茶 ×2 · 杨枝甘露 ×1', amount: 42, queue: 'A102', status: 'done', statusText: '已取餐' }
]
Page({
  data: { orders: demoOrders, active: 'all', tabs: [
    { key: 'all', label: '全部' },
    { key: 'making', label: '制作中' },
    { key: 'ready', label: '待取餐' },
    { key: 'done', label: '已完成' }
  ] },
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
      <view>{{item.no}}</view>
      <view class="st st-{{item.status}}">{{item.statusText}}</view>
    </view>
    <view class="oi">{{item.items}}</view>
    <view class="of">
      <view class="price">¥{{item.amount}}</view>
      <view class="queue" wx:if="{{item.queue}}">取餐号 {{item.queue}}</view>
    </view>
  </view>
</view>
'''
wxss = '''.page { padding: 20rpx; }
.tabs { display: flex; background: #fff; border-radius: 12rpx; padding: 8rpx; margin-bottom: 20rpx; }
.tab { flex: 1; text-align: center; padding: 16rpx 0; color: #666; border-radius: 8rpx; }
.tab.on { background: #ff9f43; color: #fff; font-weight: 600; }
.order { background: #fff; border-radius: 16rpx; padding: 24rpx; margin-bottom: 20rpx; }
.oh { display: flex; justify-content: space-between; color: #666; font-size: 24rpx; }
.st { color: #ff9f43; font-weight: 600; }
.st-making { color: #409eff; }
.st-done { color: #67c23a; }
.oi { margin: 16rpx 0; font-size: 28rpx; }
.of { display: flex; justify-content: space-between; align-items: center; }
.queue { font-size: 24rpx; color: #232946; background: #eef1ff; padding: 8rpx 20rpx; border-radius: 20rpx; }
'''
open(base + '.js', 'w', encoding='utf-8', newline='\n').write(js)
open(base + '.wxml', 'w', encoding='utf-8', newline='\n').write(wxml)
open(base + '.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
print('orders page rewritten, no Chinese class names')
