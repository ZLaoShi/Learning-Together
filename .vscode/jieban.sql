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

 Date: 23/01/2025 18:40:29
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
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `registertime` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_email`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `unique_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of db_account
-- ----------------------------
INSERT INTO `db_account` VALUES (6, 'admin', '1234567891', '$2a$10$9ZR.J/NNRsEL9xawrEcuReTx2vNP3LZbCdhcOOB4QbMYycEVfc9QG', 'admin', '2024-12-26 10:58:19');
INSERT INTO `db_account` VALUES (8, 'testuser', '138001380', '$2a$10$PLS1hqlWaXzbttEVTiv.QO8yscq/tJTH04R8gcCyrdgqbRQ1uq7J6', 'user', '2024-12-26 12:25:42');

-- ----------------------------
-- Table structure for match_record
-- ----------------------------
DROP TABLE IF EXISTS `match_record`;
CREATE TABLE `match_record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id_1` int NOT NULL COMMENT '发起匹配的用户id',
  `user_id_2` int NOT NULL COMMENT '被匹配的用户id',
  `match_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '匹配类型:subject按科目/time按时间/place按地点/complement互补',
  `subject_id` int NULL DEFAULT NULL COMMENT '匹配的科目id',
  `place_id` int NULL DEFAULT NULL COMMENT '匹配的场地id',
  `time_slot` json NULL COMMENT '匹配的时间段,格式:{weekday:1-7,time:HH:mm-HH:mm}',
  `match_score` decimal(5, 2) NULL DEFAULT NULL COMMENT '匹配得分',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态:0待确认,1已确认,2已拒绝,3已完成',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `confirm_time` datetime NULL DEFAULT NULL COMMENT '确认时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '匹配记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of match_record
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '场地表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of place
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '信息-科目关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of post_subject
-- ----------------------------

-- ----------------------------
-- Table structure for study_request
-- ----------------------------
DROP TABLE IF EXISTS `study_request`;
CREATE TABLE `study_request`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `post_id` int NOT NULL COMMENT '关联帖子id',
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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学习请求表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of study_request
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '科目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject
-- ----------------------------

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
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `account_id`(`account_id` ASC) USING BTREE,
  CONSTRAINT `sys_log_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `db_account` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1884 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户画像表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_profile
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户-科目关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_subject
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
