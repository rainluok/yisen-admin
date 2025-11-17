# 创建数据库，如果不存在
CREATE DATABASE IF NOT EXISTS `yisen_admin` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `yisen_admin`;
-- 用户表
DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user`
(
    `id`          varchar(32) NOT NULL COMMENT '主键id',
    `username`    varchar(32) NOT NULL COMMENT '用户名',
    `password`    varchar(64) NOT NULL COMMENT '密码',
    `real_name`   varchar(32)          DEFAULT NULL COMMENT '真实姓名',
    `gender`      tinyint(1)           DEFAULT NULL COMMENT '性别 0-未知 1-男 2-女',
    `avatar`      varchar(255)         DEFAULT NULL COMMENT '头像',
    `email`       varchar(32) NOT NULL NULL COMMENT '邮箱',
    `depart_id`   varchar(32)          DEFAULT NULL COMMENT '部门ID',
    `login_ip`    varchar(45)          DEFAULT NULL COMMENT '最后登录IP',
    `login_time`  datetime             DEFAULT NULL COMMENT '最后登录时间',
    `status`      tinyint(1)  NOT NULL DEFAULT 1 COMMENT '用户状态 0-禁用 1-启用',
    `is_deleted`  tinyint(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    `create_by`   varchar(32)          DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32)          DEFAULT NULL COMMENT '更新人',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uniq_username` (`username`),
    UNIQUE KEY `uniq_email` (`email`),
    KEY `idx_status` (`status`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统用户表'
  ROW_FORMAT = Dynamic;

-- 部门表
DROP TABLE IF EXISTS `sys_depart`;

CREATE TABLE `sys_depart`
(
    `id`          varchar(32) NOT NULL COMMENT '主键id',
    `parent_id`   varchar(32)          DEFAULT NULL COMMENT '父部门ID',
    `depart_name` varchar(64) NOT NULL COMMENT '部门名称',
    `depart_code` varchar(32) NOT NULL COMMENT '部门编码',
    `leader`      varchar(32)          DEFAULT NULL COMMENT '负责人',
    `phone`       varchar(20)          DEFAULT NULL COMMENT '部门电话',
    `email`       varchar(64)          DEFAULT NULL COMMENT '部门邮箱',
    `status`      tinyint(1)  NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `sort`        int(11)              DEFAULT 0 COMMENT '排序',
    `is_deleted`  tinyint(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    `create_by`   varchar(32)          DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32)          DEFAULT NULL COMMENT '更新人',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统部门表'
  ROW_FORMAT = Dynamic;

-- 用户部门关联表
DROP TABLE IF EXISTS `sys_user_depart`;

CREATE TABLE `sys_user_depart`
(
    `id`        varchar(32) NOT NULL COMMENT '主键id',
    `user_id`   varchar(32) NOT NULL COMMENT '用户ID',
    `depart_id` varchar(32) NOT NULL COMMENT '部门ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_user_depart` (`user_id`, `depart_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_depart_id` (`depart_id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户部门关联表';

-- 角色表
DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role`
(
    `id`          varchar(32) NOT NULL COMMENT '主键id',
    `role_name`   varchar(32) NOT NULL COMMENT '角色名称',
    `role_code`   varchar(32) NOT NULL COMMENT '角色编码',
    `description` varchar(255)         DEFAULT NULL COMMENT '描述',
    `status`      tinyint(1)  NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `is_deleted`  tinyint(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`   varchar(32)          DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32)          DEFAULT NULL COMMENT '更新人',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_role_code` (`role_code`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统角色表'
  ROW_FORMAT = Dynamic;

-- 用户角色关系表
DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role`
(
    `id`      varchar(32) NOT NULL COMMENT '主键id',
    `user_id` varchar(32) NOT NULL COMMENT '用户ID',
    `role_id` varchar(32) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '用户角色关系表';

-- 菜单表（权限）
DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu`
(
    `id`          varchar(32) NOT NULL COMMENT '主键id',
    `parent_id`   varchar(32)          DEFAULT 0 NULL COMMENT '父菜单ID',
    `menu_name`   varchar(64) NOT NULL COMMENT '菜单名称',
    `menu_code`   varchar(64) NOT NULL COMMENT '菜单编码',
    `type`        tinyint(1)  NOT NULL DEFAULT 1 COMMENT '类型 1-菜单 2-按钮',
    `path`        varchar(255)         DEFAULT NULL COMMENT '路由路径',
    `component`   varchar(255)         DEFAULT NULL COMMENT '组件路径',
    `permission`  varchar(64)          DEFAULT NULL COMMENT '权限标识',
    `icon`        varchar(64)          DEFAULT NULL COMMENT '图标',
    `hidden`      tinyint(1)  NOT NULL DEFAULT 0 COMMENT '是否隐藏 0-显示 1-隐藏',
    `layout`      varchar(16)          DEFAULT 'default' COMMENT '布局类型 default-默认 blank-空白',
    `sort`        int(11)              DEFAULT 0 COMMENT '排序',
    `status`      tinyint(1)  NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `is_deleted`  tinyint(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`   varchar(32)          DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32)          DEFAULT NULL COMMENT '更新人',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '系统菜单表';

-- 角色菜单关系表
DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu`
(
    `id`      varchar(32) NOT NULL COMMENT '主键id',
    `role_id` varchar(32) NOT NULL COMMENT '角色ID',
    `menu_id` varchar(32) NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_role_menu` (`role_id`, `menu_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关系表';

-- 字典表
DROP TABLE IF EXISTS `sys_dict`;

CREATE TABLE `sys_dict`
(
    `id`          varchar(32) NOT NULL COMMENT '主键id',
    `dict_name`   varchar(64) NOT NULL COMMENT '字典名称',
    `dict_code`   varchar(64) NOT NULL COMMENT '字典编码',
    `description` varchar(255)         DEFAULT NULL COMMENT '描述',
    `status`      tinyint(1)  NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `is_deleted`  tinyint(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`   varchar(32)          DEFAULT NULL COMMENT '创建人',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32)          DEFAULT NULL COMMENT '更新人',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_dict_code` (`dict_code`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据字典';

-- 字典明细表
DROP TABLE IF EXISTS `sys_dict_item`;

CREATE TABLE `sys_dict_item`
(
    `id`         varchar(32) NOT NULL COMMENT '主键id',
    `dict_id`    varchar(32) NOT NULL COMMENT '字典ID',
    `item_name`  varchar(64) NOT NULL COMMENT '字典项名称',
    `item_value` varchar(64) NOT NULL COMMENT '字典项值',
    `sort`       int(11)              DEFAULT 0 COMMENT '排序',
    `status`     tinyint(1)  NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `is_deleted` tinyint(1)  NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_dict_id` (`dict_id`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '数据字典项';

-- 登录日志表
DROP TABLE IF EXISTS `sys_login_log`;

CREATE TABLE `sys_login_log`
(
    `id`          VARCHAR(32) NOT NULL COMMENT '主键ID',
    `user_id`     VARCHAR(32)          DEFAULT NULL COMMENT '用户ID',
    `username`    VARCHAR(64)          DEFAULT NULL COMMENT '用户名（登录账号）',
    `status`      TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '状态：0-失败，1-成功',
    `ip`          VARCHAR(45)          DEFAULT NULL COMMENT '登录IP',
    `ip_location` VARCHAR(100)         DEFAULT NULL COMMENT 'IP归属地（可选）',
    `os`          VARCHAR(64)          DEFAULT NULL COMMENT '操作系统',
    `browser`     VARCHAR(64)          DEFAULT NULL COMMENT '浏览器',
    `msg`         VARCHAR(255)         DEFAULT NULL COMMENT '描述信息（如：密码错误、验证码不正确）',
    `login_time`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    PRIMARY KEY (`id`),
    INDEX `idx_username` (`username`),
    INDEX `idx_login_time` (`login_time`),
    INDEX `idx_status` (`status`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '登录日志';

-- 操作日志表
DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log`
(
    `id`             varchar(32) NOT NULL COMMENT '主键id',
    `user_id`        varchar(32)          DEFAULT NULL COMMENT '用户ID',
    `username`       varchar(32)          DEFAULT NULL COMMENT '用户名',
    `operation`      varchar(255)         DEFAULT NULL COMMENT '操作内容',
    `operation_type` tinyint(1)  NOT NULL DEFAULT 1 COMMENT '操作类型 1-新增 2-修改 3-删除 4-查询',
    `biz_type`       VARCHAR(64)          DEFAULT NULL COMMENT '业务类型（如：user, order）',
    `biz_id`         VARCHAR(32)          DEFAULT NULL COMMENT '业务ID（关联具体数据）',
    `request_url`    varchar(255)         DEFAULT NULL COMMENT '请求URL',
    `request_method` varchar(16)          DEFAULT NULL COMMENT '请求方式',
    `request_params` text                 DEFAULT NULL COMMENT '请求参数',
    `status`         tinyint(1)  NOT NULL DEFAULT 1 COMMENT '状态 0-失败 1-成功',
    `error_message`  text                 DEFAULT NULL COMMENT '错误信息',
    `ip`             varchar(45)          DEFAULT NULL COMMENT '操作IP',
    `os`             varchar(64)          DEFAULT NULL COMMENT '操作系统',
    `browser`        varchar(64)          DEFAULT NULL COMMENT '浏览器',
    `spend_time`     int(11)              DEFAULT NULL COMMENT '耗时(ms)',
    `create_time`    datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '操作日志';

-- 通知公告表
DROP TABLE IF EXISTS `sys_notice`;

CREATE TABLE `sys_notice`
(
    `id`          varchar(32)  NOT NULL COMMENT '主键id',
    `title`       varchar(255) NOT NULL COMMENT '标题',
    `content`     text         NOT NULL COMMENT '内容',
    `type`        tinyint(1)   NOT NULL DEFAULT 1 COMMENT '类型 1-公告 2-通知',
    `status`      tinyint(1)   NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    `is_deleted`  tinyint(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_by`   varchar(32)           DEFAULT NULL COMMENT '创建人',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(32)           DEFAULT NULL COMMENT '更新人',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_is_deleted` (`is_deleted`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '通知公告表';


-- 初始化用户数据
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `gender`, `avatar`, `email`, `login_ip`,
                        `login_time`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`)
VALUES ('1', 'admin', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '超级管理员', 1, NULL,
        'admin@example.com', NULL, NULL, 1, 0, 'system', NOW(), NULL, NOW());

-- 生成20个测试用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `gender`, `avatar`, `email`, `status`, `is_deleted`,
                        `create_by`, `create_time`)
VALUES ('2', 'testuser1', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户1', 1, NULL,
        'test1@example.com', 1, 0, 'system', NOW()),
       ('3', 'testuser2', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户2', 2, NULL,
        'test2@example.com', 1, 0, 'system', NOW()),
       ('4', 'testuser3', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户3', 1, NULL,
        'test3@example.com', 1, 0, 'system', NOW()),
       ('5', 'testuser4', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户4', 2, NULL,
        'test4@example.com', 1, 0, 'system', NOW()),
       ('6', 'testuser5', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户5', 1, NULL,
        'test5@example.com', 1, 0, 'system', NOW()),
       ('7', 'testuser6', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户6', 2, NULL,
        'test6@example.com', 1, 0, 'system', NOW()),
       ('8', 'testuser7', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户7', 1, NULL,
        'test7@example.com', 1, 0, 'system', NOW()),
       ('9', 'testuser8', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户8', 2, NULL,
        'test8@example.com', 1, 0, 'system', NOW()),
       ('10', 'testuser9', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户9', 1, NULL,
        'test9@example.com', 1, 0, 'system', NOW()),
       ('11', 'testuser10', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户10', 2, NULL,
        'test10@example.com', 1, 0, 'system', NOW()),
       ('12', 'testuser11', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户11', 1, NULL,
        'test11@example.com', 1, 0, 'system', NOW()),
       ('13', 'testuser12', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户12', 2, NULL,
        'test12@example.com', 1, 0, 'system', NOW()),
       ('14', 'testuser13', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户13', 1, NULL,
        'test13@example.com', 1, 0, 'system', NOW()),
       ('15', 'testuser14', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户14', 2, NULL,
        'test14@example.com', 1, 0, 'system', NOW()),
       ('16', 'testuser15', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户15', 1, NULL,
        'test15@example.com', 1, 0, 'system', NOW()),
       ('17', 'testuser16', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户16', 2, NULL,
        'test16@example.com', 1, 0, 'system', NOW()),
       ('18', 'testuser17', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户17', 1, NULL,
        'test17@example.com', 1, 0, 'system', NOW()),
       ('19', 'testuser18', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户18', 2, NULL,
        'test18@example.com', 1, 0, 'system', NOW()),
       ('20', 'testuser19', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户19', 1, NULL,
        'test19@example.com', 1, 0, 'system', NOW()),
       ('21', 'testuser20', '$2a$12$.GkA1usulfiXs.7J49wm5u2pyPwdgCJmoyPnu8xGZd8nd.ra8kEbW', '测试用户20', 2, NULL,
        'test20@example.com', 1, 0, 'system', NOW());

-- 初始化部门数据（含更多部门及多级结构）
INSERT INTO `sys_depart` (`id`, `parent_id`, `depart_name`, `depart_code`, `leader`, `phone`, `email`, `status`, `sort`,
                          `is_deleted`, `create_by`, `create_time`, `update_by`, `update_time`)
VALUES ('100', NULL, '总部', 'ZB', 'admin', '12345678901', 'hq@example.com', 1, 0, 0, 'system', NOW(), NULL, NOW()),
       ('110', '100', '技术部', 'JSB', 'admin', '12345678902', 'tech@example.com', 1, 1, 0, 'system', NOW(), NULL,
        NOW()),
       ('120', '100', '市场部', 'SCB', 'admin', '12345678903', 'marketing@example.com', 1, 2, 0, 'system', NOW(), NULL,
        NOW()),
       ('130', '100', '人事部', 'RSB', 'admin', '12345678904', 'hr@example.com', 1, 3, 0, 'system', NOW(), NULL, NOW()),
       ('111', '110', '研发组', 'YFZ', 'admin', '12345678905', 'dev@example.com', 1, 1, 0, 'system', NOW(), NULL,
        NOW()),
       ('112', '110', '运维组', 'YWZ', 'admin', '12345678906', 'ops@example.com', 1, 2, 0, 'system', NOW(), NULL,
        NOW()),
       ('121', '120', '销售组', 'XSZ', 'admin', '12345678907', 'sales@example.com', 1, 1, 0, 'system', NOW(), NULL,
        NOW()),
       ('131', '130', '招聘组', 'ZPZ', 'admin', '12345678908', 'recruit@example.com', 1, 1, 0, 'system', NOW(), NULL,
        NOW());

-- 初始化用户部门关联
INSERT INTO `sys_user_depart` (`id`, `user_id`, `depart_id`)
VALUES ('1001', '1', '100');

-- 初始化角色数据
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `status`, `is_deleted`, `create_by`,
                        `create_time`, `update_by`, `update_time`)
VALUES ('1', '系统管理员', 'admin', '系统最高权限', 1, 0, 'system', NOW(), NULL, NOW());

-- 初始化用户角色关系
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`)
VALUES ('10001', '1', '1');

-- 初始化菜单数据及按钮权限（数字字符串id，菜单和按钮区分type；type=1为菜单，type=2为按钮）
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_code`, `type`, `path`, `component`, `permission`, `icon`,
                        `hidden`, `layout`, `sort`, `status`, `is_deleted`, `create_by`, `create_time`, `update_by`,
                        `update_time`)
VALUES ('1', 0, '仪表盘', 'dashboard', 1, '/dashboard', 'dashboard/index', NULL, 'DataBoard', 0, 'default', 1, 1, 0,
        'system', NOW(), NULL, NOW()),

       ('2', 0, '系统管理', 'system', 1, '/system', 'Layout', NULL, 'setting', 0, 'default', 2, 1, 0, 'system',
        NOW(), NULL, NOW()),

       ('21', '2', '用户管理', 'user', 1, '/system/user', 'system/user/index', 'sys:user:list', 'User', 0, 'default', 1,
        1, 0, 'system', NOW(), NULL, NOW()),
       ('211', '21', '添加用户', 'user_add', 2, NULL, NULL, 'sys:user:add', '', 0, NULL, 1, 1, 0, 'system', NOW(), NULL,
        NOW()),
       ('212', '21', '编辑用户', 'user_edit', 2, NULL, NULL, 'sys:user:edit', '', 0, NULL, 2, 1, 0, 'system', NOW(),
        NULL, NOW()),
       ('213', '21', '删除用户', 'user_delete', 2, NULL, NULL, 'sys:user:delete', '', 0, NULL, 3, 1, 0, 'system', NOW(),
        NULL, NOW()),

       ('22', '2', '角色管理', 'role', 1, '/system/role', 'system/role/index', 'sys:role:list', 'UserFilled', 0,
        'default', 2,
        1, 0, 'system', NOW(), NULL, NOW()),
       ('221', '22', '添加角色', 'role_add', 2, NULL, NULL, 'sys:role:add', '', 0, NULL, 1, 1, 0, 'system', NOW(), NULL,
        NOW()),
       ('222', '22', '编辑角色', 'role_edit', 2, NULL, NULL, 'sys:role:edit', '', 0, NULL, 2, 1, 0, 'system', NOW(),
        NULL, NOW()),
       ('223', '22', '删除角色', 'role_delete', 2, NULL, NULL, 'sys:role:delete', '', 0, NULL, 3, 1, 0, 'system', NOW(),
        NULL, NOW()),

       ('23', '2', '菜单管理', 'menu', 1, '/system/menu', 'system/menu/index', 'sys:menu:list', 'Menu', 0, 'default', 3,
        1, 0, 'system', NOW(), NULL, NOW()),
       ('231', '23', '添加菜单', 'menu_add', 2, NULL, NULL, 'sys:menu:add', '', 0, NULL, 1, 1, 0, 'system', NOW(), NULL,
        NOW()),
       ('232', '23', '编辑菜单', 'menu_edit', 2, NULL, NULL, 'sys:menu:edit', '', 0, NULL, 2, 1, 0, 'system', NOW(),
        NULL, NOW()),
       ('233', '23', '删除菜单', 'menu_delete', 2, NULL, NULL, 'sys:menu:delete', '', 0, NULL, 3, 1, 0, 'system', NOW(),
        NULL, NOW()),

       ('24', '2', '部门管理', 'depart', 1, '/system/depart', 'system/depart/index', 'sys:depart:list',
        'OfficeBuilding', 0,
        'default', 4, 1, 0, 'system', NOW(), NULL, NOW()),
       ('241', '24', '添加部门', 'depart_add', 2, NULL, NULL, 'sys:depart:add', '', 0, NULL, 1, 1, 0, 'system', NOW(),
        NULL, NOW()),
       ('242', '24', '编辑部门', 'depart_edit', 2, NULL, NULL, 'sys:depart:edit', '', 0, NULL, 2, 1, 0, 'system', NOW(),
        NULL, NOW()),
       ('243', '24', '删除部门', 'depart_delete', 2, NULL, NULL, 'sys:depart:delete', '', 0, NULL, 3, 1, 0, 'system',
        NOW(), NULL, NOW()),

       ('25', '2', '数据字典', 'dict', 1, '/system/dict', 'system/dict/index', 'sys:dict:list',
        'List', 0,
        'default', 4, 1, 0, 'system', NOW(), NULL, NOW()),
       ('251', '25', '添加字典', 'dict_add', 2, NULL, NULL, 'sys:dict:add', '', 0, NULL, 1, 1, 0, 'system', NOW(),
        NULL, NOW()),
       ('252', '25', '编辑字典', 'dict_edit', 2, NULL, NULL, 'sys:dict:edit', '', 0, NULL, 2, 1, 0, 'system', NOW(),
        NULL, NOW()),
       ('253', '25', '删除字典', 'dict_delete', 2, NULL, NULL, 'sys:dict:delete', '', 0, NULL, 3, 1, 0, 'system',
        NOW(), NULL, NOW());


-- 初始化角色菜单关系（角色1拥有全部菜单按钮权限）
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`)
VALUES ('10001', '1', '1'),
       ('10002', '1', '2'),
       ('10021', '1', '21'),
       ('10211', '1', '211'),
       ('10212', '1', '212'),
       ('10213', '1', '213'),
       ('10022', '1', '22'),
       ('10221', '1', '221'),
       ('10222', '1', '222'),
       ('10223', '1', '223'),
       ('10023', '1', '23'),
       ('10231', '1', '231'),
       ('10232', '1', '232'),
       ('10233', '1', '233'),
       ('10024', '1', '24'),
       ('10241', '1', '241'),
       ('10242', '1', '242'),
       ('10243', '1', '243'),
       ('10025', '1', '25'),
       ('10251', '1', '251'),
       ('10252', '1', '252'),
       ('10253', '1', '253');

-- 初始化字典数据
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `status`, `is_deleted`, `create_by`,
                        `create_time`, `update_by`, `update_time`)
VALUES ('1', '性别', 'gender', '性别字典', 1, 0, 'system', NOW(), NULL, NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_name`, `item_value`, `sort`, `status`, `is_deleted`)
VALUES ('1', '1', '男', '1', 1, 1, 0),
       ('2', '1', '女', '2', 2, 1, 0),
       ('3', '1', '未知', '0', 3, 1, 0);

