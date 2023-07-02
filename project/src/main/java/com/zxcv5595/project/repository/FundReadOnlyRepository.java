package com.zxcv5595.project.repository;

import com.zxcv5595.common.repository.ReadOnlyRepository;
import com.zxcv5595.project.domain.Fund;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface FundReadOnlyRepository extends ReadOnlyRepository<Fund,Long> {

    @Query(value = "SELECT SUM(amount) FROM fund WHERE project_id = :projectId", nativeQuery = true)
    Optional<Long> findTotalAmountByProjectId(Long projectId);
}
