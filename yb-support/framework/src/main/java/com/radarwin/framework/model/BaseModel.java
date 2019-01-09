package com.radarwin.framework.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.radarwin.framework.jsonhelp.DateTimeJsonDeserializer;
import com.radarwin.framework.jsonhelp.DateTimeJsonSerializer;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by josh on 15/6/17.
 */
public abstract class BaseModel implements Serializable {

    private Boolean deleted;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;

    @Column(name = "DELETED")
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Column(name = "CREATE_TIME")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "CREATE_USER")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Column(name = "UPDATE_TIME")
    @JsonSerialize(using = DateTimeJsonSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    @JsonDeserialize(using = DateTimeJsonDeserializer.class)
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "UPDATE_USER")
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