-- 用户状态
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `status`, `is_deleted`, `create_by`,
                        `create_time`, `update_by`, `update_time`)
VALUES ('2', '用户状态', 'user_status', '用户启用/禁用状态', 1, 0, 'system', NOW(), NULL, NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_name`, `item_value`, `sort`, `status`, `is_deleted`)
VALUES ('4', '2', '启用', '1', 1, 1, 0),
       ('5', '2', '禁用', '0', 2, 1, 0);

-- 是否/开关
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `status`, `is_deleted`, `create_by`,
                        `create_time`, `update_by`, `update_time`)
VALUES ('3', '是否', 'yes_no', '通用是/否选项', 1, 0, 'system', NOW(), NULL, NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_name`, `item_value`, `sort`, `status`, `is_deleted`)
VALUES ('6', '3', '是', '1', 1, 1, 0),
       ('7', '3', '否', '0', 2, 1, 0);

-- 菜单类型（与你的 MenuTreeVO.type 对应）
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `status`, `is_deleted`, `create_by`,
                        `create_time`, `update_by`, `update_time`)
VALUES ('4', '菜单类型', 'menu_type', '1-菜单 2-按钮', 1, 0, 'system', NOW(), NULL, NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_name`, `item_value`, `sort`, `status`, `is_deleted`)
VALUES ('8', '4', '菜单', '1', 1, 1, 0),
       ('9', '4', '按钮', '2', 2, 1, 0);

-- 布局类型（对应 MenuTreeVO.layout）
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `status`, `is_deleted`, `create_by`,
                        `create_time`, `update_by`, `update_time`)
VALUES ('5', '布局类型', 'layout_type', '页面布局类型', 1, 0, 'system', NOW(), NULL, NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_name`, `item_value`, `sort`, `status`, `is_deleted`)
VALUES ('10', '5', '默认布局', 'default', 1, 1, 0),
       ('11', '5', '空白布局', 'blank', 2, 1, 0);

-- 菜单显示状态（0-显示，1-隐藏）
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_code`, `description`, `status`, `is_deleted`, `create_by`,
                        `create_time`, `update_by`, `update_time`)
VALUES ('8', '菜单显示状态', 'menu_hidden', '控制菜单是否在侧边栏显示', 1, 0, 'system', NOW(), NULL, NOW());

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `item_name`, `item_value`, `sort`, `status`, `is_deleted`)
VALUES ('19', '8', '显示', '0', 1, 1, 0),
       ('20', '8', '隐藏', '1', 2, 1, 0);

-- 初始化通知公告
INSERT INTO `sys_notice` (`id`, `title`, `content`, `type`, `status`, `is_deleted`, `create_by`, `create_time`,
                          `update_by`, `update_time`)
VALUES ('1', '欢迎使用管理系统', '恭喜你，系统已初始化成功！', 1, 1, 0, 'system', NOW(), NULL, NOW());

