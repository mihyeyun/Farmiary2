package com.shop.dto;

import org.modelmapper.ModelMapper;

import com.shop.entity.BoardImg;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardImgDto {
	
	private Long id;
	
	private String imgName;
	
	private String oriImgName;
	
	private String imgUrl;
	
	private String repImgYn;
	
	private static ModelMapper modelMapper = new ModelMapper();	// ModelMapper 객체를 추가
	
	public static BoardImgDto of(BoardImg boardImg) {
		return modelMapper.map(boardImg, BoardImgDto.class);
		// boardImg 엔티티의 객체를 boardImgDto로 복사해서 반환
	}
}
