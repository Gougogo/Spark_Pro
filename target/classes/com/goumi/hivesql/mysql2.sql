select * from tab2;


select name, case when A >= B then A
                  when B >= C then B else C end as M
from tab2;


SELECT t_user.uid,
       COUNT(CASE WHEN MONTH(t_order.otime) = 3 THEN 1 ELSE NULL END) AS count_3_month_orders,
       sum(case
               when t_order.otime =
                    (SELECT MIN(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid)
                   then oamount
               else 0 end)                                            as first_amount,
       sum(case
               when t_order.otime =
                    (SELECT MAX(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid)
                   then oamount
               else 0 end)                                            as last_amount
FROM t_user
         JOIN t_order ON t_user.uid = t_order.uid
WHERE MONTH(t_order.otime) IN (1, 2, 3)
  AND YEAR(t_order.otime) = 2017
GROUP BY t_user.uid
HAVING COUNT(CASE WHEN MONTH(t_order.otime) = 1 and MONTH(t_order.otime) != 2 and MONTH(t_order.otime) = 3
THEN 1 ELSE NULL END) = 0;



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






SELECT
    t_user.uid,
    COUNT(CASE WHEN MONTH(t_order.otime) = 3 THEN 1 ELSE NULL END) AS count_3_month_orders,
    sum(case when t_order.otime = (SELECT MIN(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid) then oamount else 0 end ) as first_amount,
    sum(case when t_order.otime = (SELECT MAX(t_order.otime) FROM t_order WHERE MONTH(t_order.otime) = 3 and uid = t_user.uid) then oamount else 0 end ) as last_amount
FROM
    t_user
        JOIN t_order
             ON t_user.uid = t_order.uid
WHERE
        MONTH(t_order.otime) IN (1, 2, 3)
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
GROUP BY
    t_user.uid;


SELECT
    t1.uid,
    SUM(CASE WHEN t2.oamount > 10 THEN 1 ELSE 0 END) AS order_count_gt_10,
    MIN(t2.oamount) AS first_order_amount,
    MAX(t2.oamount) AS last_order_amount
FROM
    t_user t1
        LEFT JOIN
    t_order t2 ON t1.uid = t2.uid AND MONTH(t2.otime) = 3
WHERE
        t1.uid IN (
        SELECT t3.uid
        FROM t_order t3
        WHERE
                MONTH(t3.otime) = 1
          AND t3.uid NOT IN (
            SELECT t4.uid
            FROM t_order t4
            WHERE MONTH(t4.otime) = 2
        )
    )
GROUP BY
    t1.uid;


select dep,
       sum(case when mon = '一月份' then yj end) as '一月份',
       sum(case when mon = '二月份' then yj end) as '二月份',
       sum(case when mon = '三月份' then yj end) as '三月份'
from tab3
group by dep;


select uid, sum(case when substr(otime, 1, 7)='2017-01'
    and substr(otime, 1, 7)='2017-03'
    and substr(otime, 1, 7)!='2017-02' then 1 else 0 end)
from t_order
group by uid;

select * from t_order;


select emp.ename, taxgrade.grade from emp join taxgrade
on (emp.sal between taxgrade.taxmin and taxgrade.taxmax);



select * from (
                  select 1 as uid, 'a' as name
              )t1
                  join (
    select 'boy' as sex
)t2
