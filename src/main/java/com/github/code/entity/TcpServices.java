/**
 * Copyright (C), 2020-03-03
 * FileName: TcpServices
 * Author:   heyanbo
 * Date:     2020/3/3 16:36
 * Description:
 */
package com.github.code.entity;

import java.io.Serializable;

/**
 * <功能简要> <br>
 * <>
 *
 * @Author heyanbo
 * @createTime 2020/3/3 16:36
 * @since
 */
public class TcpServices implements Serializable{

    private String clusterName;

    private String icName;

    private String tenantName;

    private String projectName;

    private String serviceName;

    private Integer port;

    public TcpServices(String clusterName, String icName, String tenantName, String projectName, String serviceName, Integer port) {
        this.clusterName = clusterName;
        this.icName = icName;
        this.tenantName = tenantName;
        this.projectName = projectName;
        this.serviceName = serviceName;
        this.port = port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getIcName() {
        return icName;
    }

    public void setIcName(String icName) {
        this.icName = icName;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "TcpServices{" +
                "clusterName='" + clusterName + '\'' +
                ", icName='" + icName + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", port=" + port +
                '}';
    }
}
