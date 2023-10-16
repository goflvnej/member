package org.zerock.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.member.domain.Board;
import org.zerock.member.dto.BoardDTO;
import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;
import org.zerock.member.repository.BoardRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;          // 엔티티(Board)와 DTO(BoardDTO) 간의 변환
    private final BoardRepository boardRepository;  // 엔티티 객체 연결

    @Override
    public Long register(BoardDTO boardDTO) {

        Board board = modelMapper.map(boardDTO, Board.class);
        Long bno = boardRepository.save(board).getBno();

        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {

        Optional<Board> result = boardRepository.findById(bno);                 // DB에서 bno 값을 이용해서 해당 엔티티로 리턴
        Board board = result.orElseThrow();                                     // Optional<Board> 객체가 null 값이 아니면 board 객체로 담음
        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);             // Board 엔티티를 BoardDTO 객체로 리턴

        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getBno());   // boardDTO 객체에서 수정할 데이터의 bno 값을 받아 엔티티로 리턴
        Board board = result.orElseThrow();                                     // Optional<Board> 객체가 null 값이 아니면 board 객체로 담음
        board.change(boardDTO.getTitle(), boardDTO.getContent());               // 기존 엔티티의 제목과 내용을 DTO 객체의 값으로 변경
        boardRepository.save(board);                                            // DB에 수정된 데이터 값 저장
    }

    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        // 검색 조건과 키워드가 있는 경우 쿼리문 작성하여 페이징 처리된 Page<Board> 객체 리턴
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // modelMapper를 이용해 Page<Board>를 List<BoardDTO>로 변환
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class)).collect(Collectors.toList());

        // PageResponseDTO의 빌더 패턴은 withAll()로 재지정됨 (시작/끝 페이지, 이전/다음 페이지 등 처리)
        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
