create table user(id bigint auto_increment, account varchar(255),email varchar(255),firstName varchar(255),language varchar(255),lastName varchar(255) ,openId varchar(255), uuid varchar(255));

insert into user(account,email,firstName,language,lastName,openId, uuid) values('account1','coucou@coucou.com','test','fr','junit','openid','uuid');
insert into user(account,email,firstName,language,lastName,openId, uuid) values('account1','coucou2@coucou.com','test2','fr','junit2','openid2','uuid2');
insert into user(account,email,firstName,language,lastName,openId, uuid) values('account2','coucou@coucou.com','test','fr','junit','openid','uuid');
insert into user(account,email,firstName,language,lastName,openId, uuid) values('account3','coucou@coucou.com','test','fr','junit','openid','uuid');