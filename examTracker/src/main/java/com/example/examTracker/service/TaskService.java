package com.example.examTracker.service;


import com.example.examTracker.entity.Task;
import com.example.examTracker.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    public Task findByTaskTitleAndExamId(String taskTitle, String examId) {
        return taskRepository.findByTaskTitleAndExamId(taskTitle, examId);
    }
}
