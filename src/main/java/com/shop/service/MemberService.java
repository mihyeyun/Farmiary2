package com.shop.service;

import com.shop.constant.Role;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    
    //회원 정보창 로그인 비밀번호 확인
    public boolean checkPassword(String realPassword, String checkPassword) {
        boolean matches = passwordEncoder.matches(checkPassword, realPassword);
        return matches;
    }
    //회원 정보 수정
    public void UpdateMember(Member member){
        Long memberId = memberRepository.findIdByEmail(member.getEmail());
        member.setId(memberId);
        System.out.println(member.toString());
        System.out.println("======================================================================");
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    //회원 권한 설정
    public void grantRole(String email, Role role){
        Member member = memberRepository.findByEmail(email);

        System.out.println("======================================");
        System.out.println(member.toString());
        System.out.println("======================================");

        member.setRole(role);

        memberRepository.save(member);

        System.out.println("======================================");
        System.out.println(member.toString());
        System.out.println("======================================");
    }


    @Transactional(readOnly = true)
    public Page<Member> getRolePage(Pageable pageable) {
        return memberRepository.getRolePage(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Member> getRolePage2(Pageable pageable) {
        return memberRepository.getRolePage2(pageable);
    }

    //회원 탈퇴
    public void deleteMember(String email){
        Long deleteId = memberRepository.findIdByEmail(email);
        memberRepository.deleteById(deleteId);
    }


}

