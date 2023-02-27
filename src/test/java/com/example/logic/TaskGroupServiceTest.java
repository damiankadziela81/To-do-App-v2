package com.example.logic;

import com.example.model.ProjectRepository;
import com.example.model.TaskGroup;
import com.example.model.TaskGroupRepository;
import com.example.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when group by given id is not done ")
    void toggleGroup_groupIsNotDone_shouldThrowIllegalStateException() {
        //given
        TaskRepository mockTaskRepository = getTaskRepositoryReturning(true);
        //system to test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        //when
        var exception = catchThrowable(()-> toTest.toggleGroup(1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Finish all the tasks");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when group with given id is not found")
    void toggleGroup_groupIsNotFound_shouldThrowIllegalArgumentException() {
        //given
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskRepository mockTaskRepository = getTaskRepositoryReturning(false);
        //system to test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //when
        var exception = catchThrowable(()-> toTest.toggleGroup(1));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("Should toggle isDone if id is correct and group is not done")
    void toggleGroup_isDoneIsSuccessfullyToggled() {
        //given
        var group = new TaskGroup("Test Group", null);
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        //and
        TaskRepository mockTaskRepository = getTaskRepositoryReturning(false);
        //system to test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //when
        assertThat(group.isDone()).isEqualTo(false);
        toTest.toggleGroup(1);
        //then
        assertThat(group.isDone()).isEqualTo(true);
    }

    private static TaskRepository getTaskRepositoryReturning(final boolean value) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(value);
        return mockTaskRepository;
    }


}