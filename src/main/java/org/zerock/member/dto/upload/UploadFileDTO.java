package org.zerock.member.dto.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {
// 파일 업로드는 MultipartFile API로 처리 가능

    private List<MultipartFile> files;
}
