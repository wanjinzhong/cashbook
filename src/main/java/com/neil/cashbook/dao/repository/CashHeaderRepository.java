package com.neil.cashbook.dao.repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.neil.cashbook.dao.entity.CashHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CashHeaderRepository extends JpaRepository<CashHeader, Integer> {

    CashHeader findByCashDate(Date date);

    @Query("select header from CashHeader header where header.cashDate between :fromDate and :toDate order by header.cashDate desc")
    List<CashHeader> findByDateRange(@Param("fromDate") Date from, @Param("toDate") Date to);

    @Query("select sum(header.quota) - sum(header.cost) from CashHeader header where header.cashDate <= :date")
    BigDecimal findRemain(Date date);
}
