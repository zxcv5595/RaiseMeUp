package com.zxcv5595.fund.domain;

import com.zxcv5595.fund.type.FundStatus;
import com.zxcv5595.fund.type.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Fund extends BaseEntity {

    private Long memberId;
    private Long projectId;
    private Long amount;

    @Enumerated(EnumType.STRING)
    private FundStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


}
