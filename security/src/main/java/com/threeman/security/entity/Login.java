package com.threeman.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * 登录信息表(Login)表实体类
 *
 * @author songjing
 * @since 2022-02-14 16:22:23
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("login")
public class Login implements Serializable, UserDetails {

    /**
     * 用户编号
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 登录密码
     */
    @TableField("password")
    private String password;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 失效时间
     */
    @TableField("expire_time")
    private Date expireTime;

    /**
     * 账户状态
     */
    @TableField("state")
    private Boolean state;

    /**
     * 角色
     */
    private List<Role> roles;

    private String rememberMe;
    /**
     * 权限
     */
    private List<GrantedAuthority> grantedAuthorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
