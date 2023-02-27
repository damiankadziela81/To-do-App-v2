package com.example.logic;

import com.example.TaskConfiguration;
import com.example.model.*;
import com.example.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("Should throw IllegalStateException when configured to allow 1 group and still pending group exists")
    void createGroup_noMultipleGroupConfig_and_notFinishedTasksInGroupRemain_shouldThrowIllegalStateException() {
        //given
        TaskGroupRepository mockTaskGroupRepository = getTaskGroupRepositoryReturning(true);
        //and
        TaskConfiguration mockTaskConfiguration = getTaskConfigurationReturning(false);
        //system under test
        var toTest = new ProjectService(null, mockTaskGroupRepository, mockTaskConfiguration);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one pending group");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when configured ok and no projects for given id")
    void createGroup_configOk_and_noProjects_shouldThrowIllegalArgumentException() {
        //given
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskConfiguration mockTaskConfiguration = getTaskConfigurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockProjectRepository, null, mockTaskConfiguration);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when configured to allow 1 group and no groups and no projects for given id")
    void createGroup_noMultipleGroupConfig_and_noPendingProjectsExists_shouldThrowIllegalArgumentException() {
        //given
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt())).thenReturn(Optional.empty());
        //and
        TaskGroupRepository mockTaskGroupRepository = getTaskGroupRepositoryReturning(false);
        //and
        TaskConfiguration mockTaskConfiguration = getTaskConfigurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockProjectRepository, mockTaskGroupRepository, mockTaskConfiguration);

        //when
        var exception = catchThrowable(() -> toTest.createGroup(0, LocalDateTime.now()));
        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup(){
        //given
        var today = LocalDate.now().atStartOfDay();
        //and
        var project = projectWith("test project",Set.of(-1,-2));
        var mockProjectRepository = mock(ProjectRepository.class);
        when(mockProjectRepository.findById(anyInt()))
                .thenReturn(Optional.of(project));
        //and
        InMemoryTaskGroupRepository inMemoryTaskGroupRepo = inMemoryTaskGroupRepository();
        int countBeforeCall = inMemoryTaskGroupRepository().count();
        //and
        TaskConfiguration mockTaskConfiguration = getTaskConfigurationReturning(true);
        //system under test
        var toTest = new ProjectService(mockProjectRepository, inMemoryTaskGroupRepo, mockTaskConfiguration);

        //when
        GroupReadModel result = toTest.createGroup(1, today);

        //then
        assertThat(result.getDescription()).isEqualTo("test project");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("temp"));

        assertThat(countBeforeCall + 1).isEqualTo(inMemoryTaskGroupRepo.count());
    }

    private TaskGroupRepository getTaskGroupRepositoryReturning(final boolean result) {
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockTaskGroupRepository;
    }

    private TaskConfiguration getTaskConfigurationReturning(final boolean result) {
        var mockTemplate = mock(TaskConfiguration.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockTaskConfiguration = mock(TaskConfiguration.class);
        when(mockTaskConfiguration.getTemplate()).thenReturn(mockTemplate);
        return mockTaskConfiguration;
    }

    private Project projectWith (String projectDescription, Set<Integer> daysToDeadline) {
        Set<ProjectStep> steps = daysToDeadline.stream()
                .map(days -> {
                    var step = mock(ProjectStep.class);
                    when(step.getDescription()).thenReturn("temp");
                    when(step.getDaysToDeadline()).thenReturn(days);
                    return step;
                })
                .collect(Collectors.toSet());

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private InMemoryTaskGroupRepository inMemoryTaskGroupRepository() {
        return new InMemoryTaskGroupRepository();
    }

    private static class InMemoryTaskGroupRepository implements TaskGroupRepository {
        private int keyIndex = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count() {
            return map.values().size();
        }
        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(final Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(final TaskGroup entity) {
            if(entity.getId()==0) {
                //reflection so to not break encapsulation of the production code
                try {
                    var field = TaskGroup.class.getDeclaredField("id");
                    field.setAccessible(true);
                    field.set(entity, ++keyIndex);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            map.put(entity.getId(), entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(final Integer projectId) {
            return map.values()
                    .stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }

    }
}