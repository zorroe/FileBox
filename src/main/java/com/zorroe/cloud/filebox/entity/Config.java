package com.zorroe.cloud.filebox.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("configurations")
public class Config {

    @TableId
    private Long id;

    @TableField("key")
    private String key;

    @TableField("value")
    private String value;

    @TableField("description")
    private String description;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
