package com.shop.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.shop.entity.BoardImg;
import com.shop.repository.BoardImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {
	
	@Value("${boardImgLocation}")		// properties에 저장된 값을 불러옴
	private String boardImgLocation;
	
	private final BoardImgRepository boardImgRepository;
	
	private final FileService fileService;
	
	public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception{
		String oriImgName = boardImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		// 파일 업로드
		if(!StringUtils.isEmpty(oriImgName)) {
			imgName = fileService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes());
			// 저장할 경로와 파일의 이름, 파일을 담은 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드 호출
			imgUrl = "/images/board/" + imgName;
			// WebMvcConfig에서 설정한 /images/** 에 맞게 /images/를 넣고 프로퍼티 경로인 C:/shop/ 아래
			// board 폴더에 이미지가 저장되므로 상품 이미지를 불러오는 경로는 /images/board/을 붙여줘야함
		}
		
		// 게시글 이미지 정보 저장
		boardImg.updateBoardImg(oriImgName, imgName, imgUrl);
		boardImgRepository.save(boardImg);
	}
	
	public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception{
		if(!boardImgFile.isEmpty()) {	// 게시글 이미지 수정이 있으면 이미지 업데이트
			BoardImg savedBoardImg = boardImgRepository.findById(boardImgId)	// 기존 이미지 조회
					.orElseThrow(EntityNotFoundException::new);
			
			// 기존 이미지 파일 삭제
			if(!StringUtils.isEmpty(savedBoardImg.getImgName())) {
				fileService.deleteFile(boardImgLocation + "/" + savedBoardImg.getImgName());
			}
			
			String oriImgName = boardImgFile.getOriginalFilename();
			String imgName = fileService.uploadFile(boardImgLocation, oriImgName,
					boardImgFile.getBytes());
			String imgUrl = "/images/board/" + imgName;
			savedBoardImg.updateBoardImg(oriImgName, imgName, imgUrl);
		}
	}
}
