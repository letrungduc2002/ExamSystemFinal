package com.example.examsystem.repository;

import com.example.examsystem.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
public interface OptionRepository extends JpaRepository<Option, Long> {
}
