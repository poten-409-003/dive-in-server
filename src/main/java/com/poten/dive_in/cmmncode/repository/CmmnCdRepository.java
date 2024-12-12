package com.poten.dive_in.cmmncode.repository;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CmmnCdRepository extends JpaRepository<CommonCode, Long> {
    Optional<CommonCode> findByCode(String code);
    Optional<CommonCode> findByCodeName(String codeName);
}
