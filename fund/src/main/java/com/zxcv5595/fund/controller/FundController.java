package com.zxcv5595.fund.controller;

import com.zxcv5595.fund.dto.FundToProject.Request;
import com.zxcv5595.fund.service.FundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/fund")
public class FundController {

    private static final String MEMBER_ID_HEADER = "X-memberId";
    private final FundService fundService;

    @PostMapping("")
    public ResponseEntity<String> fundToProject(
            @RequestHeader(MEMBER_ID_HEADER) long memberId,
            @Valid @RequestBody Request request
    ) {
        fundService.fundToProject(memberId, request);

        return ResponseEntity.ok("후원이 완료 되었습니다.");
    }

}
