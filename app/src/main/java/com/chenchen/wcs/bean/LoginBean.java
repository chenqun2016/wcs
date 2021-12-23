package com.chenchen.wcs.bean;

import java.util.List;

/**
 * 创建时间：2021/12/20
 *
 * @Author： 陈陈陈
 * 功能描述：
 */
public class LoginBean {

    public User user;
    public String token;

    public static class User {
        public List<String> dataScopes;
        public List<String> roles;
        public User.UserBean user;

        public static class UserBean {
            public String createUserId;
            public String deptId;
            public String email;
            public Integer gender;
            public String id;
            public String password;
            public String phone;
            public String realName;
            public Integer status;
            public String username;
        }
    }
}
