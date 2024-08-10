package com.codej.codejapicommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.codej.codejapicommon.model.entity.UserInterfaceInfo;


/**
 * 调用统计接口
 */
public interface InnerUserInterfaceInfoService {
    boolean invokeCount(long interfaceInfoId,long userId);

}
