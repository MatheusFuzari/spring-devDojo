insert into "user" (id,email,first_name,last_name) values (1, 'yoichi.isagi@fromblue.com','Yoichi','Isagi');
insert into "user" (id,email,first_name,last_name) values (2, 'itochi.rin@fromblue.com','Itochi','Rin');
insert into "profile" (id,name,description) values (1, 'Admin', 'Admin profile');
insert into "user_profile" (id,profile_id,user_id) values (1, 1, 1);
insert into "user_profile" (id,profile_id,user_id) values (2, 1, 2);