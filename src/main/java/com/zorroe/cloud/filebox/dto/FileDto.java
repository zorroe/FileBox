package com.zorroe.cloud.filebox.dto;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FileDto {
    private String name;

    private String type; // file/text

    private Long size;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date expireTime;

    private Integer downloadCount = 0;

    private Integer maxDownloadCount = 0;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date updateTime;

    private String status; // active/expired/deleted

    private String directLink;

    @JsonProperty("sizeStr")
    public String getFormattedSize() {
        return formatFileSize(size);
    }

    // 格式化工具方法
    public static String formatFileSize(Long bytes) {
        if (bytes == null || bytes <= 0) {
            return "0B";
        }

        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double size = bytes.doubleValue();

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        // 保留一位小数（如果是整数则不显示 .0）
        if (size == (long) size) {
            return String.format("%d%s", (long) size, units[unitIndex]);
        } else {
            return String.format("%.1f%s", size, units[unitIndex]);
        }
    }
}
