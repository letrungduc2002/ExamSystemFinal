package com.example.examsystem.dto.request;

import lombok.Getter;
import lombok.Setter;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang Nhan Du Lieu Tu Client
 */

@Getter
@Setter
public class SubmitPartRequest {
    private Long currentPartId; // Frontend gửi lên ID của phần thi vừa làm xong
}
