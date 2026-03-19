package com.example.examsystem.pattern.factory;

import com.example.examsystem.enums.ExamType;
import com.example.examsystem.pattern.strategy.ExamExecutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


/*
 * Name: Le Trung Duc - Nhom 4
 * Date: 18-03-2026
 * Function: Áp dụng Factory Method Pattern và nguyên lý Inversion of Control (IoC) của Spring Framework.
 *            Đây là bộ định tuyến (Router) trung tâm giúp điều phối request đến đúng Strategy.

 */



@Component
public class ExamStrategyFactory {

    private final Map<ExamType, ExamExecutionStrategy> strategyMap;

    @Autowired
    public ExamStrategyFactory(List<ExamExecutionStrategy> strategies) {
        strategyMap = new EnumMap<>(ExamType.class);
        for (ExamExecutionStrategy strategy : strategies) {
            for (ExamType type : strategy.getSupportedTypes()) {
                strategyMap.put(type, strategy);
            }
        }
    }

    public ExamExecutionStrategy getStrategy(ExamType examType) {
        ExamExecutionStrategy strategy = strategyMap.get(examType);
        if (strategy == null) {
            throw new IllegalArgumentException("Chưa có chiến lược thi cho loại đề: " + examType);
        }
        return strategy;
    }
}