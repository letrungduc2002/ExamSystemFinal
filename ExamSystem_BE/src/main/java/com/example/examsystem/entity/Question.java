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


@Table(name = "questions")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "score_weight", nullable = false)
    Double scoreWeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    ExamPart examPart;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY,
            cascade = {CascadeType.REMOVE})
    List<Option> options = new ArrayList<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    List<StudentResponse> studentResponses = new ArrayList<>();
}
