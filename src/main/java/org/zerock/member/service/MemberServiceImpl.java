package org.zerock.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member.entity.Member;
import org.zerock.member.entity.MemberRole;
import org.zerock.member.dto.MemberJoinDTO;
import org.zerock.member.repository.MemberRepository;
import org.zerock.member.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {

        String mid = memberJoinDTO.getMid();

        // 같은 아이디가 존재하면 예외 발생 처리
        boolean exist = memberRepository.existsById(mid);
        
        if(exist) {
            throw new MidExistException();
        }

        Member member = modelMapper.map(memberJoinDTO, Member.class);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpassword()));
        member.addRole(MemberRole.USER);

        log.info("====== MemberServiceImpl 클래스 member 로그 출력 ======");
        log.info(member);
        log.info(member.getRoleSet());

        memberRepository.save(member);
    }

    @Override
    public MemberSecurityDTO readOne(String mid) {

        Optional<Member> result = memberRepository.findById(mid);
        Member member = result.orElseThrow();

        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        member.getMid(),
                        member.getMpassword(),
                        member.getMname(),
                        member.getMemail(),
                        member.getMbirthday(),
                        member.getMaddress(),
                        member.getMphonenum(),
                        member.isMdel(),
                        member.isMsocial(),
                        member.getRoleSet()
                                .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                                .collect(Collectors.toList())
                );

        return memberSecurityDTO;
    }

    @Override
    public void modify(MemberSecurityDTO memberSecurityDTO) {

        Member member = modelMapper.map(memberSecurityDTO, Member.class);

        member.changePassword(passwordEncoder.encode(memberSecurityDTO.getMpassword()));
        member.changeName(memberSecurityDTO.getMname());
        member.changeEmail(memberSecurityDTO.getMemail());
        member.changeAddress(memberSecurityDTO.getMaddress());
        member.changePhonenum(memberSecurityDTO.getMphonenum());

        memberRepository.save(member);
    }
}