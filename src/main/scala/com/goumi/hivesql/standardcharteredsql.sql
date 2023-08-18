insert overwrite table app.rcpmis1_2134_swzjyemx_daily partition(dt='${etldate}', dept='RB DN')
select
    '1' as ListOperType
     ,t1.BUSINESSID as BUSINESSID
     ,'' as AUDITNAME
     ,'' as AUDITDATE
     ,t1.BUSINESSID as SUBID
     ,'' as IMPORTDATE
     ,t1.BicBkAcct as BicBkAcct
     ,0 as EndBalance
from(
        select
            midtable_2133_today.BankOrgCode
             ,midtable_2133_today.BankOrgName
             ,midtable_2133_today.BUSINESSID
             ,midtable_2133_today.BicBkAcct
             ,if(rcpmis1_2133_swzjzhxx_last.infodetcode = 'Y'
                     and midtable_2133_today.infodetcode = 'N', '撤销', '') as ISINSERTTO2134
        from (
                 select accountno as BUSINESSID, infodetcode, BANKORGCODE, BankOrgName, accountno as BicBkAcct
                 from midtable_2133 --2133今天的数据
             )midtable_2133_today

            full join(
	        select
	        BicBkAcct, infodetcode
	        ,row_number() over(partition by BICBKACCT order by dt desc) rm
	        from app.rcpmis1_2133_swzjzhxx  --2133之前的最新的一笔数据
	        where dt <= '${rcpmis1_2133_swzjzhxx_last}'
        )rcpmis1_2133_swzjzhxx_last
        on rcpmis1_2133_swzjzhxx_last.BicBkAcct = midtable_2133_today.BicBkAcct
        and rcpmis1_2133_swzjzhxx_last.rm = 1
)t1
where t1.ISINSERTTO2134 = '撤销'
;


with midtable_2133 as(
select
    map_branch.payment_number as BankOrgCode
     , map_branch.payment_bank as BankOrgName
     , case when ebbs_cust.infocode in ('01', '14') then 'E96'
            when ebbs_cust.infocode in ('02','04') then 'E97'
    end as AcctType
     , case when ebbs_cust.infodetcode = 'N' then 'DS05'
            when ebbs_cust.acctcurrentstatus in ('O', 'A') then 'DS01'
            when ebbs_cust.acctcurrentstatus in ('D', 'U') then 'DS02'
    end as AcctState
     , ebbs_cust.infodetcode as infodetcode
     , ebbs_cust.accountno
     , cast(ebbs_cust.acopendate as string) as acopendate
     , ebbs_cust.uniqueid1
     , ebbs_cust.fullname_chn
from(
        select acct.relationshipno,accountbranch, accountno, productcode,ledgerbalance, acopendate,acctcurrentstatus,fullname_chn,uniqueid1,infocode,infodetcode
        from(
            select relationshipno,accountbranch, accountno,productcode,ledgerbalance,acopendate,acctcurrentstatus,informationcode_map['GBS'] infodetcode
            from dw.dw_ebbs_acct
            where dt = 'etldate' and accsegmentcodein ('60', '61', '65', '66') and informationcodemap['GBS'] is not null
            and currencycode = 'CNY'
        ) acct
        inner join(
            select relationshipno, fullnamechn, uniqueid1, infomap['IDT'] infocode from dw.dwebbscust
            where dt = '{etldate}'
        )cust
        on acct.relationshipno = cust.relationshipno
)ebbs_cust
left join(
    select branch_number_code, payment_number,payment_bank
    from cnedmp.map_branch
    where TP_SYSTEM='EBBS'
)map_branch
on ebbs_cust.accountbranch = map_branch.branch_number_code
)

