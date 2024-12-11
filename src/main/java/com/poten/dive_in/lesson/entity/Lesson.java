package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.cmmncode.entity.CmmnCd;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.instructor.entity.InstrTeamMpng;
import com.poten.dive_in.pool.entity.Pool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter @Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "swmm_cls")
public class Lesson extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cchn_team_id")
    @Column(name = "Key")
    private InstrTeamMpng mappingTeam; //수영 코칭 팀 ID

    @ManyToOne
    @JoinColumn(name = "pool_id")
    private Pool pool;

    private String lessonName;

    @ManyToOne
    @JoinColumn(name = "cd_id")
    private CmmnCd levelCd;

    private Integer nmbrOfPepl; //인원수
    private Integer prc; //가격
    private String oprtHr; //운영시간
    private String sbjc; //주제
    private String intr; //소개
    private Integer chcCnt; //조회수
    private String useYn; //사용여부

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonImage> imageList;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonInstructor> lessonInstructorList;

    @OneToMany(mappedBy = "lesson")
    private List<LessonKeyword> lessonKeywordList;


    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonApplyChannel> applyChannelList;
//
//    @OneToMany(mappedBy = "lesson")
//    private List<SwmmClsAplyQlfc> swmmClsAplyQlfcList;
//
//    @OneToMany(mappedBy = "lesson")
//    private List<SwmmClsRfnd> swmmClsRfndList;


    public void addImage(List<LessonImage> lessonImageList) {
        this.imageList = lessonImageList;
    }

    public void assignInstrTeam(InstrTeamMpng instrTeamMpng){
        this.mappingTeam = instrTeamMpng;
    }

    public void assignPool(Pool pool){
        this.pool = pool;
    }

    public void assignInstructors(List<LessonInstructor> lessonInstructorList) {
        this.lessonInstructorList.clear();
        this.lessonInstructorList.addAll(lessonInstructorList);
    }

    public void addImages(List<LessonImage> lessonImageList) {
        this.imageList.clear();
        this.imageList.addAll(lessonImageList);
    }

    public void assignApplyChannels(List<LessonApplyChannel> applyChannels) {
        this.applyChannelList.clear();
        this.applyChannelList.addAll(applyChannels);
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
