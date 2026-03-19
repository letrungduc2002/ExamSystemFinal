package com.example.examsystem.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Mapping bằng kĩ thuật ORM với các bảng ơr trong DB
 */



@Table(name = "student_responses", uniqueConstraints = {
        // Đảm bảo 1 lượt thi, 1 câu hỏi chỉ có 1 đáp án (Phục vụ UPSERT)
        @UniqueConstraint(columnNames = {"attempt_id", "question_id"})
})
@Entity
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    ExamAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    Option selectedOption;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    // Auto update thời gian mỗi khi Entity bị thay đổi
    @PreUpdate
    public void setPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
