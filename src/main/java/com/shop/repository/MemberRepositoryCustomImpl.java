package com.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.entity.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchQuery){
        return QMember.member.email.like("%" + searchQuery + "%");
    }

    private BooleanExpression selectCandidate(Role role){
        return QMember.member.role.eq(Role.CANDIDATE);
    }
    private BooleanExpression selectSeller(Role role){
        return QMember.member.role.eq(Role.SELLER);
    }

    @Override
    public Page<Member> getRolePage(Pageable pageable) {

        List<Member> content = queryFactory
                .selectFrom(QMember.member)
                .where(selectCandidate(Role.CANDIDATE))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QMember.member)
                .where(selectCandidate(Role.CANDIDATE))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Member> getRolePage2(Pageable pageable) {

        List<Member> content = queryFactory
                .selectFrom(QMember.member)
                .where(selectSeller(Role.SELLER))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QMember.member)
                .where(selectSeller(Role.SELLER))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
