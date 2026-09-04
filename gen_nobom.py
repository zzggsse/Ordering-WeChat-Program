# -*- coding: utf-8 -*-
import os, glob

root = r'D:\Order\miniapp'
exts = ('*.json', '*.js', '*.wxml', '*.wxss', '*.md')
changed = []
for ext in exts:
    for path in glob.glob(os.path.join(root, '**', ext), recursive=True):
        with open(path, 'rb') as f:
            raw = f.read()
        bom = raw.startswith(b'\xef\xbb\xbf')
        text = raw.decode('utf-8-sig')  # 自动去掉 BOM
        if bom:
            with open(path, 'w', encoding='utf-8', newline='\n') as f:
                f.write(text)
            changed.append(os.path.relpath(path, root))
        else:
            # 统一换行为 \n，避免其他问题
            norm = text.replace('\r\n', '\n')
            if text != norm:
                with open(path, 'w', encoding='utf-8', newline='\n') as f:
                    f.write(norm)
                changed.append(os.path.relpath(path, root) + ' (EOL)')
print('files rewritten:', len(changed))
for c in changed:
    print(' -', c)
