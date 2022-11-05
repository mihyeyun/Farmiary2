package com.shop.controller;

import com.shop.constant.Role;
import com.shop.dto.MemberDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    //판매자 요청 수락 페이지
    @GetMapping({"/admin/requestPermit","/admin/requestPermit/{page}"})
    public String permitPage(Model model, @PathVariable("page")Optional<Integer> page){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Member> members = memberService.getRolePage(pageable);

        model.addAttribute("members", members);
        model.addAttribute("maxPage", 5);

        return "member/candidateList";
    }

    //판매자 요청 수락
    @PostMapping("/admin/requestPermit")
    public String permitProc(MemberDto memberdto) {
        String email = memberdto.getEmail();
        System.out.println(email);
        System.out.println("=============================");
        memberService.grantRole(email, Role.SELLER);

        return "redirect:/myPage";
    }
}
