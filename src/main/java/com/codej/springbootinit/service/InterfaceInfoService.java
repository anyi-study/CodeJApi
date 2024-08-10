package com.codej.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codej.codejapicommon.model.entity.InterfaceInfo;

/**
* @author laoluo
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-08-04 20:17:14
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
