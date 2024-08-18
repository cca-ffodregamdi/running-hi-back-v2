package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    void deleteAllByMember_MemberNo(Long memberNo);

    List<Alarm> findAllByMember_MemberNo(Long memberNo);

    @Modifying
    @Query("DELETE FROM Alarm a WHERE a.isRead = true AND a.readDate < :date")
    void deleteReadAlarmsOlderThan(LocalDateTime date);

}
