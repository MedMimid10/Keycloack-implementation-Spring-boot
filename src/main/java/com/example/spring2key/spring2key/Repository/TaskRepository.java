package com.example.spring2key.spring2key.Repository;

import com.example.spring2key.spring2key.Entity.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TodoTask,Long> {
    List<TodoTask> findAllByUsername(String username);
}
