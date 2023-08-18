cust_id,doc_nbr,eac_nbr,time_stamp
1234,110103198810102315,6225882012345678,2022-10-11 12:33:24
1234,110103198810102315,null,2022-11-20 09:23:50
1234,null,6225882012345678,2022-09-13 15:26:38

1235,220103198810102315,null,2022-11-20 09:23:50
1235,null,6334882012345678,2022-09-13 15:26:38
1235,220103198810102315,6334882012345678,2022-09-20 08:23:45
对上述数据结构的缺失值进行补全处理,补全结果如下:
cust_id,doc_nbr,eac_nbr,time_stamp
1234,110103198810102315,6225882012345678,2022-10-11 12:33:24
1234,110103198810102315,6225882012345678,2022-11-20 09:23:50
1234,110103198810102315,6225882012345678,2022-09-13 15:26:38
1235,220103198810102315,6334882012345678,2022-11-20 09:23:50
1235,220103198810102315,6334882012345678,2022-09-13 15:26:38
1235,220103198810102315,6334882012345678,2022-09-20 08:23:45


CREATE TABLE table_name (
    cust_id INT,
    doc_nbr VARCHAR(50),
    eac_nbr VARCHAR(50),
    time_stamp TIMESTAMP
);

DELETE FROM table_name;


INSERT INTO table_name (cust_id, doc_nbr, eac_nbr, time_stamp)
VALUES
(1234, '110103198810102315', '6225882012345678', '2022-10-11 12:33:24'),
(1234, '110103198810102315', null, '2022-11-20 09:23:50'),
(1234, null, '6225882012345678', '2022-09-13 15:26:38'),
(1235, '220103198810102315', null, '2022-11-20 09:23:50'),
(1235, null, '6334882012345678', '2022-09-13 15:26:38'),
(1235, '220103198810102315', '6334882012345678', '2022-09-20 08:23:45');



CREATE TABLE table_name_1
SELECT *
FROM table_name;



-- 填充缺失的doc_nbr
CREATE TEMPORARY TABLE temp_table
SELECT * FROM table_name;

UPDATE table_name t1
SET t1.doc_nbr = (
    SELECT t2.doc_nbr
    FROM temp_table t2
    WHERE t1.cust_id = t2.cust_id
      AND t1.time_stamp = t2.time_stamp
      AND t2.doc_nbr IS NOT NULL
)
WHERE t1.doc_nbr IS NULL;

DROP TEMPORARY TABLE temp_table;


UPDATE table_name t1
SET t1.doc_nbr = (
    SELECT min(t2.doc_nbr)
    FROM temp_table t2
    WHERE t1.cust_id = t2.cust_id
        and t2.doc_nbr IS NOT NULL
)
WHERE t1.doc_nbr IS NULL;

select * from table_name;

-- 填充缺失的eac_nbr
UPDATE table_name t1
SET t1.eac_nbr = (
    SELECT min(t2.eac_nbr)
    FROM temp_table t2
    WHERE t1.cust_id = t2.cust_id
      AND t2.eac_nbr IS NOT NULL
)
WHERE t1.eac_nbr IS NULL;


select * from temp_table




