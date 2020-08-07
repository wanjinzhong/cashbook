package com.neil.cashbook.dao.repository;
import com.neil.cashbook.dao.entity.CashDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashDetailRepository extends JpaRepository<CashDetail, Integer> {
}
