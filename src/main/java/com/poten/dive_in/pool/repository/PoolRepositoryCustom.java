package com.poten.dive_in.pool.repository;

import com.poten.dive_in.pool.entity.Pool;

import java.util.List;

public interface PoolRepositoryCustom {
    List<Pool> findByKeyword(String keyword);
}