insert overwrite table app.rcpmis1_2133_swzjzyzh partition(dt='${etldate}', dept='RB DN')
select
    '' as ACCT2133
     ,'' as ORGINFO2133
     ,nvl(rcpmis1_2133_swzjzyzh_today.accountno, rcpmis1_2133_swzjzyzh_last.BUSINESSID) as BUSINESSID
     ,nvl(rcpmis1_2133_swzjzyzh_today.BankOrgCode, rcpmis1_2133_swzjzyzh_last.INSTCODE) as INSTCODE
     ,'' as DATASTATUS
     ,'' as BUSINESSTYPE
     ,'' as AUDITNAME
     ,'' as AUDITDATE
     ,'' as BUSINESSSEQ
     ,'' as IMPORTDATE
     ,'' as LEVYNO
     ,'' as MODIFYUSER
     ,'' as ISCOMMIT
     ,'' as DELETEUSER
from(
        select
            BankOrgCode
             ,accountno
        from midtable_2133
    )rcpmis1_2133_swzjzyzh_today
##保证跟主表的scope一致，调度：子表依赖主表
inner join(
    select BUSINESSID
    from app.rcpmis1_2133_swzjzhxx
    where dt = '${etldate}'
    and dept='RB DN'
)rcpmis1_2133_swzjzhxx
on rcpmis1_2133_swzjzhxx.BUSINESSID = rcpmis1_2133_swzjzyzh_today.accountno

--这是和历史数据比较，历史有，今天没有，那么这种数据也是要报为2撤销的。
full join(
select
ACCT2133
,ORGINFO2133
,BUSINESSID
,INSTCODE
,DATASTATUS
,BUSINESSTYPE
,AUDITNAME
,AUDITDATE
,BUSINESSSEQ
,IMPORTDATE
,LEVYNO
,MODIFYUSER
,ISCOMMIT
,DELETEUSER
,row_number() over(partition by BUSINESSID order by dt desc)rm
from app.rcpmis1_2133_swzjzyzh
where dt < '${etldate}'
)rcpmis1_2133_swzjzyzh_last
on rcpmis1_2133_swzjzyzh_last.BUSINESSID = rcpmis1_2133_swzjzyzh_today.accountno
and rcpmis1_2133_swzjzyzh_last.rm = 1


select
    xdjjh,
    dkfhzh,
    khtybh,
    khmc,
    xdhth,
    yxjgdm,
    jrxkzh,
    nbjgh,
    yxjgmc,
    xdywzl,
    hkfs,
    bz,
    zqs,
    qs,
    yhrq,
    yhje,
    yhbj,
    lxbz,
    yhlx,
    sybj,
    sfhkjhbg,
    bghkjhrq,
    tzfs,
    tzqzqs,
    cjrq,
    dt
from east4.east_415_1_dgdkhkjh t1
where dt between '20180131' and '{v_date}' and zqs <> qs and yhje <> yhbj ##当日和历史未结清
and t1.xdjjh not in (
    select distinct xdjjh as xdjjh_tmp
    ##排除当日有结清
    from east4.east_415_1_dgdkhkjh where dt = '{v_date}'
    and (zqs = qs or yhje = yhbj)
)

select
    trim(leid) as leid,
    nvl(min(from_unixtime(unix_timestamp(certgenerationdate,'MM/dd/yyyy hh:mm:ss'),'yyyy-MM-dd')),'') as certgenerationdate
    from cnedmp.cms_cn_cert where dt between '20170101' and '20210430' group by leid
union
select
    trim(coboleid) as leid,
    nvl(min(from_unixtime(unix_timestamp(certgenerationdate,'MM/dd/yyyy hh:mm:ss'),'yyyy-MM-dd')),'') as certgenerationdate
    from cnedmp.cms_cn_cert where dt between '20170101' and '20210430' group by coboleid


select
    branchcode, currencycode, dealamt, ledgerbalance
     ,case when datediff(t1.MaturityDate, t1.valuedt) > 365 then 'MoreOneYear'
           when datediff(t1.MaturityDate, t1.valuedt) <= 365 then 'LessOneYear'
    end type
