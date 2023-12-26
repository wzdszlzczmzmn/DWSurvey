package net.diaowen.dwsurvey.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用于返回用户基本信息的表单,包含用户的基本信息，实现基本的Get和Set方法
 */
public class UserInfoForm implements Serializable {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户账户名
     */
    private String loginName;
    /**
     * 用户生日
     */
    private Date birthday;
    /**
     * 用户性别
     */
    private Integer sex;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户电话号码
     */
    private String cellphone;

    /**
     * 用户基本信息表单的构造函数
     *
     * @param id 用户ID
     * @param loginName 用户账号名
     * @param birthday 用户生日
     * @param sex 用户性别
     * @param email 用户邮箱
     * @param cellphone 用户电话号码
     */
    public UserInfoForm(String id, String loginName, Date birthday, Integer sex, String email, String cellphone) {
        this.id = id;
        this.loginName = loginName;
        this.birthday = birthday;
        this.sex = sex;
        this.email = email;
        this.cellphone = cellphone;
    }

    /**
     * 用户ID的GET方法
     *
     * @return 用户ID
     */
    public String getId() {
        return id;
    }

    /**
     * 用户ID的SET方法
     *
     * @param id 用户ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 用户账户名的GET方法
     *
     * @return 用户账户名
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 用户账户名的SET方法
     *
     * @param loginName 用户账户名
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 用户生日的GET方法
     *
     * @return 用户生日
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 用户生日的SET方法
     *
     * @param birthday 用户生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 用户性别的GET方法
     *
     * @return 用户的性别
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 用户性别的SET方法
     *
     * @param sex 用户的性别
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 用户邮箱的GET方法
     *
     * @return 用户的邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 用户邮箱的SET方法
     *
     * @param email 用户的邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 用户电话号码的GET方法
     *
     * @return 用户的电话号码
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * 用户电话号码的SET方法
     *
     * @param cellphone 用户的电话号码
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }
}
