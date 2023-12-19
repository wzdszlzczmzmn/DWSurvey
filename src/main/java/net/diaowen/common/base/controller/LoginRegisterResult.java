package net.diaowen.common.base.controller;

import net.diaowen.common.plugs.httpclient.HttpResult;
import net.sf.json.JSON;

public class LoginRegisterResult {

    private String status;//表示登录和注册的状态
    private String type;//表示登录和注册的类型
    private String[] currentAuthority;//当前用户权限
    private HttpResult httpResult;//表示http请求的结果
    private static final String TYPEA = "account";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getCurrentAuthority() {
        return currentAuthority;
    }

    public void setCurrentAuthority(String[] currentAuthority) {
        this.currentAuthority = currentAuthority;
    }

    public HttpResult getHttpResult() {
        return httpResult;
    }

    public void setHttpResult(HttpResult httpResult) {
        this.httpResult = httpResult;
    }

    public static LoginRegisterResult RESULT(String status,String type){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus(status);
        loginResult.setType(type);
        loginResult.setCurrentAuthority(new String[]{});
        return loginResult;
    }

    public static LoginRegisterResult SUCCESS(String currentAuthority){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("ok");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(new String[]{currentAuthority});
        return loginResult;
    }

    public static LoginRegisterResult SUCCESS(String[] currentAuthority){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("ok");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(currentAuthority);
        return loginResult;
    }

    public static LoginRegisterResult FAILURE(){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("error");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(new String[]{"guest"});
        return loginResult;
    }

    public static LoginRegisterResult FAILURE(HttpResult httpResult){
        LoginRegisterResult loginResult = new LoginRegisterResult();
        loginResult.setStatus("error");
        loginResult.setType(TYPEA);
        loginResult.setCurrentAuthority(new String[]{"guest"});
        loginResult.setHttpResult(httpResult);
        return loginResult;
    }

}
