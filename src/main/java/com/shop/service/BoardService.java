package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shop.dto.BoardFormDto;
import com.shop.dto.BoardImgDto;
import com.shop.dto.BoardSearchDto;
import com.shop.dto.MainBoardDto;
import com.shop.entity.Board;
import com.shop.entity.BoardImg;
import com.shop.repository.BoardImgRepository;
import com.shop.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final BoardImgService boardImgService;
	private final BoardImgRepository boardImgRepository;
	
	public Long saveBoard(BoardFormDto boardFormDto,
			List<MultipartFile> boardImgFileList) throws Exception{
		
		// 게시글 등록
		Board board = boardFormDto.createBoard();	// 게시글 등록 폼으로부터 입력 받은 데이터를 이용하여 객체 생성
		boardRepository.save(board);	// 게시글 데이터 저장
		
		// 이미지 등록
		for(int i=0;i<boardImgFileList.size();i++) {
			BoardImg boardImg = new BoardImg();
			boardImg.setBoard(board);
			if(i == 0)
				boardImg.setRepimgYn("Y");	// 첫번째 이미지는 대표 이미지
			else
				boardImg.setRepimgYn("N");
			boardImgService.saveBoardImg(boardImg, boardImgFileList.get(i));
			// 게시글 이미지 정보 저장
		}
		
		return board.getId();
	}
	
	@Transactional(readOnly = true) // 읽기 전용으로 설정하면 JPA가 더티체킹을 수행하지 않아 성능이 향상됨
	public BoardFormDto getBoardDtl(Long boardId) {
		
		List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);
		List<BoardImgDto> boardImgDtoList = new ArrayList<>();
		for(BoardImg boardImg : boardImgList) {
			BoardImgDto boardImgDto = BoardImgDto.of(boardImg);
			boardImgDtoList.add(boardImgDto);
		}
		
		Board board = boardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);
		BoardFormDto boardFormDto = BoardFormDto.of(board);
		boardFormDto.setBoardImgDtoList(boardImgDtoList);
		return boardFormDto;		
	}
	
	public Long updateBoard(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception{
		
		// 게시글 수정
		Board board = boardRepository.findById(boardFormDto.getId())	// 게시글 엔티티 조회
				.orElseThrow(EntityNotFoundException::new);
		board.updateBoard(boardFormDto);	// 게시글 업데이트
		
		List<Long> boardImgIds = boardFormDto.getBoardImgIds();		// 게시글 이미지 아이디 리스트 조회
		
		// 이미지 등록
		for(int i=0;i<boardImgFileList.size();i++) {
			boardImgService.updateBoardImg(boardImgIds.get(i), boardImgFileList.get(i));
			// 게시글 이미지 아이디와 게시글 이미지 파일 정보 전달
		}
		
		return board.getId();
	}
	
	@Transactional(readOnly = true)	// 게시글 데이터 조회 메소드
	public Page<Board> getAdminBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
		return boardRepository.getAdminBoardPage(boardSearchDto, pageable);
	}
	
	@Transactional(readOnly = true)	// 게시글 검색 메소드
	public Page<MainBoardDto> getMainBoardPage(BoardSearchDto boardSearchDto, Pageable pageable){
		return boardRepository.getMainBoardPage(boardSearchDto, pageable);
	}
	
}
