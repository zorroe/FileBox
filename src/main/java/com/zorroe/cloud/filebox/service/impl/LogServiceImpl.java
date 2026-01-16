package com.zorroe.cloud.filebox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zorroe.cloud.filebox.entity.Log;
import com.zorroe.cloud.filebox.mapper.LogMapper;
import com.zorroe.cloud.filebox.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
}
