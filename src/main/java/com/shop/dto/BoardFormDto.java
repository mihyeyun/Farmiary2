package com.shop.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.shop.entity.Board;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardFormDto {
	
	private Long id;
	
	private String title;
	
	private String content;
	
	private List<BoardImgDto> boardImgDtoList = new ArrayList<>();
	// 게시글 저장 후 수정할 때 게시글 이미지 정보를 저장하는 리스트
	
	private List<Long> boardImgIds = new ArrayList<>();
	// 게시글 이미지 아이디를 저장하는 리스트, 이미지 수정시에 사용
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	public Board createBoard() {
		return modelMapper.map(this, Board.class);
	}
	
	public static BoardFormDto of(Board board) {
		return modelMapper.map(board, BoardFormDto.class);
	}
}
