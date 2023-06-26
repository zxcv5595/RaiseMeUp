package com.zxcv5595.project.dto.validation;

import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.dto.RegisterProject.Request;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CustomDateRangeValidator implements
        ConstraintValidator<CustomDateRangeValidation, Request> {
    @Override
    public boolean isValid(RegisterProject.Request request, ConstraintValidatorContext context) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        return start != null && end != null && start.isBefore(end);
    }
}
