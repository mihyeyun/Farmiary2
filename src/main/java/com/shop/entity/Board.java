package com.shop.entity;

import javax.persistence.*;

import com.shop.dto.BoardFormDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="board")
@Getter @Setter
@ToString
public class Board extends BaseEntity{
	
	@Id
	@Column(name="board_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	// 게시글 번호
	
	private String title;	// 제목
	
	private String content;		// 내용
	
	public void updateBoard(BoardFormDto boardFormDto) {	// 게시글 데이터 업데이트
		this.title = boardFormDto.getTitle();
		this.content = boardFormDto.getContent();
	}
	
}
