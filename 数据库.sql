/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : hospital

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 11/07/2024 10:46:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for administrator
-- ----------------------------
DROP TABLE IF EXISTS `administrator`;
CREATE TABLE `administrator`  (
  `id` bigint(0) NOT NULL,
  `addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `creat_date` date NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_date` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of administrator
-- ----------------------------
INSERT INTO `administrator` VALUES (1, '山东理工大学', NULL, '2024-07-03', '王五六', '81dc9bdb52d04dc20036dbd8313ed055', '19867288922', '2024-07-03');
INSERT INTO `administrator` VALUES (2, '山东理工大学', NULL, '2024-07-10', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '19862515623', '2024-07-10');

-- ----------------------------
-- Table structure for bed
-- ----------------------------
DROP TABLE IF EXISTS `bed`;
CREATE TABLE `bed`  (
  `id` bigint(0) NOT NULL,
  `number` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bed
-- ----------------------------
INSERT INTO `bed` VALUES (1, 1);
INSERT INTO `bed` VALUES (2, 2);
INSERT INTO `bed` VALUES (3, 3);
INSERT INTO `bed` VALUES (4, 4);
INSERT INTO `bed` VALUES (5, 5);
INSERT INTO `bed` VALUES (6, 6);
INSERT INTO `bed` VALUES (7, 7);
INSERT INTO `bed` VALUES (8, 8);
INSERT INTO `bed` VALUES (9, 9);
INSERT INTO `bed` VALUES (10, 10);
INSERT INTO `bed` VALUES (11, 11);
INSERT INTO `bed` VALUES (12, 12);
INSERT INTO `bed` VALUES (13, 13);
INSERT INTO `bed` VALUES (14, 14);
INSERT INTO `bed` VALUES (15, 15);
INSERT INTO `bed` VALUES (16, 16);
INSERT INTO `bed` VALUES (17, 17);
INSERT INTO `bed` VALUES (18, 18);
INSERT INTO `bed` VALUES (19, 19);
INSERT INTO `bed` VALUES (20, 20);
INSERT INTO `bed` VALUES (21, 21);

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `id` bigint(0) NOT NULL,
  `addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `creat_date` date NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_date` date NULL DEFAULT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctor
-- ----------------------------
INSERT INTO `doctor` VALUES (1, '陕西', '1.jpg', '2024-07-03', '张三', '81dc9bdb52d04dc20036dbd8313ed055', '14534567789', '2024-07-03', b'1');
INSERT INTO `doctor` VALUES (2, '山东', NULL, '2024-07-03', '李五', 'e10adc3949ba59abbe56e057f20f883e', '19878928922', '2024-07-03', b'1');
INSERT INTO `doctor` VALUES (3, '北京', NULL, '2024-07-03', '李华', '81dc9bdb52d04dc20036dbd8313ed055', '17734537872', '2024-07-03', b'1');
INSERT INTO `doctor` VALUES (4, '安徽', NULL, '2024-07-10', '王小明', '202cb962ac59075b964b07152d234b70', '13812344321', '2024-07-10', b'0');
INSERT INTO `doctor` VALUES (5, '天津', NULL, '2024-07-10', '赵六', 'caf1a3dfb505ffed0d024130f58c5cfa', '19862515230', '2024-07-11', b'0');
INSERT INTO `doctor` VALUES (6, '上海', NULL, '2024-07-10', '孙七', '0266e33d3f546cb5436a10798e657d97', '17867238822', '2024-07-10', b'0');
INSERT INTO `doctor` VALUES (7, '北京', NULL, '2024-07-10', '周八', '250cf8b51c773f3f8dc8b4be867a9a02', '17655447777', '2024-07-10', b'0');
INSERT INTO `doctor` VALUES (8, '山东', NULL, '2024-07-10', '吴九', '9fe8593a8a330607d76796b35c64c600', '16756775666', '2024-07-10', b'0');
INSERT INTO `doctor` VALUES (9, '山东', NULL, '2024-07-10', '郑十', '68053af2923e00204c3ca7c6a3150cf7', '14566667777', '2024-07-10', b'0');
INSERT INTO `doctor` VALUES (10, '山东', NULL, '2024-07-10', '冯十一', '854d9fca60b4bd07f9bb215d59ef5561', '19878237877', '2024-07-10', b'0');
INSERT INTO `doctor` VALUES (11, '山东', NULL, '2024-07-10', '陈十二', '202cb962ac59075b964b07152d234b70', '13812344321', '2024-07-12', b'0');

-- ----------------------------
-- Table structure for drug
-- ----------------------------
DROP TABLE IF EXISTS `drug`;
CREATE TABLE `drug`  (
  `id` bigint(0) NOT NULL,
  `creat_date` date NULL DEFAULT NULL,
  `drugname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isfinished` bit(1) NOT NULL,
  `number` int(0) NULL DEFAULT NULL,
  `price` float NULL DEFAULT NULL,
  `patient_id` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKjwj9t8dwptnixl9cv862wsgoo`(`patient_id`) USING BTREE,
  CONSTRAINT `FKjwj9t8dwptnixl9cv862wsgoo` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of drug
-- ----------------------------
INSERT INTO `drug` VALUES (1, '2024-07-03', '氯霉素眼药水', b'1', 21, 47.1, 5);
INSERT INTO `drug` VALUES (2, '2024-07-03', '阿司匹林', b'1', 22, 39.6, 3);
INSERT INTO `drug` VALUES (3, '2024-07-05', '阿莫西林', b'1', 23, 22.4, 3);
INSERT INTO `drug` VALUES (4, '2024-07-06', '维生素C片', b'1', 24, 29.9, 3);
INSERT INTO `drug` VALUES (5, '2024-07-07', '感冒灵颗粒', b'1', 25, 28.6, 6);
INSERT INTO `drug` VALUES (6, '2024-07-07', '双黄连口服液', b'1', 45, 45.9, NULL);
INSERT INTO `drug` VALUES (7, '2024-07-08', '清开灵注射液', b'1', 40, 24.8, NULL);
INSERT INTO `drug` VALUES (8, '2024-07-09', '盐酸二甲双胍片', b'1', 35, 76.5, NULL);
INSERT INTO `drug` VALUES (9, '2024-07-09', '美洛昔康片', b'1', 30, 36.1, NULL);
INSERT INTO `drug` VALUES (10, '2024-07-09', '复方甘草片', b'1', 35, 18, NULL);
INSERT INTO `drug` VALUES (11, '2024-07-09', '氨茶碱片', b'1', 30, 74.44, NULL);

-- ----------------------------
-- Table structure for examination
-- ----------------------------
DROP TABLE IF EXISTS `examination`;
CREATE TABLE `examination`  (
  `id` bigint(0) NOT NULL,
  `check_time` date NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `creat_time` date NULL DEFAULT NULL,
  `isfinished` bit(1) NOT NULL,
  `price` float NULL DEFAULT NULL,
  `patient_id` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKmn1l13u1iroqkv46h8dnxcmuw`(`patient_id`) USING BTREE,
  CONSTRAINT `FKmn1l13u1iroqkv46h8dnxcmuw` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of examination
-- ----------------------------
INSERT INTO `examination` VALUES (1, '2024-07-03', 'X光检测', '2024-07-03', b'1', 200, 3);
INSERT INTO `examination` VALUES (2, '2024-07-04', '抽血', '2024-07-04', b'1', 15, 3);
INSERT INTO `examination` VALUES (3, '2024-07-06', '化验', '2024-07-06', b'1', 20, 6);
INSERT INTO `examination` VALUES (4, '2024-07-06', '血常规检查', '2024-07-06', b'1', 200, NULL);
INSERT INTO `examination` VALUES (5, '2024-07-07', '超声波检查', '2024-07-07', b'1', 500, NULL);
INSERT INTO `examination` VALUES (6, '2024-07-08', '心电图检查', '2024-07-08', b'1', 250, NULL);
INSERT INTO `examination` VALUES (7, '2024-07-08', '胃镜检查', '2024-07-08', b'1', 600, NULL);
INSERT INTO `examination` VALUES (8, '2024-07-09', '心脏超声检查', '2024-07-09', b'1', 400, NULL);
INSERT INTO `examination` VALUES (9, '2024-07-09', '骨密度检查', '2024-07-09', b'1', 350, NULL);
INSERT INTO `examination` VALUES (10, '2024-07-10', 'CT扫描', '2024-07-10', b'1', 800, NULL);
INSERT INTO `examination` VALUES (11, '2024-07-10', '尿常规检查', '2024-07-10', b'1', 100, NULL);

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint(0) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES (18);

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `id` bigint(0) NOT NULL,
  `addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `creat_date` date NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_date` date NULL DEFAULT NULL,
  `bed_id` bigint(0) NULL DEFAULT NULL,
  `doctor_id` bigint(0) NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `drug_id` bigint(0) NULL DEFAULT NULL,
  `drugsprice` float NULL DEFAULT NULL,
  `create_date` datetime(6) NULL DEFAULT NULL,
  `discharged` bit(1) NOT NULL,
  `active` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK833k041j9274jwm6v3xm0f05f`(`bed_id`) USING BTREE,
  INDEX `FKmer5utvy1hiff7ovs6f4bjtnw`(`doctor_id`) USING BTREE,
  CONSTRAINT `FK833k041j9274jwm6v3xm0f05f` FOREIGN KEY (`bed_id`) REFERENCES `bed` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKmer5utvy1hiff7ovs6f4bjtnw` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES (1, '山东', NULL, '2024-07-02', '托尼', '13812344321', '2024-07-09', 1, 1, '81dc9bdb52d04dc20036dbd8313ed055', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (2, '陕西', NULL, '2024-07-04', '杰瑞', '123-122-111', '2024-07-05', 2, 2, '81dc9bdb52d04dc20036dbd8313ed055', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (3, '山东', NULL, '2024-07-03', '李四', '13812344321', '2024-07-28', 3, 3, '81dc9bdb52d04dc20036dbd8313ed055', NULL, NULL, NULL, b'0', b'1');
INSERT INTO `patient` VALUES (5, '山东', NULL, '2024-07-03', '肉丝', '13812344321', '2024-07-07', 4, 1, '81dc9bdb52d04dc20036dbd8313ed055', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (6, '山东', NULL, '2024-07-07', '杰克', '13812344321', '2024-07-08', 6, 2, '81dc9bdb52d04dc20036dbd8313ed055', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (7, '上海', NULL, '2024-07-08', '蒋十三', '19878237877', '2024-07-09', NULL, NULL, '202cb962ac59075b964b07152d234b70', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (8, '安徽', NULL, '2024-07-08', '沈十四', '19862515222', '2024-07-10', NULL, NULL, '92daa86ad43a42f28f4bf58e94667c95', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (9, '辽宁', NULL, '2024-07-09', '韩十五', '16756775666', '2024-07-11', NULL, NULL, '202cb962ac59075b964b07152d234b70', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (10, '陕西', NULL, '2024-07-09', '杨十六', '19862515230', '2024-07-11', NULL, NULL, '202cb962ac59075b964b07152d234b70', NULL, NULL, NULL, b'0', b'0');
INSERT INTO `patient` VALUES (11, '山西', NULL, '2024-07-10', '朱十七', '14566667777', '2024-07-11', NULL, NULL, '81dc9bdb52d04dc20036dbd8313ed055', NULL, NULL, NULL, b'0', b'0');

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `end_day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `start_day` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `doctor_id` bigint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKqixlhugy7jvrwut9o2s6hqnu8`(`doctor_id`) USING BTREE,
  CONSTRAINT `FKqixlhugy7jvrwut9o2s6hqnu8` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES (1, '星期天', '星期一', 1);
INSERT INTO `schedule` VALUES (2, '星期五', '星期二', 2);
INSERT INTO `schedule` VALUES (3, '星期六', '星期二', 3);
INSERT INTO `schedule` VALUES (4, '星期一', '星期三', 5);
INSERT INTO `schedule` VALUES (5, '星期天', '星期四', 4);
INSERT INTO `schedule` VALUES (6, '星期天', '星期二', 8);
INSERT INTO `schedule` VALUES (7, '星期天', '星期二', 9);
INSERT INTO `schedule` VALUES (8, '星期四', '星期一', 6);
INSERT INTO `schedule` VALUES (9, '星期三', '星期一', 10);

SET FOREIGN_KEY_CHECKS = 1;
