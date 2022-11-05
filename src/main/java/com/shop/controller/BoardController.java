package com.shop.controller;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.shop.dto.*;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.entity.Board;
import com.shop.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;

	private final MemberService memberService;

	@GetMapping(value = "/board/boardMain")
	public String main(ItemSearchDto itemSearchDto, BoardSearchDto boardSearchDto,
					   Optional<Integer> page, Model model){

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
		Page<MainBoardDto> boards = boardService.getMainBoardPage(boardSearchDto, pageable);

		model.addAttribute("boards", boards);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);

		return "board/boardMain";
	}

	
	@GetMapping(value = "/user/board/boardForm")	// 게시글 등록 페이지 요청
	public String boardForm(Model model) {
		model.addAttribute("boardFormDto", new BoardFormDto());
		return "board/boardForm";
	}
	
	@PostMapping(value = "/user/board/new")	// 게시글 등록
	public String boardNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
			Model model, @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList) {
		
		if(bindingResult.hasErrors()) {	// 필수 값이 없다면 다시 상품 등록 페이지로 전환
			return "board/boardForm";
		}
		
		if(boardImgFileList.get(0).isEmpty() && boardFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 이미지는 필수 입력 값 입니다.");
			return "board/boardForm";
		}
		
		try {
			boardService.saveBoard(boardFormDto, boardImgFileList);
			// 게시글 저장 로직 호출 매개 변수로 게시글 정보와 개시글 이미지 정보를 담고 있는 boardImgFileList를 넘김
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
			return "board/boardForm";
		}
		
		return "redirect:/";
	}
	
	@GetMapping(value = "/user/board/{boardId}")	// 게시글 수정 페이지 요청
	public String boardDtl(@PathVariable("boardId") Long boardId, Model model) {
		
		try {
			BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
			model.addAttribute("boardFormDto", boardFormDto);
		} catch(EntityNotFoundException e) {
			model.addAttribute("errorMessage", "존재하지 게시글 입니다.");
			model.addAttribute("boardFormDto", new BoardFormDto());
			return "board/boardForm";
		}
		
		return "board/boardForm";
	}
	
	@PostMapping(value = "/user/board/{boardId}")	// 게시글 수정 
	public String boardUpdate(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
			@RequestParam("boardImgFile") List<MultipartFile> boardImgFileList, Model model) {
		
		if(bindingResult.hasErrors()) {
			return "board/boardForm";
		}
		
		if(boardImgFileList.get(0).isEmpty() && boardFormDto.getId() == null) {
			model.addAttribute("errorMessage", "첫번째 이미지는 필수 입력 값 입니다.");
			return "board/boardForm";
		}
		
		try {
			boardService.updateBoard(boardFormDto, boardImgFileList);	// 게시글 수정 로직
		} catch (Exception e) {
			model.addAttribute("errorMessage", "게시글 수정 중 에러가 발생하였습니다.");
			return "board/boardForm";
		}
		
		return "redirect:/";
	}
	
	@GetMapping(value = {"/user/boards", "/user/boards/{page}"}) // 번호가 없는경우 있는경우 2가지 매핑
	public String boardManage(BoardSearchDto boardSearchDto,
			@PathVariable("page") Optional<Integer> page, Model model) {
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
		// 첫번째 파라미터는 조회할 페이지 번호, 두번째 파라미터는 한번에 가지고 올 데이터 수
		Page<Board> boards = boardService.getAdminBoardPage(boardSearchDto, pageable);
		model.addAttribute("boards", boards);
		model.addAttribute("boardSearchDto", boardSearchDto);
		model.addAttribute("maxPage", 5);	// 페이지 번호의 최대 개수
		return "board/boardMng";
	}
	
	@GetMapping(value = "/board/{boardId}")	// 게시글 상세조회
	public String boardDtl(Model model, @PathVariable("boardId") Long boardId) {
		BoardFormDto boardFormDto = boardService.getBoardDtl(boardId);
		model.addAttribute("board", boardFormDto);
		return "board/boardDtl";
	}


	//블로거 리스트 출력
	@GetMapping({"/board/bloger","/board/bloger/{page}"})
	public String permitPage(Model model, @PathVariable("page")Optional<Integer> page){

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		Page<Member> members = memberService.getRolePage2(pageable);

		model.addAttribute("members", members);
		model.addAttribute("maxPage", 5);

		return "board/sellerList";
	}
}
