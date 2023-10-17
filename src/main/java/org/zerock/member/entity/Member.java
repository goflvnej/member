package org.zerock.member.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends BaseEntity {

    @Id
    @Column(length = 100)
    private String mid;        // 아이디

    @Column(length = 1000)
    private String mpassword;  // 비밀번호

    @Column(length = 20)
    private String mname;      // 이름

    @Column(length = 30, unique = true)
    private String memail;     // 이메일

    @Column(length = 10)
    private String mbirthday;  // 생년월일

    @Column(length = 50)
    private String maddress;   // 주소

    @Column(length = 20)
    private String mphonenum;  // 핸드폰번호
    
    private boolean mdel;      // 회원탈퇴 여부
    
    private boolean msocial;   // 소셜로그인 여부

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String mpassword) {
        this.mpassword = mpassword;
    }

    public void changeName(String mname) {
        this.mname = mname;
    }

    public void changeEmail(String memail) {
        this.memail = memail;
    }

    public void changeAddress(String maddress) {
        this.maddress = maddress;
    }

    public void changePhonenum(String mphonenum) {
        this.mphonenum = mphonenum;
    }

    public void changeDel(boolean mdel) {
        this.mdel = mdel;
    }

    public void addRole(MemberRole memberRole) {
        this.roleSet.add(memberRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void changeSocial(boolean msocial) {
        this.msocial = msocial;
    }
}
