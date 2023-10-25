package org.zerock.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.zerock.member.dto.upload.UploadFileDTO;
import org.zerock.member.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {
// 파일 업로드와 파일을 보여주는 기능 처리

    // @Value 어노테이션은 application.properties 파일의 설정 정보를 읽어서 변수 값으로 사용 가능
    @Value("${org.zerock.upload.path}")
    private String uploadPath;  // 파일 업로드 경로

    // 첨부파일 업로드
    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {

        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null) {

            // 파일 업로드 이후 결과를 처리할 리스트 생성
            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {

                // 파일 업로드 시 원본 이름
                String originalName = multipartFile.getOriginalFilename();

                // 인터넷 IE나 Edge는 파일의 전체 경로가 들어옴
                String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

                // 썸네일 폴더 생성
                String thumbnailFolderPath = makeThumbnailFolder();

                // UUID로 16자리 코드 생성 (새 이름)
                String uuid = UUID.randomUUID().toString();

                // 실제 파일 저장
                Path savePath = Paths.get(uploadPath, uuid + "_" + fileName);

                boolean image = false;

                try {
                    multipartFile.transferTo(savePath);

                    // 파일 종류가 이미지일 경우
                    if(Files.probeContentType(savePath).startsWith("image")) {

                        image = true;

                        //썸네일 경로 및 이름 생성
                        String thumbnailSaveName = uploadPath + File.separator + thumbnailFolderPath +
                                File.separator + "s_"+ uuid + "_" + fileName;

                        File thumbnailFile = new File(thumbnailSaveName);

                        // 해당 파일, 썸네일 파일, 가로 길이, 세로 길이
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(fileName)
                                .image(image)
                        .build());
            });

            return list;
        }

        return null;
    }

    // 썸네일 폴더 생성
    private String makeThumbnailFolder() {
        String thumbnailPath = "Thumbnail";

        File uploadPathFolder = new File(uploadPath, thumbnailPath);

        if (uploadPathFolder.exists() == false) {
            uploadPathFolder.mkdirs();
        }
        return thumbnailPath;
    }

    // 첨부파일 조회
    @ApiOperation(value = "View File Get", notes = "GET 방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGet(@PathVariable String fileName) {

        try {
            String sourceFileName = URLDecoder.decode(fileName,"UTF-8");

            Resource resource = new FileSystemResource(uploadPath + File.separator + sourceFileName);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

            return ResponseEntity.ok().headers(headers).body(resource);

        } catch (Exception e) {
            log.error(e.getMessage());

            return ResponseEntity.internalServerError().build();
        }
    }

    // 첨부파일 삭제
    @ApiOperation(value = "remove File", notes = "DELETE 방식으로 첨부파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName) {

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try {
            String sourceFileName = URLDecoder.decode(fileName,"UTF-8");
            Resource resource = new FileSystemResource(uploadPath + File.separator + sourceFileName);

            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")) {
                File file = new File(uploadPath + File.separator + fileName);
                File thumbnailFile = new File(uploadPath + File.separator + "Thumbnail" + File.separator + "s_" + fileName);

                file.delete();
                thumbnailFile.delete();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);

        return resultMap;
    }
}
