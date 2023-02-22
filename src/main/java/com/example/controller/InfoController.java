package com.example.controller;

import com.example.TaskConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
class InfoController {

    private DataSourceProperties dataSource;
    private TaskConfiguration myProperty;

    InfoController(final DataSourceProperties dataSource, final TaskConfiguration myProperty) {
        this.dataSource = dataSource;
        this.myProperty = myProperty;
    }

    @GetMapping("/info/url")
    String url(){
        return dataSource.getUrl();
    }

    @GetMapping("/info/prop")
    boolean myProperty(){
        return myProperty.isAllowMultipleTasksFromTemplate();
    }
}
