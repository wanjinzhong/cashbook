package com.neil.cashbook.dao.repository;
import java.time.LocalDate;
import java.util.List;

import com.neil.cashbook.dao.entity.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Integer> {

    List<Dream> findByCometrue(LocalDate comeTrue);

    List<Dream> findByCometrueIsNull();

    List<Dream> findByCometrueIsNotNull();
}
