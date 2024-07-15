/*
 Navicat Premium Data Transfer

 Source Server         : discordtest
 Source Server Type    : MySQL
 Source Server Version : 80300 (8.3.0)
 Source Host           : localhost:3306
 Source Schema         : discordtest

 Target Server Type    : MySQL
 Target Server Version : 80300 (8.3.0)
 File Encoding         : 65001

 Date: 15/07/2024 12:37:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for action
-- ----------------------------
DROP TABLE IF EXISTS `action`;
CREATE TABLE `action`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `profile_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '用户ID',
  `question_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '问题ID',
  `action_type` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '动作类型 0-收藏 1-赞 2-踩',
  `answer_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '回复ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of action
-- ----------------------------
BEGIN;
INSERT INTO `action` (`id`, `profile_id`, `question_id`, `action_type`, `answer_id`, `create_time`, `update_time`) VALUES (17, 1, 3, 2, NULL, NULL, NULL), (19, 1, 3, 0, NULL, NULL, NULL), (20, 1, 1, 1, NULL, NULL, NULL), (23, 1, 9, 0, NULL, NULL, NULL), (28, 1, 7, 1, NULL, NULL, NULL), (34, 1, 23, 0, NULL, NULL, NULL), (35, 1, 18, 0, NULL, NULL, NULL), (41, 1, 28, 0, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `profile_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `question_id` bigint UNSIGNED NOT NULL COMMENT '问题ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回复内容',
  `upvotes` bigint UNSIGNED NULL DEFAULT 0 COMMENT '赞',
  `downvotes` bigint NULL DEFAULT 0 COMMENT '踩',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of answer
-- ----------------------------
BEGIN;
INSERT INTO `answer` (`id`, `profile_id`, `question_id`, `content`, `upvotes`, `downvotes`, `create_time`, `update_time`) VALUES (18, 1, 28, '<p>dddddddddddddddddddddddd</p>\n<p>dddddddddddddddddddddd</p>\n<p>ddddddddddddd</p>', 0, 0, '2024-07-12 14:05:44', '2024-07-12 14:05:44'), (20, 1, 36, '<p>当然可以。以下是一个简单的Java代码示例，它定义了一个名为`Person`的类，该类有两个私有字段（`name`和`age`）以及相应的构造器、getter和setter方法。此外，还有一个`main`方法用于演示如何创建`Person`对象并设置其属性。 ```java public class Person { private String name; private int age; // 构造器 public Person(String name, int age) { this.name = name; this.age = age; } // Getter方法 public String getName() { return name; } public int getAge() { return age; } // Setter方法 public void setName(String name) { this.name = name; } public void setAge(int age) { if (age &gt;= 0) { this.age = age; } else { System.out.println(\"Age cannot be negative.\"); } } // toString方法，用于方便打印对象信息 @Override public String toString() { return \"Person{\" + \"name=\'\" + name + \'\\\'\' + \", age=\" + age + \'}\'; } // main方法，程序的入口点 public static void main(String[] args) { // 创建一个Person对象并设置其属性 Person person = new Person(\"Alice\", 30); System.out.println(person); // 输出Person对象的信息 // 修改Person对象的属性 person.setName(\"Bob\"); person.setAge(25); System.out.println(person); // 再次输出Person对象的信息，以查看修改后的效果 } } ``` 你可以将上述代码保存为一个名为`Person.java`的文件，并使用Java编译器（如`javac`）编译它。然后，你可以运行编译后的类（使用`java Person`）来查看输出结果。</p>', 0, 0, '2024-07-12 14:51:23', '2024-07-12 14:51:23');
COMMIT;

-- ----------------------------
-- Table structure for channel_info
-- ----------------------------
DROP TABLE IF EXISTS `channel_info`;
CREATE TABLE `channel_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '频道名称',
  `type` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '频道类型 0-文本 1-音频 2-视频',
  `profile_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `server_id` bigint UNSIGNED NOT NULL COMMENT '服务器ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `ik_profile_id`(`profile_id` ASC) USING BTREE,
  INDEX `ik_server_id`(`server_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of channel_info
-- ----------------------------
BEGIN;
INSERT INTO `channel_info` (`id`, `name`, `type`, `profile_id`, `server_id`, `create_time`, `update_time`) VALUES (38, 'general', 0, 1, 33, '2024-07-02 13:58:07', '2024-07-02 13:58:07');
COMMIT;

-- ----------------------------
-- Table structure for conversation
-- ----------------------------
DROP TABLE IF EXISTS `conversation`;
CREATE TABLE `conversation`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `member_one` bigint UNSIGNED NOT NULL COMMENT '发送者ID',
  `member_two` bigint UNSIGNED NOT NULL COMMENT '接收者ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  UNIQUE INDEX `uk_one_two_id`(`member_one` ASC, `member_two` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of conversation
-- ----------------------------
BEGIN;
INSERT INTO `conversation` (`id`, `member_one`, `member_two`, `create_time`, `update_time`) VALUES (1, 22, 23, NULL, NULL), (3, 31, 22, NULL, NULL), (4, 33, 22, NULL, NULL), (5, 36, 29, NULL, NULL), (6, 22, 37, NULL, NULL), (7, 38, 29, NULL, NULL), (8, 46, 52, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for direct_message
-- ----------------------------
DROP TABLE IF EXISTS `direct_message`;
CREATE TABLE `direct_message`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `file_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件地址',
  `member_id` bigint UNSIGNED NOT NULL COMMENT '成员ID',
  `conversation_id` bigint UNSIGNED NOT NULL COMMENT '交流ID',
  `deleted` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否已经删除 0-否 1-是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `ik_member_id`(`member_id` ASC) USING BTREE,
  INDEX `conversation_id`(`conversation_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of direct_message
-- ----------------------------
BEGIN;
INSERT INTO `direct_message` (`id`, `content`, `file_url`, `member_id`, `conversation_id`, `deleted`, `create_time`, `update_time`) VALUES (1, ' 😀', NULL, 22, 1, 0, '2024-06-23 17:38:50', '2024-06-23 17:38:50'), (2, 'This message has been deleted', NULL, 22, 1, 1, '2024-06-23 17:51:46', '2024-06-23 19:17:02'), (3, 'This message has been deleted', NULL, 23, 1, 1, '2024-06-23 17:52:56', '2024-06-23 19:16:48'), (4, '还行', NULL, 22, 1, 0, '2024-06-23 17:53:04', '2024-06-23 17:53:04'), (5, 'ssss', NULL, 22, 1, 0, '2024-06-23 17:53:07', '2024-06-23 17:53:07'), (6, 'ddd', NULL, 22, 1, 0, '2024-06-23 17:53:11', '2024-06-23 17:53:11'), (7, 'ddd', NULL, 22, 1, 0, '2024-06-23 17:53:16', '2024-06-23 17:53:16'), (8, 'This message has been deleted', NULL, 23, 1, 1, '2024-06-23 17:54:54', '2024-06-23 19:16:26'), (9, 'This message has been deleted', NULL, 22, 1, 1, '2024-06-23 17:55:08', '2024-06-23 19:12:48'), (10, '牛逼', NULL, 22, 1, 0, '2024-06-24 22:55:07', '2024-06-24 22:55:07'), (11, 'sddd', NULL, 52, 8, 0, '2024-07-12 15:45:29', '2024-07-12 15:45:29'), (12, 'ddd', NULL, 46, 8, 0, '2024-07-12 15:45:35', '2024-07-12 15:45:35');
COMMIT;

-- ----------------------------
-- Table structure for member_info
-- ----------------------------
DROP TABLE IF EXISTS `member_info`;
CREATE TABLE `member_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role` tinyint UNSIGNED NOT NULL COMMENT '成员角色；0-管理员 1-版主 2-访客',
  `profile_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `server_id` bigint UNSIGNED NOT NULL COMMENT '服务器ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `ik_profile_id`(`profile_id` ASC) USING BTREE,
  INDEX `ik_server_id`(`server_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of member_info
-- ----------------------------
BEGIN;
INSERT INTO `member_info` (`id`, `role`, `profile_id`, `server_id`, `create_time`, `update_time`) VALUES (46, 0, 1, 33, '2024-07-02 13:58:07', '2024-07-02 13:58:07'), (52, 1, 2, 33, '2024-07-02 15:11:12', '2024-07-12 16:26:23');
COMMIT;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `file_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件地址',
  `channel_id` bigint UNSIGNED NOT NULL COMMENT '频道ID',
  `member_id` bigint UNSIGNED NOT NULL COMMENT '成员ID',
  `deleted` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否已删除 0-否 1-是',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id` DESC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  INDEX `ik_member_id`(`member_id` ASC) USING BTREE,
  INDEX `ik_channel_id`(`channel_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 61 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of message
-- ----------------------------
BEGIN;
INSERT INTO `message` (`id`, `content`, `file_url`, `channel_id`, `member_id`, `deleted`, `create_time`, `update_time`) VALUES (60, 'https://blog.csdn.net/nav/back-end', NULL, 38, 46, 0, '2024-07-12 16:37:28', '2024-07-12 16:37:28'), (59, 'dd', NULL, 38, 46, 0, '2024-07-12 16:26:39', '2024-07-12 16:26:39'), (58, '/wenxi', NULL, 38, 46, 0, '2024-07-10 12:30:49', '2024-07-10 12:30:49'), (57, 'jjj', NULL, 38, 46, 0, '2024-07-09 20:19:11', '2024-07-09 20:19:11'), (56, 'jj', NULL, 38, 46, 0, '2024-07-09 19:53:29', '2024-07-09 19:53:29'), (55, NULL, 'http://localhost/uploads/2024/07/04/533a9c979bd388a95332faa6e59fc10a.pdf', 38, 46, 0, '2024-07-04 21:43:42', '2024-07-04 21:43:42'), (54, 'dd', NULL, 38, 46, 0, '2024-07-04 21:43:26', '2024-07-04 21:43:26'), (53, '你好', NULL, 38, 46, 0, '2024-07-04 12:44:51', '2024-07-04 12:44:51'), (52, '？', NULL, 38, 46, 0, '2024-07-04 00:28:25', '2024-07-04 00:28:25'), (51, '你好', NULL, 38, 46, 0, '2024-07-03 23:42:24', '2024-07-03 23:42:24'), (50, 'jjj', NULL, 39, 46, 0, '2024-07-02 15:27:29', '2024-07-02 15:27:29'), (49, 'This message has been deleted', 'http://localhost/uploads/2024/07/02/2d66f25069f317d50958bd97a29b087e.jpg', 38, 52, 1, '2024-07-02 15:25:23', '2024-07-02 15:37:43'), (48, 'test11', NULL, 38, 46, 0, '2024-07-02 15:20:05', '2024-07-02 15:37:57');
COMMIT;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题内容',
  `upvotes` bigint UNSIGNED NULL DEFAULT 0 COMMENT '赞',
  `downvotes` bigint NULL DEFAULT 0 COMMENT '踩',
  `profile_id` bigint UNSIGNED NOT NULL COMMENT '用户ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `answer_count` bigint UNSIGNED NULL DEFAULT 0 COMMENT '回复数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of question
-- ----------------------------
BEGIN;
INSERT INTO `question` (`id`, `title`, `content`, `upvotes`, `downvotes`, `profile_id`, `create_time`, `update_time`, `answer_count`) VALUES (28, '的事实是事实是事实是事实', '<p>的方式水水水水水水水水水水</p>', 0, 0, 1, '2024-07-10 15:42:06', '2024-07-10 15:42:06', 2), (29, '顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶', '<p>ddddddddddddddddddd</p>', 0, 0, 1, '2024-07-10 15:44:46', '2024-07-10 15:44:46', 0), (30, 'dddddddddddddd ', '<p>ddddddddddddddddddddddddddddd</p>', 0, 0, 1, '2024-07-10 15:46:21', '2024-07-10 15:46:21', 0), (31, 'ddddddddddddddddddd', '<p>dddddddddddddddddddddd</p>', 0, 0, 1, '2024-07-12 14:22:53', '2024-07-12 14:22:53', 0), (32, 'ddddddddddddd', '<p>sssssssssssssssssssssss</p>\n<p>ddddddddddddddsss</p>', 0, 0, 1, '2024-07-12 14:31:34', '2024-07-12 14:31:34', 0), (33, 'dddddddddddddddd', '<p>ccccccccccccccccccddd</p>\n<p>ddddddddddddd</p>', 0, 0, 1, '2024-07-12 14:35:45', '2024-07-12 14:35:45', 0), (34, 'dddddddddddd', '<p>ddddddddddddddd</p>\n<p>ddddddddf</p>\n<p>ddddddddddddd</p>\n<p>fffff</p>', 0, 0, 1, '2024-07-12 14:38:42', '2024-07-12 14:38:42', 0), (35, 'sddddddddddddddd', '<p>ddddddddddddddddd</p>\n<p>dddddddddddddd</p>', 0, 0, 1, '2024-07-12 14:43:37', '2024-07-12 14:43:37', 0), (36, 'fssssssss', '<p>写一段java代码</p>', 0, 0, 1, '2024-07-12 14:50:37', '2024-07-12 14:50:37', 1), (37, 'java问题', '<p>dddddddddddd</p>\n<p>dddddddddd</p>', 0, 0, 2, '2024-07-12 16:18:10', '2024-07-12 16:18:10', 0);
COMMIT;

-- ----------------------------
-- Table structure for question_tag
-- ----------------------------
DROP TABLE IF EXISTS `question_tag`;
CREATE TABLE `question_tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `question_id` bigint UNSIGNED NOT NULL COMMENT '问题ID',
  `tag_id` bigint UNSIGNED NOT NULL COMMENT '标签ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of question_tag
-- ----------------------------
BEGIN;
INSERT INTO `question_tag` (`id`, `question_id`, `tag_id`, `create_time`, `update_time`) VALUES (26, 28, 2, NULL, NULL), (27, 29, 4, NULL, NULL), (28, 30, 1, NULL, NULL), (30, 31, 5, '2024-07-12 14:22:53', '2024-07-12 14:22:53'), (31, 32, 5, '2024-07-12 14:31:50', '2024-07-12 14:31:50'), (33, 33, 5, '2024-07-12 14:35:45', '2024-07-12 14:35:45'), (34, 34, 5, '2024-07-12 14:38:42', '2024-07-12 14:38:42'), (35, 35, 5, '2024-07-12 14:43:37', '2024-07-12 14:43:37'), (36, 35, 7, '2024-07-12 14:43:37', '2024-07-12 14:43:37'), (37, 36, 5, '2024-07-12 14:50:37', '2024-07-12 14:50:37'), (38, 37, 5, '2024-07-12 16:18:10', '2024-07-12 16:18:10');
COMMIT;

-- ----------------------------
-- Table structure for server_info
-- ----------------------------
DROP TABLE IF EXISTS `server_info`;
CREATE TABLE `server_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '服务器名称',
  `image_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器照片地址',
  `profile_id` bigint UNSIGNED NOT NULL COMMENT '服务器创建者',
  `invite_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务器邀请码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE,
  UNIQUE INDEX `uk_servername`(`name` ASC) USING BTREE,
  UNIQUE INDEX `uk_invite_code`(`invite_code` ASC) USING BTREE,
  INDEX `ik_profile_id`(`profile_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of server_info
-- ----------------------------
BEGIN;
INSERT INTO `server_info` (`id`, `name`, `image_url`, `profile_id`, `invite_code`, `create_time`, `update_time`) VALUES (27, 'dddddddddddd', 'http://localhost/uploads/2024/07/01/bea75712d1692ad1c10ebd17b524e86a.jpg', 2, 'e6209f9d777d358bb5f11c3126079b20', '2024-07-01 21:45:03', '2024-07-01 21:45:03'), (33, 'lwq server', 'http://localhost/uploads/2024/07/02/f0d418b37e4d4cfb1b3dc0452a6a3daf.jpg', 1, '76d6c70991b80e4803953d093934d855', '2024-07-02 13:58:07', '2024-07-02 13:58:07');
COMMIT;

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名字',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `question_count` bigint UNSIGNED NULL DEFAULT 0 COMMENT '问题数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of tag
-- ----------------------------
BEGIN;
INSERT INTO `tag` (`id`, `name`, `create_time`, `update_time`, `question_count`) VALUES (1, 'css', '2024-07-10 16:04:14', '2024-07-10 16:04:16', 1), (2, 'javascript', '2024-07-10 16:04:07', '2024-07-10 16:04:10', 1), (4, 'c++', '2024-07-10 15:44:46', '2024-07-10 15:44:46', 1), (5, 'java', '2024-07-12 14:22:53', '2024-07-12 14:22:53', 6), (7, 'html', '2024-07-12 14:43:37', '2024-07-12 14:43:37', 1);
COMMIT;

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_sex` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '用户性别； 0-男，1-女',
  `image_url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户电话号码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id` DESC) USING BTREE,
  UNIQUE INDEX `uk_username`(`name` ASC) USING BTREE,
  UNIQUE INDEX `pk_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表';

-- ----------------------------
-- Records of user_profile
-- ----------------------------
BEGIN;
INSERT INTO `user_profile` (`id`, `name`, `password`, `nick_name`, `user_sex`, `image_url`, `email`, `phone`, `create_time`, `update_time`) VALUES (2, 'lwq2号', '018bde06713746e72dbf382c8664c9a1', 'lwq123', NULL, 'http://localhost/uploads/2024/06/21/82df01c97db62d098b00d9e5934102e0.jpg', 'lwq6218@gmail.com', '19875822326', '2024-06-17 17:09:36', '2024-07-01 21:03:50'), (1, '李文秋', '018bde06713746e72dbf382c8664c9a1', 'lwq6218', NULL, 'http://localhost/uploads/2024/07/01/748cd6a42e7ec064a7b6a3fb90ac92c2.jpg', '747373139@qq.com', '17727119290', '2024-06-14 12:15:29', '2024-07-01 16:22:49');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
