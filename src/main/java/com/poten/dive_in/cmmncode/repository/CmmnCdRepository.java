package com.poten.dive_in.cmmncode.repository;

import com.poten.dive_in.cmmncode.entity.CmmnCd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CmmnCdRepository extends JpaRepository<CmmnCd, Long> {
    Optional<CmmnCd> findByCode(String code);
}
