package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.shop.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>,
QuerydslPredicateExecutor<Board>, BoardRepositoryCustom{
	// JpaRepository에서 지원하는 기본 메소드
	// <S extends T> save(S entity)	엔티티 저장 및 수정
	// void delete(T entity)		엔티티 삭제
	// count()					엔티티 총 개수 반환
	// Iterable<T> findAll()	모든 엔티티 조회
	
	// QuerydslPredicateExecutor 기본 메소드
	// long count(Predicate)	조건에 맞는 데이터의 총 개수 반환
	// boolean exists(Predicate)	조건에 맞는 데이터의 존재 여부 반환
	// Iterable findAll(Predicate)	조건에 맞는 모든 데이터 반환
	// Page<T> findAll(Predicate, Pageable)		조건에 맞는 페이지 데이터 반환
	// Iterable findAll(Predicate, Sort)	조건에 맞는 정렬된 데이터 반환
	// T findOne(Predicate)					조건에 맞는 데이터 1개 반환
	
	List<Board> findByTitle(String title);	// 제목으로 조회
	
	List<Board> findByContent(String content);	// 내용으로 조회
	
	List<Board> findByTitleOrContent(String title, String content);	// 상품명 또는 내용으로 검색
}
