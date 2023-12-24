package net.diaowen.dwsurvey.controller.user;

import net.diaowen.common.base.controller.LoginRegisterResult;
import net.diaowen.common.base.entity.User;
import net.diaowen.common.base.service.AccountManager;
import net.diaowen.common.plugs.httpclient.HttpResult;
import net.diaowen.common.plugs.security.filter.FormAuthenticationWithLockFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @Autowired
    private FormAuthenticationWithLockFilter formAuthFilter;

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
//            String name = request.getParameter("userName");
            Date birthday = dateFormat.parse(birthString);
            // 在这里执行对日期的操作，比如保存到数据库或者进行其他业务逻辑
            String sexString = request.getParameter("sex");
            Integer sex = 1;
            if ("男".equals(sexString)) {
                sex = 0;
            } else if ("女".equals(sexString)) {
                sex = 1;
            } else {
                // 处理其他情况，可能性有限制性别参数
            }
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            //判断邮箱重复且不会与自己重复
            if (accountManager.findUserByEmail(email) != null && !accountManager.findUserByEmail(email).getId().equals(user.getId())) {
                return HttpResult.FAILURE_MSG("邮箱重复");
            }

            user.setBirthday(birthday);
            user.setSex(sex);
            user.setEmail(email);
            user.setCellphone(phone);
            // 更新用户信息
            accountManager.saveUp(user);
            return HttpResult.SUCCESS();

        } catch (ParseException e) {
            // 处理日期格式不正确的异常情况
            e.printStackTrace(); // 或者记录日志等其他操作
        }

        return HttpResult.FAILURE(HttpResult.FAILURE_MSG("日期错误"));
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
