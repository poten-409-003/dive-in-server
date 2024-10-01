package com.poten.dive_in.academy.repository;

import com.poten.dive_in.academy.entity.Academy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcademyRepository extends JpaRepository<Academy,Long> {
}
