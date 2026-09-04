# -*- coding: utf-8 -*-
import os
from PIL import Image, ImageDraw

out_dir = r'D:\Order\miniapp\images\tabbar'
os.makedirs(out_dir, exist_ok=True)
S = 81  # 微信 tabBar 推荐尺寸

GRAY = (155, 155, 155, 255)
RED  = (232, 51, 45, 255)

def draw_icon(d, name):
    w = 6  # 线宽
    if name == 'home':
        # 屋顶 + 房子 + 门
        d.polygon([(10,36),(40,12),(70,36)], outline=None, width=0)
        d.line([(10,36),(40,12),(70,36)], fill=0, width=w)
        d.line([(16,32),(16,66),(64,66),(64,32)], fill=0, width=w)
        d.rectangle([34,48,46,66], outline=0, width=w)
    elif name == 'menu':
        # 奶茶杯: 盖子 + 杯身 + 吸管
        d.line([(24,20),(56,20)], fill=0, width=w)          # 上沿
        d.line([(28,20),(30,62)], fill=0, width=w)          # 左杯壁
        d.line([(52,20),(50,62)], fill=0, width=w)          # 右杯壁
        d.line([(30,62),(50,62)], fill=0, width=w)          # 杯底
        d.rectangle([30,26,44,44], fill=0, width=0)         # 杯内液体
        d.rectangle([30,26,44,44], outline=0, width=w)
        # 吸管
        d.line([(52,14),(58,60)], fill=0, width=7)
    elif name == 'cart':
        # 购物车
        d.line([(14,26),(70,26)], fill=0, width=w)          # 把手上沿
        d.line([(18,30),(64,30)], fill=0, width=w)          # 提手底部线
        d.line([(30,30),(26,58),(64,58)], fill=0, width=w)  # 车篮
        d.line([(26,58),(58,66)], fill=0, width=w)
        d.ellipse([40,64,50,74], outline=0, width=3)        # 轮子
        d.ellipse([58,64,68,74], outline=0, width=3)
    elif name == 'orders':
        # 单据
        d.rectangle([20,14,60,66], outline=0, width=w)
        d.line([(28,24),(52,24)], fill=0, width=4)
        d.line([(28,34),(52,34)], fill=0, width=4)
        d.line([(28,44),(44,44)], fill=0, width=4)
        d.line([(28,54),(52,54)], fill=0, width=4)
    elif name == 'mine':
        # 人形
        d.ellipse([30,14,50,34], outline=0, width=w)        # 头
        d.arc([18,34,62,78], 0, 360, fill=0, width=w)       # 肩/身弧
        d.line([(24,44),(24,62)], fill=0, width=w)
        d.line([(56,44),(56,62)], fill=0, width=w)

def gen(name, color):
    img = Image.new('RGBA', (S, S), (0,0,0,0))
    d = ImageDraw.Draw(img)
    draw_icon(d, name)
    # 把轮廓着色的简化法：先生成白色线稿再重造？此处直接替换 alpha 填充色
    # 由于上面用的是黑色线条，这里通过合成把黑色换成指定色
    px = img.load()
    for y in range(S):
        for x in range(S):
            r,g,b,a = px[x,y]
            if a > 0 and r < 60 and g < 60 and b < 60:
                px[x,y] = (color[0], color[1], color[2], a)
    return img

for name in ['home','menu','cart','orders','mine']:
    gen(name, GRAY).save(os.path.join(out_dir, name + '.png'))
    gen(name, RED).save(os.path.join(out_dir, name + '-active.png'))
print('generated 10 tabbar icons in', out_dir)
print(sorted(os.listdir(out_dir)))
