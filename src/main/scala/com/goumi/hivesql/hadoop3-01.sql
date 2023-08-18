


select * from practice_sql.emp;

select * from practice_sql.taxgrade;

select * from practice_sql.emp as t1 join practice_sql.taxgrade as t2
    on t1.sal>t2.taxmin and t1.sal< t2.taxmax;

select id from (
     select 1 as id, 'a' as name
     union
     select 2 as id, 'b' as name
)t1;
