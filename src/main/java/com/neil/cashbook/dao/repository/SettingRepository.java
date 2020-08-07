package com.neil.cashbook.dao.repository;
import com.neil.cashbook.dao.entity.Setting;
import com.neil.cashbook.enums.SettingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Integer> {

    Setting findByKey(SettingKey key);
}
