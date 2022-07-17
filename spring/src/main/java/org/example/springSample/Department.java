package org.example.springSample;

import java.io.Serializable;

public class Department implements Serializable {
    /* id*/
    private Integer id;

    /* department name*/
    private String name;

    /* 领导*/
    private Integer leader;

    /* 部门类型 研发 财务 生产 市场……*/
    private Byte type;

    /* 上级部门*/
    private Integer parent;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLeader() {
        return leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}