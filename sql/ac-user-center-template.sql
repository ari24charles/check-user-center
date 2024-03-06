# the database of ari-user-center-template
DROP DATABASE IF EXISTS `ac-user-center-template`;

CREATE DATABASE IF NOT EXISTS `ac-user-center-template`;

USE `ac-user-center-template`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user`
(
    id          BIGINT                      NOT NULL COMMENT '雪花算法主键',
    username    VARCHAR(255)                NOT NULL COMMENT '账号',
    password    VARCHAR(255)                NOT NULL COMMENT '密码',
    nickname    VARCHAR(255)  DEFAULT ''    NOT NULL COMMENT '昵称',
    phone       VARCHAR(255)  DEFAULT ''    NOT NULL COMMENT '电话',
    email       VARCHAR(255)  DEFAULT NULL  NULL COMMENT '邮箱',
    avatar_url  VARCHAR(1024) DEFAULT NULL  NULL COMMENT '头像地址',
    gender      TINYINT       DEFAULT 0     NULL COMMENT '性别: 0 -> 保密, 1 -> 男, 2 -> 女',
    status      TINYINT       DEFAULT 0     NOT NULL COMMENT '用户状态: 0 -> 正常, 1 -> 封禁',
    role        TINYINT       DEFAULT 2     NOT NULL COMMENT '用户角色: 0 -> 超级管理员, 1 -> 管理员, 2 -> 普通用户',
    create_time DATETIME      DEFAULT NOW() NOT NULL COMMENT '创建时间',
    update_time DATETIME      DEFAULT NOW() NOT NULL COMMENT '更新时间' ON UPDATE NOW(),
    is_deleted  TINYINT       DEFAULT 0     NOT NULL COMMENT '逻辑删除: 0 -> 正常, 1 -> 删除',
    PRIMARY KEY (id),
    KEY `idx_nickname` (nickname)
) COMMENT '用户表';

# insert super admin
INSERT INTO `user`(id, username, password, nickname,
                   phone, email, avatar_url, role)
# user_password: 12345678 SALT = "ac-user-center"
VALUES (1, 'ariCharles', '88605e9c90610f5b77b251556b8000fc', 'Ari Charles', '11111111111',
        'ari.charles@outlook.com', 'https://img1.ppt118.com/png/detail/2019/09/04/6596e22b.jpg', 0);

# insert admin
INSERT INTO `user`(id, username, password, nickname,
                   phone, email, gender, status, role)
# user_password: 12345678
VALUES (2, 'leiYang', '88605e9c90610f5b77b251556b8000fc',
        'leiYang', '11111111112', 'leiYang@chevfwe.na', 1, 0, 1),
       # user_password: 12345678
       (3, 'Johnson', '88605e9c90610f5b77b251556b8000fc',
        'Margaret Johnson', '11111111113', 'Margaret.Johnson@tfxfgpjr.ke', 0, 1, 1),
       # user_password: 87654321
       (4, 'TomFang', '29aa7fe8cc56f2610affe2d32234fee4',
        '汤芳', '11111111114', 'r.kuwgj@fndiezsifz.cy', 2, 0, 1);

# insert user
INSERT INTO `user`(id, username, password,
                   nickname, phone, email, gender)
# user_password: 12345678
VALUES (5, 'MRZeng', '88605e9c90610f5b77b251556b8000fc',
        'MRZeng', '11111111115', 'MRZeng@sbl.zm', 1),
       # user_password: 12345678
       (6, '123456789', '88605e9c90610f5b77b251556b8000fc',
        '叶娟', '11111111116', '123456789@jhbttkrths.org.cn', 2),
       # user_password: 87654321
       (7, 'taotaotao', '29aa7fe8cc56f2610affe2d32234fee4',
        '方涛', '11111111117', 't.qyoptersv@rclrrozbq.hr', 1);