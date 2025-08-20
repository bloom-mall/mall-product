package io.bloom.mall.product.infrastructure.util;

import lombok.experimental.UtilityClass;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

import com.alibaba.cloud.commons.lang.StringUtils;

@UtilityClass
public class RequestUtil {

    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(requestAttributes).getRequest();
    }

    /**
     * 请求信息，含uri和请求参数
     * 示例：GET:/api/getUser?id=1
     */
    public String getRequestInfo() {
        HttpServletRequest request = getRequest();
        String queryString = request.getQueryString();
        queryString = (StringUtils.isEmpty(queryString) || "null".equals(queryString)) ? "" : "?" + queryString;
        return request.getMethod() + ":" + request.getRequestURI() + queryString;
    }
}
