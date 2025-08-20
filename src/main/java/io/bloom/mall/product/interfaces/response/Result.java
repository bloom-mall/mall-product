package io.bloom.mall.product.interfaces.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.bloom.mall.product.infrastructure.exception.ResultCode;
import lombok.Getter;

/**
 * 通用响应结果类
 *
 * @param <T> 数据类型
 */
@Getter
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Result<T> {
    private final int code;
    private String msg;
    private T data;

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> fail(ResultCode code, String msg) {
        return new Result<>(code.getCode(), msg);
    }

    public static <T> Result<T> fail(ResultCode code) {
        return new Result<>(code.getCode(), code.getMessage());
    }
}