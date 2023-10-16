package org.zerock.member.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {

    private String mid;

    private String mpassword;

    private String mname;

    private String memail;

    private String mbirthday;

    private String maddress;

    private String mphonenum;

    private boolean mdel;

    private boolean msocial;
}
