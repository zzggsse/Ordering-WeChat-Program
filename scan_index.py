# -*- coding: utf-8 -*-
import os, re, glob, sys
sys.stdout.reconfigure(encoding='utf-8', errors='replace')
root = r'D:\Order\miniapp'
hits = []
for ext in ('*.json','*.js','*.wxml','*.wxss'):
    for p in glob.glob(os.path.join(root,'**',ext), recursive=True):
        raw = open(p,'rb').read().decode('utf-8','replace')
        if re.search(r'pages/index/index|/pages/index', raw):
            hits.append(os.path.relpath(p, root))
print('references to old index page:', hits if hits else 'NONE')
print('--- project config files ---')
for f in ('project.config.json','project.private.config.json'):
    p=os.path.join(root,f)
    if os.path.exists(p):
        t=open(p,'rb').read().decode('utf-8','replace')
        print(f, 'contains index:', 'index' in t, '| contains compileType:', re.search(r'"compileType"\s*:\s*"[^"]*"', t))
