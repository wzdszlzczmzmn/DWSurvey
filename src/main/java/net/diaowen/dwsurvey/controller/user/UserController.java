package net.diaowen.dwsurvey.controller.user;

import net.diaowen.common.base.entity.User;
import net.diaowen.common.base.service.AccountManager;
import net.diaowen.common.plugs.httpclient.HttpResult;
import net.diaowen.common.plugs.security.filter.FormAuthenticationWithLockFilter;
import net.diaowen.dwsurvey.service.UserManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

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
     * 与账户管理相关的业务逻辑的服务组件
     */
    private final AccountManager accountManager;
    /**
     * 表单身份验证过滤器
     */
    private final FormAuthenticationWithLockFilter formAuthFilter;
    /**
     * 与用户管理相关的业务逻辑的服务组件
     */
    private final UserManager userManager;
    /**
     * 日志
     */
    private final Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController(AccountManager accountManager, FormAuthenticationWithLockFilter formAuthFilter, UserManager userManager) {
        this.accountManager = accountManager;
        this.formAuthFilter = formAuthFilter;
        this.userManager = userManager;
    }

    /**
     * 获取当前登录的用户对象
     *
     * @return 获取是否成功的结果
     */
    @RequestMapping("/currentUser.do")
    @ResponseBody
    public HttpResult currentUser() throws Exception {
        User user = accountManager.getCurUser();
        return HttpResult.SUCCESS(user);
    }

    /**
     * 用户修改个人信息
     *
     * @param request HTTP
     * @return 修改是否成功的结果
     */
    @RequestMapping("/up-info.do")
    @ResponseBody
    public HttpResult save(HttpServletRequest request) throws Exception {
        User user = accountManager.getCurUser();

        String birthString = request.getParameter("birth");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // 获取修改后的用户邮箱
            String email = request.getParameter("email");
            // 获取修改后的用户电话号码
            String phone = request.getParameter("phone");

            //判断邮箱重复且不会与自己重复
            if (userManager.findEmailUn(user.getId(), email) != null) {
                return HttpResult.FAILURE_MSG("该邮箱已被使用");
            }

            // 判断修改的电话号码是否唯一
            if (userManager.findCellPhoneUn(user.getId(), phone) != null){
                return HttpResult.FAILURE("该电话号码已被使用");
            }

            // 获取修改后用户的生日信息
            Date birthday = dateFormat.parse(birthString);
            // 获取修改后用户的性别信息
            String sexString = request.getParameter("sex");
            // 设置新的性别信息
            if ("男".equals(sexString)) {
                user.setSex(0);
            } else if ("女".equals(sexString)) {
                user.setSex(1);
            }
            user.setBirthday(birthday); // 设置新的生日
            user.setEmail(email); // 设置新邮箱
            user.setCellphone(phone); // 设置新电话号码
            // 更新用户信息
            accountManager.saveUp(user);
            return HttpResult.SUCCESS();
        } catch (ParseException e) {
            // 处理日期格式不正确的异常情况
            logger.warning(e.getMessage());
            return HttpResult.FAILURE(HttpResult.FAILURE_MSG("日期错误"));
        }
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
