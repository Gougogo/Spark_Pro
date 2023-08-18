show databases;

use sqltest;

CREATE TABLE sqltest.emp (
  empno INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  ename VARCHAR(30) NOT NULL,
  sal double
);

CREATE TABLE sqltest.taxgrade (
     taxmin double,
     taxmax double,
     grade INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY
);

select * from emp join taxgrade on emp.empno = taxgrade.grade;

INSERT INTO emp (ename, sal) VALUES ('王六', 44), ('田七', 33);

select * from taxgrade;

select * from emp;

INSERT INTO taxgrade (taxmin, taxmax) VALUES
(10, 20)
,(21, 30)
,(31, 40)
,(41, 50)
,(51, 60);

select emp.ename, taxgrade.grade from emp, taxgrade
where (emp.sal between taxgrade.taxmin and taxgrade.taxmax);

select emp.ename, taxgrade.grade from emp join taxgrade
on (emp.sal between taxgrade.taxmin and taxgrade.taxmax);

select id, name from (
    select a.id, name, sex from (
          select 1 as id, 'a' as name
          union
          select 2 as id, 'b' as name
    )a
    join (
        select 1 as id, 'boy' as sex
    )b
    on a.id = b.id
)t1;




CREATE TABLE sqltest.tab1 (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(30) NOT NULL,
p_id VARCHAR(30) NOT NULL
);

insert into tab1 (name, p_id) values ('一层', 'null')
,('二层A', '01')
,('二层B', '01')
,('三层A', '02')
,('三层B', '03');

select * from tab1;

select id, b.name, p_id from(
    select id, name from
    tab1) as a left join (select name, p_id from tab1 )as b  on  a.name=b.name;



CREATE TABLE sqltest.tab2 (
name VARCHAR(30) NOT NULL,
`A` double,
`B` double,
`C` double
);

insert into tab2 (name, A, B, C) values ('A', '10','20','30')
                                       ,('B', '3' ,'1','8')
                                       ,('C', '21','14','6')
                                       ,('D', '1' ,'37','55')
                                       ,('E', '5' ,'31','21')
                                       ,('F', '54','1','24');

select * from tab2;



select name, (case when A >= B then A
                   when A >= C then A
                   when B >= C then B else C end )
from tab2;


CREATE TABLE sqltest.tab3 (
                              mon VARCHAR(30) NOT NULL,
                              dep INT(6),
                              yj INT(6)
);

insert into tab3 (mon, dep, yj) values
('一月份', 1, 10),
('一月份', 2, 10),
('一月份', 3, 5),
('二月份', 2, 8),
('二月份', 4, 9),
('三月份', 3, 8);

select * from tab3;


'一月份', 2
'二月份', 2
'三月份', 2

select dep,
       sum(case mon when '一月份' then yj else 0 end) as '一月份',
       sum(case mon when '二月份' then yj else 0 end) as '二月份',
       sum(case mon when '三月份' then yj else 0 end) as '三月份'
from tab3
group by dep;

select dep,
       case mon when '一月份' then yj else 0 end as '一月份',
       case mon when '二月份' then yj else 0 end as '二月份',
       case mon when '三月份' then yj else 0 end as '三月份'
from tab3;


CREATE TABLE date_table (
                            date_key DATE PRIMARY KEY,
                            year INT,
                            quarter INT,
                            month INT,
                            day INT
);

INSERT INTO date_table (date_key, year, quarter, month, day)
SELECT
    date_key,
    YEAR(date_key) AS year,
    QUARTER(date_key) AS quarter,
    MONTH(date_key) AS month,
    DAY(date_key) AS day
FROM (
         SELECT
                 DATE_SUB(CURRENT_DATE(), INTERVAL 1 YEAR) + INTERVAL (a + (10 * b)) DAY AS date_key
         FROM
             (SELECT 0 AS a UNION SELECT 1 UNION SELECT 2 UNION SELECT 3) AS d,
             (SELECT 0 AS b UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
              UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) AS m
     ) AS dates
WHERE date_key < CURRENT_DATE();

select * from date_table;


create table t_user (
    uid int
);

create table t_order (
    oid int,
    uid int,
    otime date,
    oamount int
);

insert into t_user(uid) values
(1),
(2),
(3),
(4),
(5),
(6);

insert into t_order(oid, uid, otime, oamount) values
(11, 1, '2017-05-21', 30),
(12, 1, '2017-05-21', 30),
(13, 2, '2017-05-21', 30),
(14, 3, '2017-05-21', 30),
(15, 4, '2017-05-21', 30),
(16, 5, '2017-05-21', 30),
(17, 6, '2017-05-21', 30),
(18, 6, '2017-05-15', 30),
(19, 6, '2017-05-24', 30),
(20, 1, '2017-05-21', 15);

select * from t_order;

