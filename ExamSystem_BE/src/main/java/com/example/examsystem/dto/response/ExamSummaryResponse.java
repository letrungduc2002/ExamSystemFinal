package com.example.examsystem.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;

/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang chuyen doi du lieu
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExamSummaryResponse {
    String title;
    List<PartSummaryDTO> parts;
}