package com.zorroe.cloud.filebox.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zorroe.cloud.filebox.entity.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<File> {
}
