package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<Member> getRolePage(Pageable pageable);
    Page<Member> getRolePage2(Pageable pageable);
}
