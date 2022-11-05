package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entity.BoardImg;

public interface BoardImgRepository extends JpaRepository<BoardImg, Long>{
	
	// 게시글 아이디로 게시글 이미지 아이디의 오름차순으로 데이터를 가져오는 쿼리 메소드
	List<BoardImg> findByBoardIdOrderByIdAsc(Long BoardId);
	
	// 대표 이미지를 찾는 쿼리 메소드
	BoardImg findByBoardIdAndRepimgYn(Long boardId, String repimgYn);
}
