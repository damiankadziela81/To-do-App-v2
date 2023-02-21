package com.example.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource
public interface TaskRepository extends JpaRepository<Task,Integer> {

    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);

    @Override
    @RestResource(exported = false)
    void delete(Task entity);

    //DSL - Domain Specific Language (usage of Spring Data QueryDSL)
    @RestResource(path = "done", rel = "done")
    List<Task> findByDone(@Param("state") boolean done);
    //localhost:8080/tasks/search/done?state=true
    //localhost:8080/tasks/search/done?state=false

}
