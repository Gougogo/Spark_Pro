


select * from practice_sql.emp;

select * from practice_sql.taxgrade;

select * from practice_sql.emp as t1 join practice_sql.taxgrade as t2
    on t1.sal>t2.taxmin and t1.sal< t2.taxmax;

select id from (
     select 1 as id, 'a' as name
     union
     select 2 as id, 'b' as name
)t1;


'A',1,2,2300
'B',2,1,2890
'C',1,1,2980
'D',3,1,5000
'E',1,2,3409
'F',2,2,2344
'G',3,1,2334