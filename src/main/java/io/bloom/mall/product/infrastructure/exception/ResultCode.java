package io.bloom.mall.product.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    /*
     * 响应码格式
     * AA BB CC
     * AA: 模块编号(20代表通用)
     * BB: 错误分类
     * CC: 具体错误序号
     * */
    // 通用响应码(200000-2099999)
    SUCCESS(200000, "操作成功"),
    ILLEGAL_ARGUMENT(200101, "参数不合法"),
    METHOD_NOT_ALLOWED(200102, "请求方法不允许"),
    SYSTEM_ERROR(200201, "系统错误"),
    CONNECT_TIME_OUT(200301, "系统超时"),
    NO_OPERATE_PERMISSION(200401, "无操作权限"),
    RESOURCE_NOT_FOUND(200501, "资源不存在"),
    SUB_RESOURCE_BLOCKING_DELETION(200502, "子资源存在，不允许删除");

    // 产品模块响应码(300000-309999)

    private final int code;
    private final String message;
}
