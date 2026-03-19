package com.example.examsystem.repository;

import com.example.examsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Xư lý thao tác lấy dữ liệu ở DB
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
