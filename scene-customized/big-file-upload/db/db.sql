create table if not exists file_chunk
(
    id bigint unsigned auto_increment
        primary key,
    file_name varchar(255) null comment '文件名',
    chunk_number int null comment '当前分片，从1开始',
    chunk_size bigint null comment '分片大小',
    current_chunk_size bigint null comment '当前分片大小',
    total_size bigint null comment '文件总大小',
    total_chunk int null comment '总分片数',
    identifier varchar(128) null comment '文件校验码，md5',
    relative_path varchar(255) null comment '相对路径',
    create_by varchar(128) null comment '创建者',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by varchar(128) null comment '更新人',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '文件块存储';

create table if not exists file_storage
(
    id bigint auto_increment comment '主键'
        primary key,
    real_name varchar(128) null comment '文件真实姓名',
    file_name varchar(128) null comment '文件名',
    suffix varchar(32) null comment '文件后缀',
    file_path varchar(255) null comment '文件路径',
    file_type varchar(255) null comment '文件类型',
    size bigint null comment '文件大小',
    identifier varchar(128) null comment '检验码 md5',
    create_by varchar(128) null comment '创建者',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by varchar(128) null comment '更新人',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'

)
    comment '文件存储表';

