package com.runninghi.runninghibackv2.keyword.domain.repository;

import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordDomainRepository extends JpaRepository<Keyword, Long> {
}
