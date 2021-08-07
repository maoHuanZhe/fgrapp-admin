package com.fgrapp.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fgrapp.admin.domain.SysMenuDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TreeSelectVo
 *
 * @author fan guang rui
 * @date 2021年08月07日 20:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeSelectVo {
    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectVo> children;
    public TreeSelectVo(SysMenuDo menu){
        this.id = menu.getId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelectVo::new).collect(Collectors.toList());
    }
}
