package com.neil.cashbook.dao.repository;
import com.neil.cashbook.dao.entity.DreamPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DreamPicRepository extends JpaRepository<DreamPic, Integer> {
}
