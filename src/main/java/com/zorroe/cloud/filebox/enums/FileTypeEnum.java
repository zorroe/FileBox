package com.zorroe.cloud.filebox.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileTypeEnum {

    FILE("file"),
    TEXT("text");

    private final String code;

}
