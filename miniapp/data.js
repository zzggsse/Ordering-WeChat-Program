const base = '/images/'
module.exports = {
  categories: ['经典奶茶', '果茶', '芝士奶盖', '鲜奶', '咖啡'],
  products: [
    { id: 1, name: '珍珠奶茶', category: '经典奶茶', price: 12, image: base + 'zhenzhu-naicha.png', sales: 1024, recommended: true },
    { id: 2, name: '茉莉奶绿', category: '果茶', price: 11, image: base + 'ganlan-naivlv.png', sales: 910, recommended: true },
    { id: 3, name: '杨枝甘露', category: '果茶', price: 18, image: base + 'yangzhi-manglu.png', sales: 866, recommended: true },
    { id: 4, name: '四季春芝士', category: '芝士奶盖', price: 16, image: base + 'zhizhi-naigai.png', sales: 742, soldout: true },
    { id: 5, name: '奶茶三兄弟', category: '经典奶茶', price: 14, image: base + 'naicha-sanxongdi.png', sales: 689 },
    { id: 6, name: '柠檬气泡水', category: '果茶', price: 13, image: base + 'ningmeng-qipa.png', sales: 645 },
    { id: 7, name: '椰奶拿铁', category: '咖啡', price: 19, image: base + 'yaliu-naicha.png', sales: 538 },
    { id: 8, name: '草莓奶昔', category: '鲜奶', price: 15, image: base + 'caomei-naihui.png', sales: 531 },
    { id: 9, name: '芋圆鲜奶', category: '鲜奶', price: 17, image: base + 'yuyuan-xiannai.png', sales: 402 },
    { id: 10, name: '桂花酒酿', category: '芝士奶盖', price: 13, image: base + 'guihua-jiuniang.png', sales: 328 }
  ]
}
