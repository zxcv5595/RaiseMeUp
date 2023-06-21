package com.zxcv5595.member.domain;

import com.zxcv5595.member.type.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member extends BaseEntity {

    private String username;
    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;


}
