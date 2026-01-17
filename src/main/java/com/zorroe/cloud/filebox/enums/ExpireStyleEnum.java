package com.zorroe.cloud.filebox.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExpireStyleEnum {

    MINUTES("minutes", "分钟"),
    HOURS("hours", "小时"),
    DAYS("days", "天"),
    WEEKS("weeks", "周"),
    MONTHS("months", "月"),
    YEARS("years", "年");

    private final String code;

    private final String label;
}
