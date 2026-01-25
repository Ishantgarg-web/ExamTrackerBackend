package com.example.examTracker.repository;

import com.example.examTracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, String> {

    @Query(
            value = "SELECT * FROM tasks t WHERE t.task_title = :taskTitle AND t.exam_id = :examId",
            nativeQuery = true
    )
    Task findByTaskTitleAndExamId(@Param("taskTitle") String taskTitle, @Param("examId") String examId);
}
