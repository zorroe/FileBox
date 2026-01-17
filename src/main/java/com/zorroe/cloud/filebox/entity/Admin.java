package com.zorroe.cloud.filebox.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("admin")
public class Admin {

    @TableId
    private Long id;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
