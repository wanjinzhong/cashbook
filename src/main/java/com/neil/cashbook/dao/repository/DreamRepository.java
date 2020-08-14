package com.neil.cashbook.dao.repository;
import java.time.LocalDate;
import java.util.List;

import com.neil.cashbook.dao.entity.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Integer> {

    @Query("from Dream order by coalesce(comeTrueDate, deadline) desc")
    List<Dream> findAll();

    List<Dream> findByComeTrueDate(LocalDate comeTrue);

    @Query("from Dream where comeTrueDate is null order by deadline desc")
    List<Dream> findByComeTrueDateIsNull();

    @Query("from Dream where comeTrueDate is not null order by comeTrueDate desc")
    List<Dream> findByComeTrueDateIsNotNull();
}
