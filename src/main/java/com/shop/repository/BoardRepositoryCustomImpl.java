package com.shop.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.dto.BoardSearchDto;
import com.shop.dto.MainBoardDto;
import com.shop.dto.QMainBoardDto;
import com.shop.entity.Board;
import com.shop.entity.QBoard;
import com.shop.entity.QBoardImg;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom{
	
	private JPAQueryFactory queryFactory;	// 동적 쿼리를 생성하기 위해 선언
	
	public BoardRepositoryCustomImpl(EntityManager em) {
		// JPAQueryFactory의 생성자로 EntityManager 객체를 넣어줌
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	private BooleanExpression regDtsAfter(String searchDateType) {
		LocalDateTime dateTime = LocalDateTime.now();
		
		if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
			return null;
		}else if(StringUtils.equals("1d", searchDateType)) {
			dateTime = dateTime.minusDays(1);
		}else if(StringUtils.equals("1w", searchDateType)) {
			dateTime = dateTime.minusWeeks(1);
		}else if(StringUtils.equals("1m", searchDateType)) {
			dateTime = dateTime.minusMonths(1);
		}else if(StringUtils.equals("6m", searchDateType)) {
			dateTime = dateTime.minusMonths(6);
		}
		
		return QBoard.board.regTime.after(dateTime);
	}
	
	private BooleanExpression searchByLike(String searchBy, String searchQuery) {
		
		if(StringUtils.equals("title", searchBy)) {
			return QBoard.board.title.like("%" + searchQuery + "%");
		}else if(StringUtils.equals("createdBy", searchBy)) {
			return QBoard.board.createdBy.like("%" + searchQuery + "%");
		}else if(StringUtils.equals("content", searchBy)) {
			return QBoard.board.content.like("%" + searchQuery + "%");
		}
		
		return null;
	}

	@Override
	public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
		List<Board> content = queryFactory
				.select(QBoard.board)
				.where(regDtsAfter(boardSearchDto.getSarchDateType()),
						searchByLike(boardSearchDto.getSearchBy(),
								boardSearchDto.getSearchQuery()))
				.orderBy(QBoard.board.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = content.size();
		return new PageImpl<>(content, pageable, total);
	}
	
	private BooleanExpression titleLike(String searchQuery) {	// 검색어가 null 이 아니면 검색어 포함
		return StringUtils.isEmpty(searchQuery) ? null : QBoard.board.title.like("%" + searchQuery + "%");
	}

	@Override
	public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable) {
		QBoard board = QBoard.board;
		QBoardImg boardImg = QBoardImg.boardImg;
		
		List<MainBoardDto> content = queryFactory
				.select(
						new QMainBoardDto(
								board.id,
								board.title,
								board.content,
								boardImg.imgUrl)
						)
				.from(boardImg)
				.join(boardImg.board, board)	// boardImg와 board를 내부 조인
				.where(boardImg.repimgYn.eq("Y"))	// 대표 이미지만 불러옴
				.where(titleLike(boardSearchDto.getSearchQuery()))
				.orderBy(board.id.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		long total = content.size();
		return new PageImpl<>(content, pageable, total);
	}
}
