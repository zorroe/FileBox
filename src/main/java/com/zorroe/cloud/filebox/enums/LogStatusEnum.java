package com.zorroe.cloud.filebox.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogStatusEnum {

    SUCCESS("success", "成功"),

    FAILURE("fail", "失败");

    private final String code;

    private final String label;
}
