package com.fgrapp.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SysRoleMenu
 *
 * @author fan guang rui
 * @date 2021年08月07日 20:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleMenu {
    /** 角色ID */
    private Long roleId;

    /** 菜单ID */
    private Long menuId;
}