update t_order
    set otime = '2017-03-22'
where oid = 10;

TRUNCATE TABLE t_order;

create temporary table tmp_1 as
select * from t_order where uid in (
    select uid from(
       select uid, sum(t1) as t1_sum, sum(t2) as t2_sum, sum(t3) as t3_sum from(
              select *, (case when substr(otime, 1, 7)='2017-01'then 1 else 0 end) as t1,
                     (case when substr(otime, 1, 7)='2017-02'then 1 else 0 end) as t2,
                     (case when substr(otime, 1, 7)='2017-03' then 1 else 0 end) as t3
              from t_order
          )t_1
       group by uid
       having t1_sum >= 1 and t3_sum >= 1 and t2_sum != 1
  )t_2
)and substr(otime,1, 7) in( '2017-01'
        ,'2017-02'
        ,'2017-03')
order by uid, otime;



select * from tmp_1;

select uid, count(1) as sd
from tmp_1
group by uid
;

select uid, otime as minTime, oamount
from tmp_1 where otime = (
    select min(otime)
    from tmp_1
    where substr(otime, 1, 7) = '2017-03'
    group by uid
);

select *
from(
SELECT uid, otime AS minTime, oamount
FROM (
         select * from t_order where uid in (
             select uid from(
                                select uid, sum(t1) as t1_sum, sum(t2) as t2_sum, sum(t3) as t3_sum from(
                                                                                                            select *, (case when substr(otime, 1, 7)='2017-01'then 1 else 0 end) as t1,
                                                                                                                   (case when substr(otime, 1, 7)='2017-02'then 1 else 0 end) as t2,
                                                                                                                   (case when substr(otime, 1, 7)='2017-03' then 1 else 0 end) as t3
                                                                                                            from t_order
                                                                                                        )t_1
                                group by uid
                                having t1_sum >= 1 and t3_sum >= 1 and t2_sum != 1
                            )t_2
         )and substr(otime,1, 7) in( '2017-01'
             ,'2017-02'
             ,'2017-03')
         order by uid, otime
)ttt
WHERE (uid, otime) IN (
    SELECT uid, MAX(otime)
    FROM tmp_1
    WHERE otime LIKE '2017-03%'
    GROUP BY uid
)
)t1
join (
SELECT uid, otime AS minTime, oamount
FROM (
         select * from t_order where uid in (
             select uid from(
                                select uid, sum(t1) as t1_sum, sum(t2) as t2_sum, sum(t3) as t3_sum from(
                                                                                                            select *, (case when substr(otime, 1, 7)='2017-01'then 1 else 0 end) as t1,
                                                                                                                   (case when substr(otime, 1, 7)='2017-02'then 1 else 0 end) as t2,
                                                                                                                   (case when substr(otime, 1, 7)='2017-03' then 1 else 0 end) as t3
                                                                                                            from t_order
                                                                                                        )t_1
                                group by uid
                                having t1_sum >= 1 and t3_sum >= 1 and t2_sum != 1
                            )t_2
         )and substr(otime,1, 7) in( '2017-01'
             ,'2017-02'
             ,'2017-03')
         order by uid, otime
     )ttt
WHERE (uid, otime) IN (
    SELECT uid, MIN(otime)
    FROM tmp_1
    WHERE otime LIKE '2017-03%'
    GROUP BY uid
))t2
on t1.uid = t2.uid
;





select substr('2017-05-12', 1, 7)

select * from t_order;


SELECT
    t_user.uid,
    COUNT(CASE WHEN t_order.otime LIKE '2017-03%' THEN t_order.oid END) AS order_count,
    MIN(CASE WHEN t_order.otime LIKE '2017-03%' THEN t_order.oamount END) AS first_order_amount,
    MAX(CASE WHEN t_order.otime LIKE '2017-03%' THEN t_order.oamount END) AS last_order_amount
FROM t_user
JOIN t_order
ON t_user.uid = t_order.uid
WHERE t_order.otime LIKE '2017-01%'
AND t_user.uid NOT IN (
    SELECT uid FROM t_order WHERE otime LIKE '2017-02%'
)
GROUP BY t_user.uid
HAVING COUNT(CASE WHEN t_order.otime LIKE '2017-03%' THEN t_order.oid END) > 10;


SELECT
    *
FROM t_user
JOIN t_order
ON t_user.uid = t_order.uid
WHERE t_order.otime LIKE '2017-01%';


