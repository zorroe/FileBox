package com.zorroe.cloud.filebox.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("files")
public class File {

    @TableId
    private Long id;

    @TableField("code")
    private String code;

    @TableField("name")
    private String name;

    @TableField("type")
    private String type; // file/text

    @TableField("size")
    private Long size;

    @TableField("storage_path")
    private String storagePath;

    @TableField("expire_time")
    private Date expireTime;

    @TableField("download_count")
    private Integer downloadCount = 0;

    @TableField("max_download_count")
    private Integer maxDownloadCount = 0;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("status")
    private String status; // active/expired/deleted
}
