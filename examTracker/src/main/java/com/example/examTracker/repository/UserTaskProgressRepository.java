package com.example.examTracker.repository;

import com.example.examTracker.entity.Task;
import com.example.examTracker.entity.UserTaskProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface UserTaskProgressRepository extends JpaRepository<UserTaskProgress, String> {

    @Query(
            value = "SELECT * FROM user_task_progress utp WHERE utp.user_id = :userId " +
                    "AND utp.task_task_id = :taskId AND" +
                    " utp.completed_at = :completedDate",
            nativeQuery = true
    )
    UserTaskProgress findByUserIdTaskIdCompletedAt(@Param("userId") String userId,
                                       @Param("taskId") String taskId, @Param("completedDate") LocalDate completedDate);
}
