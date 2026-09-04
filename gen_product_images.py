# -*- coding: utf-8 -*-
"""为课程项目生成统一风格的奶茶商品占位图（带奶茶杯造型），存入 uploads/。"""
import os
from PIL import Image, ImageDraw, ImageFont

OUT = os.path.join(os.path.dirname(os.path.abspath(__file__)), "uploads")
os.makedirs(OUT, exist_ok=True)

PRODUCTS = [
    ("zhenzhu-naicha",  "珍珠奶茶",  (200, 150, 110), (70, 45, 30),   (250, 230, 200)),
    ("yangzhi-manglu",  "杨枝甘露",  (255, 190, 90),  (210, 110, 35), (250, 240, 210)),
    ("zhizhi-naigai",   "四季春芝士", (245, 225, 190), (175, 140, 90), (250, 250, 240)),
    ("caomei-naihui",   "草莓奶昔",  (255, 170, 180), (205, 85, 110), (255, 235, 235)),
    ("naicha-sanxongdi","奶茶三兄弟", (200, 150, 110), (90, 60, 40),   (245, 225, 200)),
    ("ganlan-naivlv",   "茉莉奶绿",  (205, 230, 185), (110, 150, 110),(235, 250, 235)),
    ("guihua-jiuniang", "桂花酒酿",  (250, 210, 150), (190, 145, 80), (255, 245, 220)),
    ("yuyuan-xiannai",  "芋圆鲜奶",  (205, 165, 225), (120, 80, 165), (250, 240, 250)),
    ("ningmeng-qipa",   "柠檬气泡水",(255, 220, 120), (215, 175, 40), (250, 250, 230)),
    ("yaliu-naicha",    "椰奶拿铁",  (200, 170, 150), (110, 80, 60),  (250, 240, 230)),
]

SIZE = 600


def font(size):
    for p in [r"C:\Windows\Fonts\msyh.ttc", r"C:\Windows\Fonts\simhei.ttf"]:
        if os.path.exists(p):
            return ImageFont.truetype(p, size)
    return ImageFont.load_default()


def draw_cup(d, main, rim, inner):
    """在画布中央绘制奶茶杯造型。"""
    # 杯身（圆角矩形）
    body = (138, 200, 462, 505)
    d.rounded_rectangle(body, radius=40, fill=inner, outline=main, width=8)
    # 杯内液体高光（上部亮条）
    d.rounded_rectangle((150, 205, 450, 250), radius=16, fill=main)
    # 杯底珍珠
    for dx in (-95, -30, 40, 105):
        d.ellipse([body[0]+dx-16, body[3]-60, body[0]+dx+16, body[3]-28], fill=(70, 45, 30))
    # 杯盖（宽于杯口的圆角矩形）
    d.rounded_rectangle((124, 118, 476, 205), radius=26, fill=main, outline=rim, width=8)
    # 盖沿凸起小圆顶
    d.ellipse([228, 88, 372, 168], fill=main, outline=rim, width=8)
    # 吸管（贯穿杯盖进入液体）
    d.rounded_rectangle((330, 40, 366, 320), radius=18, fill=(250, 250, 250),
                        outline=(210, 210, 210), width=6)
    # 吸管顶端收口
    d.ellipse([330, 40, 366, 64], fill=(250, 250, 250), outline=(210, 210, 210), width=6)


def make(name, main, rim, inner):
    img = Image.new("RGB", (SIZE, SIZE))
    d = ImageDraw.Draw(img)
    # 对角渐变背景
    for y in range(SIZE):
        t = y / SIZE
        c = tuple(int(rim[i] + (main[i] - rim[i]) * t) for i in range(3))
        d.line([(0, y), (SIZE, y)], fill=c)
    # 杯中造型
    draw_cup(d, main, rim, inner)
    # 商品名（底部居中，两行）
    f = font(46)
    ln1 = name[:4]
    ln2 = name[4:] if len(name) > 4 else None
    if ln2:
        t = d.textlength(ln1, font=f)
        d.text(((SIZE - t) / 2, 520), ln1, font=f, fill=(255, 255, 255))
        t = d.textlength(ln2, font=f)
        d.text(((SIZE - t) / 2, 574), ln2, font=f, fill=(255, 255, 255))
    else:
        t = d.textlength(ln1, font=f)
        d.text(((SIZE - t) / 2, 545), ln1, font=f, fill=(255, 255, 255))
    return img


for fn, name, main, rim, inner in PRODUCTS:
    img = make(name, main, rim, inner)
    img.save(os.path.join(OUT, fn + ".png"))
    print("saved", fn + ".png")

rows = [("/uploads/" + p[0] + ".png", p[1]) for p in PRODUCTS]
with open(os.path.join(OUT, "README.md"), "w", encoding="utf-8") as f:
    f.write("# 商品图片目录（课程项目占位图）\n\n")
    f.write("> 带奶茶杯造型的统一风格占位图，无版权，用于课程演示。正式上线应替换为真实摄影图。\n\n")
    f.write("| 图片路径 | 商品名（示例） |\n| --- | --- |\n")
    for url, name in rows:
        f.write(f"| `{url}` | {name} |\n")
print("done:", len(rows))
