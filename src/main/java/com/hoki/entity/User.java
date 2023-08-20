package com.hoki.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 *
 * @author Hoki_Lin
 * @date 2023-08-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    String userId;

    String nickName;

    String type;
}
