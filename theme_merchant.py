# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp\pages\merchant\workbench'
js = '''Page({
  data: {
    stats: [
      { label: '今日营收', value: '¥3268' },
      { label: '今日订单', value: '186' },
      { label: '待接单', value: '14' }
    ],
    orders: [
      { no: '20260831-0001', items: '珍珠奶茶 ×2 · 杨枝甘露 ×1', amount: 42, time: '10:24' },
      { no: '20260831-0005', items: '奶茶三兄弟 ×3 · 柠檬气泡水 ×1', amount: 55, time: '10:44' }
    ]
  },
  accept(e) {
    const no = e.currentTarget.dataset.no
    wx.showToast({ title: '已接单', icon: 'success' })
  }
})
'''
wxml = '''<view class="page">
  <view class="row" wx:for="{{stats}}" wx:key="label">
    <view class="cell"><view class="sv">{{item.value}}</view><view class="sl">{{item.label}}</view></view>
  </view>

  <view class="sec">待接单</view>
  <view class="order" wx:for="{{orders}}" wx:key="no">
    <view class="oh"><text class="ono">{{item.no}}</text><text class="time">{{item.time}}</text></view>
    <view class="oi">{{item.items}}</view>
    <view class="of">
      <view class="price">¥{{item.amount}}</view>
      <view class="btn" bindtap="accept" data-no="{{item.no}}">接单</view>
    </view>
  </view>

  <view class="sec">快捷操作</view>
  <view class="quick">
    <view class="q"><view class="qi">🍧</view><view>出杯叫号</view></view>
    <view class="q"><view class="qi">🚫</view><view>售罄设置</view></view>
    <view class="q"><view class="qi">📊</view><view>今日看板</view></view>
  </view>
</view>
'''
wxss = '''.page { padding: 20rpx; }
.row { display: flex; background: #e8332d; border-radius: 20rpx; padding: 30rpx 0; color: #fff; box-shadow: 0 8rpx 20rpx rgba(232,51,45,.3); }
.cell { flex: 1; text-align: center; }
.sv { font-size: 32rpx; font-weight: 700; }
.sl { font-size: 22rpx; opacity: .9; margin-top: 6rpx; }
.sec { font-weight: 700; margin: 26rpx 8rpx 14rpx; font-size: 30rpx; }
.order { background: #fff; border-radius: 20rpx; padding: 26rpx; margin-bottom: 16rpx; box-shadow: 0 4rpx 16rpx rgba(0,0,0,.05); }
.oh { display: flex; justify-content: space-between; color: #9b9b9b; font-size: 24rpx; }
.oi { font-size: 30rpx; font-weight: 600; margin: 16rpx 0; }
.of { display: flex; justify-content: space-between; align-items: center; }
.price { color: #e8332d; font-weight: 700; }
.btn { background: #e8332d; color: #fff; padding: 12rpx 44rpx; border-radius: 40rpx; font-weight: 600; }
.quick { display: flex; gap: 18rpx; }
.q { flex: 1; background: #fff; border-radius: 20rpx; text-align: center; padding: 30rpx 0; font-size: 24rpx; color: #606266; box-shadow: 0 4rpx 16rpx rgba(0,0,0,.05); }
.qi { font-size: 54rpx; margin-bottom: 10rpx; }
'''
open(base + '.js', 'w', encoding='utf-8', newline='\n').write(js)
open(base + '.wxml', 'w', encoding='utf-8', newline='\n').write(wxml)
open(base + '.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
print('merchant page themed')
