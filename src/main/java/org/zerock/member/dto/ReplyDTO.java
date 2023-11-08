package org.zerock.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    
    // JSON 처리 시 포맷팅 지정
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDate;          // 댓글 작성일

    // JSON 변환 시 화면에서 제외
    @JsonIgnore
    private LocalDateTime modDate;          // 댓글 수정일
    
}
