/*
 Navicat Premium Data Transfer

 Source Server         : xue_xi
 Source Server Type    : MySQL
 Source Server Version : 80300 (8.3.0)
 Source Host           : localhost:3306
 Source Schema         : jieban

 Target Server Type    : MySQL
 Target Server Version : 80300 (8.3.0)
 File Encoding         : 65001

 Date: 27/01/2025 19:30:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for db_account
-- ----------------------------
DROP TABLE IF EXISTS `db_account`;
CREATE TABLE `db_account`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `registertime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of db_account
-- ----------------------------
INSERT INTO `db_account` VALUES (6, 'admin', '$2a$10$9ZR.J/NNRsEL9xawrEcuReTx2vNP3LZbCdhcOOB4QbMYycEVfc9QG', 'admin', '2024-12-26 10:58:19');
INSERT INTO `db_account` VALUES (8, 'testuser', '$2a$10$PLS1hqlWaXzbttEVTiv.QO8yscq/tJTH04R8gcCyrdgqbRQ1uq7J6', 'user', '2024-12-26 12:25:42');
INSERT INTO `db_account` VALUES (15, 'test1', '$2a$10$vcUA84i.yh1xnSYB/OEjsurtLGLXpIonwFLrk5.FVCJgwJspBnZSu', 'user', '2025-01-26 18:47:33');
INSERT INTO `db_account` VALUES (16, 'test2', '$2a$10$WP9PJBdwHQDOgB8jN7PUV.Axo3ci2Yb54tv7JNps0bgCQsvlMz9qC', 'user', '2025-01-26 18:47:37');
INSERT INTO `db_account` VALUES (17, 'test3', '$2a$10$38ZSh5CHxLy5DQNVKawbGOkTKVZIOpiI0iQhrE9OPHN5/kIl5P446', 'user', '2025-01-26 18:47:59');
INSERT INTO `db_account` VALUES (18, 'test4', '$2a$10$AuOpLoQ1c1AYxPRmRIjC7Oc6kkxXMlC4hHBpKxjaX7bjSaC2.cqK6', 'user', '2025-01-26 18:48:02');

-- ----------------------------
-- Table structure for match_record
-- ----------------------------
DROP TABLE IF EXISTS `match_record`;
CREATE TABLE `match_record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id_1` int NOT NULL COMMENT '发起匹配的用户id',
  `user_id_2` int NOT NULL COMMENT '被匹配的用户id',
  `match_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '匹配类pair结对/time按时间/place按地点/complement互补',
  `subject_id` int NULL DEFAULT NULL COMMENT '匹配的科目id',
  `place_id` int NULL DEFAULT NULL COMMENT '匹配的场地id',
  `time_slot` json NULL COMMENT '匹配的时间段,格式:{weekday:1-7,time:HH:mm-HH:mm}',
  `match_score` decimal(5, 2) NULL DEFAULT NULL COMMENT '匹配得分',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态:0待确认,1已确认,2已拒绝,3已完成',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '匹配记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of match_record
-- ----------------------------
INSERT INTO `match_record` VALUES (12, 15, 16, 'pair', 1, NULL, NULL, 50.00, 1, '2025-01-27 03:52:32', '2025-01-27 16:00:07', NULL);
INSERT INTO `match_record` VALUES (13, 15, 16, 'complement', NULL, NULL, NULL, 90.00, 1, '2025-01-27 04:10:13', '2025-01-27 16:01:05', NULL);
INSERT INTO `match_record` VALUES (14, 15, 8, 'time', NULL, NULL, NULL, 50.00, 0, '2025-01-27 04:18:08', NULL, NULL);
INSERT INTO `match_record` VALUES (15, 15, 8, 'place', NULL, NULL, NULL, 40.00, 0, '2025-01-27 04:33:13', NULL, NULL);

-- ----------------------------
-- Table structure for place
-- ----------------------------
DROP TABLE IF EXISTS `place`;
CREATE TABLE `place`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '场地名称',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '具体位置',
  `available_time` json NULL COMMENT '可用时间段,格式:[{weekday:1-7,time:HH:mm-HH:mm}]',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态:1启用,0禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '场地表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of place
-- ----------------------------
INSERT INTO `place` VALUES (1, '图书馆', 'A区二楼', '[{\"time\": \"08:00-22:00\", \"weekday\": 1}]', 1, '2025-01-25 22:12:19', '2025-01-25 22:15:07');
INSERT INTO `place` VALUES (2, '自习室', '教学楼B区', '[{\"time\": \"08:00-18:00\", \"weekday\": 1}, {\"time\": \"08:00-18:00\", \"weekday\": 2}]', 1, '2025-01-25 22:13:10', '2025-01-25 22:13:10');

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '发布用户id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型:help求助/resource资源',
  `subject_id` int NULL DEFAULT NULL COMMENT '相关科目id',
  `place_id` int NULL DEFAULT NULL COMMENT '期望学习地点id',
  `time_slot` json NULL COMMENT '期望时间段,格式:{weekday:1-7,time:HH:mm-HH:mm}',
  `invite_enabled` tinyint NOT NULL DEFAULT 0 COMMENT '是否接受学习邀请:0关闭,1开放',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态:0待审核,1已发布,2已关闭',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_subject`(`subject_id` ASC) USING BTREE,
  INDEX `idx_place`(`place_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------
INSERT INTO `post` VALUES (1, 8, '求助高数题目(管理员测试修改)', '求解高等数学第三章习题', 'help', 1, 1, '{\"time\": \"19:00-21:00\", \"weekday\": 1}', 1, 1, '2025-01-26 02:33:05', '2025-01-27 19:00:20');

-- ----------------------------
-- Table structure for post_subject
-- ----------------------------
DROP TABLE IF EXISTS `post_subject`;
CREATE TABLE `post_subject`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL COMMENT '信息id',
  `subject_id` int NOT NULL COMMENT '科目id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_post_subject`(`post_id` ASC, `subject_id` ASC) USING BTREE COMMENT '帖子-科目唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '信息-科目关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_subject
-- ----------------------------

-- ----------------------------
-- Table structure for study_request
-- ----------------------------
DROP TABLE IF EXISTS `study_request`;
CREATE TABLE `study_request`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NULL DEFAULT NULL COMMENT '关联帖子id,可以为空',
  `from_user_id` int NOT NULL COMMENT '发起请求的用户id',
  `to_user_id` int NOT NULL COMMENT '接收请求的用户id',
  `message` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求留言',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态:0待处理,1已接受,2已拒绝',
  `place_id` int NULL DEFAULT NULL COMMENT '期望学习地点',
  `time_slot` json NULL COMMENT '期望学习时间,格式:{weekday:1-7,time:HH:mm-HH:mm}',
  `create_time` datetime NOT NULL COMMENT '发起时间',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_post`(`post_id` ASC) USING BTREE,
  INDEX `idx_from_user`(`from_user_id` ASC) USING BTREE,
  INDEX `idx_to_user`(`to_user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学习请求表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of study_request
-- ----------------------------
INSERT INTO `study_request` VALUES (1, 1, 15, 16, '一起学习吧', 1, 1, '{\"time\": \"19:00-21:00\", \"weekday\": 1}', '2025-01-27 18:07:33', '2025-01-27 18:14:17');
INSERT INTO `study_request` VALUES (2, 1, 15, 16, '一起学习吧2', 2, 1, '{\"time\": \"19:00-21:00\", \"weekday\": 1}', '2025-01-27 18:10:30', '2025-01-27 18:14:22');
INSERT INTO `study_request` VALUES (3, 1, 15, 16, '一起学习吧3', 0, 1, '{\"time\": \"19:00-21:00\", \"weekday\": 1}', '2025-01-27 18:10:34', NULL);

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '科目名称',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态:1启用,0禁用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE COMMENT '科目名称唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '科目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES (1, '高等数学A', 1, '2025-01-25 21:22:53', '2025-01-25 21:27:31');
INSERT INTO `subject` VALUES (2, '语文', 1, '2025-01-26 20:58:22', '2025-01-26 20:58:22');
INSERT INTO `subject` VALUES (3, '大学英语', 1, '2025-01-26 20:58:31', '2025-01-26 20:58:31');
INSERT INTO `subject` VALUES (4, '数据结构', 1, '2025-01-26 20:58:39', '2025-01-26 20:58:39');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `account_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `operation` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `time` bigint NULL DEFAULT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '接口响应数据',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `account_id`(`account_id` ASC) USING BTREE,
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `db_account` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1992 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (1884, 6, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-23 18:43:25', NULL);
INSERT INTO `sys_log` VALUES (1885, 6, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-23 20:36:19', NULL);
INSERT INTO `sys_log` VALUES (1886, 6, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-23 20:36:33', NULL);
INSERT INTO `sys_log` VALUES (1887, 6, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-23 20:37:33', NULL);
INSERT INTO `sys_log` VALUES (1888, NULL, '注册', 'com.example.controller.AuthorizeController.register', '[{\"password\":\"admin123\",\"username\":\"admin\"}]', 438, '127.0.0.1', '2025-01-25 20:38:42', NULL);
INSERT INTO `sys_log` VALUES (1889, 8, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-25 20:38:49', NULL);
INSERT INTO `sys_log` VALUES (1890, 8, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-25 20:40:06', NULL);
INSERT INTO `sys_log` VALUES (1891, 6, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-25 21:08:41', NULL);
INSERT INTO `sys_log` VALUES (1892, 8, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-26 02:31:19', NULL);
INSERT INTO `sys_log` VALUES (1893, 6, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-26 02:34:08', NULL);
INSERT INTO `sys_log` VALUES (1894, NULL, '注册', 'com.example.controller.AuthorizeController.register', '[{\"password\":\"123456\",\"username\":\"test1\"}]', 506, '127.0.0.1', '2025-01-26 18:47:33', NULL);
INSERT INTO `sys_log` VALUES (1895, NULL, '注册', 'com.example.controller.AuthorizeController.register', '[{\"password\":\"123456\",\"username\":\"test2\"}]', 71, '127.0.0.1', '2025-01-26 18:47:37', NULL);
INSERT INTO `sys_log` VALUES (1896, NULL, '注册', 'com.example.controller.AuthorizeController.register', '[{\"password\":\"123456\",\"username\":\"test3\"}]', 77, '127.0.0.1', '2025-01-26 18:47:59', NULL);
INSERT INTO `sys_log` VALUES (1897, NULL, '注册', 'com.example.controller.AuthorizeController.register', '[{\"password\":\"123456\",\"username\":\"test4\"}]', 74, '127.0.0.1', '2025-01-26 18:48:02', NULL);
INSERT INTO `sys_log` VALUES (1898, 15, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-26 20:59:42', NULL);
INSERT INTO `sys_log` VALUES (1899, 15, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[1,2],[3,4]]', 319, '127.0.0.1', '2025-01-26 21:02:12', NULL);
INSERT INTO `sys_log` VALUES (1900, 15, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[1,2],[3,4]]', 24, '127.0.0.1', '2025-01-26 21:02:21', NULL);
INSERT INTO `sys_log` VALUES (1901, 15, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[1,2],[3,4]]', 256, '127.0.0.1', '2025-01-26 21:07:18', NULL);
INSERT INTO `sys_log` VALUES (1902, 15, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 38, '127.0.0.1', '2025-01-26 21:08:38', NULL);
INSERT INTO `sys_log` VALUES (1903, 15, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[],[]]', 13, '127.0.0.1', '2025-01-26 21:10:38', NULL);
INSERT INTO `sys_log` VALUES (1904, 15, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[1,3],[2]]', 20, '127.0.0.1', '2025-01-26 21:10:54', NULL);
INSERT INTO `sys_log` VALUES (1905, 15, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 339, '127.0.0.1', '2025-01-26 21:17:17', NULL);
INSERT INTO `sys_log` VALUES (1906, 15, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 309, '127.0.0.1', '2025-01-26 21:20:13', NULL);
INSERT INTO `sys_log` VALUES (1907, 15, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 13, '127.0.0.1', '2025-01-26 21:25:09', NULL);
INSERT INTO `sys_log` VALUES (1908, 15, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 9, '127.0.0.1', '2025-01-26 21:29:26', NULL);
INSERT INTO `sys_log` VALUES (1909, 8, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-26 21:32:37', NULL);
INSERT INTO `sys_log` VALUES (1910, 8, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 6, '127.0.0.1', '2025-01-26 21:32:44', NULL);
INSERT INTO `sys_log` VALUES (1911, 8, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 7, '127.0.0.1', '2025-01-26 21:36:39', NULL);
INSERT INTO `sys_log` VALUES (1912, 8, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 345, '127.0.0.1', '2025-01-26 21:37:57', NULL);
INSERT INTO `sys_log` VALUES (1913, 15, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 14, '127.0.0.1', '2025-01-26 21:38:47', NULL);
INSERT INTO `sys_log` VALUES (1914, 16, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-27 02:03:16', NULL);
INSERT INTO `sys_log` VALUES (1915, 16, '更新用户画像', 'com.example.controller.UserProfileController.updateProfile', '[{\"availableTimes\":[{\"time\":\"19:00-21:00\",\"weekday\":1},{\"time\":\"09:00-11:00\",\"weekday\":4}],\"className\":\"计科2班\",\"phone\":\"123123646\",\"preferredPlaces\":[2,3],\"realName\":\"测试用户2\"}]', 424, '127.0.0.1', '2025-01-27 02:05:11', NULL);
INSERT INTO `sys_log` VALUES (1916, 16, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[2],[1]]', 11, '127.0.0.1', '2025-01-27 02:07:43', NULL);
INSERT INTO `sys_log` VALUES (1917, 17, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-27 02:07:59', NULL);
INSERT INTO `sys_log` VALUES (1918, 17, '更新用户画像', 'com.example.controller.UserProfileController.updateProfile', '[{\"availableTimes\":[{\"time\":\"19:00-21:00\",\"weekday\":2},{\"time\":\"14:00-16:00\",\"weekday\":5}],\"className\":\"软工1班\",\"phone\":\"123123647\",\"preferredPlaces\":[1,3],\"realName\":\"测试用户3\"}]', 10, '127.0.0.1', '2025-01-27 02:08:26', NULL);
INSERT INTO `sys_log` VALUES (1919, 17, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[1,2],[]]', 8, '127.0.0.1', '2025-01-27 02:08:46', NULL);
INSERT INTO `sys_log` VALUES (1920, 18, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-27 02:08:57', NULL);
INSERT INTO `sys_log` VALUES (1921, 18, '更新用户画像', 'com.example.controller.UserProfileController.updateProfile', '[{\"availableTimes\":[{\"time\":\"19:00-21:00\",\"weekday\":3},{\"time\":\"09:00-11:00\",\"weekday\":6}],\"className\":\"软工2班\",\"phone\":\"123123648\",\"preferredPlaces\":[2,4],\"realName\":\"测试用户4\"}]', 10, '127.0.0.1', '2025-01-27 02:09:18', NULL);
INSERT INTO `sys_log` VALUES (1922, 18, '更新用户科目绑定', 'com.example.controller.UserProfileController.updateSubjects', '[[],[1,2]]', 18, '127.0.0.1', '2025-01-27 02:09:33', NULL);
INSERT INTO `sys_log` VALUES (1923, 15, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-27 02:14:39', NULL);
INSERT INTO `sys_log` VALUES (1924, 15, '按科目匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 297, '127.0.0.1', '2025-01-27 02:26:58', NULL);
INSERT INTO `sys_log` VALUES (1925, 15, '按科目匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 326, '127.0.0.1', '2025-01-27 02:29:55', NULL);
INSERT INTO `sys_log` VALUES (1926, 15, '按科目匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 324, '127.0.0.1', '2025-01-27 02:31:07', NULL);
INSERT INTO `sys_log` VALUES (1927, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 9504, '127.0.0.1', '2025-01-27 02:44:06', NULL);
INSERT INTO `sys_log` VALUES (1928, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 9162, '127.0.0.1', '2025-01-27 02:48:38', NULL);
INSERT INTO `sys_log` VALUES (1929, 15, '获取用户画像', 'com.example.controller.UserProfileController.getUserProfile', '[]', 10, '127.0.0.1', '2025-01-27 02:58:06', NULL);
INSERT INTO `sys_log` VALUES (1930, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 451, '127.0.0.1', '2025-01-27 03:05:51', NULL);
INSERT INTO `sys_log` VALUES (1931, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 442, '127.0.0.1', '2025-01-27 03:24:05', NULL);
INSERT INTO `sys_log` VALUES (1932, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 66, '127.0.0.1', '2025-01-27 03:25:28', NULL);
INSERT INTO `sys_log` VALUES (1933, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 419, '127.0.0.1', '2025-01-27 03:39:44', NULL);
INSERT INTO `sys_log` VALUES (1934, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 53, '127.0.0.1', '2025-01-27 03:39:54', NULL);
INSERT INTO `sys_log` VALUES (1935, 15, '结对匹配', 'com.example.controller.MatchController.matchBySubject', '[]', 397, '127.0.0.1', '2025-01-27 03:52:32', NULL);
INSERT INTO `sys_log` VALUES (1936, 15, '互补匹配', 'com.example.controller.MatchController.matchByComplement', '[]', 370, '127.0.0.1', '2025-01-27 04:10:13', NULL);
INSERT INTO `sys_log` VALUES (1937, 15, '按时间匹配', 'com.example.controller.MatchController.matchByTime', '[]', 379, '127.0.0.1', '2025-01-27 04:18:08', NULL);
INSERT INTO `sys_log` VALUES (1938, 15, '按地点匹配', 'com.example.controller.MatchController.matchByPlace', '[]', 439, '127.0.0.1', '2025-01-27 04:33:13', NULL);
INSERT INTO `sys_log` VALUES (1939, 15, '获取待确认的匹配请求', 'com.example.controller.MatchController.getPendingMatches', '[]', 357, '127.0.0.1', '2025-01-27 15:55:05', NULL);
INSERT INTO `sys_log` VALUES (1940, 15, '获取待确认的匹配请求', 'com.example.controller.MatchController.getPendingMatches', '[]', 3, '127.0.0.1', '2025-01-27 15:55:43', NULL);
INSERT INTO `sys_log` VALUES (1941, 15, '获取用户的匹配记录', 'com.example.controller.MatchController.getUserMatches', '[]', 23, '127.0.0.1', '2025-01-27 15:55:48', NULL);
INSERT INTO `sys_log` VALUES (1942, 15, '获取待确认的匹配请求', 'com.example.controller.MatchController.getPendingMatches', '[]', 4, '127.0.0.1', '2025-01-27 15:57:19', NULL);
INSERT INTO `sys_log` VALUES (1943, 15, '确认匹配', 'com.example.controller.MatchController.confirmMatch', '[15]', 4, '127.0.0.1', '2025-01-27 15:58:19', NULL);
INSERT INTO `sys_log` VALUES (1944, 16, '确认匹配', 'com.example.controller.MatchController.confirmMatch', '[15]', 3, '127.0.0.1', '2025-01-27 15:59:11', NULL);
INSERT INTO `sys_log` VALUES (1945, 16, '确认匹配', 'com.example.controller.MatchController.confirmMatch', '[15]', 3, '127.0.0.1', '2025-01-27 15:59:15', NULL);
INSERT INTO `sys_log` VALUES (1946, 16, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-27 15:59:39', NULL);
INSERT INTO `sys_log` VALUES (1947, 16, '确认匹配', 'com.example.controller.MatchController.confirmMatch', '[12]', 13, '127.0.0.1', '2025-01-27 16:00:07', NULL);
INSERT INTO `sys_log` VALUES (1948, 16, '确认匹配', 'com.example.controller.MatchController.confirmMatch', '[13]', 4, '127.0.0.1', '2025-01-27 16:01:06', NULL);
INSERT INTO `sys_log` VALUES (1949, 15, '获取用户的匹配记录', 'com.example.controller.MatchController.getUserMatches', '[]', 19, '127.0.0.1', '2025-01-27 16:01:16', NULL);
INSERT INTO `sys_log` VALUES (1950, 16, '获取待确认的匹配请求', 'com.example.controller.MatchController.getPendingMatches', '[]', 3, '127.0.0.1', '2025-01-27 16:01:35', NULL);
INSERT INTO `sys_log` VALUES (1951, 8, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-27 16:01:56', NULL);
INSERT INTO `sys_log` VALUES (1952, 8, '获取待确认的匹配请求', 'com.example.controller.MatchController.getPendingMatches', '[]', 10, '127.0.0.1', '2025-01-27 16:02:04', NULL);
INSERT INTO `sys_log` VALUES (1953, 15, '发起学习请求', 'com.example.controller.StudyRequestController.createRequest', '[{\"message\":\"一起学习吧\",\"placeId\":1,\"postId\":1,\"timeSlot\":{\"time\":\"19:00-21:00\",\"weekday\":1},\"toUserId\":16}]', 319, '127.0.0.1', '2025-01-27 18:05:12', NULL);
INSERT INTO `sys_log` VALUES (1954, 15, '获取学习请求列表', 'com.example.controller.StudyRequestController.getUserRequests', '[]', 35, '127.0.0.1', '2025-01-27 18:05:53', NULL);
INSERT INTO `sys_log` VALUES (1955, 15, '获取学习请求列表', 'com.example.controller.StudyRequestController.getUserRequests', '[]', 265, '127.0.0.1', '2025-01-27 18:07:28', NULL);
INSERT INTO `sys_log` VALUES (1956, 15, '发起学习请求', 'com.example.controller.StudyRequestController.createRequest', '[{\"message\":\"一起学习吧\",\"placeId\":1,\"postId\":1,\"timeSlot\":{\"time\":\"19:00-21:00\",\"weekday\":1},\"toUserId\":16}]', 9, '127.0.0.1', '2025-01-27 18:07:33', NULL);
INSERT INTO `sys_log` VALUES (1957, 15, '获取学习请求列表', 'com.example.controller.StudyRequestController.getUserRequests', '[]', 22, '127.0.0.1', '2025-01-27 18:07:35', NULL);
INSERT INTO `sys_log` VALUES (1958, 15, '获取待处理的学习请求', 'com.example.controller.StudyRequestController.getPendingRequests', '[]', 3, '127.0.0.1', '2025-01-27 18:09:27', NULL);
INSERT INTO `sys_log` VALUES (1959, 16, '获取待处理的学习请求', 'com.example.controller.StudyRequestController.getPendingRequests', '[]', 12, '127.0.0.1', '2025-01-27 18:10:11', NULL);
INSERT INTO `sys_log` VALUES (1960, 15, '发起学习请求', 'com.example.controller.StudyRequestController.createRequest', '[{\"message\":\"一起学习吧2\",\"placeId\":1,\"postId\":1,\"timeSlot\":{\"time\":\"19:00-21:00\",\"weekday\":1},\"toUserId\":16}]', 9, '127.0.0.1', '2025-01-27 18:10:30', NULL);
INSERT INTO `sys_log` VALUES (1961, 15, '发起学习请求', 'com.example.controller.StudyRequestController.createRequest', '[{\"message\":\"一起学习吧3\",\"placeId\":1,\"postId\":1,\"timeSlot\":{\"time\":\"19:00-21:00\",\"weekday\":1},\"toUserId\":16}]', 5, '127.0.0.1', '2025-01-27 18:10:34', NULL);
INSERT INTO `sys_log` VALUES (1962, 16, '获取待处理的学习请求', 'com.example.controller.StudyRequestController.getPendingRequests', '[]', 27, '127.0.0.1', '2025-01-27 18:10:35', NULL);
INSERT INTO `sys_log` VALUES (1963, 15, '接受学习请求', 'com.example.controller.StudyRequestController.acceptRequest', '[1]', 5, '127.0.0.1', '2025-01-27 18:12:04', NULL);
INSERT INTO `sys_log` VALUES (1964, 15, '接受学习请求', 'com.example.controller.StudyRequestController.acceptRequest', '[1]', 3, '127.0.0.1', '2025-01-27 18:12:16', NULL);
INSERT INTO `sys_log` VALUES (1965, 15, '接受学习请求', 'com.example.controller.StudyRequestController.acceptRequest', '[1]', 3, '127.0.0.1', '2025-01-27 18:12:38', NULL);
INSERT INTO `sys_log` VALUES (1966, 16, '获取待处理的学习请求', 'com.example.controller.StudyRequestController.getPendingRequests', '[]', 42, '127.0.0.1', '2025-01-27 18:12:51', NULL);
INSERT INTO `sys_log` VALUES (1967, 16, '接受学习请求', 'com.example.controller.StudyRequestController.acceptRequest', '[1]', 19, '127.0.0.1', '2025-01-27 18:12:57', NULL);
INSERT INTO `sys_log` VALUES (1968, 16, '获取待处理的学习请求', 'com.example.controller.StudyRequestController.getPendingRequests', '[]', 18, '127.0.0.1', '2025-01-27 18:13:06', NULL);
INSERT INTO `sys_log` VALUES (1969, 15, '拒绝学习请求', 'com.example.controller.StudyRequestController.rejectRequest', '[1]', 4, '0:0:0:0:0:0:0:1', '2025-01-27 18:13:45', NULL);
INSERT INTO `sys_log` VALUES (1970, 16, '拒绝学习请求', 'com.example.controller.StudyRequestController.rejectRequest', '[1]', 9, '0:0:0:0:0:0:0:1', '2025-01-27 18:13:53', NULL);
INSERT INTO `sys_log` VALUES (1971, 16, '获取待处理的学习请求', 'com.example.controller.StudyRequestController.getPendingRequests', '[]', 14, '127.0.0.1', '2025-01-27 18:14:05', NULL);
INSERT INTO `sys_log` VALUES (1972, 16, '接受学习请求', 'com.example.controller.StudyRequestController.acceptRequest', '[1]', 13, '127.0.0.1', '2025-01-27 18:14:17', NULL);
INSERT INTO `sys_log` VALUES (1973, 16, '拒绝学习请求', 'com.example.controller.StudyRequestController.rejectRequest', '[2]', 8, '0:0:0:0:0:0:0:1', '2025-01-27 18:14:22', NULL);
INSERT INTO `sys_log` VALUES (1974, 6, '用户登录', 'AuthenticationSuccess', NULL, NULL, '127.0.0.1', '2025-01-27 18:56:22', NULL);
INSERT INTO `sys_log` VALUES (1975, 6, '管理员获取所有帖子', 'com.example.controller.PostController.listAllPosts', '[]', 305, '127.0.0.1', '2025-01-27 18:57:21', NULL);
INSERT INTO `sys_log` VALUES (1976, 6, '更新帖子状态', 'com.example.controller.PostController.updatePostStatus', '[1,2]', 8, '127.0.0.1', '2025-01-27 18:58:00', NULL);
INSERT INTO `sys_log` VALUES (1977, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 12, '127.0.0.1', '2025-01-27 18:58:04', NULL);
INSERT INTO `sys_log` VALUES (1978, 6, '更新帖子状态', 'com.example.controller.PostController.updatePostStatus', '[1,1]', 5, '127.0.0.1', '2025-01-27 18:58:09', NULL);
INSERT INTO `sys_log` VALUES (1979, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 6, '127.0.0.1', '2025-01-27 18:58:10', NULL);
INSERT INTO `sys_log` VALUES (1980, 6, '更新帖子状态', 'com.example.controller.PostController.updatePostStatus', '[1,0]', 8, '127.0.0.1', '2025-01-27 18:58:19', NULL);
INSERT INTO `sys_log` VALUES (1981, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 6, '127.0.0.1', '2025-01-27 18:58:21', NULL);
INSERT INTO `sys_log` VALUES (1982, 6, '更新帖子状态', 'com.example.controller.PostController.updatePostStatus', '[1,2]', 13, '127.0.0.1', '2025-01-27 18:58:26', NULL);
INSERT INTO `sys_log` VALUES (1983, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 2, '127.0.0.1', '2025-01-27 18:58:30', NULL);
INSERT INTO `sys_log` VALUES (1984, 6, '更新帖子状态', 'com.example.controller.PostController.updatePostStatus', '[1,1]', 8, '127.0.0.1', '2025-01-27 18:58:34', NULL);
INSERT INTO `sys_log` VALUES (1985, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 8, '127.0.0.1', '2025-01-27 18:58:36', NULL);
INSERT INTO `sys_log` VALUES (1986, 6, '更新帖子状态', 'com.example.controller.PostController.updatePostStatus', '[1,0]', 318, '127.0.0.1', '2025-01-27 19:00:14', NULL);
INSERT INTO `sys_log` VALUES (1987, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 19, '127.0.0.1', '2025-01-27 19:00:16', NULL);
INSERT INTO `sys_log` VALUES (1988, 6, '更新帖子状态', 'com.example.controller.PostController.updatePostStatus', '[1,1]', 5, '127.0.0.1', '2025-01-27 19:00:20', NULL);
INSERT INTO `sys_log` VALUES (1989, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 15, '127.0.0.1', '2025-01-27 19:00:22', NULL);
INSERT INTO `sys_log` VALUES (1990, 6, '获取发帖统计', 'com.example.controller.PostController.getPostStats', '[]', 267, '127.0.0.1', '2025-01-27 19:08:10', NULL);
INSERT INTO `sys_log` VALUES (1991, 8, '获取帖子列表', 'com.example.controller.PostController.listPosts', '[]', 82, '127.0.0.1', '2025-01-27 19:29:21', '{\"code\":200,\"data\":[{\"content\":\"求解高等数学第三章习题\",\"createTime\":\"2025-01-26 02:33:05\",\"id\":1,\"inviteEnabled\":1,\"placeId\":1,\"placeName\":\"图书馆\",\"status\":1,\"subjectId\":1,\"subjectName\":\"高等数学A\",\"title\":\"求助高数题目(管理员测试修改)\",\"type\":\"help\",\"updateTime\":\"2025-01-27 19:00:20\",\"userId\":8,\"username\":\"testuser\"}],\"message\":\"请求成功\"}');

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '关联用户id',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `preferred_places` json NULL COMMENT '偏好学习地点列表,格式:[place_id1, place_id2]',
  `available_times` json NULL COMMENT '空闲时间段列表,格式:[{weekday:1-7,time:HH:mm-HH:mm}]',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户画像表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_profile
-- ----------------------------
INSERT INTO `user_profile` VALUES (2, 8, '测试用户', '测试班级', '13800138000', '[1, 2]', '[{\"time\": \"19:00-21:00\", \"weekday\": 1}, {\"time\": \"14:00-16:00\", \"weekday\": 3}, {\"time\": \"09:00-11:00\", \"weekday\": 6}]', '2025-01-25 20:49:59', '2025-01-25 20:52:37');
INSERT INTO `user_profile` VALUES (5, 17, '测试用户3', '软工1班', '123123647', '[1, 3]', '[{\"time\": \"19:00-21:00\", \"weekday\": 2}, {\"time\": \"14:00-16:00\", \"weekday\": 5}]', '2025-01-26 18:48:14', '2025-01-27 02:08:26');
INSERT INTO `user_profile` VALUES (6, 18, '测试用户4', '软工2班', '123123648', '[2, 4]', '[{\"time\": \"19:00-21:00\", \"weekday\": 3}, {\"time\": \"09:00-11:00\", \"weekday\": 6}]', '2025-01-26 18:48:14', '2025-01-27 02:09:18');
INSERT INTO `user_profile` VALUES (7, 16, '测试用户2', '计科2班', '123123646', '[2, 3]', '[{\"time\": \"19:00-21:00\", \"weekday\": 1}, {\"time\": \"09:00-11:00\", \"weekday\": 4}]', '2025-01-26 18:48:14', '2025-01-27 02:05:11');
INSERT INTO `user_profile` VALUES (8, 15, '测试用户1', '计科1班', '123123645', '[1, 2]', '[{\"time\": \"19:00-21:00\", \"weekday\": 1}, {\"time\": \"14:00-16:00\", \"weekday\": 3}]', '2025-01-26 18:48:14', '2025-01-26 18:48:14');

-- ----------------------------
-- Table structure for user_subject
-- ----------------------------
DROP TABLE IF EXISTS `user_subject`;
CREATE TABLE `user_subject`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户id',
  `subject_id` int NOT NULL COMMENT '科目id',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型:good擅长/need需求',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_subject`(`user_id` ASC, `subject_id` ASC, `type` ASC) USING BTREE COMMENT '用户-科目-类型唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-科目关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_subject
-- ----------------------------
INSERT INTO `user_subject` VALUES (5, 15, 1, 'good');
INSERT INTO `user_subject` VALUES (7, 15, 2, 'need');
INSERT INTO `user_subject` VALUES (6, 15, 3, 'good');
INSERT INTO `user_subject` VALUES (9, 16, 1, 'need');
INSERT INTO `user_subject` VALUES (8, 16, 2, 'good');
INSERT INTO `user_subject` VALUES (10, 17, 1, 'good');
INSERT INTO `user_subject` VALUES (11, 17, 2, 'good');
INSERT INTO `user_subject` VALUES (12, 18, 1, 'need');
INSERT INTO `user_subject` VALUES (13, 18, 2, 'need');

SET FOREIGN_KEY_CHECKS = 1;
