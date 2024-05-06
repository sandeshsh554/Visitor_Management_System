package com.idbiintech.visitormgmtsystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name="mst_emp_dtls")
public class MstVisitorDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="empname")
	private String empname;
	
	@Column(name="department")
	private String department;
	
	@Column(name="designation")
	private String designation;
	
	@Column(name="mobile_no")
	private String mobileno;
	
	@Column(name="user_Role")
	private String userRole;
	
	
	
	@Column(name="fb_token")
	private String firebaseToken;
	
	@Column(name="device_os")
	private String deviceOS;
	
	@Column(name="imei_no")
	private String imeiNumber;
	
	
	@Column(name="version_Code")
	private String versionCode;
	
	
	@Column(name="version_name")
	private String versionName;
	
	
	@Column(name="emppass")
	private String emppass;
	
	
	
	private Integer empcnt;
	
	
	public String getMobileno() 
	{
		return mobileno;
	}

	public void setMobileno(String mobileno) 
	{
		this.mobileno = mobileno;
	}

	public String getEmailid() 
	{
		return emailid;
	}

	public void setEmailid(String emailid) 
	{
		this.emailid = emailid;
	}

	@Column(name="email_id")
	private String emailid;

	
	public String getEmpname() 
	{
		return empname;
	}

	public void setEmpname(String empname) 
	{
		this.empname = empname;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public Integer getId() 
	{
		return id;
	}

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
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

	public Integer getEmpcnt() 
	{
		return empcnt;
	}

	public void setEmpcnt(Integer empcnt) 
	{
		this.empcnt = empcnt;
	}
	
	

	public String getEmppass() 
	{
		return emppass;
	}

	public void setEmppass(String emppass) 
	{
		this.emppass = emppass;
	}

	@Override
	public String toString() {
		return "MstVisitorDtls [id=" + id + ", empname=" + empname + ", department=" + department + ", designation="
				+ designation + ", mobileno=" + mobileno + ", userRole=" + userRole + ", firebaseToken=" + firebaseToken
				+ ", deviceOS=" + deviceOS + ", imeiNumber=" + imeiNumber + ", versionCode=" + versionCode
				+ ", versionName=" + versionName + ", emppass=" + emppass + ", empcnt=" + empcnt + ", emailid="
				+ emailid + "]";
	}

	

	 
		
}
