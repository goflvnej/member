package org.zerock.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;
import org.zerock.member.dto.ReplyDTO;
import org.zerock.member.entity.Reply;
import org.zerock.member.repository.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDTO replyDTO) {

        Reply reply = modelMapper.map(replyDTO, Reply.class);   // ReplyDTO 객체를 Reply 엔티티로 변환

        Long rno = replyRepository.save(reply).getRno();        // Reply 엔티티를 DB에 저장하고 댓글 번호 리턴

        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {

        Optional<Reply> replyOptional = replyRepository.findById(rno);

        Reply reply = replyOptional.orElseThrow();

        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {

        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());

        Reply reply = replyOptional.orElseThrow();

        reply.changeText(replyDTO.getReplyText());  // 댓글 내용만 수정 가능

        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {

        replyRepository.deleteById(rno);
    }

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {

        // 페이징 처리
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage() - 1,   // 페이지 번호
                pageRequestDTO.getSize(),                                           // 한 페이지당 댓글 개수
                Sort.by("rno").ascending());                              // 댓글 번호(rno) 오름차순

        // 특정 게시글의 댓글을 조회해서 페이징 처리
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        // Reply 객체의 목록을 ReplyDTO 객체로 변환
        List<ReplyDTO> dtoList = result.getContent().stream()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)         // 페이징 처리를 위해 필요
                .dtoList(dtoList)                       // 댓글 객체 리스트
                .total((int)result.getTotalElements())  // 댓글 개수
                .build();
    }
}
