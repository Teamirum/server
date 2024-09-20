package server.global.security.domain;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import server.domain.member.domain.Member;

import java.util.Collection;
import java.util.List;

@Builder
public class CustomUserDetails implements UserDetails {

    private String id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
//    private Map<String, Object> attributes;
    private List<String> roles;

    public static CustomUserDetails create(Member member) {
        return CustomUserDetails.builder()
                .id(member.getMemberId())
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(member.getRole().toString()))
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void updateAuthorities(Member member) {
        this.authorities = AuthorityUtils.createAuthorityList(member.getRole().toString());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    public String getMemberId() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
