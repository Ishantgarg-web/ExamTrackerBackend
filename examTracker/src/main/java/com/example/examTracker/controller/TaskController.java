package com.example.examTracker.controller;

import com.example.examTracker.dto.CreateTaskDTO;
import com.example.examTracker.entity.Exam;
import com.example.examTracker.entity.Task;
import com.example.examTracker.exceptions.TaskAlreadyExistException;
import com.example.examTracker.exceptions.WrongExamCodeException;
import com.example.examTracker.service.ExamService;
import com.example.examTracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    ExamService examService;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDTO createTaskDTO) {
        // check given {task_title, exam_id} already exist or not.
        Task taskExists = taskService.findByTaskTitleAndExamId(createTaskDTO.getTaskTitle(), createTaskDTO.getExamId());
        if (taskExists != null) {
            throw new TaskAlreadyExistException("Task Already Exist for given Exam");
        }
        Exam exam = examService.getExamByExamId(createTaskDTO.getExamId());
        Task task = Task.builder()
                .dailyTarget(createTaskDTO.getDailyTarget())
                .taskTitle(createTaskDTO.getTaskTitle())
                .exam(exam)
                .build();
        taskService.saveTask(task);
        return ResponseEntity.ok("Task created successfuly for exam code: "+exam.getExamCode());
    }

}
