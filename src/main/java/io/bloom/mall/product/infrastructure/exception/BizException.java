package io.bloom.mall.product.infrastructure.exception;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private final ResultCode code;
    private final String message;

    public BizException(ResultCode code, String message) {
        super(code.getCode() + message);
        this.code = code;
        this.message = code.getMessage();
    }
}
