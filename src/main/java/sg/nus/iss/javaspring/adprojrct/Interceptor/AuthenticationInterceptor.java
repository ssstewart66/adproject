package sg.nus.iss.javaspring.adprojrct.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import sg.nus.iss.javaspring.adprojrct.Models.User;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // 用户已登录
            return true;
        }

        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/login") || requestURI.equals("/api/register") || requestURI.equals("/policy")) {
            return true;
        }

        // 判断是否是 AJAX 请求
        String ajaxHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(ajaxHeader)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }

        response.sendRedirect("/login");
        return false;
    }
}
