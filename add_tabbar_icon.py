# -*- coding: utf-8 -*-
import json, os
p = r'D:\Order\miniapp\app.json'
t = open(p,'rb').read().decode('utf-8')
j = json.loads(t)
tab = []
defs = [
    ('pages/home/home','首页','home'),
    ('pages/menu/menu','点单','menu'),
    ('pages/cart/cart','购物车','cart'),
    ('pages/orders/orders','订单','orders'),
    ('pages/mine/mine','我的','mine'),
]
for pg, text, icon in defs:
    tab.append({
        'pagePath': pg,
        'text': text,
        'iconPath': 'images/tabbar/%s.png' % icon,
        'selectedIconPath': 'images/tabbar/%s-active.png' % icon,
    })
j['tabBar']['list'] = tab
open(p,'w',encoding='utf-8',newline='\n').write(json.dumps(j, ensure_ascii=False, indent=2))
# 校验
j2 = json.loads(open(p,'rb').read().decode('utf-8'))
import glob
ok = True
for it in j2['tabBar']['list']:
    for key in ('iconPath','selectedIconPath'):
        fp = os.path.join(r'D:\Order\miniapp', it[key].replace('/', '\\'))
        if not os.path.exists(fp):
            ok = False; print('MISSING', it[key])
print('app.json updated, tabBar items:', len(j2['tabBar']['list']))
print('all icon files exist:', ok)
