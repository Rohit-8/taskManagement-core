package com.taskManagement.task_service.repository;

import com.taskManagement.task_service.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT t FROM Task t ORDER BY " +
            "CASE t.priority " +
            "WHEN 'HIGH' THEN 1 " +
            "WHEN 'MEDIUM' THEN 2 " +
            "WHEN 'LOW' THEN 3 " +
            "ELSE 4 END")
    Page<Task> findAllOrderByPriorityAsc(Pageable pageable);

    @Query("SELECT t FROM Task t ORDER BY " +
            "CASE t.priority " +
            "WHEN 'LOW' THEN 1 " +
            "WHEN 'MEDIUM' THEN 2 " +
            "WHEN 'HIGH' THEN 3 " +
            "ELSE 4 END")
    Page<Task> findAllOrderByPriorityDesc(Pageable pageable);

}
