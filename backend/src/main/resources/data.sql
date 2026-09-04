-- 种子数据（INSERT IGNORE 避免重启重复插入）
INSERT IGNORE INTO store (id, name, address, phone, business_hours, status, created_at, updated_at)
VALUES (1, '中心店 · 示例', '示例市示例区某某路 88 号', '13800000000', '09:00-22:00', 1, NOW(), NOW());

INSERT IGNORE INTO category (id, store_id, name, sort, status) VALUES
 (1, 1, '经典奶茶', 1, 1),
 (2, 1, '果茶', 2, 1),
 (3, 1, '芝士奶盖', 3, 1),
 (4, 1, '鲜奶', 4, 1),
 (5, 1, '咖啡', 5, 1),
 (6, 1, '限定款', 6, 1);

INSERT IGNORE INTO product (id, store_id, category_id, category, name, image, price, description, sales, status, recommended, soldout, created_at, updated_at) VALUES
 (1, 1, 1, '经典奶茶', '珍珠奶茶', '/uploads/zhenzhu-naicha.png', 12.00, 'Q 弹珍珠 + 经典奶茶', 1024, 1, 1, 0, NOW(), NOW()),
 (2, 1, 2, '果茶', '茉莉奶绿', '/uploads/ganlan-naivlv.png', 11.00, '清冽茉莉茶底', 910, 1, 1, 0, NOW(), NOW()),
 (3, 1, 2, '果茶', '杨枝甘露', '/uploads/yangzhi-manglu.png', 18.00, '芒果果肉 满杯真料', 866, 1, 1, 0, NOW(), NOW()),
 (4, 1, 3, '芝士奶盖', '四季春芝士', '/uploads/zhizhi-naigai.png', 16.00, '手作芝士奶盖', 742, 1, 0, 1, NOW(), NOW()),
 (5, 1, 1, '经典奶茶', '奶茶三兄弟', '/uploads/naicha-sanxongdi.png', 14.00, '珍珠 布丁 仙草', 689, 1, 0, 0, NOW(), NOW()),
 (6, 1, 2, '果茶', '柠檬气泡水', '/uploads/ningmeng-qipa.png', 13.00, '柠檬片 + 气泡', 645, 1, 0, 0, NOW(), NOW()),
 (7, 1, 5, '咖啡', '椰奶拿铁', '/uploads/yaliu-naicha.png', 19.00, '咖啡 + 生椰乳', 538, 1, 0, 0, NOW(), NOW()),
 (8, 1, 4, '鲜奶', '草莓奶昔', '/uploads/caomei-naihui.png', 15.00, '整颗草莓榨打', 531, 1, 0, 0, NOW(), NOW()),
 (9, 1, 4, '鲜奶', '芋圆鲜奶', '/uploads/yuyuan-xiannai.png', 17.00, '手作芋圆', 402, 1, 0, 0, NOW(), NOW()),
 (10, 1, 6, '限定款', '桂花酒酿', '/uploads/guihua-jiuniang.png', 13.00, '桂花香 + 酒酿米香', 328, 1, 0, 0, NOW(), NOW());

INSERT IGNORE INTO inventory_item (id, store_id, name, spec, stock, threshold, unit, status, updated_at) VALUES
 (1, 1, '红茶底', 'L', 18, 10, '桶', 1, NOW()),
 (2, 1, '珍珠（热带）', '袋', 6, 8, '袋', 1, NOW()),
 (3, 1, '鲜奶油', 'L', 3, 4, '罐', 1, NOW()),
 (4, 1, '芒果果肉', 'g/包', 24, 12, '包', 1, NOW()),
 (5, 1, '椰浆', 'L', 15, 6, '罐', 1, NOW()),
 (6, 1, '一次性杯', '500ml', 1200, 500, '个', 1, NOW());

INSERT IGNORE INTO coupon (id, name, type, threshold, discount, total, used, start_at, end_at, status, created_at) VALUES
 (1, '新人立减 3 元', 'FULL_REDUCTION', 0.00, 3.00, 1000, 0, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 365 DAY, 1, NOW()),
 (2, '满 30 减 5', 'FULL_REDUCTION', 30.00, 5.00, 500, 0, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 365 DAY, 1, NOW());

-- 店主账号：手机号 13800000000 登录即为店长（可进商家工作台 + 员工管理）
INSERT INTO wechat_user (openid, phone, nickname, level, role, balance, total_orders, total_amount, created_at, updated_at)
VALUES ('phone_13800000000', '13800000000', '店长', '普通会员', '店长', 0, 0, 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE role = '店长';

-- 积分兑换优惠券（更多档位）
INSERT IGNORE INTO coupon (id, name, type, threshold, discount, points_cost, total, used, start_at, end_at, status, created_at) VALUES
 (3, '满 20 减 3', 'FULL_REDUCTION', 20.00, 3.00, 10, 500, 0, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 180 DAY, 1, NOW()),
 (4, '新人立减 5 元', 'FULL_REDUCTION', 0.00, 5.00, 15, 300, 0, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 180 DAY, 1, NOW()),
 (5, '满 50 减 8', 'FULL_REDUCTION', 50.00, 8.00, 30, 400, 0, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 180 DAY, 1, NOW()),
 (6, '满 80 减 10', 'FULL_REDUCTION', 80.00, 10.00, 40, 300, 0, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 180 DAY, 1, NOW()),
 (7, '满 100 减 15', 'FULL_REDUCTION', 100.00, 15.00, 50, 200, 0, NOW() - INTERVAL 1 DAY, NOW() + INTERVAL 180 DAY, 1, NOW());

-- 优惠券积分价（150 起步）与新人限领配置
UPDATE coupon SET points_cost=150, once_per_user=1 WHERE id=1;
UPDATE coupon SET points_cost=150, once_per_user=0 WHERE id=3;
UPDATE coupon SET points_cost=200, once_per_user=1 WHERE id=4;
UPDATE coupon SET points_cost=220, once_per_user=0 WHERE id=2;
UPDATE coupon SET points_cost=300, once_per_user=0 WHERE id=5;
UPDATE coupon SET points_cost=400, once_per_user=0 WHERE id=6;
UPDATE coupon SET points_cost=500, once_per_user=0 WHERE id=7;
