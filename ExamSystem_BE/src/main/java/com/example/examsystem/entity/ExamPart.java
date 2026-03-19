package com.example.examsystem.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;



/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Mapping bằng kĩ thuật ORM với các bảng ơr trong DB
 */

@Table(name = "exam_parts")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    Exam exam;

    @OneToMany(mappedBy = "examPart", fetch = FetchType.LAZY
            , cascade = {CascadeType.REMOVE})
    List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "currentPart", fetch = FetchType.LAZY)
    List<ExamAttempt> currentAttempts = new ArrayList<>();
}
