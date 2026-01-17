package com.zorroe.cloud.filebox.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileStatusEnum {

    ACTIVE("active", "正常"),

    EXPIRED("expired", "已过期"),

    DELETED("deleted", "已删除");

    private final String code;

    private final String label;
}
