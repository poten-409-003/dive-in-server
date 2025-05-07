package com.poten.dive_in.instructor.repository;


import com.poten.dive_in.instructor.entity.Instructor;

import java.util.Optional;

public interface InstructorRepositoryCustom {
    /**
     * 회원 ID를 통해 강사 엔티티를 조회합니다.
     *
     * @param memberId 회원 ID
     * @return 강사 엔티티 (Optional)
     */
    Optional<Instructor> findByMemberId(Long memberId);

    /**
     * 강사 ID를 통해 강사 엔티티와 연관된 팀 정보를 함께 조회합니다.
     *
     * @param instructorId 강사 ID
     * @return 강사 엔티티 (Optional)
     */
    Optional<Instructor> findWithTeamById(Long instructorId);
}
