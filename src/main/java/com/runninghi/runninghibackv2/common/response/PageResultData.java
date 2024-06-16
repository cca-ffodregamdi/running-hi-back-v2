package com.runninghi.runninghibackv2.common.response;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class PageResultData<T> {
    private List<T> content;
    private int pageNumber;
    private int totalPages;

    /**
     * PageResultData 생성자. 페이지화된 결과 데이터를 초기화합니다.
     *
     * @param content 페이지의 내용 리스트
     * @param pageable 페이지 정보 (페이지 번호와 크기 등을 포함)
     * @param total 전체 항목 수
     *
     * 추후 필요한 데이터를 더 추가할 수 있습니다.
     */
    public PageResultData(List<T> content, Pageable pageable, long total) {
        this.content = content;
        this.pageNumber = pageable.getPageNumber();
        this.totalPages = (int) Math.floor((double) total / pageable.getPageSize());
    }
}

