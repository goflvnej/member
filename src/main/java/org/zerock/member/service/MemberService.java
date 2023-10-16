package org.zerock.member.service;

import org.zerock.member.dto.BoardDTO;
import org.zerock.member.dto.MemberJoinDTO;
import org.zerock.member.security.dto.MemberSecurityDTO;

import java.util.List;

public interface MemberService {

    static class MidExistException extends Exception {

    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

    // 조회
    MemberSecurityDTO readOne(String id);
    
    // 수정
    void modify(MemberSecurityDTO memberSecurityDTO);

}
