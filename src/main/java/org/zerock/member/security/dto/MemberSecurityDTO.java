package org.zerock.member.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    @NotEmpty
    private String mid;

    private String mpassword;

    private String mname;

    @NotEmpty
    private String memail;

    private String mbirthday;

    private String maddress;

    private String mphonenum;

    private boolean mdel;

    private boolean msocial;

    private Map<String, Object> props;  // 소셜 로그인 정보

    public MemberSecurityDTO(String mid, String mpassword, String mname, String memail,
                             String mbirthday, String maddress, String mphonenum,
                             boolean mdel, boolean msocial,
                             Collection<? extends GrantedAuthority> authorities) {

        super(mid, mpassword, authorities);

        this.mid = mid;
        this.mpassword = mpassword;
        this.mname = mname;
        this.memail = memail;
        this.mbirthday = mbirthday;
        this.maddress = maddress;
        this.mphonenum = mphonenum;
        this.mdel = mdel;
        this.msocial = msocial;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.mid;
    }
}
