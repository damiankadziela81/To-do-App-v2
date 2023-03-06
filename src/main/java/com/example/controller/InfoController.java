package com.example.controller;

import com.example.TaskConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/info")
class InfoController {

    private DataSourceProperties dataSource;
    private TaskConfiguration myProperty;

    InfoController(final DataSourceProperties dataSource, final TaskConfiguration myProperty) {
        this.dataSource = dataSource;
        this.myProperty = myProperty;
    }

    @GetMapping("/url")
    String url(){
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean myProperty(){
        return myProperty.getTemplate().isAllowMultipleTasks();
    }
}
