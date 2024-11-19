package com.emobile.springtodo.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseEntity implements Serializable {

    @Schema(description = "Дата и время создания записи", example = "2024-11-16T12:34:56")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время последнего обновления записи", example = "2024-11-16T15:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "Версия записи для обеспечения оптимистической блокировки", example = "1")
    private Long version;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = 0L;
    }

}
