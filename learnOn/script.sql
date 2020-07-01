drop table if exists courses;
create table courses
( id int NOT NULL AUTO_INCREMENT,
title varchar (40),
duration_hours int,
price double,
instructor varchar (40),
subject varchar (40),
description varchar(255),
type varchar (40),
level varchar (40),
image varchar (40),
primary key (id)
);

drop table if exists users;
create table users
( username varchar (40) NOT NULL,
password varchar(40),
name varchar (40),
image varchar (40),
primary key (username)
);

INSERT INTO `courses` VALUES (1,'Get your mat and start with me',10,'yoga2.jpg','Addriane Pari','Beginner',10.5,'Yoga','Yoga with Addriane','Exercise'),(2,'Ideal for everyone that looks to get more flexibility and elasticity',16,'yoga1.jpg','Johane Smith','All',15,'Yoga','Increase your elasticity','Exercise'),(3,'Some exercises need you have at least 4 years of yoga practice.',20,'yoga3.jpg','Mark Neos','Average',45,'Yoga','Yoga with Mark','Exercise'),(4,'Start yoga and fall in love with it.',6,'yoga4.jpg','Elizabeth Jampson','Begginer',0,'Yoga','Yoga for Begginers','Exercise'),(5,'Learn all the basics',30,'taichi1.jpg','Philipa Alms','Begginer',80,'Taichi','Taichi for Begginers','Exercise'),(6,'Everything that you need to start teaching taichi',150,'taichi2.jpg','Anex Naumras','Expert',200,'Taichi','Become an Taichi teacher','Exercise'),(7,'All the details and curiosities about St Patricks are in this course',2,'stPatrick1.jpg','John Molls','All',0,'Ireland','The history of St Patricks','History'),(8,'About portuguese discoveries by Jose Hermano Saraiva. It have English subtitles',4,'pt1.jpg','Jose Hermano Saraiva','All',0,'Portugal','Portuguese Discoveries','History'),(9,'Learn how to identify if a food is healthy or not to you',40,'nutrition1.jpg','Andrew Ritrs','All',10,'Nutrition','Health or Unhealthy?','Health'),(10,'The body is full of mechanisms and learn about them is just good to you',20,'imune1.jpg','Philipa Romasle','All',0,'Body','How the system immunity works?','Health'),(11,'Is just 1hour, why not?',1,'sql1.jpg','Costes Goiues','Begginer',0,'SQL','SQL','Programming'),(12,'This course is good to everyone, either with javascript knowledge or not',20,'jquery1.jpg','James Haklen','Begginer',12,'iQuery','Write less and do More with jQuery','Programming'),(13,'Ideal to is new in programming',20,'java1.jpg','John Canes','Begginer',0,'java','Java for Begginers I','Programming'),(14,'If you do not have any base in programming please start with my previous course Java for Begginers I',20,'java3.jpg','John Canes','Begginer',0,'java','Java for Begginers II','Programming'),(15,'Learn how to take advantage of all java collections',10,'java2.jpg','Theresa Smigkls','Average',4,'java','Java - Collections','Programming'),(16,'This is a very complete course that will improve your code',30,'java4.jpg','Petter Bitter','Average',15,'java','Java - all the features','Programming');
INSERT INTO `users` VALUES ('AnneMo','user1.png','Anne Moss','Anne1234'),('ClareHoSm','user1.jpg','Clare Homnes Smith','Clar1234'),('ClarePaMo','user7.png','Clare Padly Mosk','Clare1234'),('JannetKl','user5.png','Jannet Klars','Jannet1234'),('JohnSm','user3.png','John Smith','John1234'),('KeanuJa','user4.png','Keanu Jackson','Keanu1234'),('RoseMa','user6.png','Rose Mabj','Rose1234'),('ThomasMo','user2.png','Thomas Morly','Thomas1234');


