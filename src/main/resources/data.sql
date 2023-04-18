insert into user(agree_promotion, is_withdraw, password, username, role)
select 0, 0, 'admin', 'Zv5wuh01Y37RQ', 'ROLE_ADMIN'
    where not exists (
    select username from user where username = 'admin'
);