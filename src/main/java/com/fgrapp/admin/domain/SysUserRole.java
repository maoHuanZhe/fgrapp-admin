package com.fgrapp.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SysUserRole
 *
 * @author fan guang rui
 * @date 2021年08月07日 14:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRole {
    /** 用户ID */
    private Long userId;

    /** 角色ID */
    private Long roleId;
}
