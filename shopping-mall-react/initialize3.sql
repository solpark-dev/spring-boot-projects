-- ===============================================
-- initialize3.sql - ì‚¬ìš©ìž ì œì•ˆ ë°˜ì˜ ìµœì¢… ë²„ì „
-- ===============================================

-- ===============================================
-- 1. ê¸°ì¡´ ê°ì²´ ì‚­ì œ (ì°¸ì¡° ì—­ìˆœ)
-- ===============================================
-- í…Œì´ë¸” ì‚­ì œ
drop table new_payments;
drop table new_cart;
drop table new_order_details;
drop table new_orders;
drop table product_files;
drop table user_social_accounts;
drop table user_addresses;
drop table new_product;
drop table new_categories;
drop table new_users;

-- ì‹œí€€ìŠ¤ ì‚­ì œ
drop sequence seq_user_no;
drop sequence seq_social_account_id;
drop sequence seq_product_prod_no;
drop sequence seq_orders_order_no;
drop sequence seq_product_files_id;
drop sequence seq_categories_id;
drop sequence seq_addresses_id;
drop sequence seq_cart_id;
drop sequence seq_order_details_id;

-- ===============================================
-- 2. ì‹œí€€ìŠ¤ ìƒì„±
-- ===============================================
create sequence seq_user_no increment by 1 start with 1;
create sequence seq_social_account_id increment by 1 start with 1;
create sequence seq_product_prod_no increment by 1 start with 10000;
create sequence seq_orders_order_no increment by 1 start with 10000;
create sequence seq_product_files_id increment by 1 start with 1;
create sequence seq_categories_id increment by 1 start with 100;
create sequence seq_addresses_id increment by 1 start with 1;
create sequence seq_cart_id increment by 1 start with 1;
create sequence seq_order_details_id increment by 1 start with 1;

-- ===============================================
-- 3. í…Œì´ë¸” ìƒì„±
-- ===============================================

-- 3-1. ì¹´í…Œê³ ë¦¬ í…Œì´ë¸”
create table new_categories (
   category_id   number not null,
   category_name varchar2(50) not null,
   primary key ( category_id )
);

-- 3-2. ì‚¬ìš©ìž í…Œì´ë¸”
create table new_users (
   user_no   number not null,
   user_id   varchar2(50) unique,
   user_name varchar2(50) not null,
   password  varchar2(20),
   role      varchar2(5) default 'user' check ( role in ( 'user',
                                                     'admin' ) ),
   status    varchar2(10) default 'ACTIVE' not null check ( status in ( 'ACTIVE',
                                                                     'WITHDRAWN',
                                                                     'DORMANT' ) ),
   email     varchar2(50),
   reg_date  date,
   primary key ( user_no )
);

-- 3-3. ì†Œì…œ ê³„ì • ì—°ë™ ì •ë³´ í…Œì´ë¸”
create table user_social_accounts (
   social_account_id number not null,
   user_no           number not null,
   provider          varchar2(20) not null,
   provider_user_id  varchar2(100) not null,
   primary key ( social_account_id ),
   foreign key ( user_no )
      references new_users ( user_no ),
   unique ( provider,
            provider_user_id )
);

-- 3-4. ë‹¤ì¤‘ ë°°ì†¡ì§€ í…Œì´ë¸”
create table user_addresses (
   address_id    number not null,
   user_no       number not null,
   address_alias varchar2(50) not null,
   receiver_name varchar2(50) not null,
   address       varchar2(200) not null,
   phone_number  varchar2(20) not null,
   is_default    char(1) default 'N' check ( is_default in ( 'Y',
                                                          'N' ) ),
   primary key ( address_id ),
   foreign key ( user_no )
      references new_users ( user_no )
);

-- 3-5. ìƒí’ˆ í…Œì´ë¸”
create table new_product (
   prod_no         number not null,
   category_id     number,
   prod_name       varchar2(100) not null,
   prod_detail     varchar2(200),
   manufacture_day varchar2(8),
   price           number(10) check ( price >= 0 ),
   stock           number(10) default 0 check ( stock >= 0 ),
   reg_date        date,
   primary key ( prod_no ),
   foreign key ( category_id )
      references new_categories ( category_id )
);

