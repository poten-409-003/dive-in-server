package com.poten.dive_in.instructor.repository;

import com.poten.dive_in.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
