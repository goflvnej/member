package org.zerock.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.member.dto.upload.UploadFileDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Log4j2
public class UpDownController {
// 파일 업로드와 파일을 보여주는 기능 처리

    // @Value 어노테이션은 application.properties 파일의 설정 정보를 읽어서 변수 값으로 사용 가능
    @Value("${org.zerock.upload.path}")
    private String uploadPath;  // 파일 업로드 경로

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(UploadFileDTO uploadFileDTO) {

        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null) {

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                // 파일 업로드 시 원본 이름
                String originalName = multipartFile.getOriginalFilename();

                //날짜 폴더 생성
                String folderPath = makeFolder();

                // UUID로 16자리 코드 생성 (새 이름)
                String uuid = UUID.randomUUID().toString();

                // 서버에 저장되는 파일 이름
                String saveName = uploadPath + File.separator + folderPath +
                        File.separator + uuid + "_" + originalName;

                // 실제 파일 저장
                Path savePath = Paths.get(saveName);

                try {
                    multipartFile.transferTo(savePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        return null;
    }

    // 날짜 폴더 생성
    private String makeFolder() {
        String formattedTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = formattedTime.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }
}
