package com.wth.demo.constants;

public class Constant {

    /**
     * Contants 加入 用户名 key 常量
     */
    public static final String JWT_USER_NAME = "jwt-user-name-key";

    /**
     * 角色信息key
     */
    public static final String ROLES_INFOS_KEY = "roles-infos-key";

    /**
     * 权限信息key
     */
    public static final String PERMISSION_INFOS_KEY = "permission-infos-key";

    /**
     * 业务token
     */
    public static final String ACCESS_TOKEN = "authorization";

    /**
     * 刷新token
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 主动去刷新 token key （场景 比如修改用户的角色/权限刷新token）
     */
    public static final String JWT_REFRESH_KEY = "jwt-refresh-key_";

    /**
     * 标记用户是否已经被锁定
     */
    public static final String ACCOUNT_LOCK_KEY = "account-lock-key_";

    /**
     * 标记用户是否已经被删除
     */
    public static final String DELETED_USER_KEY = "deleted-user-key_";

    /**
     * 用户权鉴缓存 key
     */
    public static final String IDENTIFY_CACHE_KEY="shiro-cache:com.wth.demo.shiro.CustomRealm.authorizationCache:";
}
