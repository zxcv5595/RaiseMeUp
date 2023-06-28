package com.zxcv5595.project.controller;

import com.zxcv5595.project.dto.RegisterProject;
import com.zxcv5595.project.dto.UpdateProject;
import com.zxcv5595.project.service.ProjectService;
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
@RequestMapping("/v1/api/project")
public class ProjectController {

    private static final String MEMBER_ID_HEADER = "X-memberId";
    private final ProjectService projectService;

    @PostMapping("/register")
    public ResponseEntity<String> registerProject(
            @RequestHeader(MEMBER_ID_HEADER) long memberId,
            @Valid @RequestBody RegisterProject.Request request) {
        projectService.registerProject(memberId, request);

        return ResponseEntity.ok("프로젝트 등록에 성공하였습니다.");
    }


    @PostMapping("/update")
    public ResponseEntity<String> updateProject(
            @RequestHeader(MEMBER_ID_HEADER) long memberId,
            @Valid @RequestBody UpdateProject.Request request
    ) {
        projectService.updateProject(memberId, request);

        return ResponseEntity.ok("프로젝트 업데이트에 성공하였습니다.");
    }
}
