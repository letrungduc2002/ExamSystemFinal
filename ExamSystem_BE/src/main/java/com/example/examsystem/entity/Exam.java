package com.example.examsystem.entity;

import com.example.examsystem.enums.ExamType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Mapping bằng kĩ thuật ORM với các bảng ơr trong DB
 */

@Table(name = "exams")
@Entity
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "exam_type",nullable = false)
    @Enumerated(EnumType.STRING)
    ExamType examType;

    @Column(name = "total_duration")
    Long totalDuration;

    @Column(name = "total_max_score")
    Double totalMaxScore;

    @Column(name = "created_at")
    LocalDate createdAt;

    @Column(name = "difficulty", nullable = false)
    Long difficulty;

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE})
    List<ExamPart> examParts = new ArrayList<>();

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     List<ExamAttempt> examAttempts = new ArrayList<>();
}
