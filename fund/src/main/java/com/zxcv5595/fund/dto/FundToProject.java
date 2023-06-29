package com.zxcv5595.fund.dto;

import com.zxcv5595.fund.domain.Fund;
import com.zxcv5595.fund.type.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class FundToProject {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        @NotNull(message = "projectId는 필수입니다.")
        private Long projectId;

        @NotNull(message = "amount는 필수입니다.")
        @Min(value = 1000, message = "amount는 1000 이상이어야 합니다.")
        private Long amount;

        @Enumerated(EnumType.STRING)
        private PaymentStatus paymentStatus;

        public static Fund toEntity(Request request){
            return Fund.builder()
                    .projectId(request.getProjectId())
                    .amount(request.getAmount())
                    .paymentStatus(request.getPaymentStatus())
                    .build();
        }
    }
}
