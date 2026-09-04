# 奶茶点单系统 · 后端（Spring Boot 3）

配套技术设计《技术设计.md》实现的单体后端，供顾客端微信小程序与商家 PC 管理后台共用一套 REST API。

## 技术栈
- Java 17 + Spring Boot 3.4
- Spring Security + JWT（jjwt 0.12）
- MyBatis-Plus 3.5.9（分页、CRUD）
- MySQL 8.0（主数据）
- Spring Data Redis（依赖可选，当前未强依赖，未启动 Redis 也可运行）
- WebSocket（接单/出杯/叫号实时推送）
- springdoc-openapi（Swagger 在线文档）

## 目录结构
```
backend/src/main/java/com/tea/order
├─ config        # MyBatis-Plus 分页、静态资源、OpenAPI、WebSocket、数据初始化
├─ controller    # REST 入口（顾客端 + 管理端）
├─ service       # 业务逻辑
├─ mapper        # MyBatis-Plus 数据访问
├─ entity / dto / vo
├─ security      # JWT 签发/校验、登录态、Spring Security
├─ websocket     # 订单状态 / 叫号推送
└─ common        # 统一返回体、全局异常、状态枚举
```

## 运行方式

### 前置
- JDK 17+
- Maven 3.9+
- MySQL 8.0（本机已装）

### 1. 建库（首次）
```bash
mysql -uroot -p123456 -e "CREATE DATABASE IF NOT EXISTS tea_order DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```
> 表结构在 `src/main/resources/schema.sql`，种子数据在 `data.sql`，应用启动时自动执行（`spring.sql.init.mode=always`），重复启动已用 `IF NOT EXISTS`/`INSERT IGNORE` 保证幂等。

### 2. 启动
```bash
cd backend
mvn spring-boot:run
# 或打包
mvn package -DskipTests
java -jar target/tea-order-backend-1.0.0.jar
```
- 默认端口 `8080`
- Swagger：http://localhost:8080/swagger-ui.html
- 数据库连接默认 `root/123456@localhost:3306/tea_order`，可用 `DB_USERNAME`/`DB_PASSWORD`/`DB_HOST`/`DB_PORT` 覆盖。

### 3. 默认账号
| 端 | 账号 | 说明 |
| --- | --- | --- |
| PC 管理后台 | admin / 123456 | 首次启动自动创建 |
| 小程序微信授权 | 任意 code（如 demo1） | mock：code 即 openid，自动建档 |
| 小程序手机号登录 | 任意手机号 + 验证码 | mock：验证码直接返回，固定 `123456` 也可 |

### 4. 配置说明（application.yml）
- `app.pay.mock=true`：课程演示，`/pay` 与 `POST /api/v1/pay/mock-notify` 直接置订单已支付；接真实微信支付时置 false 并接入 V3 SDK。
- `app.sms.mock=true`、`app.sms.mock-code=123456`：短信 mock，`POST /auth/sms/send` 直接返回验证码；接真实短信网关时替换 `SmsService`。
- `app.upload.dir`：默认 `../uploads`（映射 `/uploads/**` 静态访问）。
- `app.jwt.secret`：生产务必用环境变量 `JWT_SECRET` 覆盖。

## 核心接口（前缀 `/api/v1`）
统一返回体 `{ code, message, data }`，`code=0` 成功。管理端接口需 `Authorization: Bearer <token>`。

| 模块 | 接口 |
| --- | --- |
| PC 登录 | `POST /auth/login` |
| 微信授权登录 | `POST /auth/wechat/login`（code/nickname/avatar/phone） |
| 手机号登录 | `POST /auth/sms/send`（发验证码）、`POST /auth/phone/login`（phone+code） |
| 当前用户 | `GET /auth/me` |
| 门店 | `GET/PUT /stores/current` |
| 商品/分类 | `GET /products`、`GET /products/{id}`（公开）；`POST/PUT/DELETE /products`、`PATCH /products/{id}/status`（管理） |
| 下单 | `POST /orders`、`GET /orders/mine`、`GET /orders/{id}`、`POST /orders/{id}/pay`、`POST /orders/{id}/cancel` |
| 管理订单 | `GET /admin/orders`、`POST /admin/orders/{id}/accept|ready|done|reject|refund/approve` |
| 支付回调 | `POST /pay/mock-notify`（模拟微信回调） |
| 看板 | `GET /dashboard/stats` |
| 库存 | `GET/POST /inventory`、`PUT/DELETE /inventory/{id}` |
| 会员 | `GET /members`、`GET /members/{id}`、`PATCH /members/{id}/level` |
| 营销 | `GET/POST /coupons`、`PUT/DELETE /coupons/{id}` |
| 员工 | `GET/POST /staff`、`PUT /staff/{id}`、`PATCH /staff/{id}/status`、`DELETE /staff/{id}` |
| 报表 | `GET /reports/sales`、`GET /reports/products`、`GET /reports/timeslots` |
| 上传 | `POST /upload`（返回 `/uploads/xxx` 相对路径） |

## 订单状态机
```
UNPAID(待支付) --支付/回调--> PAID(待接单) --接单--> MAKING(制作中) --出杯--> READY(待取餐) --取餐--> DONE(已完成)
   |                              |--拒单--> CANCELED(已取消)
   |--取消--> CANCELED            |--退款申请--> REFUNDING --审批--> REFUNDED(已退款)
```
关键流转通过 WebSocket（`/ws`）广播 `{type, orderNo, status, pickupNo, storeId}`。

## 小程序登录对接
- 网络层：`miniapp/utils/config.js`（改 baseUrl）、`request.js`、`api.js`
- 登录页：`pages/login/login`，支持「微信授权登录」与「手机号 + 验证码登录」双模式
- 「我的」页根据 token 调 `/auth/me` 展示登录态，支持退出登录
- 开发者工具需勾选「不校验合法域名」，真机调试把 `config.js` 的 baseUrl 改成电脑局域网 IP
