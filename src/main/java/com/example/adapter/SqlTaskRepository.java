package com.example.adapter;

import com.example.model.Task;
import com.example.model.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface SqlTaskRepository extends TaskRepository, JpaRepository<Task,Integer> {
    @Override
//    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=?1")
//    boolean existsById(Integer id);
    @Query(nativeQuery = true, value = "select count(*) > 0 from tasks where id=:id")
    boolean existsById(@Param("id") Integer id);

    @Override
    boolean existByDoneIsFalseAndGroup_Id(Integer groupId);
}