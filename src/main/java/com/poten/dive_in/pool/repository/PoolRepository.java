package com.poten.dive_in.pool.repository;

import com.poten.dive_in.pool.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoolRepository extends JpaRepository<Pool, Long>, PoolRepositoryCustom {
    List<Pool> findByNameContaining(String name);
}
