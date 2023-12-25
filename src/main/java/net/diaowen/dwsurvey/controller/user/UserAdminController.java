package net.diaowen.dwsurvey.controller.user;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.plugs.httpclient.HttpResult;
import net.diaowen.common.plugs.httpclient.PageResult;
import net.diaowen.common.plugs.httpclient.ResultUtils;
import net.diaowen.common.plugs.page.Page;
import net.diaowen.common.plugs.security.filter.FormAuthenticationWithLockFilter;
import net.diaowen.dwsurvey.config.DWSurveyConfig;
import net.diaowen.dwsurvey.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.logging.Logger;

/**
 * 用户管理的Controller,包括用户的新增、修改、分页查询、批量删除的功能
 */
@Controller
@RequestMapping("/api/dwsurvey/admin/user")
public class UserAdminController {
    /**
     * 用户管理相关业务逻辑的服务组件
     */
    @Autowired
    private UserManager userManager;
    /**
     * 处理表单认证并实现用户登录功能的过滤器
     */
    @Autowired
    private FormAuthenticationWithLockFilter formAuthFilter;

    /**
     * 日志
     */
    private final Logger logger = Logger.getLogger(UserAdminController.class.getName());

    /**
     * 获取用户列表
     *
     * @param pageResult 分页查询的结果
     * @param status 用户状态码
     * @param loginName 用户登录名
     * @param name 姓名
     * @param email 用户邮箱
     * @param cellphone 用户电话号码
     * @return 分页查询的结果
     */
    @RequestMapping(value = "/list.do",method = RequestMethod.GET)
    @ResponseBody
    public PageResult<User> list(PageResult<User> pageResult,Integer status, String loginName, String name, String email,String cellphone) {
        Page page = ResultUtils.getPageByPageResult(pageResult);
        // 分页查询用户列表
        page = userManager.findPage(page, status, loginName, name, email, cellphone);
        pageResult = ResultUtils.getPageResultByPage(page,pageResult);
        return pageResult;
    }

    /**
     * 管理员添加新用户
     *
     * @param user 新用户对象
     * @return 添加是否成功的结果
     */
    @RequestMapping(value = "/add.do",method = RequestMethod.POST)
    @ResponseBody
    public HttpResult add(@RequestBody User user) {
        try{
            if("demo".equals(DWSurveyConfig.DWSURVEY_SITE)) return HttpResult.FAILURE("DEMO环境不允许操作");
            // 添加用户
            User result = userManager.adminSave(user);
            if(result!=null) return HttpResult.SUCCESS();
        }catch (Exception e){
            logger.warning(e.getMessage());
        }
        return HttpResult.FAILURE();
    }

    /**
     * 管理员修改用户信息
     *
     * @param user 包含用户新信息的用户对象
     * @return 修改用户信息是否成功的结果
     */
    @RequestMapping(value = "/edit.do",method = RequestMethod.PUT)
    @ResponseBody
    public HttpResult up(@RequestBody User user) {
        try{
            if("demo".equals(DWSurveyConfig.DWSURVEY_SITE)) return HttpResult.FAILURE("DEMO环境不允许操作");
            // 修改用户信息
            HttpResult httpResult = userManager.upData(user);
            return httpResult;
        }catch (Exception e){
            logger.warning(e.getMessage());
            return HttpResult.FAILURE(e.getMessage());
        }
    }

    /**
     * 批量删除用户
     *
     * @return 批量删除是否成功的结果
     */
    @RequestMapping(value = "/delete.do",method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delete(@RequestBody Map<String, String[]> map) {
        try{
            if("demo".equals(DWSurveyConfig.DWSURVEY_SITE)) return HttpResult.FAILURE("DEMO环境不允许操作");
            if(map!=null){
                if(map.containsKey("id")){
                    String[] ids = map.get("id");
                    if(ids!=null){
                        // 批量删除用户
                        userManager.deleteData(ids);
                    }
                }
            }
            return HttpResult.SUCCESS();
        }catch (Exception e){
            logger.warning(e.getMessage());
        }
        return HttpResult.FAILURE();
    }


    /**
     * 重置用户登录
     *
     * @param user User对象
     * @return 重置是否成功的结果
     */
    @RequestMapping(value = "/reset-login.do",method = RequestMethod.PUT)
    @ResponseBody
    public HttpResult resetLogin(@RequestBody User user) {
        try{
            formAuthFilter.resetAccountLock(user.getLoginName());
            return HttpResult.SUCCESS();
        }catch (Exception e){
            logger.warning(e.getMessage());
            return HttpResult.FAILURE(e.getMessage());
        }
    }
}
