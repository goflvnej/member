package org.zerock.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
// 페이징 정보(page/size), 검색 종류(type), 검색 키워드(keyword) 설정

    @Builder.Default        // 빌더 패턴 사용 시 기본값
    private int page = 1;

    @Builder.Default
    private int size = 10;  // 한 페이지당 게시글 개수 10개
    
    private String type;    // 검색 조건(t, c, w, tc, tw, tcw)

    private String keyword; // 검색 키워드

    private String link;    // 링크 조건

    // BoardRepository에서 검색 조건(type)을 String[]으로 처리
    public String[] getTypes() {

        if(type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    // 페이징 처리 (매개값으로 정렬 기준을 받음)
    public Pageable getPageable(String ...props) {

        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }

    // 검색 조건과 페이징 조건 등을 문자열로 구성
    public String getLink() {

        if(link == null) {
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if(type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }

            if(keyword != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }
            }

            link = builder.toString();
        }

        return link;
    }
}
