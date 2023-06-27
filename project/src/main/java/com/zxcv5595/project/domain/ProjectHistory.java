package com.zxcv5595.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "project_history")
public class ProjectHistory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project projectId;
    private String description;

    private boolean failedMessage;
}
