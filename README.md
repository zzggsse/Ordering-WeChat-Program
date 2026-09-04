# 奶奶泡的茶 · 奶茶点单系统

微信小程序 + Spring Boot 后端的奶茶在线点单系统，含顾客端小程序、店员/店长商家工作台与 PC 管理端（演示）。

## 目录结构

```
Order/
├── backend/     Spring Boot 后端（Java 17 + Maven + MyBatis-Plus + MySQL）
├── miniapp/     微信小程序（顾客点单 / 员工工作台 / 我的）
├── admin-pc/    PC 管理端（Vue3 + Vite，mock 演示）
├── public/      静态资源
├── uploads/     运行时上传文件（头像、商品图等）
├── 产品需求文档（PRD）.md
└── 技术设计.md
```

## 技术栈

- 后端：Java 17 · Spring Boot 3.4 · MyBatis-Plus · Spring Security(JWT) · MySQL · Redis · WebSocket · OpenAPI(Swagger)
- 小程序：微信原生小程序
- 管理端：Vue3 + Vite

## 功能

- 登录：微信授权登录 + 手机号短信验证码登录；未登录进入积分商城自动跳转登录
- 顾客端：首页 / 点单（分类、搜索、购物车）/ 下单支付（演示）/ 我的（改名换头像）/ 我的订单 / 积分商城
- 订单：底部导航「订单」页含 今日订单 / 历史订单 两个 Tab；顾客点击「已取餐」确认后订单变为已完成，超时 2 小时未确认自动完成；取餐号小的优先排队显示
- 点单 / 首页数据实时拉取自后端，商家下架或售罄的商品顾客端即时不可见、且后端拦截下单（提示「已下架 / 已售罄」）
- 积分商城：充值送积分、积分兑换优惠券（兑换价 150 积分起步）；「新人立减」单账号限兑一次，兑换后从列表消失
- 商家工作台（店员 / 店长）：今日订单接单→出杯→顾客取餐确认；库存管理（商品上下架、售罄、单品库存数量、原料库存）；店长可管理员工（按手机号搜索、授予 / 收回店员权限）
- 会员积分规则：普通会员消费 1:1 送积分；累计消费满 500 升黄金会员 1:1.2；单次充值满 500 升钻石会员 1:2；充值送积分
- 权限：顾客 / 店员 / 店长 三级角色；底部导航已配置图标

## 快速启动

### 1. 数据库（MySQL）

启动 MySQL，执行建库（表结构与初始数据在启动时由后端自动执行）：

```sql
CREATE DATABASE tea_order DEFAULT CHARACTER SET utf8mb4;
```

默认连接信息：`localhost:3306` / `tea_order` / `root` / `123456`，可用环境变量 `DB_HOST`、`DB_PORT`、`DB_USERNAME`、`DB_PASSWORD` 覆盖。

### 2. 后端

```bash
cd backend
mvn -DskipTests spring-boot:run
```

- 端口：`8080`
- 接口文档：`http://localhost:8080/swagger-ui.html`
- 短信 / 支付为演示模式，验证码固定为 `123456`

### 3. 小程序

- 用「微信开发者工具」导入 `miniapp/` 目录。
- 真机预览时修改 `miniapp/utils/config.js` 的 `baseUrl` 为局域网 IP。
- 需在小程序后台把后端域名加入合法域名，或开发阶段勾选「不校验合法域名」。

### 4. PC 管理端（可选，mock）

```bash
cd admin-pc
npm install && npm run dev
```

## 测试账号

| 角色 | 手机号 | 验证码 |
| --- | --- | --- |
| 店长 | 13800000000 | 123456 |
| 顾客 | 13444445555 | 123456 |

## 说明

- `backend/src/main/resources/schema.sql`、`data.sql` 为表结构与演示数据，启动时自动初始化。
- 开发演示项目，短信、微信支付为 mock，上线前请替换为真实服务。
