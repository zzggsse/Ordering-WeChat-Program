# -*- coding: utf-8 -*-
base = r'D:\Order\miniapp'
appjson = '''{
  "pages": [
    "pages/index/index",
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
    "navigationBarTitleText": "奶茶点单",
    "backgroundColor": "#f7f6f2",
    "backgroundTextStyle": "dark"
  },
  "tabBar": {
    "color": "#9b9b9b",
    "selectedColor": "#e8332d",
    "backgroundColor": "#ffffff",
    "borderStyle": "black",
    "list": [
      { "pagePath": "pages/index/index", "text": "首页" },
      { "pagePath": "pages/cart/cart", "text": "购物车" },
      { "pagePath": "pages/orders/orders", "text": "订单" },
      { "pagePath": "pages/mine/mine", "text": "我的" }
    ]
  },
  "style": "v2",
  "sitemapLocation": "sitemap.json"
}
'''
wxss = '''page {
  --guming-red: #e8332d;
  --guming-red-dark: #c0251f;
  --guming-bg: #f7f6f2;
  --guming-text: #333333;
  --guming-text-2: #7a7a7a;
  --guming-line: #f0e9e2;
  --guming-price: #e8332d;
  background: #f7f6f2;
  color: #333333;
  font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", "PingFang SC", "Microsoft YaHei", sans-serif;
}
.price { color: #e8332d; font-weight: 700; }
'''
open(base + r'\app.json', 'w', encoding='utf-8', newline='\n').write(appjson)
open(base + r'\app.wxss', 'w', encoding='utf-8', newline='\n').write(wxss)
print('global theme done')
