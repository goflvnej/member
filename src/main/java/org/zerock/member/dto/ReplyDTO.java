package org.zerock.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    
    private Long rno;                       // 댓글 번호

    @NotNull
    private Long bno;                       // 게시물 번호

    @NotEmpty
    private String replyText;               // 댓글 내용

    @NotEmpty
    private String replyer;                 // 댓글 작성자
    
    private LocalDateTime regDate, modDate; // 댓글 작성일, 수정일
    
}
