package com.poten.dive_in.instructor.entity;

import com.poten.dive_in.academy.entity.Academy;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "swmm_instr")
public class Instructor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instr_id")
    private Long id;

    private Long memberId;

    @Column(name= "field")
    private String instructorName;

    //선수출신여부
    @Column(name= "field2")
    private String isFormerAthlete;

    //수상경력
    @Column(name = "field3")
    private String awards;

    @Column(name = "field4")
    private String certificate;

    @Column(name = "field5")
    private String instructorInfo;

    private String useYn;
//    @ManyToOne
//    @JoinColumn(name = "academy_id" )
//    private Academy academy;
}

//public class Instructor extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "instructor_id")
//    private Long id;
//
//    private String instructorName;
//
//    private String instructorInfo;
//
//    @ManyToOne
//    @JoinColumn(name = "academy_id" )
//    private Academy academy;
//}
