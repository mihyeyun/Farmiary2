package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearchDto {
	
	private String sarchDateType;	// 시간별 게시글 검색
	// all : 전체
	// 1d : 하루
	// 1w : 일주일
	// 1m : 한달
	// 6m : 6개월
	
	private String searchBy;	// 검색 유형
	// title : 제목
	// content : 내용
	// createBy : 상품 등록자 아이디
	
	private String searchQuery = "";	// 조회할 검색어를 저장할 변수
}
