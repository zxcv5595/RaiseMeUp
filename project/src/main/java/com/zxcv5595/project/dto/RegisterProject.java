package com.zxcv5595.project.dto;

import com.zxcv5595.project.domain.Project;
import com.zxcv5595.project.dto.validation.CustomDateRangeValidation;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

public class RegisterProject {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @CustomDateRangeValidation(message = "Start date must be before end date and they must not be null")
    public static class Request {

        @NotEmpty(message = "Title cannot be empty")
        private String title;

        @NotEmpty(message = "Description cannot be empty")
        private String description;

        @NotNull(message = "Goal amount cannot be null")
        @PositiveOrZero(message = "Goal amount must be a positive or zero value")
        private Long goalAmount;

        @FutureOrPresent(message = "Start date must not be before current date")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;

        public static Project toEntity(Request request) {
            return Project.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .goalAmount(request.getGoalAmount())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .build();
        }

    }


}
