package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Alarm;
import com.runninghi.runninghibackv2.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    void deleteAllByMember(Member member);

}
