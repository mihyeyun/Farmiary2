package com.shop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shop.dto.BoardSearchDto;
import com.shop.dto.MainBoardDto;
import com.shop.entity.Board;

public interface BoardRepositoryCustom {
	
	Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
	// 상품 조회 조건과 페이징 정보를 파라미터로 받아 Page<Board> 객체를 반환
	
	Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable);
}
