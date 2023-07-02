package com.zxcv5595.project.domain;

import com.zxcv5595.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "failed_message")
public class FailedMessage extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project projectId;
    boolean failure;
}
