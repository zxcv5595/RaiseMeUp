package com.zxcv5595.fund.domain;

import com.zxcv5595.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project extends BaseEntity {

    private Long memberId;
    private LocalDate endDate;
}