from(
        select
            dw_ebbs_acct.branchcode, dw_ebbs_acct.MaturityDate, dw_ebbs_deal.valuedt,
            dw_ebbs_deal.dealamt, dw_ebbs_acct.ledgerbalance,dw_ebbs_acct.currencycode
        from(
            select fundsacctno, RELATIONSHIPNO, DealNo, valuedt, dealamt
            from dw.dw_ebbs_deal
            where dt='20201231'
        )dw_ebbs_deal
        inner join(
            select relationshipno, accountno, currencycode, MaturityDate, branchcode, ledgerbalance
            from dw.dw_ebbs_acct
            where dt = '20210901'
            and acc_segmentcode  in ('60','61','65','66')
            and custsegmtcode in('110','111','112','113','115','116','121','122')
            and accountno not like '0%'
        )dw_ebbs_acct
        on dw_ebbs_deal.relationshipno = dw_ebbs_acct.relationshipno
        and dw_ebbs_deal.fundsacctno = dw_ebbs_acct.accountno

        union all

        select branchcode, MaturityDate, null as valuedt, null as dealamt, ledgerbalance, currencycode
        from dw.dw_ebbs_acct --
        where dt=20210810
        and acc_segmentcode  in ('60','61','65','66')
        and custsegmtcode in('110','111','112','113','115','116','121','122')
        and accountno not like '0%'
)t1


select
    branchcode, currencycode, dealamt
     ,case when datediff(t1.MaturityDate, t1.valuedt) > 365 then 'MoreOneYear'
           when datediff(t1.MaturityDate, t1.valuedt) <= 365 then 'LessOneYear'
    end type
from(
    select branchcode, MaturityDate, valuedt, dealamt, currencycode
    from(
        select
            dw_ebbs_acct.branchcode, dw_ebbs_acct.MaturityDate,
            dw_ebbs_deal.valuedt, dw_ebbs_deal.dealamt, dw_ebbs_acct.currencycode,
            row_number() over(partition by dw_ebbs_acct.branchcode, dw_ebbs_deal.RELATIONSHIPNO, dw_ebbs_acct.CURRENCYCODE, dw_ebbs_deal.DealNo, dw_ebbs_acct.ledgerbalance order by 1)rm
        from(
                select fundsacctno, RELATIONSHIPNO, DealNo, valuedt, dealamt
                from dw.dw_ebbs_deal
                where dt = '${etldate}'
        )dw_ebbs_deal
        inner join(
            select relationshipno, accountno, currencycode, MaturityDate, branchcode, ledgerbalance
            from dw.dw_ebbs_acct
            where dt = '${etldate}'
            and acc_segmentcode  in ('60','61','65','66')
            and custsegmtcode in('110','111','112','113','115','116','121','122')
            and substr(accountno, 0, 1) <> '0'
        )dw_ebbs_acct
        on dw_ebbs_deal.relationshipno = dw_ebbs_acct.relationshipno
        and dw_ebbs_deal.fundsacctno = dw_ebbs_acct.accountno
    )t1
    where t1.rm = 1

    union all

    select branchcode, MaturityDate, null as valuedt, ledgerbalance as dealamt, currencycode
    from dw.dw_ebbs_acct --
    where dt = '${etldate}'
    and acc_segmentcode  in ('60','61','65','66')
    and custsegmtcode in('110','111','112','113','115','116','121','122')
    and substr(accountno, 0, 1) <> '0'
)t1





