package com.carnival.protaskflowv.repository;

import com.carnival.protaskflowv.domain.Task;
import com.carnival.protaskflowv.domain.enumeration.TaskStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link Task} entity.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    /**
     * Counts the number of tasks assigned to a specific user with a given status.
     * This method is used to populate the dashboard with task counts.
     *
     * @param assignedToId The ID of the user to whom the tasks are assigned.
     * @param status The status of the tasks (e.g., TO_DO, IN_PROGRESS, COMPLETED).
     * @return The count of tasks matching the criteria.
     */
    long countByAssignedToIdAndStatus(Long assignedToId, TaskStatus status);
}