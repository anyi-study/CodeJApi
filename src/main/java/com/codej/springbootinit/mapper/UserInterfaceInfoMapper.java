package com.codej.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codej.codejapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author laoluo
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-08-07 00:28:19
* @Entity generator.domain.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




