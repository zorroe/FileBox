package com.zorroe.cloud.filebox.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zorroe.cloud.filebox.entity.Admin;
import com.zorroe.cloud.filebox.mapper.AdminMapper;
import com.zorroe.cloud.filebox.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
