package com.carnival.protaskflowv.service;

import com.carnival.protaskflowv.domain.enumeration.TaskStatus;
import com.carnival.protaskflowv.repository.TaskRepository;
import com.carnival.protaskflowv.security.SecurityUtils;
import com.carnival.protaskflowv.service.dto.TaskDashboardDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Task.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final SecurityUtils securityUtils;

    public TaskService(TaskRepository taskRepository, SecurityUtils securityUtils) {
        this.taskRepository = taskRepository;
        this.securityUtils = securityUtils;
    }

    /**
     * Get the task counts for the current authenticated user for the dashboard.
     *
     * @return a {@link TaskDashboardDTO} containing the counts of tasks by status.
     */
    public TaskDashboardDTO getTaskDashboardCountsForCurrentUser() {
        log.debug("Request to get task dashboard counts for current user");

        Optional<Long> currentUserId = securityUtils.getCurrentUserId();

        if (currentUserId.isEmpty()) {
            log.warn("No current user ID found. Returning empty dashboard counts.");
            // In a secured context, this should ideally not happen.
            // Returning a DTO with zero counts or throwing an Unauthorized exception
            // depends on specific error handling strategy. Zero counts is safer for a dashboard.
            return new TaskDashboardDTO(0L, 0L, 0L);
        }

        Long userId = currentUserId.get();

        Long toDoCount = taskRepository.countByAssignedToIdAndStatus(userId, TaskStatus.TO_DO);
        Long inProgressCount = taskRepository.countByAssignedToIdAndStatus(userId, TaskStatus.IN_PROGRESS);
        Long completedCount = taskRepository.countByAssignedToIdAndStatus(userId, TaskStatus.COMPLETED);

        return new TaskDashboardDTO(toDoCount, inProgressCount, completedCount);
    }
}