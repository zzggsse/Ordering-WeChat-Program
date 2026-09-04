# -*- coding: utf-8 -*-
import re, glob, os
root=r'D:\Order\miniapp'
pat = re.compile(r'[\u4e00-\u9fff]+')
for ext in ('*.wxss','*.wxml'):
    for path in glob.glob(os.path.join(root,'**',ext), recursive=True):
        raw=open(path,'rb').read()
        try: t=raw.decode('utf-8')
        except: continue
        for i,ln in enumerate(t.split('\n'),1):
            # 类名选择器 或 wxml 中 class 绑定的中文
            if ('.' in ln and pat.search(ln) and ('{:' in ln)) or ('class="' in ln and pat.search(ln)):
                print(f'{os.path.relpath(path,root)}:{i}: {ln.strip()}')
