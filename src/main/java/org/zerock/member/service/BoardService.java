package org.zerock.member.service;

import org.zerock.member.dto.*;
import org.zerock.member.entity.Board;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    // 등록
    Long register(BoardDTO boardDTO);

    // 조회
    BoardDTO readOne(Long bno);

    // 수정
    void modify(BoardDTO boardDTO);

    // 삭제
    void remove(Long bno);

    // 목록/검색
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    // 댓글 개수까지 처리하는 목록/검색
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    // 이미지와 댓글 개수를 포함한 게시글 목록
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    // ModelMapper는 단순한 객체를 다른 타입으로 변환할 때만 편리하기 때문에 파일 처리를 위해 따로 메서드 작성
    default Board dtoToEntity(BoardDTO boardDTO) {

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if(boardDTO.getFileNames() != null) {

            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }

        return board;
    }

    default BoardDTO entityToDTO(Board board) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid() + "_" + boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }
}
