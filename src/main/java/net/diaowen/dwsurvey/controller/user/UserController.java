package net.diaowen.dwsurvey.controller.user;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.base.service.AccountManager;
import net.diaowen.common.plugs.httpclient.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录用户个人信息管理的Controller,实现用户个人信息的修改和密码的修改
 *
 * @author KeYuan(keyuan258@gmail.com)
 *
 * https://github.com/wkeyuan/DWSurvey
 * http://dwsurvey.net
 *
 */
@Controller
@RequestMapping("/api/dwsurvey/app/user")
public class UserController {
    /**
     * 与用户管理相关的业务逻辑的服务组件
     */
    @Autowired
    private AccountManager accountManager;

    /**
     * 获取当前登录的用户对象
     *
     * @return 获取是否成功的结果
     */
    @RequestMapping("/currentUser.do")
    @ResponseBody
    public HttpResult currentUser() throws Exception {
        User user=accountManager.getCurUser();
        return HttpResult.SUCCESS(user);
    }

    /**
     * 用户修改个人用户名和头像
     *
     * @param request HTTP
     * @param name 新用户名
     * @param avatar 新头像
     * @return 修改是否成功的结果
     */
    @RequestMapping("/up-info.do")
    @ResponseBody
    public HttpResult save(HttpServletRequest request,String name,String avatar) throws Exception {
        User user=accountManager.getCurUser();
        user.setName(name);
        user.setAvatar(avatar);
        accountManager.saveUp(user);
        return HttpResult.SUCCESS();
    }

    /**
     * 用户修改个人信息
     *
     * @param curpwd 当前密码
     * @param pwd 新密码
     * @return 修改是否成功的结果
     */
    @RequestMapping("/up-pwd.do")
    @ResponseBody
    public HttpResult updatePwd(String curpwd,String pwd) throws Exception {
        System.out.println("curpwd:"+curpwd);
        boolean isOk = accountManager.updatePwd(curpwd,pwd);
        if(isOk){
            return HttpResult.SUCCESS();
        }
        return HttpResult.FAILURE();
    }
}
