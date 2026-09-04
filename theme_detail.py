# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp\pages\order\detail'
js = '''Page({
  data: { order: { no: '20260831-0003', time: '2026-08-31 10:37', type: '自取', items: [{ name: '四季春芝士', qty: 2, price: 16 }], amount: 32, queue: 'A108', note: '少冰' } }
})
'''
wxml = '''<view class="page">
  <view class="hero">
    <view class="tag">🍹 正在叫号</view>
    <view class="qn">{{order.queue}}</view>
    <view class="qst">您的奶茶已准备好，请凭号取餐</view>
  </view>
  <view class="card">
    <view class="l"><text>订单号</text><text class="v">{{order.no}}</text></view>
    <view class="l"><text>下单时间</text><text class="v">{{order.time}}</text></view>
    <view class="l"><text>取餐方式</text><text class="v">{{order.type}}</text></view>
    <view class="l" wx:for="{{order.items}}" wx:key="name"><text>{{item.name}} ×{{item.qty}}</text><text class="v">¥{{item.price * item.qty}}</text></view>
    <view class="l" wx:if="{{order.note}}"><text>备注</text><text class="v">{{order.note}}</text></view>
    <view class="l total"><text>实付金额</text><text class="price">¥{{order.amount}}</text></view>
  </view>
  <view class="btn-row"><view class="again">再来一单</view><view class="help">联系门店</view></view>
</view>
'''
wxss = '''.hero { background: #e8332d; color: #fff; text-align: center; padding: 56rpx 0 66rpx; }
.tag { font-size: 24rpx; opacity: .9; }
.qn { font-size: 140rpx; font-weight: 800; line-height: 1.1; }
.qst { font-size: 28rpx; }
.card { background: #fff; margin: -34rpx 20rpx 0; border-radius: 20rpx; padding: 20rpx 26rpx; position: relative; box-shadow: 0 8rpx 24rpx rgba(0,0,0,.08); }
.l { display: flex; justify-content: space-between; padding: 20rpx 0; color: #9b9b9b; font-size: 28rpx; border-bottom: 1rpx solid #f6f2ec; }
.v { color: #333; }
.total { font-weight: 700; color: #333; }
.price { color: #e8332d; font-size: 36rpx; font-weight: 800; }
.btn-row { display: flex; gap: 20rpx; margin: 30rpx 20rpx; }
.again { flex: 1; text-align: center; background: #e8332d; color: #fff; padding: 22rpx 0; border-radius: 48rpx; font-weight: 600; }
.help { flex: 1; text-align: center; background: #fff; color: #7a7a7a; padding: 22rpx 0; border-radius: 48rpx; }
'''
open(base + '.js', 'w', encoding='utf-8', newline='\n').write(js)
open(base + '.wxml', 'w', encoding='utf-8', newline='\n').write(wxml)
open(base + '.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
