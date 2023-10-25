package org.zerock.member.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {

    private String uuid;

    private String fileName;

    private boolean image;

    // getLink()는 메서드를 직접 호출하지 않아도 JSON으로 처리될 때 link 속성으로 자동 처리됨
    public String getLink() {

        if(image) {
            return "s_" + uuid + "_" + fileName;    // 이미지일 경우 섬네일 이름
        } else {
            return uuid + "_" + fileName;
        }
    }
}
