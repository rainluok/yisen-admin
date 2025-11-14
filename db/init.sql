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
  `email`       varchar(32)   NOT NULL       DEFAULT NULL COMMENT '邮箱',
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
  `parent_id`   varchar(32)          DEFAULT NULL COMMENT '父菜单ID',
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

-- 操作日志表
DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log`
(
  `id`             varchar(32) NOT NULL COMMENT '主键id',
  `user_id`        varchar(32)          DEFAULT NULL COMMENT '用户ID',
  `username`       varchar(32)          DEFAULT NULL COMMENT '用户名',
  `operation`      varchar(255)         DEFAULT NULL COMMENT '操作内容',
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
