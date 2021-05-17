/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : shiliu

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 16/05/2021 23:02:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for content_basic_info
-- ----------------------------
DROP TABLE IF EXISTS `content_basic_info`;
CREATE TABLE `content_basic_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `annex` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publish_time` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `refrence` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联的帖子',
  `permissions` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限',
  `refUser` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联人',
  `starts` tinyint(0) NULL DEFAULT NULL,
  `comments` tinyint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content_basic_info
-- ----------------------------
INSERT INTO `content_basic_info` VALUES ('ccf75fa4dd2c42a1b1c3266889776b92', '722abcfba9c54e4d96bb088e913054c1', 'hello content', '[\"/dragon/document/content/1620749042404.jpg\",\"/dragon/document/content/1620749042417.jpg\"]', '1620749042428', NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for content_comments
-- ----------------------------
DROP TABLE IF EXISTS `content_comments`;
CREATE TABLE `content_comments`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contentId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `comments_time` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `starts` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content_comments
-- ----------------------------

-- ----------------------------
-- Table structure for content_extend_info
-- ----------------------------
DROP TABLE IF EXISTS `content_extend_info`;
CREATE TABLE `content_extend_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`name`, `value`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of content_extend_info
-- ----------------------------

-- ----------------------------
-- Table structure for operation_info
-- ----------------------------
DROP TABLE IF EXISTS `operation_info`;
CREATE TABLE `operation_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `uri` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int(0) NULL DEFAULT NULL,
  `start_time` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `end_time` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_info
-- ----------------------------
INSERT INTO `operation_info` VALUES ('36660633782c4b45873c32805c8bca17', NULL, '127.0.0.1', '/error', 500, '1621157179386', '1621157179409');
INSERT INTO `operation_info` VALUES ('37fb2c1348854390ae04483fceae0f6a', NULL, '127.0.0.1', '/dragon/code/sms', 200, '1621157173192', '1621157173224');
INSERT INTO `operation_info` VALUES ('560fea9fdc914990873230c0ecbe3c5c', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/school/add', 200, '1621176385039', '1621176385101');
INSERT INTO `operation_info` VALUES ('56cac4dfe6734e9580fb892cbd4f9474', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user', 200, '1620924166702', '1620924166796');
INSERT INTO `operation_info` VALUES ('5bfa437b34a4459fb20be1e84f137c54', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user/hotUsers', 200, '1621159603527', '1621159616281');
INSERT INTO `operation_info` VALUES ('64b82a8f1e82444ca7c39a1d7e3d3c20', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user', 200, '1621158592935', '1621158593057');
INSERT INTO `operation_info` VALUES ('84e342426e6b4449a58b43e7d0860822', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/school/cqupt', 200, '1621176401616', '1621176401623');
INSERT INTO `operation_info` VALUES ('88e38b6997b541c2b8459fea6a8d3272', NULL, '127.0.0.1', '/dragon/user/register', 200, '1621157158182', '1621157158412');
INSERT INTO `operation_info` VALUES ('89f81dea476c4ba38776b4d14b754609', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user/hotUsers', 200, '1621159955150', '1621159958761');
INSERT INTO `operation_info` VALUES ('9804cac5ab324b7bacdea91facf55193', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user/modify', 200, '1621159419418', '1621159419478');
INSERT INTO `operation_info` VALUES ('da3f9a789d934269878a4ef85ee71ccb', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/school/query', 200, '1621176391833', '1621176391841');
INSERT INTO `operation_info` VALUES ('dbdbeb402387454fa9e75bc35a4e29f5', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user/hotUsers', 200, '1621159440514', '1621159480033');
INSERT INTO `operation_info` VALUES ('ed1379019c1d4615931d9a7ae313b76c', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user/hotUsers', 200, '1621159249657', '1621159249737');
INSERT INTO `operation_info` VALUES ('ed161d7e54354b1ab8786ac5552e11bf', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user/hotUsers', 200, '1621155059702', '1621155059734');
INSERT INTO `operation_info` VALUES ('ed35b8b85bac4010a53cca206b75879e', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user/hotUsers', 200, '1621159329717', '1621159419466');
INSERT INTO `operation_info` VALUES ('ff3195343d254ae2bf679593ef82d242', '722abcfba9c54e4d96bb088e913054c1', '127.0.0.1', '/dragon/user', 200, '1621155055683', '1621155055798');

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info`  (
  `id` int(0) NOT NULL,
  `roleName` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `level` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_info
-- ----------------------------

-- ----------------------------
-- Table structure for school_info
-- ----------------------------
DROP TABLE IF EXISTS `school_info`;
CREATE TABLE `school_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `annex` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of school_info
-- ----------------------------
INSERT INTO `school_info` VALUES ('c0d6ebd541de4e8fb51f7569b0b55d1e', 'cqupt', '重庆邮电大学', 'https://www.cqupt.edu', NULL);

-- ----------------------------
-- Table structure for user_audit_info
-- ----------------------------
DROP TABLE IF EXISTS `user_audit_info`;
CREATE TABLE `user_audit_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `meterials` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `postData` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `auditData` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `managerId` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isManager` char(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_audit_info
-- ----------------------------

-- ----------------------------
-- Table structure for user_basic_info
-- ----------------------------
DROP TABLE IF EXISTS `user_basic_info`;
CREATE TABLE `user_basic_info`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mobile` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `origin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `birthday` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `majorIn` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` tinyint(0) NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `register_time` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mobile`(`mobile`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_basic_info
-- ----------------------------
INSERT INTO `user_basic_info` VALUES ('722abcfba9c54e4d96bb088e913054c1', '8613012345679', '$2a$10$HRjs.dxYWmMiB7TYw6Uq7OuMER8w7eeh18ZSPZ7N4mha0z6ux8RyK', 'hunan', 'ouyangchao哈哈哈', 'cqupt', '12345678', 'bioinfo', 1, '这个人很懒，什么也没留下', '1620402948980');

-- ----------------------------
-- Table structure for user_extend_info
-- ----------------------------
DROP TABLE IF EXISTS `user_extend_info`;
CREATE TABLE `user_extend_info`  (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_extend_info
-- ----------------------------
INSERT INTO `user_extend_info` VALUES ('722abcfba9c54e4d96bb088e913054c1', 'portraitUri', '/dragon/document/portrait/defaultPortrait.png');

-- ----------------------------
-- Table structure for user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `user_role_relation`;
CREATE TABLE `user_role_relation`  (
  `id` int(0) NOT NULL,
  `userId` int(0) NULL DEFAULT NULL,
  `roleId` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  INDEX `roleId`(`roleId`) USING BTREE,
  CONSTRAINT `roleId` FOREIGN KEY (`roleId`) REFERENCES `role_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role_relation
-- ----------------------------

-- ----------------------------
-- Table structure for user_watcher
-- ----------------------------
DROP TABLE IF EXISTS `user_watcher`;
CREATE TABLE `user_watcher`  (
  `follow` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `uper` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` int(0) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `follower`(`follow`) USING BTREE,
  UNIQUE INDEX `uper`(`uper`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_watcher
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
