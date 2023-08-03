create table post_like (
                           post_like_id bigint not null auto_increment,
                           created_at varchar(255),
                           updated_at varchar(255),
                           liker_id bigint not null,
                           post_id bigint not null,
                           primary key (post_like_id)
) engine=InnoDB;

alter table post_like
    add constraint FK6q4r76iektbov6h8ofpacmu7
        foreign key (liker_id)
            references member (member_id);