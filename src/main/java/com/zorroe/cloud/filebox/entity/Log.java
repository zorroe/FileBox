package com.zorroe.cloud.filebox.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Log {

    @TableId
    private Long id;

    @TableField("ip")
    private String ip;

    @TableField("action")
    private String action;

    @TableField("status")
    private String status; // success/fail

    @TableField("timestamp")
    private Date timestamp;

    @TableField("details")
    private String details;
}