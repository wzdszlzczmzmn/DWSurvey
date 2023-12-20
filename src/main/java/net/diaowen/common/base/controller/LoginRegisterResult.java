package net.diaowen.common.base.controller;

import net.diaowen.common.plugs.httpclient.HttpResult;
import net.sf.json.JSON;
/**
 * LoginRegisterResult类表示登录和注册的结果。
 */
public class LoginRegisterResult {

    private String status;//表示登录和注册的状态
    private String type;//表示登录和注册的类型
    private String[] currentAuthority;//当前用户权限
    private HttpResult httpResult;//表示http请求的结果
    private static final String TYPEA = "account";
    /**
     * 获取登录和注册的状态
     * @return status
     */
    public String getStatus() {
        return status;
    }
    /**
     * 设置登录和注册的状态
     * @param status 状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * 获取登录和注册的类型
     * @return type
     */
    public String getType() {
        return type;
    }
    /**
     * 设置登录和注册的类型
     * @param type 类型
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * 获取当前用户权限
     * @return currentAuthority
     */
    public String[] getCurrentAuthority() {
        return currentAuthority;
    }
    /**
     * 设置当前用户权限
     * @param  currentAuthority 当前用户权限
     */
    public void setCurrentAuthority(String[] currentAuthority) {
        this.currentAuthority = currentAuthority;
    }
    /**
     * 获取http请求的结果
     * @return httpResult
     */
    public HttpResult getHttpResult() {
        return httpResult;
    }
    /**
     * 设置http请求的结果
     * @param httpResult 请求结果
     */
    public void setHttpResult(HttpResult httpResult) {
        this.httpResult = httpResult;
    }
    /**
     * 此方法使用给定的状态和类型创建LoginRegisterResult对象。
     * @param status LoginRegisterResult对象的状态。
     * @param type LoginRegisterResult对象的类型。
     * @return 具有给定状态和类型的LoginRegisterResult对象。
     */
    public static LoginRegisterResult RESULT(String status,String type){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus(status);
        loginResult.setType(type);
        loginResult.setCurrentAuthority(new String[]{});
        return loginResult;
    }
    /**
     * 此方法使用给定的currentAuthority创建LoginRegisterResult对象。
     * @param currentAuthority LoginRegisterResult对象的currentAuthority。
     * @return 具有给定currentAuthority的LoginRegisterResult对象。
     */
    public static LoginRegisterResult SUCCESS(String currentAuthority){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("ok");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(new String[]{currentAuthority});
        return loginResult;
    }
    /**
     * 此方法使用给定的currentAuthority数组创建LoginRegisterResult对象。
     * @param currentAuthority LoginRegisterResult对象的currentAuthority数组。
     * @return 具有给定currentAuthority数组的LoginRegisterResult对象。
     */
    public static LoginRegisterResult SUCCESS(String[] currentAuthority){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("ok");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(currentAuthority);
        return loginResult;
    }
    /**
     * 此方法创建具有默认状态、类型和currentAuthority的LoginRegisterResult对象。
     * @return 具有默认状态、类型和currentAuthority的LoginRegisterResult对象。
     */
    public static LoginRegisterResult FAILURE(){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("error");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(new String[]{"guest"});
        return loginResult;
    }
    /**
     * 此方法使用给定的HttpResult创建LoginRegisterResult对象。
     * @param httpResult LoginRegisterResult对象的HttpResult。
     * @return 具有给定HttpResult的LoginRegisterResult对象。
     */
    public static LoginRegisterResult FAILURE(HttpResult httpResult){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("error");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(new String[]{"guest"});
        loginResult.setHttpResult(httpResult);
        return loginResult;
    }

}