-- 3-6. ìƒí’ˆ íŒŒì¼ í…Œì´ë¸” (ìƒì„¸ ì •ë³´ ì»¬ëŸ¼ ë³µì›)
create table product_files (
   file_id       number not null,
   prod_no       number not null,
   original_name varchar2(200) not null,
   saved_name    varchar2(200) not null,
   file_size     number,
   file_type     varchar2(100),
   upload_date   date default sysdate,
   primary key ( file_id ),
   foreign key ( prod_no )
      references new_product ( prod_no )
         on delete cascade
);

-- 3-7. ì£¼ë¬¸ í…Œì´ë¸” (CHECK ì œì•½ì¡°ê±´ ì¶”ê°€)
create table new_orders (
   order_no          number not null,
   buyer_user_no     number not null,
   address_id        number not null,
   total_price       number(12) not null,
   order_status_code varchar2(20) default 'PAYMENT_PENDING' not null check ( order_status_code in ( 'PAYMENT_PENDING',
                                                                                                    'PAYMENT_COMPLETE',
                                                                                                    'PREPARING_SHIPMENT',
                                                                                                    'SHIPPED',
                                                                                                    'DELIVERED',
                                                                                                    'CANCELLED' ) ),
   order_date        date default sysdate,
   primary key ( order_no ),
   foreign key ( buyer_user_no )
      references new_users ( user_no ),
   foreign key ( address_id )
      references user_addresses ( address_id )
);

-- 3-8. ì£¼ë¬¸ ìƒì„¸ í…Œì´ë¸”
create table new_order_details (
   order_detail_id number not null,
   order_no        number not null,
   prod_no         number not null,
   quantity        number not null check ( quantity > 0 ),
   price_per_item  number not null,
   primary key ( order_detail_id ),
   foreign key ( order_no )
      references new_orders ( order_no ),
   foreign key ( prod_no )
      references new_product ( prod_no )
);

-- 3-9. ìž¥ë°”êµ¬ë‹ˆ í…Œì´ë¸”
create table new_cart (
   cart_id  number not null,
   user_no  number not null,
   prod_no  number not null,
   quantity number default 1 not null,
   reg_date date default sysdate,
   primary key ( cart_id ),
   foreign key ( user_no )
      references new_users ( user_no ),
   foreign key ( prod_no )
      references new_product ( prod_no ),
   unique ( user_no,
            prod_no )
);

-- 3-10. ê²°ì œ ì •ë³´ í…Œì´ë¸”
create table new_payments (
   payment_id     varchar2(50) not null,
   order_no       number not null,
   payment_method varchar2(20) not null,
   amount         number not null,
   status         varchar2(20) not null,
   paid_at        date default sysdate,
   primary key ( payment_id ),
   foreign key ( order_no )
      references new_orders ( order_no )
);

-- ===============================================
-- 4. ë”ë¯¸ ë°ì´í„° ì‚½ìž…
-- ===============================================
-- ì¹´í…Œê³ ë¦¬
insert into new_categories values ( seq_categories_id.nextval,
                                    'ë…¸íŠ¸ë¶' );
insert into new_categories values ( seq_categories_id.nextval,
                                    'ìŠ¤í¬ì¸ ìš©í’ˆ' );

-- ì‚¬ìš©ìž (user_no: 1, 2, 3, 4)
insert into new_users (
   user_no,
   user_id,
   user_name,
   password,
   role,
   email,
   reg_date
) values ( seq_user_no.nextval,
           'admin',
           'ê´€ë¦¬ìž',
           '1234',
           'admin',
           'admin@mvc.com',
           sysdate );
insert into new_users (
   user_no,
   user_id,
   user_name,
   password,
   role,
   email,
   reg_date
) values ( seq_user_no.nextval,
           'user01',
           'ìŠ¤ìº‡',
           '1111',
           'user',
           'user01@mvc.com',
           sysdate );
insert into new_users (
   user_no,
   user_id,
   user_name,
   password,
   role,
   email,
   reg_date
) values ( seq_user_no.nextval,
           'user02',
           'íƒ€ì´ê±°',
           '2222',
           'user',
           'user02@mvc.com',
           sysdate );
