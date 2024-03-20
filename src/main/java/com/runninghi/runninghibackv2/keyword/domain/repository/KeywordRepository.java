package com.runninghi.runninghibackv2.keyword.domain.repository;

import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByKeywordName(String keywordName);

}
