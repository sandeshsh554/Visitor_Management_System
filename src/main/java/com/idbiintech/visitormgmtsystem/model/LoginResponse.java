package com.idbiintech.visitormgmtsystem.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
public class LoginResponse {
	
	private Integer id;
	
	private String empname;
	
	private String empId;

	private String userRole;
	
	private String jwtToken;
	
	private String department;
	
	private String designation;
	
	private String mobileno;
	
	private String firebaseToken;
	
	private String deviceOS;
	
	private String imeiNumber;
	
	
	private String versionCode;
	
	
	private String versionName;
	
    private String emailid;
    
    
    private String key;
    
    private String accountname;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getFirebaseToken() {
		return firebaseToken;
	}

	public void setFirebaseToken(String firebaseToken) {
		this.firebaseToken = firebaseToken;
	}

	public String getDeviceOS() {
		return deviceOS;
	}

	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAccountname() {
		return accountname;
	}


	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	@Override
	public String toString() {
		return "LoginResponse [id=" + id + ", empname=" + empname + ", empId=" + empId + ", userRole=" + userRole
				+ ", jwtToken=" + jwtToken + ", department=" + department + ", designation=" + designation
				+ ", mobileno=" + mobileno + ", firebaseToken=" + firebaseToken + ", deviceOS=" + deviceOS
				+ ", imeiNumber=" + imeiNumber + ", versionCode=" + versionCode + ", versionName=" + versionName
				+ ", emailid=" + emailid + ", key=" + key + ", accountname=" + accountname + "]";
	}

	
    

}