with midtable as (
    select *
    from(
        select t2.externalsystemkey, t2.accountno, t2.masterno, t2.creditdebit, externalsystemkey_pre
        from(
            #主表取数逻辑：首先数据应该是成pair，也就是externalsystemkey前面的几位是一样的，
            # 而且可能存在一对多。然后accountno不能相同，如果相同，需要剔除。
            #最后accountno必须存在于mianCNYacc或者CNYACcount或者MainFCYAcc或者FCYAccount
            select
            externalsystemkey, accountno, masterno, creditdebit, externalsystemkey_pre
            ,count(1) over(partition by externalsystemkey_pre, accountno order by 1) accountno_counts
            from(
                select externalsystemkey, split(externalsystemkey, '')[0] as externalsystemkey_pre, accountno, masterno, creditdebit
                ,count(1) over(partition by split(externalsystemkey, '')[0] order by 1) externalsystemkey_counts
                from(
                    SELECT 'CT10002110280460_OPL_CHG_D_004' AS externalsystemkey, '501511019693' AS accountno, '' AS masterno, 'D' AS creditdebit
                    UNION ALL
                    SELECT 'CT10002110280460_OPL_MTN_D_005' AS externalsystemkey, '501511019693' AS accountno, '' AS masterno, 'D' AS creditdebit
                    UNION ALL
                    SELECT 'IT10002110271191_OPL_MTN_C_001' AS externalsystemkey, '501511309022' AS accountno, '' AS masterno, 'C' AS creditdebit
                    UNION ALL
                    SELECT 'IT10002110271191_OPL_MTN_D_005' AS externalsystemkey, '5015100131' AS accountno, '' AS masterno, 'D' AS creditdebit
                    UNION ALL
                    SELECT 'IT10002110271191_OPL_MTN_D_005' AS externalsystemkey, '1234567891' AS accountno, '' AS masterno, 'D' AS creditdebit
                    UNION ALL
                    SELECT 'IT10012110271192_OPL_MTN_C_001' AS externalsystemkey, '501511032564' AS accountno, '' AS masterno, 'C' AS creditdebit
                    UNION ALL
                    SELECT 'IT10012110271192_OPL_MTN_D_005' AS externalsystemkey, '501511032564' AS accountno, '' AS masterno, 'D' AS creditdebit
                    UNION ALL
                    SELECT 'IT10012110281508_OPL_MTN_C_001' AS externalsystemkey, '501511019693' AS accountno, '' AS masterno, 'C' AS creditdebit
                    UNION ALL
                    SELECT 'IT10012110281508_OPL_MTN_D_005' AS externalsystemkey, '501511075073' AS accountno, '' AS masterno, 'D' AS creditdebit
                ) dw_ebbs_trnx
                order by externalsystemkey
            )t1
            where t1.externalsystemkey_counts > 1
        )t2
        where t2.accountno_counts <= 1
    )main
)
select
    '1' as OperType
     ,credit_t.accountno as 贷方
     ,debit_t.accountno as 借方
from(
    #贷方
    select *
    from midtable
    where creditdebit = 'C'
)credit_t
left join(
    #借方
    select *
    from midtable
    where creditdebit = 'D'
)debit_t
on credit_t.externalsystemkey_pre = debit_t.externalsystemkey_pre
;

with midtable as (
    select t1.*, ebbs_cust.productcode
    from(
        select trn_ref_no as transaction_ref, substring(REGULATORY_REF,3,5) as branch_code, ben_acc_number, originator_acc as PayerAcct
             ,ben_name as PayeeName, ben_bank_code, ben_bank_code as SWIFTBIC, from_unixtime(unix_timestamp(trim(value_date),'MM/dd/yyyy'),'yyyy-MM-dd') as PayerDate, cast(trn_amount as decimal(24,2)) as Amt
             ,payment_type
        from cnedmp.dotopal_cnaps_olt
        where dt = '${etldate}'
          and X_Border_CNY_Trx='1'
          and segment_code in ('60','61','65','66')
          and payment_category<>'Return'
          and payment_type in ('RMTX','WMCS','WMCN')

        union all

        select
            transaction_ref, branch_code, ben_acc_number, PayerAcct, PayeeName, ben_bank_code, SWIFTBIC, PayerDate,
            Amt, '' as payment_type
        from(
            select transaction_ref, substring(REGULATORY_REF,3,5) as branch_code
                 , originator_account_no as ben_acc_number, originator_account_no as PayerAcct
                 ,beneficiary_line_1 as PayeeName, ben_bank_code
                 ,if(nvl(ben_bank_code, '') = '', concat(a_c_with_inst_line1, a_c_with_inst_line2)
                 , ben_bank_code) as SWIFTBIC
                 ,from_unixtime(unix_timestamp(trim(value_date),'MM/dd/yyyy'),'yyyy-MM-dd') as PayerDate
                 , cast(remitting_amount as decimal(24,2)) as Amt, originator_account_no
            from cnedmp.dotopal_outward_payment_details
            where dt = '${etldate}'
            and currency_of_debit_account = 'CNY'
        )tmp
    )t1
    inner join(
        select accountno,productcode
        from dw.dw_ebbs_acct
        where dt = '${etldate}' and acc_segmentcode in ('60','61','65','66')
        and currencycode = 'CNY'
    )ebbs_cust
    on t1.PayerAcct=ebbs_cust.accountno
)
insert overwrite table app.rcpmis1_2111_kjzcfwmy partition(dt='${etldate}', dept='RB DN')
select
    '1' as ListOperType
     ,cast(row_number() over(partition by transaction_ref, branch_code, ben_acc_number, PayerAcct, PayeeName, ben_bank_code, SWIFTBIC, PayerDate, payment_type, productcode order by 1) as DECIMAL(20,0)) as SeqNo
     ,'223029' as BalOfPayCode
     ,cast(main.Amt as DECIMAL(24,2)) as Amt
     ,'' as CONTRACTNO
     ,'' as CertificateNo
     ,main.transaction_ref as BUSINESSID
     ,'' as AUDITNAME
     ,'' as AUDITDATE
     ,concat_ws('-', main.transaction_ref,cast(row_number() over(partition by transaction_ref, branch_code, ben_acc_number, PayerAcct, PayeeName, ben_bank_code, SWIFTBIC, PayerDate, payment_type, productcode order by 1) as string))as SUBID
     ,'' as IMPORTDATE
