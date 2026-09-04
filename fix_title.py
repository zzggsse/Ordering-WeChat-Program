# -*- coding: utf-8 -*-
import json, os
p = r'D:\Order\miniapp\app.json'
t = open(p,'rb').read().decode('utf-8')
t = t.replace('古茗', '奶奶泡的茶')
open(p,'w',encoding='utf-8',newline='\n').write(t)
j = json.loads(t)
print('app.json navigationBarTitleText =', j['window']['navigationBarTitleText'])
print('remaining 古茗 in app.json:', '古茗' in t)
