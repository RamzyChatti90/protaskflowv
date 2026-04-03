package com.carnival.protaskflowv.service;

import com.carnival.protaskflowv.domain.enumeration.TaskStatus;
import com.carnival.protaskflowv.repository.TaskRepository;
import com.carnival.protaskflowv.security.SecurityUtils;
import com.carnival.protaskflowv.service.dto.TaskDashboardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core