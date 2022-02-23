create table notification
(
    id bigint auto_increment primary key,
    notifier bigint not null,
    receiver bigint not null,
    outerId bigint not null,
    type int not null,
    gmt_create bigint not null,
    status int default 0 not null
);
-- notifier  通知人 谁回复了问题
-- receiver  接收人 谁的问题被回复了
-- outerId   那个问题被回复了
-- type      这个问题是什么类型的
-- status    状态 已读还是未读