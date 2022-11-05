package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.shop.dto.BoardSearchDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainBoardDto;
import com.shop.dto.MainItemDto;
import com.shop.service.BoardService;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;
    private final BoardService boardService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, BoardSearchDto boardSearchDto,
    		Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        Page<MainBoardDto> boards = boardService.getMainBoardPage(boardSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("boards", boards);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    @GetMapping(value = "/myPage")
    public String myPage(){
        return "/member/myPage";
    }

}