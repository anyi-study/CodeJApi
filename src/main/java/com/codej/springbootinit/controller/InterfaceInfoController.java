package com.codej.springbootinit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.codej.codejapiclientsdk.client.CodeJApiClient;
import com.codej.codejapicommon.model.entity.InterfaceInfo;
import com.codej.codejapicommon.model.entity.User;
import com.codej.springbootinit.annotation.AuthCheck;
import com.codej.springbootinit.common.*;
import com.codej.springbootinit.constant.CommonConstant;
import com.codej.springbootinit.exception.BusinessException;
import com.codej.springbootinit.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.codej.springbootinit.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.codej.springbootinit.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.codej.springbootinit.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.codej.springbootinit.model.enums.InterfaceInfoStatusEnum;
import com.codej.springbootinit.service.InterfaceInfoService;
import com.codej.springbootinit.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * 
 * 
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserService userService;
    @Resource
    private CodeJApiClient codeJApiClient;
    // region 增删改查
    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }
    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }
    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                     HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }
    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }
    /**
     * 获取列表（仅管理员可使用）
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }
    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }
    // endregion
    /**
     * 发布
     *
     * @param idRequest  id
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
//        如果id是null或者是0，则抛出异常
        if (idRequest == null || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        校验接口是否存在
//        获取idRequest 对象的id值
        Long id = idRequest.getId();
//        根据id查询接口
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
//        如果查询为空
        if (oldInterfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
//        判断接口是否可以调用
//        先模拟一下调用
        com.codej.codejapiclientsdk.model.User user = new com.codej.codejapiclientsdk.model.User();
        user.setUsername("test");
//        通过client调用
        String username = codeJApiClient.getUserNameByPost(user);
//        如果username为空或者空字符串
        if (StringUtils.isBlank(username)){
//            抛出异常接口验证失败
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口验证失败");
        }
//        创建InterfaceInfo对象
        InterfaceInfo interfaceInfo = new InterfaceInfo();
//        设置InterfaceInfo的id属性值为id
        interfaceInfo.setId(id);
//        修改接口数据库的状态字段为1
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
//        调用InterfaceInfoService的updateById接口，传入interfaceInfo对象，并返回结果给result
        boolean result = interfaceInfoService.updateById(interfaceInfo);
//        返回成功响应,附上result值
        return ResultUtils.success(result);
    }
    /**
     * 下线
     *
     * @param idRequest id
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest,
                                                     HttpServletRequest request) {
//        如果id是null或者是0，则抛出异常
        if (idRequest == null || idRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        校验接口是否存在
//        获取idRequest 对象的id值
        Long id = idRequest.getId();
//        根据id查询接口
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
//        如果查询为空
        if (oldInterfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

//        创建InterfaceInfo对象
        InterfaceInfo interfaceInfo = new InterfaceInfo();
//        设置InterfaceInfo的id属性值为id
        interfaceInfo.setId(id);
//        修改接口数据库的状态字段为1
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
//        调用InterfaceInfoService的updateById接口，传入interfaceInfo对象，并返回结果给result
        boolean result = interfaceInfoService.updateById(interfaceInfo);
//        返回成功响应,附上result值
        return ResultUtils.success(result);
    }

    /**
     * 测试调用
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                      HttpServletRequest request) {
//        如果检查id是null或者是0，则抛出异常
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//        校验接口是否存在
//        获取idRequest 对象的id值
        Long id = interfaceInfoInvokeRequest.getId();
//        获取用户请求参数
        String userRequestParams = interfaceInfoInvokeRequest.getUserRequestParams();

//      检查是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
//        检查接口状态是否为下线状态
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已关闭");
        }
//        获取当前用户的ak与sk，用自己身份去调用
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
//        测试调用
        Gson gson = new Gson();
//        将用户请求参数转换成user对象
        com.codej.codejapiclientsdk.model.User user = gson.fromJson(userRequestParams, com.codej.codejapiclientsdk.model.User.class);
//        调用client种的getUserNameByPost ，传入用户对象，获取用户名
        String userNameByPost = codeJApiClient.getUserNameByPost(user);
//        返回成功响应,即调用结果
        return ResultUtils.success(userNameByPost);
    }
}