# -*- coding: utf-8 -*-
import os, glob, sys
sys.stdout.reconfigure(encoding='utf-8', errors='replace')
root = r'D:\Order\miniapp'
# 要替换的文案
repls = {
    '古茗': '奶奶泡的茶',
}
paths = ['app.json'] + glob.glob(os.path.join(root,'pages','**','*'), recursive=True)
changed = []
for p in paths:
    if not os.path.isfile(p) or not p.endswith(('.js','.wxml','.wxss','.json')):
        continue
    raw = open(p,'rb').read().decode('utf-8', errors='replace')
    # 避免把代码里无关的"古茗"误改（本就只有文案）
    if '古茗' in raw:
        # 特例：品牌 hero 想强调店名，处理常见两种写法
        new = raw.replace('古茗 · 点单','奶奶泡的茶').replace('🍹 古茗','🍵 奶奶泡的茶')
        new = new.replace('古茗', '奶奶泡的茶')
        changed.append(os.path.relpath(p, root))
        open(p,'w',encoding='utf-8',newline='\n').write(new)
print('files updated:')
for c in changed: print(' -', c)
print('updated files:', len(changed))