SELECT
    t_user.uid,
    COUNT(CASE WHEN MONTH(t_order.otime) = 3 THEN 1 ELSE NULL END) AS count_3_month_orders,
    sum(case when t_order.otime = (SELECT MIN(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid) then oamount else 0 end ) as first_amount,
    sum(case when t_order.otime = (SELECT MAX(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid) then oamount else 0 end ) as last_amount
FROM
    t_user
        JOIN t_order ON t_user.uid = t_order.uid
WHERE
    MONTH(t_order.otime) IN (1, 2, 3)
    AND YEAR(t_order.otime) = 2017
GROUP BY
    t_user.uid
HAVING COUNT(CASE WHEN MONTH(t_order.otime) = 2 THEN 1 ELSE NULL END) = 0;




select count(1) from t_order
where MONTH(t_order.otime) IN (1, 3)
GROUP BY
t_order.uid
having COUNT(CASE WHEN MONTH(t_order.otime) = 2 THEN 1 ELSE NULL END) = 0;




select
uid,
sum(case when otime = (select min(otime) from t_order where uid = 1) then oamount else 0 end) as sum_min_amount,
sum(case when otime = (select max(otime) from t_order where uid = 1) then oamount else 0 end) as sum_max_amount
from t_order
where uid = 1
group by uid;


SELECT
    t_user.uid,
    COUNT(CASE WHEN MONTH(t_order.otime) = 3 THEN 1 ELSE NULL END) AS count_3_month_orders,
    sum(case when t_order.otime = (SELECT MIN(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid) then oamount else 0 end ) as first_amount,
    sum(case when t_order.otime = (SELECT MAX(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid) then oamount else 0 end ) as last_amount
FROM t_user
JOIN t_order
ON t_user.uid = t_order.uid
WHERE MONTH(t_order.otime) IN (1, 3)
  and t_order.uid in (
    select uid from(
        select uid, sum(t1) as t1_sum, sum(t2) as t2_sum, sum(t3) as t3_sum from(
                 select *, (case when substr(otime, 1, 7)='2017-01'then 1 else 0 end) as t1,
                        (case when substr(otime, 1, 7)='2017-02'then 1 else 0 end) as t2,
                        (case when substr(otime, 1, 7)='2017-03' then 1 else 0 end) as t3
                 from t_order
             )t_1
        group by uid
        having t1_sum >= 1 and t3_sum >= 1 and t2_sum != 1
    )t_2
)
  AND YEAR(t_order.otime) = 2017
GROUP BY t_user.uid;


select * from t_order;

SELECT oid FROM t_order WHERE uid > (select AVG(oamount) from t_order WHERE oamount > AVG(oamount));


CREATE TABLE student_table (
                               id INT ,
                               name VARCHAR(50) ,
                               birth DATE ,
                               sex CHAR(1)
);

drop table student_table;

INSERT INTO student_table (id, name, birth, sex) VALUES
('1004', '张三', '2000-08-06', '男'),
('1005', NULL, '2001-12-01', '女'),
('1006', '张三', '2000-08-06', '女'),
('1007', '王五', '2001-12-01', '男'),
('1008', '李四', NULL, '女'),
('1009', '李四', NULL, '男'),
('1010', '李四', '2001-12-01', '女');

select * from student_table;


SELECT shirt_id，shirt_name，shirt_price FROM student_table WHERE shirt_price > AVG(id)


select *
from (
         select * from student_table where sex = '男' ) t1
         left  join
     (select * from student_table where sex = '女')t2
     on t1.birth = t2.birth and t1.name = t2.name ;


CREATE TABLE tt_tmp (
                        gt DATE,
                        result VARCHAR(1)
);

INSERT INTO tt_tmp VALUES ('2018-05-19', 'S')

select gt, sum(case when result='S' then 1 else 0 end)'success',
       sum(case when result='F' then 1 else 0 end)'failure'
from tt_tmp
group by gt;


select s.gt,s.success,f.failure from(
    select gt,count(*) as 'success' from tt_tmp where result='S'group by gt
) s
inner join
(
    select gt,count(*) as 'failure' from tt_tmp where result='F'group by gt
) f
on s.gt=f.gt;


select gt, sum(case when result='F' then 1 else 0 end) as 'success',
       sum(case when result='S' then 1 else 0 end) as 'failure'
from tt_tmp
group by gt;


select s.gt,s.success,f.failure from
(select gt,count(*) as 'success' from tt_tmp where result='S'group by gt) s,
(select gt,count(*) as 'failure' from tt_tmp where result='F'group by gt) f
where s.gt=f.gt



SELECT i.itemname,s.name FROM grade g,
(
    SELECT itemid iid,MAX(mark) max FROM grade WHERE itemid IN
    (SELECT itemid FROM item WHERE location='体育馆') GROUP BY itemid
) temp,item i,sporter s
WHERE g.itemid=temp.iid AND g.mark=temp.max
AND temp.iid=i.itemid AND s.sporterid=g.sporterid;



CREATE TABLE drill (
                       id INT NOT NULL PRIMARY KEY,
                       date DATE,
                       kilometer INT
);


INSERT INTO drill (id, date, kilometer) VALUES
(1, '2020-07-01', 10),
(2, '2020-07-02', 12),
(3, '2020-07-03', 11),
(4, '2020-07-04', 15);



SELECT d2.id,d1.kilometer,d2.kilometer
FROM drill d1,drill d2
WHERE DATEDIFF(d2.date,d1.date)=1
AND d1.kilometer<d2.kilometer


SELECT d2.id,d2.kilometer
FROM drill d1 CROSS JOIN drill d2
ON DATEDIFF(d2.date,d1.date)=1
WHERE d1.kilometer<d2.kilometer


SELECT d2.id,d2.kilometer
FROM drill d1 JOIN drill d2
ON( DATE_ADD(d2.date,INTERVAL 1 day)=1)
WHERE d1.kilometer<d2.kilometer



SELECT AVG(salary) AS avg_salary
FROM salaries
WHERE salary NOT IN
(SELECT MAX(salary) FROM salaries WHERE to_date = '9999-01-01')
AND salary NOT IN
(SELECT MIN(salary) FROM salaries WHERE to_date = '9999-01-01')
AND to_date = '9999-01-01';



select t3.*,t2.subject_name,t2.score
from (
    select stu_id,max(score) as c1 from score_table2 group by stu_id order by c1 desc limit 1
) t1
inner join
(
    select * from score_table2
)t2
    on t1.stu_id = t2.stu_id and t1.c1 = t2.score
inner join student_table2 t3
on t1.stu_id = t3.id;


select t3.*,t2.subject_name,t2.score
from (
         select stu_id,max(score) as c1 from score_table2 group by stu_id
     ) t1
         inner join
     (
         select * from score_table2
     )t2 on t1.stu_id = t2.stu_id
         inner join student_table2 t3
                    on t1.stu_id = t3.id;

select t1.stu_id, t2.subject_name, t2.score, t3.sex, t3.name, t3.birth from (
                  select stu_id, max(score) as c1
                  from score_table2
                  group by stu_id
              )t1
inner join
    (
        select * from score_table2
        )t2
on t1.stu_id = t2.stu_id
and t1.c1 = t2.score
inner join(
    select * from student_table2
    )t3
on t1.stu_id = t3.id;


select t3.*,t2.subject_name,t2.score
from (
         select stu_id,max(score) as c1 from score_table2 group by stu_id
     ) t1
         inner join
     (
         select * from score_table2
     )t2 on t1.stu_id = t2.stu_id and t1.c1 = t2.score
         inner join student_table2 t3
                    on t1.stu_id = t3.id;



CREATE TABLE student_table2 (
                               id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(50) NOT NULL,
                               birth DATE NOT NULL,
                               sex ENUM('男', '女') NOT NULL
);


CREATE TABLE score_table2 (
                             stu_id INT NOT NULL,
                             subject_name VARCHAR(50) NOT NULL,
                             score INT NOT NULL,
                             PRIMARY KEY (stu_id, subject_name),
                             FOREIGN KEY (stu_id) REFERENCES student_table2(id)
);


INSERT INTO student_table2 (name, birth, sex) VALUES
('张三', '2000-01-01', '男'),
('李四', '1999-02-02', '女'),
('王五', '2001-03-03', '男');


INSERT INTO score_table2 (stu_id, subject_name, score) VALUES
(1, '数学', 80),
(1, '英语', 90),
(2, '数学', 85),
(2, '英语', 95),
(3, '数学', 90);


show tables ;

Create table If Not Exists Trips (id int, client_id int, driver_id int, city_id int, status ENUM('completed', 'cancelled_by_driver', 'cancelled_by_client'), request_at varchar(50))


Create table If Not Exists Users (users_id int, banned varchar(50), role ENUM('client', 'driver', 'partner'))

Truncate table Trips;

insert into Trips (id, client_id, driver_id, city_id, status, request_at) values ('1', '1', '10', '1', 'completed', '2013-10-01')
,('2', '2', '11', '1', 'cancelled_by_driver', '2013-10-01')
,('3', '3', '12', '6', 'completed', '2013-10-01')
,('4', '4', '13', '6', 'cancelled_by_client', '2013-10-01')
,('5', '1', '10', '1', 'completed', '2013-10-02')
,('6', '2', '11', '6', 'completed', '2013-10-02')
,('7', '3', '12', '6', 'completed', '2013-10-02')
,('8', '2', '12', '12', 'completed', '2013-10-03')
,('9', '3', '10', '12', 'completed', '2013-10-03')
,('10', '4', '13', '12', 'cancelled_by_driver', '2013-10-03')



Truncate table Users

insert into Users (users_id, banned, role) values ('1', 'No', 'client')
,('2', 'Yes', 'client')
,('3', 'No', 'client')
,('4', 'No', 'client')
,('10', 'No', 'driver')
,('11', 'No', 'driver')
,('12', 'No', 'driver')
,('13', 'No', 'driver');






