package com.zorroe.cloud.filebox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zorroe.cloud.filebox.entity.Config;
import com.zorroe.cloud.filebox.mapper.ConfigMapper;
import com.zorroe.cloud.filebox.service.ConfigService;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
}
