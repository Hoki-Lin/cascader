package com.hoki.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 二级菜单节点
 *
 * @author Hoki_Lin
 * @date 2023-08-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CascaderNode {
    String value;

    String label;

    List<CascaderNode> children;
}
