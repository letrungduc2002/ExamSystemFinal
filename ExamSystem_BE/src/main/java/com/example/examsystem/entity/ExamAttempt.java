package com.example.examsystem.entity;


import com.example.examsystem.enums.AttemptStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;




/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Mapping bằng kĩ thuật ORM với các bảng ơr trong DB
 */

@Entity
@Table(name = "exam_attempts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "start_time", nullable = false)
    LocalDateTime startTime;

    @Column(name = "submit_time")
    LocalDateTime submitTime;

    @Column(name = "total_score")
    Double totalScore;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AttemptStatus status;

    // Tham chiếu đến sinh viên đang làm bài
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    // Tham chiếu đến bài thi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    Exam exam;

    // Tham chiếu đến phần thi đang làm dở (Hỗ trợ HSA/TSA)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_part_id")
    ExamPart currentPart;


    @OneToMany(mappedBy = "attempt", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    List<StudentResponse> responses = new ArrayList<>();
}
