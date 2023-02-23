package com.example.logic;

import com.example.model.TaskGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class TempService {

    @Autowired
    List<String> temp(TaskGroupRepository repository){
        //FIXME: N+1 Selects
        return repository.findAll()
                .stream()
                .flatMap(taskGroup -> taskGroup.getTasks().stream())
                .map(task -> task.getDescription())
                .collect(Collectors.toList());
    }
}
