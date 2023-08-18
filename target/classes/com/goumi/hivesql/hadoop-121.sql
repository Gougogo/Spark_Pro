show tables ;


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

select * from test;

set hive.exec.dynamic.partition.mode=nonstrict;

set hive.strict.checks.cartesian.product = true;

set hive.mapred.mode = nonstrict;

select * from (
    select 1 as uid, 'a' as name
)t1
cross join (
    select 'boy' as sex
)t2;

show databases ;

use default;

CREATE TABLE emp(
    ename STRING,
    empno INT,
    sal FLOAT
)
COMMENT 'Employee details'
ROW FORMAT DELIMITED
    FIELDS TERMINATED BY ','
STORED AS TEXTFILE;


Create table If Not Exists Salary (id int, employee_id int, amount int, pay_date date);
Create table If Not Exists Employee (employee_id int, department_id int);

TRUNCATE TABLE Salary;

insert into Salary (id, employee_id, amount, pay_date) values
 ('1', '1', '9000', '2017-03-31')
,('2', '2', '6000', '2017-03-31')
,('3', '3', '10000', '2017-03-31')
,('4', '1', '7000', '2017-02-28')
,('5', '2', '6000', '2017-02-28')
,('6', '3', '8000', '2017-02-28');

select *
from Salary;



insert into Employee (employee_id, department_id) values
 ('1', '1')
,('2', '2')
,('3', '2');

select *
from Employee;

/*
 首先应该公司每个发放日的平均工资A
 然后求每个发放日，部门的平均工资B
 然后将两者进行比较，B>A的为higher，B<A的为lower，B=A的为same

 每个发放日的平均工资为amount的和/id的count，groupby发放日

 发放日部门平均工资B为groupby发放日，部门，sum(amount)/count(id)
 */

select t1.pay_date as pay_month, t2.department_id as department_id,
       case when t2.t2_sal > t1.t1_sal then 'higher'
            when t2.t2_sal < t1.t1_sal then 'lower'
            when t2.t2_sal = t1.t1_sal then 'same'
            end as comparison
from(
    select date_format(pay_date, 'yyyy-MM') as pay_date, sum(amount)/count(id) as t1_sal from Salary
    group by date_format(pay_date, 'yyyy-MM')
)t1
inner join
(
    select E.department_id, date_format(pay_date, 'yyyy-MM') as pay_date, avg(amount) as t2_sal
    from Salary S left join Employee E
    on S.employee_id = E.employee_id
    group by date_format(pay_date, 'yyyy-MM'), E.department_id
)t2
on t1.pay_date = t2.pay_date;


select
    substr(t3.pay_date, 1, 7) as pay_month, t3.department_id
     ,case when t3.avgDep < t4.avgAll then 'lower'
           when t3.avgDep = t4.avgAll then 'same'
           when t3.avgDep > t4.avgAll then 'higher'
    end as comparison
from(
        select t2.department_id, t1.pay_date, avg(t1.amount) avgDep
        from(
                select employee_id, amount, date_format(pay_date, '%Y-%m') as pay_date
                from salary
            )t1

                left join(
            select employee_id, department_id
            from employee
        )t2
        on t1.employee_id  = t2.employee_id
        group by t1.pay_date, t2.department_id
)t3

left join(
    select date_format(pay_date, '%Y-%m') as pay_date, avg(amount) avgAll
    from salary
    group by date_format(pay_date, '%Y-%m')
)t4
on t3.pay_date = t4.pay_date
;

select date_format('2017/12/01')


dfs -ls /;

set hive.execution.engine;

set hive.execution.engine=mr;


Create table If Not Exists Student (name varchar(50), continent varchar(7));


insert into Student (name, continent) values
 ('Jane', 'America')
,('Pascal', 'Europe')
,('Xi', 'Asia')
,('Jack', 'America');


select max(case when continent = 'America' then name end) as America,
       max(case when continent = 'Asia' then name end) as Asia,
       max(case when continent = 'Europe' then name end) as Europe,
       rn
from (
    select *,
           row_number() over (partition by continent order by name) rn
    from student
)t1
group by rn;


