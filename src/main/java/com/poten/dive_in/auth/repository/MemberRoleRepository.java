package com.poten.dive_in.auth.repository;

import com.poten.dive_in.auth.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
}
