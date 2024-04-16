package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByKeywordName(String keywordName);

}
