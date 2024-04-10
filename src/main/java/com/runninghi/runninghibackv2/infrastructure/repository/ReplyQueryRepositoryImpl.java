package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.application.dto.reply.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.domain.repository.ReplyQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.runninghi.runninghibackv2.reply.domain.aggregate.entity.QReply.reply;

@Repository
@RequiredArgsConstructor
public class ReplyQueryRepositoryImpl implements ReplyQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private static final int REPORTED_COUNT = 1;

    @Override
    public Page<GetReplyListResponse> findAllReportedByPageableAndSearch(GetReportedReplyRequest request) {

        Long count = getCount(request);
        List<GetReplyListResponse> content = getReportedReplyList(request);

        return new PageImpl<>(content, request.pageable(), count);
    }

    private Long getCount(GetReportedReplyRequest request) {

        return jpaQueryFactory
                .select(reply.count())
                .from(reply)
                .where(likeNickname(request.search()),
                        eqReportStatus(request.reportStatus()),
                        reply.reportedCount.goe(REPORTED_COUNT))
                .fetchOne();

    }

    private List<GetReplyListResponse> getReportedReplyList (GetReportedReplyRequest request) {

        // request.reportStatus로 정렬
        return jpaQueryFactory
                .select(Projections.fields(GetReplyListResponse.class,
                        reply.replyNo,
                        reply.writer.nickname.as("memberName"),
                        reply.reportedCount,
                        reply.isDeleted,
                        reply.createDate,
                        reply.updateDate
                        ))
                .from(reply)
                .where(likeNickname(request.search()),
                        eqReportStatus(request.reportStatus()),
                        reply.reportedCount.goe(REPORTED_COUNT) )
                .orderBy(
                        getOrderSpecifierList(request.pageable().getSort())
                                .toArray(OrderSpecifier[]::new) )
                .offset(request.pageable().getOffset())
                .limit(request.pageable().getPageSize())
                .fetch();
    }

    private BooleanExpression likeNickname (String search) {
        if (!StringUtils.hasText(search)) return null;   // space bar까지 막아줌
        return reply.writer.nickname.like("%" + search + "%");
    }

    private BooleanExpression eqReportStatus (ProcessingStatus reportStatus) {
        if (!StringUtils.hasText(reportStatus.name())) return  null;
        return reply.reportStatus.eq(reportStatus);
    }


    private List<OrderSpecifier<?>> getOrderSpecifierList (Sort sort) {

        List<OrderSpecifier<?>> orderList = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            Path<Object> target = Expressions.path(Object.class, reply,  property);
            orderList.add(new OrderSpecifier(direction, target));
        });

        return orderList;
    }

}