package com.zxcv5595.project.batch;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.repository.FundReadOnlyRepository;
import com.zxcv5595.project.repository.ProjectRepository;
import com.zxcv5595.project.type.ProjectStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectStatusReader implements ItemReader<List<Project>> {

    private static final String REDIS_PREFIX = "project:";
    private final ProjectRepository projectRepository;
    private final FundReadOnlyRepository fundReadOnlyRepository;
    private final RedisTemplate<String, Long> redisTemplate;
    private boolean executed = false;

    @Override
    public List<Project> read() {
        if (executed) {
            return null; // 이미 실행되었으므로 중지
        }

        List<Project> projects = projectRepository.findByEndDate(LocalDate.now());
        if (projects.isEmpty()) {
            setExecuted(true);
            return null;
        }

        List<Project> failureList = updateProjectStatus(projects);
        projectRepository.saveAll(projects);

        setExecuted(true);
        return failureList;
    }

    private List<Project> updateProjectStatus(List<Project> projects) {
        List<Project> failureList = new ArrayList<>();

        Map<String, Long> projectAmounts = getProjectAmounts(projects);

        for (Project project : projects) {
            Long accumulatedAmount = projectAmounts.get(getRedisKey(project.getId()));
            if (project.getGoalAmount() <= accumulatedAmount) {
                project.setStatus(ProjectStatus.SUCCESS);
            } else {
                project.setStatus(ProjectStatus.FAILURE);
                failureList.add(project);
            }
        }

        return failureList;
    }

    private Map<String, Long> getProjectAmounts(List<Project> projects) {
        List<String> redisKeys = projects.stream()
                .map(project -> getRedisKey(project.getId()))
                .collect(Collectors.toList());

        List<Long> amounts = redisTemplate.opsForValue().multiGet(redisKeys);

        Map<String, Long> projectAmounts = new HashMap<>();
        for (int i = 0; i < redisKeys.size(); i++) {
            String redisKey = redisKeys.get(i);
            Long accumulatedAmount = Objects.requireNonNull(amounts).get(i);
            if (accumulatedAmount == null) {
                // Redis 값이 만료되어 없을 경우, 데이터베이스에서 가져옴 없으면 총 후원금 0원
                accumulatedAmount = fundReadOnlyRepository.findTotalAmountByProjectId(
                        projects.get(i).getId()).orElse(0L);
            }
            projectAmounts.put(redisKey, accumulatedAmount);
        }

        return projectAmounts;
    }

    private String getRedisKey(Long projectId) {
        return REDIS_PREFIX + projectId;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}