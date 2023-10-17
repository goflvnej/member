package org.zerock.member.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.zerock.member.entity.Member;
import org.zerock.member.entity.MemberRole;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1,99).forEach(i -> {
            Member member = Member.builder()
                    .mid("member" + i)
                    .mpassword(passwordEncoder.encode("1111"))
                    .memail("email" + i + "@aaa.bbb")
                    .build();

            member.addRole(MemberRole.USER);

            if(i >= 90) {
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }

    @Test
    public void testRead() {

        Optional<Member> result = memberRepository.getWithRoles("member100");
        Member member = result.orElseThrow();

        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));
    }

    @Test
    @Commit
    public void testUpdate() {

        String mid = "goflvnej@naver.com";
        String mpassword = passwordEncoder.encode("54321");

        memberRepository.updatePassword(mid, mpassword);
    }
}
