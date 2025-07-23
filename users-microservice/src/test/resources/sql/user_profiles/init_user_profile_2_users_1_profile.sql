insert into user (id,email,first_name,last_name,password,roles) values (1, 'yoichi.isagi@fromblue.com','Yoichi','Isagi','{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci','USER');
insert into user (id,email,first_name,last_name,password,roles) values (2, 'itochi.rin@fromblue.com','Itochi','Rin','{bcrypt}$2a$10$LaNgnLoLcy8Y/vKvn3nmT.kNk1qXwV3jdwzHyblWulpzD3zatOZci','USER');
insert into profile (id,name,description) values (1, 'Admin', 'Admin profile');
insert into user_profile (id,profile_id,user_id) values (1, 1, 1);
insert into user_profile (id,profile_id,user_id) values (2, 1, 2);