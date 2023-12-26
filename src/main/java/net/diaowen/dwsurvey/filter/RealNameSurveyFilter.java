package net.diaowen.dwsurvey.filter;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.base.service.AccountManager;
import net.diaowen.dwsurvey.entity.SurveyDetail;
import net.diaowen.dwsurvey.service.SurveyDetailManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实名问卷过滤器，当用户访问问卷填写地址时，将触发该过滤器，若为实名问卷，当用户未登录时，会跳转至提示页面，提示用户进行登录或者放弃填写该问卷；
 * 若为匿名问卷，则不做相应的限制
 *
 * @version 1.0
 */
@WebFilter(urlPatterns = "/static/diaowen/answer-p.html")
public class RealNameSurveyFilter implements Filter {
    /**
     * 与问卷配置相关业务逻辑的服务组件
     */
    private final SurveyDetailManager surveyDetailManager;
    /**
     * 与用户管理相关的业务逻辑的服务组件
     */
    private final AccountManager accountManager;

    /**
     * 构造函数
     *
     * @param surveyDetailManager 与问卷配置相关业务逻辑的服务组件
     * @param accountManager 与用户管理相关的业务逻辑的服务组件
     */
    public RealNameSurveyFilter(SurveyDetailManager surveyDetailManager, AccountManager accountManager) {
        this.surveyDetailManager = surveyDetailManager;
        this.accountManager = accountManager;
    }

    /**
     * 用于初始化过滤器，在过滤器被创建时被调用
     *
     * @param filterConfig 包含了过滤器的配置信息的对象
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * 过滤HTTP请求的方法，当用户访问问卷填写页面时将触发该方法，该方法将检查该问卷是否为实名问卷，若为实名问卷，则检查用户的登录状态，若用户未
     * 登录，则重定向到提示页面，提示用户登录系统或放弃填写，若用户已登录，则允许用户填写问卷；若为匿名问卷，则允许用户填写问卷
     *
     * @param servletRequest 请求
     * @param servletResponse 响应
     * @param filterChain FilterChain
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        // 获取请求的sid参数
        String sid = httpServletRequest.getParameter("sid");
        // 获取该sid相对应的问卷配置信息
        SurveyDetail surveyDetail = surveyDetailManager.getSurveyDetailBySid(sid);
        Integer isRealName = surveyDetail.getIsRealName(); // 是否为实名问卷标识
        if (isRealName == 1) {
            // 获取当前系统的登录用户
            User user = accountManager.getCurUser();
            if (user != null) { // 用户已登录,允许继续访问问卷填写页面
                filterChain.doFilter(servletRequest, servletResponse);
            } else { // 用户未登录,重定向到提示页面
                httpServletResponse.sendRedirect("/#/avoid");
            }
        } else { // 匿名问卷,允许用户访问问卷填写页面
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 过滤器销毁时被调用，用于释放资源或进行一些清理操作
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
