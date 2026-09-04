# -*- coding: utf-8 -*-
import os, glob
root = r'D:\Order\miniapp'
# 确认 app.json 标题
a = open(os.path.join(root,'app.json'),'rb').read().decode('utf-8')
import json
j = json.loads(a)
print('navigationBarTitleText:', j['window']['navigationBarTitleText'])
# 全量扫描残留"古茗"
left = []
for ext in ('*.js','*.wxml','*.wxss','*.json'):
    for p in glob.glob(os.path.join(root,'**',ext), recursive=True):
        t = open(p,'rb').read().decode('utf-8','replace')
        if '古茗' in t: left.append(os.path.relpath(p,root))
print('remaining 古茗 refs:', left if left else 'NONE')
print('奶奶泡的茶 occurrences:')
for ext in ('*.wxml','*.json','*.wxss','*.js'):
    for p in glob.glob(os.path.join(root,'**',ext), recursive=True):
        t = open(p,'rb').read().decode('utf-8','replace')
        n = t.count('奶奶泡的茶')
        if n: print(' ', os.path.relpath(p,root), 'x', n)
