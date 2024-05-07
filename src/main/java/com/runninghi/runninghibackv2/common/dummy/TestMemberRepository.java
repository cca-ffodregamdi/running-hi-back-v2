package com.runninghi.runninghibackv2.common.dummy;

import com.runninghi.runninghibackv2.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestMemberRepository extends JpaRepository<Member, Long> {

    Member findByNickname(String nickname);

}
