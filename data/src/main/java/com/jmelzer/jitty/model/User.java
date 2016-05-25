/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jmelzer.jitty.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Standard User entity with attributes such as name, password etc.
 * <p>
 * We also tie in to the security framework and implement
 * the Acegi UserDetails interface so that Acegi can take care
 * of Authentication and Authorization
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = -2388299912396255263L;
    @Id
    @GeneratedValue
    private Long id;

    private Integer type;
    @Column(nullable = false)
    private String loginName;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = true)
    private String locale;
    @Column(nullable = false)
    private boolean locked = true;
    @Column(nullable = true)
    byte[] avatar;

//    private Set<UserRole> roles = new LinkedHashSet<UserRole>();

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }



    public String getPassword() {
        return password;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int compareTo(User u) {
        if (u == null) {
            return 1;
        }
        if (u.name == null) {
            if (name == null) {
                return 0;
            }
            return 1;
        }
        if (name == null) {
            return -1;
        }
        return name.compareTo(u.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User u = (User) o;
        return u.getLoginName().equals(loginName);
    }

    @Override
    public int hashCode() {
        if (loginName == null) {
            return 0;
        }
        return loginName.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("User");
        sb.append("{type=").append(type);
        sb.append(", loginName='").append(loginName).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", locale='").append(locale).append('\'');
        sb.append(", locked=").append(locked);
        sb.append('}');
        return sb.toString();
    }

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "USER_TO_ROLES", joinColumns = @JoinColumn(name = "USER_ID"),
//            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
//    public Set<UserRole> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<UserRole> roles) {
//        this.roles = roles;
//    }
//
//    public void addRole(UserRole userRole) {
//        roles.add(userRole);
//    }

//    @Transient
//    public boolean isAdmin() {
//        for (UserRole role : roles) {
//            if (role.getName().equals(UserRole.Roles.ROLE_ADMIN.name())) {
//                return true;
//            }
//        }
//        return false;
//    }

}
