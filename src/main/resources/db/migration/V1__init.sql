create table member (
                        member_id bigint not null auto_increment,
                        created_at varchar(255),
                        updated_at varchar(255),
                        email varchar(255) not null,
                        role varchar(255) not null,
                        primary key (member_id)
) engine=InnoDB;

create table member_general (
                        member_general_id bigint not null auto_increment,
                        created_at varchar(255),
                        updated_at varchar(255),
                        is_auto bit not null,
                        member_id bigint,
                        password varchar(255) not null,
                        primary key (member_general_id)
) engine=InnoDB;

create table member_social (
                        member_social_id bigint not null auto_increment,
                        created_at varchar(255),
                        updated_at varchar(255),
                        member_id bigint,
                        provider varchar(255) not null,
                        primary key (member_social_id)
) engine=InnoDB;

create table member_profile (
                        member_profile_id bigint not null auto_increment,
                        created_at varchar(255),
                        updated_at varchar(255),
                        birth varchar(255) not null,
                        member_id bigint,
                        name varchar(255) not null,
                        phone varchar(255) not null,
                        primary key (member_profile_id)
) engine=InnoDB;

create table member_agreement (
                        member_agreement_id bigint not null auto_increment,
                        created_at varchar(255),
                        updated_at varchar(255),
                        member_id bigint,
                        personal_info bit not null,
                        third_party bit not null,
                        primary key (member_agreement_id)
) engine=InnoDB;

create table refresh_token (
                       rt_key varchar(255) not null,
                       rt_value varchar(255),
                       primary key (rt_key)
) engine=InnoDB;

create table store (
                       store_id bigint not null auto_increment,
                       created_at varchar(255),
                       updated_at varchar(255),
                       category varchar(255) not null,
                       image_url varchar(255),
                       location varchar(255) not null,
                       name varchar(255) not null,
                       phone varchar(255),
                       running_time varchar(255),
                       web_url varchar(255),
                       primary key (store_id)
) engine=InnoDB;

create table store_review (
                      store_review_id bigint not null auto_increment,
                      created_at varchar(255),
                      updated_at varchar(255),
                      content varchar(255) not null,
                      star integer not null,
                      reviewer_id bigint,
                      store_id bigint,
                      primary key (store_review_id)
) engine=InnoDB;

create table store_review_image (
                    store_review_image_id bigint not null auto_increment,
                    created_at varchar(255),
                    updated_at varchar(255),
                    image_url varchar(255),
                    store_review_id bigint,
                    primary key (store_review_image_id)
) engine=InnoDB;

create table store_bookmark (
                    store_bookmark_id bigint not null auto_increment,
                    created_at varchar(255),
                    updated_at varchar(255),
                    liker_id bigint,
                    store_id bigint,
                    primary key (store_bookmark_id)
) engine=InnoDB;

create table product (
                     product_id bigint not null auto_increment,
                     created_at varchar(255),
                     updated_at varchar(255),
                     delivery_fee integer not null,
                     image_url varchar(255) not null,
                     name varchar(255) not null,
                     price integer not null,
                     store_id bigint,
                     primary key (product_id)
) engine=InnoDB;

create table product_review (
                    product_review_id bigint not null auto_increment,
                    created_at varchar(255),
                    updated_at varchar(255),
                    content varchar(255) not null,
                    star integer not null,
                    product_id bigint,
                    reviewer_id bigint,
                    primary key (product_review_id)
) engine=InnoDB;

create table product_review_image (
                    product_review_image_id bigint not null auto_increment,
                    created_at varchar(255),
                    updated_at varchar(255),
                    image_url varchar(255),
                    product_review_id bigint,
                    primary key (product_review_image_id)
) engine=InnoDB;

create table product_bookmark (
                      product_bookmark_id bigint not null auto_increment,
                      created_at varchar(255),
                      updated_at varchar(255),
                      liker_id bigint,
                      product_id bigint,
                      primary key (product_bookmark_id)
) engine=InnoDB;

create table post (
                      post_id bigint not null auto_increment,
                      created_at varchar(255),
                      updated_at varchar(255),
                      content varchar(500) not null,
                      hits integer not null,
                      title varchar(255) not null,
                      writer_id bigint not null,
                      primary key (post_id)
) engine=InnoDB;

create table post_file (
                       post_file_id bigint not null auto_increment,
                       created_at varchar(255),
                       updated_at varchar(255),
                       file_url varchar(255) not null,
                       post_id bigint not null,
                       primary key (post_file_id)
) engine=InnoDB;

create table comment (
                        comment_id bigint not null auto_increment,
                        created_at varchar(255),
                        updated_at varchar(255),
                        content varchar(255) not null,
                        post_id bigint not null,
                        writer_id bigint not null,
                        primary key (comment_id)
) engine=InnoDB;

alter table comment
    add constraint FKjfdaen6h2c8o1axvh7j6go3rq
        foreign key (writer_id)
            references member (member_id);

alter table post
    add constraint FKh3voybp05rhyyvwlhflfrlti2
        foreign key (writer_id)
            references member (member_id);

alter table post_file
    add constraint FKn75omflablcagq3jsuoognqwy
        foreign key (post_id)
            references post (post_id);

alter table product
    add constraint FKjlfidudl1gwqem0flrlomvlcl
        foreign key (store_id)
            references store (store_id);

alter table product_bookmark
    add constraint FKn2xbshwu451bbb828sryxpsxl
        foreign key (liker_id)
            references member (member_id);

alter table product_bookmark
    add constraint FK554jnev92ugtgncb8gwh8s1wl
        foreign key (product_id)
            references product (product_id);

alter table product_review
    add constraint FKkaqmhakwt05p3n0px81b9pdya
        foreign key (product_id)
            references product (product_id);

alter table product_review
    add constraint FK9u6oygkfjryl8a7bdrrstclpa
        foreign key (reviewer_id)
            references member (member_id);

alter table product_review_image
    add constraint FKo86f5juukvexswduvbeuv1xb6
        foreign key (product_review_id)
            references product_review (product_review_id);

alter table store_bookmark
    add constraint FKjdrm0butylwoywfjv0456yu6c
        foreign key (liker_id)
            references member (member_id);

alter table store_bookmark
    add constraint FKr3gnv4i5p8owng94njp5yrm1o
        foreign key (store_id)
            references store (store_id);

alter table store_review
    add constraint FK1ts3vqxc66b406uh0xufs2qd7
        foreign key (reviewer_id)
            references member (member_id);

alter table store_review
    add constraint FK26ja9yjvjjka5drdk52y7fpt6
        foreign key (store_id)
            references store (store_id);

alter table store_review_image
    add constraint FKd36wcnn48jf4r5vms4wekion5
        foreign key (store_review_id)
            references store_review (store_review_id);