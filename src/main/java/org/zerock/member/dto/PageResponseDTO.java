package org.zerock.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {
// 화면에 DTO의 목록과 시작 페이지, 끝 페이지 처리 담당

    private int page;

    private int size;

    private int total;
    
    private int start;      // 시작 페이지 번호
    
    private int end;        // 끝 페이지 번호
    
    private boolean prev;   // 이전 페이지의 존재 여부
    
    private boolean next;   // 다음 페이지의 존재 여부
    
    private List<E> dtoList;
    
    @Builder(builderMethodName = "withAll") // builderMethodName 속성은 Builder() 메서드의 이름을 재지정
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> dtoList, int total) {

        if(total <= 0) {
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10;     // 화면에서의 마지막 페이지 번호
        this.start = this.end - 9;                              // 화면에서의 시작 페이지 번호

        int last = (int)(Math.ceil((total / (double) size)));   // 전체 마지막 페이지 번호

        this.end = end > last ? last : end;                     // end가 last보다 클 경우 전체 마지막 페이지 번호(last)가 화면상 마지막 번호(end)가 됨
        this.prev = this.start > 1;                             // start가 1보다 클 경우 이전 페이지 이동 존재
        this.next = total > this.end * this.size;               // 전체 게시글 수가 화면에서의 마지막 페이지 번호와 화면에서의 페이지 번호의 개수 곱보다 클 경우 다음 페이지 이동 존재
    }
}
