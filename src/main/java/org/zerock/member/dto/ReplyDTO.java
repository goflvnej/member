package org.zerock.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    
    private Long rno;   // 댓글 번호
    
    private Long bno;   // 게시물 번호
    
    private String replyText;
    
    private String replyer;
    
    private LocalDateTime regDate, modDate;
    
}
