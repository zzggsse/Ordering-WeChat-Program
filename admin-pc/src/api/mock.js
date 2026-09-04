// 课程项目演示用 mock 数据（后续可替换为真实接口）
export const productImages = (name) =>
  '/uploads/' + name + '.png'

export const dashboardStats = [
  { label: '今日营收', value: '¥ 3,268', trend: '+12.4%', icon: 'Money', color: '#67c23a' },
  { label: '今日订单', value: '186', trend: '+8.1%', icon: 'List', color: '#409eff' },
  { label: '待接单', value: '14', trend: '排队中', icon: 'Bell', color: '#e6a23c' },
  { label: '客单价', value: '¥ 17.6', trend: '+2.2%', icon: 'Star', color: '#f56c6c' },
]

export const menuList = [
  { id: 1, name: '珍珠奶茶', category: '经典奶茶', price: 12, image: 'zhenzhu-naicha', sales: 1024, status: 1 },
  { id: 2, name: '杨枝甘露', category: '果茶', price: 18, image: 'yangzhi-manglu', sales: 866, status: 1 },
  { id: 3, name: '四季春芝士', category: '芝士奶盖', price: 16, image: 'zhizhi-naigai', sales: 742, status: 1 },
  { id: 4, name: '草莓奶昔', category: '鲜奶', price: 15, image: 'caomei-naihui', sales: 531, status: 1 },
  { id: 5, name: '奶茶三兄弟', category: '经典奶茶', price: 14, image: 'naicha-sanxongdi', sales: 689, status: 0 },
  { id: 6, name: '茉莉奶绿', category: '果茶', price: 11, image: 'ganlan-naivlv', sales: 910, status: 1 },
  { id: 7, name: '桂花酒酿', category: '限定款', price: 13, image: 'guihua-jiuniang', sales: 328, status: 1 },
  { id: 8, name: '芋圆鲜奶', category: '鲜奶', price: 17, image: 'yuyuan-xiannai', sales: 402, status: 1 },
  { id: 9, name: '柠檬气泡水', category: '果茶', price: 13, image: 'ningmeng-qipa', sales: 645, status: 0 },
  { id: 10, name: '椰奶拿铁', category: '咖啡', price: 19, image: 'yaliu-naicha', sales: 538, status: 1 },
]

export const orderList = [
  { no: '20260831-0001', period: '10:24', type: '自取', items: '珍珠奶茶 x2 · 杨枝甘露 x1', amount: 42, status: '待接单' },
  { no: '20260831-0002', period: '10:31', type: '自取', items: '茉莉奶绿 x1 · 椰奶拿铁 x1', amount: 30, status: '制作中' },
  { no: '20260831-0003', period: '10:37', type: '外带', items: '四季春芝士 x2', amount: 32, status: '待取餐' },
  { no: '20260831-0004', period: '10:40', type: '自取', items: '草莓奶昔 x1', amount: 15, status: '已完成' },
  { no: '20260831-0005', period: '10:44', type: '外带', items: '奶茶三兄弟 x3 · 柠檬气泡水 x1', amount: 55, status: '待接单' },
  { no: '20260831-0006', period: '10:48', type: '自取', items: '桂花酒酿 x2', amount: 26, status: '制作中' },
]

export const inventoryList = [
  { id: 1, name: '红茶底', spec: 'L', stock: 18, threshold: 10, unit: '桶' },
  { id: 2, name: '珍珠（热带）', spec: '袋', stock: 6, threshold: 8, unit: '袋' },
  { id: 3, name: '鲜奶油', spec: 'L', stock: 3, threshold: 4, unit: '罐' },
  { id: 4, name: '芒果果肉', spec: 'g/包', stock: 24, threshold: 12, unit: '包' },
  { id: 5, name: '椰浆', spec: 'L', stock: 15, threshold: 6, unit: '罐' },
  { id: 6, name: '一次性杯', spec: '500ml', stock: 1200, threshold: 500, unit: '个' },
]
