package com.zxcv5595.fund.repository;

import com.zxcv5595.fund.domain.Fund;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FundRepository extends JpaRepository<Fund, Long> {

    @Query(value = "SELECT SUM(amount) FROM fund WHERE project_id = :projectId", nativeQuery = true)
    Optional<Long> findTotalAmountByProjectId(Long projectId);
}
