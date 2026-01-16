package com.zorroe.cloud.filebox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zorroe.cloud.filebox.entity.File;
import com.zorroe.cloud.filebox.mapper.FileMapper;
import com.zorroe.cloud.filebox.service.FileService;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
}
