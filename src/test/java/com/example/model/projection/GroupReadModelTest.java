package com.example.model.projection;

import com.example.model.Task;
import com.example.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class GroupReadModelTest {

    @Test
    @DisplayName("should create null deadline for group when tasks have no deadlines ")
    void constructor_noDeadlines_createsNullDeadline() {
        //given
        var source = new TaskGroup();
        source.setDescription("test group");
        source.setTasks(Set.of(new Task("test task",null)));

        //when
        var result = new GroupReadModel(source);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("deadline",null);
    }

}