package com.codej.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codej.codejapicommon.model.entity.UserInterfaceInfo;

/**
* @author laoluo
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-08-07 00:28:19
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    boolean invokeCount(long interfaceInfoId ,long userId);
}
