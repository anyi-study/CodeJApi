package com.codej.codejapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.codej.codejapicommon.model.entity.User;

/**
 * 用户服务
 */
public interface InnerUserService {

 /**
  * 数据库中是否已分配密钥给用户
  * @param accessKey
  * @return
  */
 User getInvokeUser(String accessKey);
}