from(
        select transaction_ref, branch_code, ben_acc_number, PayerAcct
             ,PayeeName, ben_bank_code, SWIFTBIC, PayerDate
             ,Amt, productcode, payment_type
        from midtable
        where (payment_type <> 'WMCN' and payment_type <> 'WMCS')
        and productcode = '911'
)main

union all

select
    '3' as LISTOPERTYPE
     ,rcpmis1_2111_kjzcfwmy.seqno
     ,rcpmis1_2111_kjzcfwmy.BALOFPAYCODE
     ,rcpmis1_2111_kjzcfwmy.AMT
     ,rcpmis1_2111_kjzcfwmy.CONTRACTNO
     ,rcpmis1_2111_kjzcfwmy.CERTIFICATENO
     ,rcpmis1_2111_kjzcfwmy.BUSINESSID
     ,rcpmis1_2111_kjzcfwmy.AUDITNAME
     ,rcpmis1_2111_kjzcfwmy.AUDITDATE
     ,rcpmis1_2111_kjzcfwmy.SUBID
     ,rcpmis1_2111_kjzcfwmy.IMPORTDATE
from(
        select regexp_extract(regexp_replace(lower(narration1), ' ', ''), '(reverse|revers|reversal|return|repay)( )(ct.)', 3) as filter_narration1_res
        from dw.dw_ebbs_trnx
        where dt = '${etldate}'
          and segmentcode in ('60','61','65','66')
          and regexp_extract(regexp_replace(lower(narration1), ' ', ''), '(reverse|revers|reversal|return|repay)( )(ct.)', 3) <> ''
)ebbs_acmov

inner join(
    select *
    from(
            select *,
            row_number() over(partition by BANKTRANO order by dt desc) rm
            from app.rcpmis1_2111_kjzcxx #历史表支出信息表
            where dt < '${etldate}'
        )t1
    where t1.rm = 1
)rcpmis1_2111_kjzcxx
on lower(trim(rcpmis1_2111_kjzcxx.BANKTRANO)) = ebbs_acmov.filter_narration1_res
inner join(
    select *
    from app.rcpmis1_2111_kjzcfwmy
    #首先从trnx找出符合条件当天的所有交易的这些账户，然后关联主表的历史数据找到对应的账户，
    # 并从子表中查找对应account，同一天的字段信息。
    where dt < '${etldate}'
)rcpmis1_2111_kjzcfwmy
on rcpmis1_2111_kjzcfwmy.BUSINESSID = rcpmis1_2111_kjzcxx.BUSINESSID
and rcpmis1_2111_kjzcfwmy.dt = rcpmis1_2111_kjzcxx.dt