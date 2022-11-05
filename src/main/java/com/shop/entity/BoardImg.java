package com.shop.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board_img")
@Getter @Setter
public class BoardImg extends BaseEntity{
	
	@Id @Column(name = "board_img_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String imgName;	// 이미지 파일명
	
	private String oriImgName;	// 원본 이미지 파일명
	
	private String imgUrl;	// 이미지 조회 경로
	
	private String repimgYn;	// 대표 이미지 여부
	
	@ManyToOne(fetch = FetchType.LAZY)	// 보드 엔티티와 다대일 단방향 관계 매핑
	@JoinColumn(name = "board_id")
	private Board board;
	
	public void updateBoardImg(String oriImgName, String imgName, String imgUrl) {
		this.oriImgName = oriImgName;
		this.imgName = imgName;
		this.imgUrl = imgUrl;
	}
}
