package com.zxcv5595.fund.service;

import com.zxcv5595.fund.domain.Fund;
import com.zxcv5595.fund.repository.projection.project.ProjectionEndDate;
import com.zxcv5595.fund.dto.FundToProject;
import com.zxcv5595.fund.dto.FundToProject.Request;
import com.zxcv5595.fund.repository.projection.project.ProjectionMemberId;
import com.zxcv5595.fund.exception.CustomException;
import com.zxcv5595.fund.repository.FundRepository;
import com.zxcv5595.fund.repository.ProjectReadOnlyRepository;
import com.zxcv5595.fund.type.ErrorCode;
import com.zxcv5595.fund.type.FundStatus;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FundService {

    private static final String REDIS_PREFIX = "project:";
    private final FundRepository fundRepository;
    private final ProjectReadOnlyRepository projectReadOnlyRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    @Transactional
    public void fundToProject(
            long memberId,
            FundToProject.Request request
    ) {

        validatePermission(memberId, request);

        Fund newFund = Request.toEntity(request);

        newFund.setStatus(FundStatus.SUCCESS);
        newFund.setMemberId(memberId);

        fundRepository.save(newFund);

        updateAccumulatedAmountInRedis(request);
    }

    private void validatePermission(long memberId, Request request) {

        //프로젝트 작성자, 자신은 스스로 후원할 수 없습니다.
        ProjectionMemberId writer = projectReadOnlyRepository.findById(request.getProjectId(), ProjectionMemberId.class)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT));

        Long writerId = writer.getMemberId();

        if (writerId == memberId) {
            throw new CustomException(ErrorCode.INVALID_PERMISSION);
        }
    }

    private void updateAccumulatedAmountInRedis(Request request) {
        // Redis에 저장 및 누적
        String key = REDIS_PREFIX + request.getProjectId(); // Redis 키
        Long amount = request.getAmount(); // 증가시킬 amount

        ValueOperations<String, Long> valueOps = redisTemplate.opsForValue();
        Long accumulatedAmount = valueOps.increment(key, amount); // 값 누적 (원자적으로 처리)

        if (Objects.equals(accumulatedAmount, amount)) {
            // Redis에 값이 없는 경우 초기값, 만료기간 설정
            ProjectionEndDate projectionEndDate = projectReadOnlyRepository.findById(
                            request.getProjectId(), ProjectionEndDate.class)
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROJECT));

            Duration expired = Duration.between(LocalDateTime.now(),
                    projectionEndDate.getEndDate().plusDays(2).atStartOfDay());
            valueOps.set(key, accumulatedAmount, expired);
        }

        log.info(String.valueOf(valueOps.get(key)));
    }

}
