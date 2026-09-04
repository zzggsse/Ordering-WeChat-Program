# -*- coding: utf-8 -*-
import os, glob, sys
sys.stdout.reconfigure(encoding='utf-8', errors='replace')
root = r'D:\Order\miniapp'
left = []
for ext in ('*.js','*.wxml','*.wxss','*.json'):
    for p in glob.glob(os.path.join(root,'**',ext), recursive=True):
        t = open(p,'rb').read().decode('utf-8','replace')
        if '古茗' in t: left.append(os.path.relpath(p,root))
print('remaining 古茗:', left if left else 'NONE - all replaced')
# BOM 检查
bad = []
for ext in ('*.json','*.js','*.wxml','*.wxss'):
    for p in glob.glob(os.path.join(root,'**',ext), recursive=True):
        if open(p,'rb').read().startswith(b'\xef\xbb\xbf'): bad.append(os.path.relpath(p,root))
print('BOM files:', bad if bad else 'NONE')
