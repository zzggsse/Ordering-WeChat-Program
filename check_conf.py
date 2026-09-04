# -*- coding: utf-8 -*-
import os, re, sys
sys.stdout.reconfigure(encoding='utf-8', errors='replace')
root = r'D:\Order\miniapp'
for f in ('project.config.json','project.private.config.json'):
    p = os.path.join(root, f)
    if not os.path.exists(p):
        print(f, '-> not exists'); continue
    t = open(p,'rb').read().decode('utf-8','replace')
    # 找所有成串路径/页面字段
    print('==== ', f, ' ====')
    for m in re.finditer(r'"[^"\\]*(?:\\.[^"\\]*)*"', t):
        val = m.group(0)
        if 'index' in val or 'pages/' in val or 'compileType' in val or 'condition' in val:
            print('  ', val[:120])
