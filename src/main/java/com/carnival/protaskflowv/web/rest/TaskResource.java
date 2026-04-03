package com.carnival.protaskflowv.web.rest;

import com.carnival.protaskflowv.domain.enumeration.TaskStatus;
import com.carnival.protaskflowv.security.SecurityUtils;
import com.carnival.protaskflowv.service.TaskService;
import com.carnival.protaskflowv.web.rest.errors.BadRequestAlertException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link com.carnival.protaskflowv.domain.Task}.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    private final TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * {@code GET /tasks/dashboard-counts} : Get the task counts for the current user's dashboard.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the body containing the task counts.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the current user cannot be determined or is not authenticated.
     */
    @GetMapping("/tasks/dashboard-counts")
    public ResponseEntity<Map<String, Long>> getTaskDashboardCountsForCurrentUser() {
        log.debug("REST request to get task dashboard counts for current user");

        if (!SecurityUtils.isAuthenticated()) {
            throw new BadRequestAlertException("Current user is not authenticated", ENTITY_NAME, "unauthenticatedUser");
        }

        Map<TaskStatus, Long> rawCounts = taskService.getTaskCountsForCurrentUser();

        // Convert TaskStatus enum keys to String for better JSON serialization
        Map<String, Long> formattedCounts = new HashMap<>();
        rawCounts.forEach((status, count) -> formattedCounts.put(status.name(), count));

        return ResponseEntity.ok().body(formattedCounts);
    }
}