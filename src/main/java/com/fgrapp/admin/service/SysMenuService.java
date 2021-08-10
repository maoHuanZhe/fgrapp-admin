package com.fgrapp.admin.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fgrapp.admin.dao.SysMenuMapper;
import com.fgrapp.admin.dao.SysRoleMapper;
import com.fgrapp.admin.domain.SysMenuDo;
import com.fgrapp.admin.domain.SysRoleDo;
import com.fgrapp.admin.domain.vo.MetaVo;
import com.fgrapp.admin.domain.vo.RouterVo;
import com.fgrapp.admin.domain.vo.TreeSelectVo;
import com.fgrapp.base.constant.Constants;
import com.fgrapp.base.constant.UserConstants;
import com.fgrapp.base.result.exception.ResultException;
import com.fgrapp.base.service.FgrService;
import com.fgrapp.base.utils.FgrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SysMenuService
 *
 * @author fan guang rui
 * @date 2021年07月31日 15:29
 */
@Service
public class SysMenuService extends FgrService<SysMenuMapper, SysMenuDo> {

    @Autowired
    private SysRoleMapper roleMapper;

    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = baseMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StrUtil.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    public List<SysMenuDo> selectMenuTreeByUserId(Long userId) {
        List<SysMenuDo> menus;
        if (FgrUtil.isAdmin(userId)) {
            menus = baseMapper.selectMenuTreeAll();
        } else {
            menus = baseMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenuDo> getChildPerms(List<SysMenuDo> list, int parentId) {
        List<SysMenuDo> returnList = new ArrayList<>();
        for (SysMenuDo t : list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list list
     * @param t t
     */
    private void recursionFn(List<SysMenuDo> list, SysMenuDo t) {
        // 得到子节点列表
        List<SysMenuDo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenuDo tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenuDo> list, SysMenuDo t) {
        return getChildList(list, t).size() > 0;
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenuDo> getChildList(List<SysMenuDo> list, SysMenuDo t) {
        List<SysMenuDo> tlist = new ArrayList<>();
        for (SysMenuDo n : list) {
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    public List<RouterVo> buildMenus(List<SysMenuDo> menus) {
        List<RouterVo> routers = new LinkedList<>();
        for (SysMenuDo menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StrUtil.equals("1", menu.getIsCache())));
            List<SysMenuDo> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StrUtil.equals("1", menu.getIsCache())));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenuDo menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StrUtil.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenuDo menu) {
        String routerPath = menu.getPath();
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenuDo menu) {
        String component = UserConstants.LAYOUT;
        if (StrUtil.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StrUtil.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenuDo menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenuDo menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    public List<SysMenuDo> list(SysMenuDo info, Long userId) {
        List<SysMenuDo> menuList;
        // 管理员显示所有菜单信息
        if (FgrUtil.isAdmin(userId)) {
            menuList = baseMapper.selectMenuList(info);
        } else {
            menuList = baseMapper.selectMenuListByUserId(info, userId);
        }
        return menuList;
    }

    public int delete(Long menuId) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<SysMenuDo>()
                .eq(SysMenuDo::getParentId, menuId));
        if (count > 0) {
            throw new ResultException("存在子菜单,不允许删除");
        }
        int count2 = baseMapper.checkMenuExistRole(menuId);
        if (count2 > 0) {
            throw new ResultException("菜单已分配,不允许删除");
        }
        return baseMapper.deleteById(menuId);
    }

    public int add(SysMenuDo menu) {
        if (checkMenuNameUnique(menu)) {
            throw new ResultException("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            throw new ResultException("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        return baseMapper.insert(menu);
    }

    private boolean checkMenuNameUnique(SysMenuDo menuDo) {
        List<SysMenuDo> sysMenuDos = baseMapper.selectList(new LambdaQueryWrapper<SysMenuDo>()
                .eq(SysMenuDo::getParentId, menuDo.getParentId())
                .eq(SysMenuDo::getMenuName, menuDo.getMenuName()));
        return FgrUtil.checkUnique(menuDo, sysMenuDos);
    }

    public int edit(SysMenuDo menu) {
        if (checkMenuNameUnique(menu)) {
            throw new ResultException("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        }
        if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS)) {
            throw new ResultException("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        if (menu.getId().equals(menu.getParentId())) {
            throw new ResultException("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        return baseMapper.updateById(menu);
    }

    public List<TreeSelectVo> treeselect(SysMenuDo menu) {
        List<SysMenuDo> menus = list(menu, FgrUtil.getUserId());
        List<SysMenuDo> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelectVo::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    public List<SysMenuDo> buildMenuTree(List<SysMenuDo> menus) {
        List<SysMenuDo> returnList = new ArrayList<>();
        List<Long> tempList = new ArrayList<>();
        for (SysMenuDo dept : menus) {
            tempList.add(dept.getId());
        }
        for (SysMenuDo menu : menus) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    public List<Integer> selectMenuListByRoleId(Long roleId) {
        SysRoleDo roleDo = roleMapper.selectById(roleId);
        return baseMapper.selectMenuListByRoleId(roleId,roleDo.getMenuCheckStrictly());
    }
}
