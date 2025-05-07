package com.poten.dive_in.lesson.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 수영 클래스 검색 및 필터링 조건 DTO (기존 구조 유지)
@Getter
@Setter // @ModelAttribute 바인딩을 위해 Setter 필요
@NoArgsConstructor
@AllArgsConstructor
public class SwimClassSearchCondition {

    private String className; // 수업 이름 검색어
    private String level; // 수업 레벨 필터링 (CommonCode CD 값)
    private Integer minPrice; // 최소 가격 필터링
    private Integer maxPrice; // 최대 가격 필터링
    private String keyword; // 키워드 검색어 (수업 키워드 코드 이름 또는 수업 이름에서 검색)
    private Long poolId; // 수영장 ID 필터링
    private Long instructorTeamId; // 코칭 팀 ID 필터링

    // 필요에 따라 다른 검색/필터링 조건 필드 추가
}
