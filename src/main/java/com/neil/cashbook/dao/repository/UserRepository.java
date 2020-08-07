package com.neil.cashbook.dao.repository;
import com.neil.cashbook.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByOpenId(String openId);
}
