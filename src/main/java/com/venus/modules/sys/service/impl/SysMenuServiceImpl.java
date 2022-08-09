package com.venus.modules.sys.service.impl;

import com.venus.common.base.service.impl.BaseServiceImpl;
import com.venus.common.exception.ErrorCode;
import com.venus.common.exception.VenusException;
import com.venus.common.utils.ConvertUtils;
import com.venus.modules.security.user.UserDetail;
import com.venus.modules.sys.dao.SysMenuDao;
import com.venus.modules.sys.dto.SysMenuDTO;
import com.venus.modules.sys.entity.SysMenuEntity;
import com.venus.modules.sys.enums.SuperAdminEnum;
import com.venus.modules.sys.service.SysMenuService;
import com.venus.modules.sys.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public SysMenuDTO get(Long id) {
        SysMenuEntity entity = baseDao.getById(id);

        SysMenuDTO dto = ConvertUtils.sourceToTarget(entity, SysMenuDTO.class);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //保存菜单
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //上级菜单不能为自身
        if (entity.getId().equals(entity.getPid())) {
            throw new VenusException(ErrorCode.SUPERIOR_MENU_ERROR);
        }

        //更新菜单
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //删除菜单
        deleteById(id);

        //删除角色菜单关系
        sysRoleMenuService.deleteByMenuId(id);
    }

    @Override
    public List<SysMenuDTO> getAllMenuList(Integer type) {
        List<SysMenuEntity> menuList = baseDao.getMenuList(type);

        return ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);
    }

    @Override
    public List<SysMenuDTO> getUserMenuList(UserDetail user, Integer type) {
        List<SysMenuEntity> menuList;

        //系统管理员，拥有最高权限
        if (user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            menuList = baseDao.getMenuList(type);
        } else {
            menuList = baseDao.getUserMenuList(user.getId(), type);
        }

        return ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);
    }

    @Override
    public List<SysMenuDTO> getListPid(Long pid) {
        List<SysMenuEntity> menuList = baseDao.getListPid(pid);

        return ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);
    }

}
