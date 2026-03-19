package com.example.examsystem.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Thuc Hien Chuc Nang chuyen doi du lieu
 */


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OptionResponse {
    Long id;
    String content;
}