package com.poten.dive_in.pool.repository;

import com.poten.dive_in.pool.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoolRepository extends JpaRepository<Pool, Long>, PoolRepositoryCustom {
}
