/*
 Navicat Premium Data Transfer

 Source Server         : Mysql
 Source Server Type    : MySQL
 Source Server Version : 90200 (9.2.0)
 Source Host           : localhost:3306
 Source Schema         : inforsys

 Target Server Type    : MySQL
 Target Server Version : 90200 (9.2.0)
 File Encoding         : 65001

 Date: 02/05/2025 06:17:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for departments
-- ----------------------------
DROP TABLE IF EXISTS `departments`;
CREATE TABLE `departments`  (
  `department_id` int NOT NULL AUTO_INCREMENT,
  `department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of departments
-- ----------------------------
INSERT INTO `departments` VALUES (1, '中华民族共同体学院');
INSERT INTO `departments` VALUES (2, '海南国际学院');
INSERT INTO `departments` VALUES (3, '筑牢中华民族共同体意识研究院');
INSERT INTO `departments` VALUES (4, '民族与社会学学院');
INSERT INTO `departments` VALUES (5, '中国语言文学学部');
INSERT INTO `departments` VALUES (6, '中国民族语文应用研究院');
INSERT INTO `departments` VALUES (7, '文学院');
INSERT INTO `departments` VALUES (8, '历史文化学院');
INSERT INTO `departments` VALUES (9, '哲学与宗教学院');
INSERT INTO `departments` VALUES (10, '新闻与传播学院');
INSERT INTO `departments` VALUES (11, '外国语学院');
INSERT INTO `departments` VALUES (12, '马克思主义学院');
INSERT INTO `departments` VALUES (13, '经济学院');
INSERT INTO `departments` VALUES (14, '管理学院');
INSERT INTO `departments` VALUES (15, '法学院');
INSERT INTO `departments` VALUES (16, '生命与环境科学学院');
INSERT INTO `departments` VALUES (17, '药学院');
INSERT INTO `departments` VALUES (18, '理学院');
INSERT INTO `departments` VALUES (19, '信息工程学院');
INSERT INTO `departments` VALUES (20, '音乐学院');
INSERT INTO `departments` VALUES (21, '舞蹈学院');
INSERT INTO `departments` VALUES (22, '美术学院');
INSERT INTO `departments` VALUES (23, '体育学院');
INSERT INTO `departments` VALUES (24, '教育学院');
INSERT INTO `departments` VALUES (25, '国际教育学院');
INSERT INTO `departments` VALUES (26, '预科教育学院');
INSERT INTO `departments` VALUES (27, '继续教育学院（干部培训部）');
INSERT INTO `departments` VALUES (28, '藏学研究院');
INSERT INTO `departments` VALUES (29, '中国少数民族研究中心（少数民族事业发展协同创新中心）（处级独立科研平台）');
INSERT INTO `departments` VALUES (30, '国家安全研究院');
INSERT INTO `departments` VALUES (31, '图书馆');
INSERT INTO `departments` VALUES (32, '民族博物馆');
INSERT INTO `departments` VALUES (33, '校医院（疾病防控办公室）');
INSERT INTO `departments` VALUES (34, '出版社有限责任公司');
INSERT INTO `departments` VALUES (35, '期刊社');
INSERT INTO `departments` VALUES (36, '中央民族大学附属中学');
INSERT INTO `departments` VALUES (37, '中央民族大学幼儿园');
INSERT INTO `departments` VALUES (38, '社区居委会');
INSERT INTO `departments` VALUES (39, '民族艺术研究院');
INSERT INTO `departments` VALUES (40, '党委办公室、校长办公室（学校档案馆、督查室）');
INSERT INTO `departments` VALUES (41, '党委组织部（党校、机关党委）');
INSERT INTO `departments` VALUES (42, '党委宣传部（新闻中心）');
INSERT INTO `departments` VALUES (43, '党委统战部');
INSERT INTO `departments` VALUES (44, '纪委办公室、监察处');
INSERT INTO `departments` VALUES (45, '党委巡察工作办公室');
INSERT INTO `departments` VALUES (46, '人才交流中心');
INSERT INTO `departments` VALUES (47, '党委学生工作部（武装部）、学生处（学生资助管理中心）');
INSERT INTO `departments` VALUES (48, '党委保卫部、保卫处');
INSERT INTO `departments` VALUES (49, '团委');
INSERT INTO `departments` VALUES (50, '工会（教代会）');
INSERT INTO `departments` VALUES (51, '丰台校区管理委员会');
INSERT INTO `departments` VALUES (52, '教务处（考试中心、教师教学发展中心）');
INSERT INTO `departments` VALUES (53, '研究生院（学位办公室）');
INSERT INTO `departments` VALUES (54, '科研处');
INSERT INTO `departments` VALUES (55, '发展规划处');
INSERT INTO `departments` VALUES (56, '财务处');
INSERT INTO `departments` VALUES (57, '招生就业工作处');
INSERT INTO `departments` VALUES (58, '审计处');
INSERT INTO `departments` VALUES (59, '国际合作处（港澳台事务办公室）');
INSERT INTO `departments` VALUES (60, '国内合作处（校友工作办公室、教育基金会秘书处）');
INSERT INTO `departments` VALUES (61, '基本建设管理处（丰台校区建设办公室）');
INSERT INTO `departments` VALUES (62, '资产管理处（实验室建设管理中心、招标采购中心）');
INSERT INTO `departments` VALUES (63, '后勤保障处（后勤集团）、后勤保障处直属党支部');
INSERT INTO `departments` VALUES (64, '信息化建设管理处');
INSERT INTO `departments` VALUES (65, '离退休人员');
INSERT INTO `departments` VALUES (66, '离退休工作处、离退休教职工党委');

-- ----------------------------
-- Table structure for notification_index
-- ----------------------------
DROP TABLE IF EXISTS `notification_index`;
CREATE TABLE `notification_index`  (
  `index_id` int NOT NULL AUTO_INCREMENT,
  `notification_id` int NOT NULL,
  `recipient_id` int NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT 0,
  `read_time` datetime NULL DEFAULT NULL,
  `notification_type` enum('TEACHING_NOTICE','STUDENT_NOTICE','OFFICE_NOTICE','RESEARCH_NOTICE','OTHER') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `recipient_type` enum('INDIVIDUAL','DEPARTMENT','ALL_STUDENTS','ALL_TEACHERS','ALL') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`index_id`) USING BTREE,
  INDEX `notification_id`(`notification_id` ASC) USING BTREE,
  INDEX `idx_notification_index_recipient`(`recipient_id` ASC) USING BTREE,
  CONSTRAINT `notification_index_ibfk_1` FOREIGN KEY (`notification_id`) REFERENCES `notifications` (`notification_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `notification_index_ibfk_2` FOREIGN KEY (`recipient_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification_index
-- ----------------------------
INSERT INTO `notification_index` VALUES (1, 1, 2, 0, NULL, 'TEACHING_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (2, 1, 9, 0, NULL, 'TEACHING_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (4, 2, 1, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (5, 2, 3, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (6, 2, 7, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (7, 2, 10, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (11, 3, 5, 0, NULL, 'TEACHING_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (12, 4, 4, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (13, 5, 1, 0, NULL, 'OFFICE_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (14, 5, 3, 0, NULL, 'OFFICE_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (15, 5, 7, 0, NULL, 'OFFICE_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (16, 5, 10, 0, NULL, 'OFFICE_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (20, 6, 1, 0, NULL, 'RESEARCH_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (21, 6, 3, 0, NULL, 'RESEARCH_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (22, 6, 7, 0, NULL, 'RESEARCH_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (23, 6, 10, 0, NULL, 'RESEARCH_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (27, 7, 2, 0, NULL, 'OTHER', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (28, 7, 9, 0, NULL, 'OTHER', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (30, 1, 1, 0, NULL, 'TEACHING_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (31, 1, 7, 0, NULL, 'TEACHING_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (32, 2, 3, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (33, 3, 4, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (34, 4, 6, 0, NULL, 'STUDENT_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (35, 5, 8, 0, NULL, 'OFFICE_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (36, 6, 9, 0, NULL, 'RESEARCH_NOTICE', 'INDIVIDUAL');
INSERT INTO `notification_index` VALUES (37, 7, 2, 0, NULL, 'OTHER', 'INDIVIDUAL');

-- ----------------------------
-- Table structure for notification_recipients
-- ----------------------------
DROP TABLE IF EXISTS `notification_recipients`;
CREATE TABLE `notification_recipients`  (
  `recipient_id` int NOT NULL AUTO_INCREMENT,
  `notification_id` int NOT NULL,
  `recipient_type` enum('INDIVIDUAL','DEPARTMENT','ALL_STUDENTS','ALL_TEACHERS') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `recipient_identifier` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`recipient_id`) USING BTREE,
  INDEX `notification_id`(`notification_id` ASC) USING BTREE,
  CONSTRAINT `notification_recipients_ibfk_1` FOREIGN KEY (`notification_id`) REFERENCES `notifications` (`notification_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification_recipients
-- ----------------------------
INSERT INTO `notification_recipients` VALUES (1, 1, 'DEPARTMENT', 19);
INSERT INTO `notification_recipients` VALUES (2, 2, 'DEPARTMENT', 13);
INSERT INTO `notification_recipients` VALUES (3, 3, 'ALL_STUDENTS', NULL);
INSERT INTO `notification_recipients` VALUES (4, 4, 'DEPARTMENT', 4);
INSERT INTO `notification_recipients` VALUES (5, 5, 'DEPARTMENT', 19);
INSERT INTO `notification_recipients` VALUES (6, 6, 'DEPARTMENT', 19);
INSERT INTO `notification_recipients` VALUES (7, 7, 'ALL_TEACHERS', NULL);

-- ----------------------------
-- Table structure for notifications
-- ----------------------------
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications`  (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sent_time` datetime NOT NULL,
  `notification_type` enum('TEACHING_NOTICE','STUDENT_NOTICE','OFFICE_NOTICE','RESEARCH_NOTICE','OTHER') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sender_id` int NULL DEFAULT NULL,
  `sender_department_id` int NULL DEFAULT NULL,
  `status` enum('DRAFT','SENT') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DRAFT',
  PRIMARY KEY (`notification_id`) USING BTREE,
  INDEX `sender_id`(`sender_id` ASC) USING BTREE,
  INDEX `sender_department_id`(`sender_department_id` ASC) USING BTREE,
  INDEX `idx_notification_sent_time`(`sent_time` ASC) USING BTREE,
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`sender_department_id`) REFERENCES `departments` (`department_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notifications
-- ----------------------------
INSERT INTO `notifications` VALUES (1, '课程调整通知', '明天上午的课程改为下午三点。', '2025-02-25 13:22:51', 'TEACHING_NOTICE', 2, 13, 'SENT');
INSERT INTO `notifications` VALUES (2, '校园活动通知', '周五举行校运会，请大家积极参与。', '2025-02-25 13:22:51', 'STUDENT_NOTICE', 3, 19, 'SENT');
INSERT INTO `notifications` VALUES (3, '实验室安全须知', '本周五进行实验室安全培训，请所有教师参加。', '2025-02-25 13:22:51', 'TEACHING_NOTICE', 5, 7, 'SENT');
INSERT INTO `notifications` VALUES (4, '毕业论文答辩安排', '毕业论文答辩时间为下周二上午9点，请相关同学准时参加。', '2025-02-25 13:22:51', 'STUDENT_NOTICE', 4, 4, 'SENT');
INSERT INTO `notifications` VALUES (5, '新学期注册通知', '请所有学生于9月1日前完成学期注册。', '2025-02-25 13:22:51', 'OFFICE_NOTICE', 3, 19, 'SENT');
INSERT INTO `notifications` VALUES (6, '教师研讨会通知', '教师研讨会将于周三下午在主楼302室举行。', '2025-02-25 13:22:51', 'RESEARCH_NOTICE', 7, 19, 'SENT');
INSERT INTO `notifications` VALUES (7, '学校活动公告', '本周末将在操场举办春季游园会，欢迎全校师生参加！', '2025-02-25 13:22:51', 'OTHER', 9, 13, 'SENT');

-- ----------------------------
-- Table structure for user_departments
-- ----------------------------
DROP TABLE IF EXISTS `user_departments`;
CREATE TABLE `user_departments`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `department_id` int NOT NULL,
  `role` enum('STUDENT','TEACHER','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC, `department_id` ASC) USING BTREE,
  INDEX `department_id`(`department_id` ASC) USING BTREE,
  CONSTRAINT `user_departments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_departments_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_departments
-- ----------------------------
INSERT INTO `user_departments` VALUES (1, 1, 19, 'STUDENT');
INSERT INTO `user_departments` VALUES (2, 2, 13, 'TEACHER');
INSERT INTO `user_departments` VALUES (3, 3, 19, 'ADMIN');
INSERT INTO `user_departments` VALUES (4, 4, 4, 'STUDENT');
INSERT INTO `user_departments` VALUES (5, 5, 7, 'TEACHER');
INSERT INTO `user_departments` VALUES (6, 6, 16, 'STUDENT');
INSERT INTO `user_departments` VALUES (7, 7, 19, 'TEACHER');
INSERT INTO `user_departments` VALUES (8, 8, 11, 'STUDENT');
INSERT INTO `user_departments` VALUES (9, 9, 13, 'TEACHER');
INSERT INTO `user_departments` VALUES (10, 10, 19, 'ADMIN');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_type` enum('ADMIN','STUDENT','TEACHER') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  INDEX `idx_user_email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'Alice', 'alice@example.com', 'password123', 'STUDENT');
INSERT INTO `users` VALUES (2, 'Bob', 'bob@example.com', 'password123', 'TEACHER');
INSERT INTO `users` VALUES (3, 'Charlie', 'charlie@example.com', 'password123', 'STUDENT');
INSERT INTO `users` VALUES (4, 'Daisy', 'daisy@example.com', 'password123', 'TEACHER');
INSERT INTO `users` VALUES (5, 'Edward', 'edward@example.com', 'password123', 'STUDENT');
INSERT INTO `users` VALUES (6, 'Fiona', 'fiona@example.com', 'password123', 'TEACHER');
INSERT INTO `users` VALUES (7, 'George', 'george@example.com', 'password123', 'ADMIN');
INSERT INTO `users` VALUES (8, 'Hannah', 'hannah@example.com', 'password123', 'STUDENT');
INSERT INTO `users` VALUES (9, 'Isabel', 'isabel@example.com', 'password123', 'TEACHER');
INSERT INTO `users` VALUES (10, 'Jack', 'jack@example.com', 'password123', 'ADMIN');

-- ----------------------------
-- Triggers structure for table notifications
-- ----------------------------
DROP TRIGGER IF EXISTS `trg_after_notification_insert`;
delimiter ;;
CREATE TRIGGER `trg_after_notification_insert` AFTER INSERT ON `notifications` FOR EACH ROW BEGIN
    IF NEW.status = 'SENT' THEN
        INSERT INTO notification_index (notification_id, recipient_id, is_read, notification_type, recipient_type)
        SELECT NEW.notification_id, user_id, FALSE, NEW.notification_type, 'INDIVIDUAL'
        FROM user_departments
        WHERE department_id = NEW.sender_department_id;
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
