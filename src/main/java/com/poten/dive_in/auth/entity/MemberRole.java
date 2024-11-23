package com.poten.dive_in.auth.entity;

import com.poten.dive_in.auth.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_nm")
    private Role name;

    @Column(name = "role_expln")
    private String explain;

    private Character useYn;
}