insert into new_users (
   user_no,
   user_id,
   user_name,
   password,
   role,
   email,
   reg_date
) values ( seq_user_no.nextval,
           null,
           'ê¹€ì´ì„œ',
           null,
           'user',
           'seo@example.com',
           sysdate );

-- ì†Œì…œ ê³„ì • ì—°ë™ ì •ë³´ (4ë²ˆ íšŒì›ì„ ì¹´ì¹´ì˜¤ ê³„ì •ê³¼ ì—°ë™)
insert into user_social_accounts (
   social_account_id,
   user_no,
   provider,
   provider_user_id
) values ( seq_social_account_id.nextval,
           4,
           'KAKAO',
           '1234567890' );

-- ë°°ì†¡ì§€
insert into user_addresses values ( seq_addresses_id.nextval,
                                    2,
                                    'ì§‘',
                                    'ìŠ¤ìº‡',
                                    'ì„œìš¸ì‹œ ê°•ë‚¨êµ¬ í…Œí—¤ëž€ë¡œ',
                                    '010-1111-1111',
                                    'Y' );
insert into user_addresses values ( seq_addresses_id.nextval,
                                    3,
                                    'íšŒì‚¬',
                                    'íƒ€ì´ê±°',
                                    'ê²½ê¸°ë„ ì„±ë‚¨ì‹œ ë¶„ë‹¹êµ¬',
                                    '010-2222-2222',
                                    'Y' );

-- ìƒí’ˆ
insert into new_product (
   prod_no,
   category_id,
   prod_name,
   prod_detail,
   price,
   stock,
   reg_date
) values ( seq_product_prod_no.nextval,
           100,
           'vaio vgn FS70B',
           'ì†Œë‹ˆ ë°”ì´ì˜¤ ë…¸íŠ¸ë¶',
           2000000,
           10,
           sysdate );
insert into new_product (
   prod_no,
   category_id,
   prod_name,
   prod_detail,
   price,
   stock,
   reg_date
) values ( seq_product_prod_no.nextval,
           101,
           'ìžì „ê±°',
           'ìžì „ê±° ì¢‹ì•„ìš”~',
           10000,
           50,
           sysdate );

-- ìƒí’ˆ íŒŒì¼ (ë”ë¯¸ ë°ì´í„° ë³µì›)
insert into product_files values ( seq_product_files_id.nextval,
                                   10000,
                                   'vaio_laptop.jpg',
                                   'uuid_save_name_001.jpg',
                                   245760,
                                   'image/jpeg',
                                   sysdate );
insert into product_files values ( seq_product_files_id.nextval,
                                   10001,
                                   'bicycle.jpg',
                                   'uuid_save_name_002.jpg',
                                   189440,
                                   'image/jpeg',
                                   sysdate );

-- ì£¼ë¬¸ (2ë²ˆ íšŒì›(ìŠ¤ìº‡)ì´ ë…¸íŠ¸ë¶ 1ê°œ ì£¼ë¬¸)
insert into new_orders (
   order_no,
   buyer_user_no,
   address_id,
   total_price,
   order_status_code,
   order_date
) values ( seq_orders_order_no.nextval,
           2,
           1,
           2000000,
           'PAYMENT_COMPLETE',
           sysdate );
insert into new_order_details (
   order_detail_id,
   order_no,
   prod_no,
   quantity,
   price_per_item
) values ( seq_order_details_id.nextval,
           10000,
           10000,
           1,
           2000000 );

-- ê²°ì œ ì •ë³´
insert into new_payments (
   payment_id,
   order_no,
   payment_method,
   amount,
   status,
   paid_at
) values ( 'pg_tid_12345abcde',
           10000,
           'KAKAOPAY',
           2000000,
           'COMPLETED',
           sysdate );

-- ìž¥ë°”êµ¬ë‹ˆ (3ë²ˆ íšŒì›(íƒ€ì´ê±°)ì´ ìžì „ê±° 2ê°œë¥¼ ë‹´ì•„ë‘ )
insert into new_cart (
   cart_id,
   user_no,
   prod_no,
   quantity,
   reg_date
) values ( seq_cart_id.nextval,
           3,
           10001,
           2,
           sysdate );

commit;