package com.example.examsystem.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang chuyen doi du lieu
 */

@Getter
@Setter
@Builder
public class SubmitPartResponse {
    private boolean isFinished; // Báo cho Frontend biết đã hết toàn bộ bài thi chưa
    private Long nextPartId;    // Nếu chưa hết, trả về ID của phần tiếp theo
}