package com.poten.dive_in.instructor.repository;


import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.instructor.entity.QInstructor;
import com.poten.dive_in.instructor.entity.QInstructorTeamMapping;
import com.poten.dive_in.lesson.entity.QCoachingTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class InstructorRepositoryCustomImpl implements InstructorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 회원 ID를 통해 강사 엔티티를 조회합니다.
     *
     * @param memberId 회원 ID
     * @return 강사 엔티티 (Optional)
     */
    @Override
    public Optional<Instructor> findByMemberId(Long memberId) {
        QInstructor instructor = QInstructor.instructor;

        return Optional.ofNullable(queryFactory
                .selectFrom(instructor)
                .where(instructor.member.id.eq(memberId)) // Member 엔티티의 ID와 조인 (또는 OneToOne 관계 활용)
                .fetchOne());
    }

    /**
     * 강사 ID를 통해 강사 엔티티와 연관된 팀 정보를 함께 조회합니다.
     *
     * @param instructorId 강사 ID
     * @return 강사 엔티티 (Optional)
     */
    @Override
    public Optional<Instructor> findWithTeamById(Long instructorId) {
        QInstructor instructor = QInstructor.instructor;
        QInstructorTeamMapping instructorTeamMapping = QInstructorTeamMapping.instructorTeamMapping;
        QCoachingTeam coachingTeam = QCoachingTeam.coachingTeam;

        return Optional.ofNullable(queryFactory
                .selectFrom(instructor)
                .leftJoin(instructor.teamMappings, instructorTeamMapping).fetchJoin() // teamMappings 연관 관계 fetch join
                .leftJoin(instructorTeamMapping.swmmCchnTeam, coachingTeam).fetchJoin() // swmmCchnTeam 연관 관계 fetch join
                .where(instructor.id.eq(instructorId))
                .fetchOne());
    }

}

