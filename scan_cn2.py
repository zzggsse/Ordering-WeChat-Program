# -*- coding: utf-8 -*-
import re, glob, os, sys
sys.stdout.reconfigure(encoding='utf-8', errors='replace')
root = r'D:\Order\miniapp'
han = re.compile(r'[\u4e00-\u9fff]')
hits = 0
for ext in ('*.wxss', '*.wxml'):
    for path in glob.glob(os.path.join(root, '**', ext), recursive=True):
        t = open(path, 'rb').read().decode('utf-8', errors='replace')
        rel = os.path.relpath(path, root)
        # WXSS 中文选择器 .xxx{ or .xxx, .yyy{
        sel = re.compile(r'\.([\u4e00-\u9fff][\w\u4e00-\u9fff-]*)\s*[,{ ]')
        for m in sel.finditer(t):
            print('WXSS chinese selector', rel, ': ', m.group(1)); hits += 1
        # WXML class 属性里含中文
        for i, ln in enumerate(t.split('\n'), 1):
            m = re.search(r'class="([^"]*[\u4e00-\u9fff][^"]*)"', ln)
            if m:
                print('WXML chinese class', rel, 'line', i, ':', m.group(1)); hits += 1
print('total hits:', hits)
