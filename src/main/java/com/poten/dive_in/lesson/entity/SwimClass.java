package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.cmmncode.entity.CommonCode;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.instructor.dto.LessonInstructorResponseDto;
import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.instructor.entity.InstructorTeamMapping;
import com.poten.dive_in.pool.entity.Pool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedEntityGraph(
        name = "SwimClass.detail",
        attributeNodes = {
                @NamedAttributeNode("instructorTeam"),
                @NamedAttributeNode("pool"),
                @NamedAttributeNode("images"),
                @NamedAttributeNode("qualifications"),
                @NamedAttributeNode("refunds"),
                @NamedAttributeNode("keywords"),
                @NamedAttributeNode("applicationMethods")
        }
)
@Table(name = "swmm_cls")
public class SwimClass extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cls_id")
    private Long classId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Key", referencedColumnName = "cchn_team_id")
    private CoachingTeam instructorTeam; // 수영 코칭 팀 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pool_id")
    private Pool pool; // 수영장

    @Column(name = "lesson_name")
    private String name; // 수업 이름

    @Column(name = "level_cd")
    private String level; // 수업 레벨

    @Column(name = "nmbr_of_pepl")
    private Integer participantCount; // 인원수

    @Column(name = "prc")
    private Integer price; // 가격

    @Column(name = "oprt_hr")
    private String operatingHours; // 운영시간

    @Column(name = "sbjc")
    private String subject; // 주제

    @Column(name = "intr")
    private String introduction; // 소개

    @Column(name = "chc_cnt")
    private Integer viewCount; // 조회수

    @Column(name = "use_yn")
    private String isActive; // 사용 여부

    @OneToMany(mappedBy = "swimClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SwimClassImage> images;

    @OneToMany(mappedBy = "swimClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicationQualification> qualifications; // 수업 자격 리스트

    @OneToMany(mappedBy = "swimClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RefundPolicy> refunds; // 환불 리스트

    // 삭제: 강사 리스트를 관리하는 LessonInstructor를 제거하고, InstructorTeamMapping을 사용
    // @OneToMany(mappedBy = "swimClass", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<LessonInstructor> instructors = new ArrayList<>();

    @OneToMany(mappedBy = "swimClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LessonKeyword> keywords;

    @OneToMany(mappedBy = "swimClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicationMethod> applicationMethods;

    public void addImage(List<SwimClassImage> imageList) {
        for (SwimClassImage image : imageList) {
            images.add(image);
            image.assignSwimClass(this); // 이미지와 수업 간의 관계 설정
        }
    }

    // 삭제: LessonInstructor는 InstructorTeamMapping으로 대체되므로 삭제
    // public void addInstructor(LessonInstructor instructor) {
    //     instructors.add(instructor);
    //     instructor.assignSwimClass(this);
    // }

    public void assignPool(Pool pool) {
        this.pool = pool;
    }

    public void assignCoachingTeam(CoachingTeam team) {
        this.instructorTeam = team;
    }

    public void addImages(List<SwimClassImage> lessonImageList) {
        this.images.clear();
        this.images.addAll(lessonImageList);
    }

    public void assignApplyChannels(List<ApplicationMethod> applicationMethodList) {
        this.applicationMethods.clear();
        this.applicationMethods.addAll(applicationMethodList);
        for (ApplicationMethod channel : applicationMethodList) {
            channel.assignSwimClass(this); // 신청 채널과 수업 간의 관계 설정
        }
    }

    public void assignQualifications(List<ApplicationQualification> qualificationList) {
        this.qualifications.clear(); // 기존 자격 삭제
        this.qualifications.addAll(qualificationList); // 새로운 자격 추가
        for (ApplicationQualification qualification : qualificationList) {
            qualification.assignSwimClass(this); // 자격과 수업 간의 관계 설정
        }
    }

    public void assignRefunds(List<RefundPolicy> refundList) {
        this.refunds.clear(); // 기존 환불 정보 삭제
        this.refunds.addAll(refundList); // 새로운 환불 추가
        for (RefundPolicy refund : refundList) {
            refund.assignSwimClass(this); // 환불과 수업 간의 관계 설정
        }
    }

    public List<LessonInstructorResponseDto> getInstructorList() {
        List<LessonInstructorResponseDto> instructorDtos = new ArrayList<>();
        if (instructorTeam != null && instructorTeam.getInstructorTeamMappings() != null) {
            for (InstructorTeamMapping mapping : instructorTeam.getInstructorTeamMappings()) {
                Instructor instructor = mapping.getInstructor();
                if (instructor != null) {
                    instructorDtos.add(LessonInstructorResponseDto.ofEntity(instructor));
                }
            }
        }
        return instructorDtos;
    }

    public String getKeyword() {

        String keywords = null;
        if (this.keywords != null && this.keywords.size() != 0) {
            List<String> keywordList = new ArrayList<>();
            for (LessonKeyword keyword : this.keywords) {
                keywordList.add(keyword.getKeyword().getCodeName());
            }
            keywords = keywordList.toString();
        }
        return keywords;
    }
}
//public class Lesson extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "lesson_id")
//    private Long id;
//
//    private String lessonName;
//
//    private String level;
//
//    private String capacity;
//
//    private String price;
//
//    private String keyword;
//
//    @Column(length = 3000)
//    private String lessonDetail;
//
//    private String lessonSchedule;
//
//    @Enumerated(EnumType.STRING)
//    private LessonStatus lessonStatus;
//
//    @ManyToOne
//    @JoinColumn(name = "academy_id")
//    private Academy academy;
//
//    @ManyToOne
//    @JoinColumn(name = "pool_id")
//    private Pool pool;
//
//    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<LessonImage> imageList;
//
//    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<LessonInstructor> lessonInstructorList;
//
//    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<LessonApplyChannel> applyChannelList;
//
//
//    public void addImage(List<LessonImage> lessonImageList) {
//        this.imageList = lessonImageList;
//    }
//
//    public void assignInstrTeam(Academy academy){
//        this.academy = academy;
//    }
//
//    public void assignPool(Pool pool){
//        this.pool = pool;
//    }
//
//    public void assignInstructors(List<LessonInstructor> lessonInstructorList) {
//        this.lessonInstr+uctorList.clear();
//        this.lessonInstructorList.addAll(lessonInstructorList);
//    }
//
//    public void addImages(List<LessonImage> lessonImageList) {
//        this.imageList.clear();
//        this.imageList.addAll(lessonImageList);
//    }
//
//    public void assignApplyChannels(List<LessonApplyChannel> applyChannels) {
//        this.applyChannelList.clear();
//        this.applyChannelList.addAll(applyChannels);
//    }
//
//
//
//}
