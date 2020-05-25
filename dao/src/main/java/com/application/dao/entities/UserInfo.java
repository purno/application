package com.application.dao.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "USER_INFO",
        indexes = {
                @Index(name = "INDEX_CLIENT_EMAIL", columnList = "CLIENT_EMAIL", unique = true)
        })
public class UserInfo extends BaseEntity implements Serializable, UserDetails {

    public static final long                    serialVersionUID = 20703495952479258L;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "BIGINT(20)")
    private Long id;
    @Column(name = "CLIENT_EMAIL", columnDefinition = "VARCHAR(50)", nullable = false)
    private String clientEmail;
    @Column(name = "ENCODED_CLIENT_SECRET", columnDefinition = "VARCHAR(255)", nullable = false)
    private String encodedClientSecret;
    @Column(name = "PERMISSIONS", columnDefinition = "VARCHAR(255)", nullable = false)
    @Getter(AccessLevel.NONE)
    private String permissions;
    @Column(name = "IS_ACTIVE")
    private boolean isActive;


    public String[] getPermissions() {
        return permissions.split(",");
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] roles = getPermissions();
        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (String role : roles) {
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
            grantedAuthorityList.add(grantedAuthority);
        }
        return grantedAuthorityList;
    }

    @Override
    public String getPassword() {
        return encodedClientSecret;
    }

    @Override
    public String getUsername() {
        return clientEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
