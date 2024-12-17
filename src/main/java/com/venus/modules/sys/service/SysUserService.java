package com.venus.modules.sys.service;

import com.venus.common.page.PageData;
import com.venus.common.base.service.BaseService;
import com.venus.modules.sys.dto.SysUserDTO;
import com.venus.modules.sys.entity.SysUserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SysUserService extends BaseService<SysUserEntity> {

    PageData<SysUserDTO> page(Map<String, Object> params);

    List<SysUserDTO> list(Map<String, Object> params);

    SysUserDTO get(Long id);

    SysUserDTO getByUsername(String username);

    SysUserDTO getByAccount(String account);

    Integer getByEmail(String email);

    Integer getByMobile(String mobile);

    void save(SysUserDTO dto);

    void update(SysUserDTO dto);

    void delete(Long[] ids);

    void deleteBatchIds(Long[] ids);

    /**
     * 修改密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     */
    void updatePassword(Long id, String newPassword);

    /**
     * 根据部门ID，查询用户数
     */
    int countByDeptId(Long deptId);

    /**
     * 根据部门ID,查询用户Id列表
     */
    List<Long> getUserIdListByDeptId(List<Long> deptIdList);

    PageData<SysUserDTO> getListByRoleId(Map<String, Object> params);

    List<SysUserEntity> importExcel(MultipartFile file) throws Exception;
}
