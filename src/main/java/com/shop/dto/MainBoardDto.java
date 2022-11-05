package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainBoardDto {
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private String imgUrl;
	
	@QueryProjection	// Querydsl로 결과 조회 시 MainBoardDto 객체로 바로 받아 오도록 활용
	public MainBoardDto(Long id, String title, String content, String imgUrl) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.imgUrl = imgUrl;
	}
}
