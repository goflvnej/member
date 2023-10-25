package org.zerock.member.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice   // 컨트롤러에서 발생하는 예외에 대해 JSON 같은 순수 응답 메시지 전송
@Log4j2
public class CustomRestAdvice {
// REST 컨트롤러는 Ajax 같은 방식으로 서버를 호출하고 결과를 전송하므로 에러 발생 시 알아보기가 어려움
// @Valid 과정에서 문제 발생 시 어떤 에러가 발생했는지 결과 출력

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {

        log.error(e);
        Map<String, String> errorMap = new HashMap<>();

        if(e.hasErrors()) {

            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }

        return ResponseEntity.badRequest().body(errorMap);
    }

    // 외래키에서 없는 값을 사용할 때 500 에러가 뜨는 걸 400 코드로 변경
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleFKException(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", time());
        errorMap.put("message", "제약 조건 위반");

        return ResponseEntity.badRequest().body(errorMap);
    }

    // 없는 값을 사용해서 조회/삭제할 때 발생하는 500 에러 예외 처리
    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String, String>> handleNoSuchElement(Exception e) {

        log.error(e);

        Map<String, String> errorMap = new HashMap<>();

        errorMap.put("time", time());
        errorMap.put("message", "존재하지 않는 값");

        return ResponseEntity.badRequest().body(errorMap);
    }

    // 현재 시각 포맷팅
    public String time() {

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedCurrentTime = timeFormat.format(currentTime);

        return formattedCurrentTime;
    }
}
