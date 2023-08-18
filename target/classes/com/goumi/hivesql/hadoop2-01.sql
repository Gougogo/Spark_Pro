show tables ;




select id from (
                   select 1 as id, 'a' as name
                   union
                   select 2 as id, 'b' as name
)t1;
