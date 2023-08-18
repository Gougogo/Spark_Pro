select uni_cust_id, address_cn, region_code, region_description
from (
        select *, row_number() over (partition by uni_cust_id order by if(lc = 0, 100, lc), if(lc2 = 0, 100, lc2)) rn
        from (
            select c.uni_cust_id
                 , c.address_cn
                 , r.region_code
                 , r.region_description
                 , locate(r.region_description, c.address) lc
                 , locate(r.region_desc, c.address) lc2
            from (
                select c.uni_cust_id,
                       c.cc_addr_map_chn['MAI'] as address_cn,
                       regexp_replace(c.cc_addr_map_chn['MAI'], '..路|..开发区|..广场|..大厦', '') as address
                from dwd.dwd_d_corp_cust c
                where dt = 'etldate' and c.addresscnisn not null and c.addresscnrlike '[u4e00-u9fa5].+'
            )c join (
                select regioncode, regiondescription, regexpreplace(regiondescription, '省∣特别新政区∣地区∣自治州∣自治区∣市∣县∣新区','')regiondesc
                from cnedmp.administrativeregioncoder
                where (regioncoderlike '[1-9]00' or substr(region_description, -1, 1) = '市')
                end dt = '202010'
            ) r
        ) region
        where lc >= 1 or lc2 >=1
)region_final
where rn = 1;



select limit_mb_cust_id
     ,sum(case when min_stage = 1 AND limit_stage = 1 THEN limit_approved_amt
               when min_stage != 1 THEN limit_approved_amt
    end) as limit_approved_amt
from(
        select limit_mb_cust_id, limit_stage, limit_approved_amt
             ,row_number() over(partition by limit_mb_cust_id, limit_top_outer
            order by if(limit_stage = 1,1,0) desc,cast(limit_approved_amt as DOUBLE) desc) rm
             ,min(limit_stage) over(partition by limit_mb_cust_id) min_stage
        from(
                select limit_mb_cust_id, limit_approved_amt, limit_stage, limit_top_outer
                from (
                         select 1 as limit_mb_cust_id, 12 as limit_top_outer, 1 as limit_stage, 800 as limit_approved_amt
                         union all
                         select 1 as limit_mb_cust_id, 12 as limit_top_outer, 1 as limit_stage, 700 as limit_approved_amt
                         union all
                         select 1 as limit_mb_cust_id, 13 as limit_top_outer, 1 as limit_stage, 800 as limit_approved_amt
                         union all
                         select 1 as limit_mb_cust_id, 13 as limit_top_outer, 4 as limit_stage, 900 as limit_approved_amt
                         union all
                         select 2 as limit_mb_cust_id, 54 as limit_top_outer, 2 as limit_stage, 1000 as limit_approved_amt
                         union all
                         select 2 as limit_mb_cust_id, 54 as limit_top_outer, 8 as limit_stage, 1500 as limit_approved_amt

                     )dwd_d_loan_facility
            )t1
    )dwd_d_loan_facility
where dwd_d_loan_facility.rm = 1
group by limit_mb_cust_id


bash -x /apps/CNEDMp/etl-jobs/scripts/generic_generator.sh /apps/CNEDMp/etl-jobs/sql/app/crrs_aml_classification/crrs_aml_classification_enhancement.sql /apps/CNEDMp/etl-jobs/report/crrs_aml_classification/cnEDMP_ClientIndustryC.txt -p today=20210331 -m csv -o header=true 'sep=|' --keeporder false


regexp_replace(customer_num, '(\\.00)', '')
