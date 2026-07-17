package com.spring.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 将前端发送的 *.do 请求 URL 后缀去掉，使其匹配后端的 @RequestMapping
 *
 * 前端 api.js 中所有接口都使用 .do 后缀（如 jingdianxinxi_list.do），
 * 但后端 Controller 的 @RequestMapping 路径不包含 .do（如 /jingdianxinxi_list）。
 * Spring Boot 2.7 默认 use-suffix-pattern=false，不会自动匹配 .do 后缀的路径。
 * 此 Filter 在请求到达 DispatcherServlet 之前将 .do 后缀移除。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DoSuffixFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        if (uri != null && uri.endsWith(".do")) {
            // 用包装器把 .do 后缀去掉，Spring MVC 就能匹配到对应的 handler
            request = new DoStrippingRequestWrapper(request);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * 包装 HttpServletRequest，移除请求 URI 末尾的 .do 后缀
     */
    private static class DoStrippingRequestWrapper extends HttpServletRequestWrapper {

        private final String requestURI;
        private final String servletPath;
        private final String contextPath;

        public DoStrippingRequestWrapper(HttpServletRequest request) {
            super(request);
            String originalURI = request.getRequestURI();
            // 去掉末尾 .do
            this.requestURI = originalURI.substring(0, originalURI.length() - 3);

            String originalServletPath = request.getServletPath();
            this.servletPath = originalServletPath != null && originalServletPath.endsWith(".do")
                    ? originalServletPath.substring(0, originalServletPath.length() - 3)
                    : originalServletPath;

            String originalContextPath = request.getContextPath();
            this.contextPath = originalContextPath != null ? originalContextPath : "";
        }

        @Override
        public String getRequestURI() {
            return requestURI;
        }

        @Override
        public String getServletPath() {
            return servletPath;
        }

        @Override
        public String getContextPath() {
            return contextPath;
        }

        @Override
        public StringBuffer getRequestURL() {
            StringBuffer url = new StringBuffer();
            url.append(getScheme()).append("://").append(getServerName());
            if (getServerPort() != 80 && getServerPort() != 443) {
                url.append(":").append(getServerPort());
            }
            url.append(requestURI);
            return url;
        }
    }
}
