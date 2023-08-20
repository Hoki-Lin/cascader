package com.hoki.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付平台枚举类
 *
 * @author Hoki_Lin
 * @date 2023-08-20
 */
@Getter
@AllArgsConstructor
public enum Pay_PLATFORM_TYPE {
    WE_CHAT("1", "微信"),
    ALI_PAY("2", "支付宝"),
    TEST_PAY("3", "测试支付"),
    IOS_PAY("4", "苹果支付");

    private final String status;
    private final String desc;

}
