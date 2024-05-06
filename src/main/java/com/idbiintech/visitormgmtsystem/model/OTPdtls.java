package com.idbiintech.visitormgmtsystem.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity  
@Table(name="otp_dtls")
@Component
public class OTPdtls 
{
	
	
	@JsonIgnore
	@Column(name = "emp_Id")
	private int empid;
	
	
	@Column(name = "account_name")
	private String accountName;
	
	
	@Id
	@Column(name = "secret_Key")
	private String secretKey;
	
	@Transient
	private String otp;





	public int getEmpid() {
		return empid;
	}

	public void setEmpid(int empid) {
		this.empid = empid;
	}

	public String getSecretKey() 
	{
		return secretKey;
	}

	public void setSecretKey(String secretKey) 
	{
		this.secretKey = secretKey;
	}

	
	public String getAccountName() 
	{
		return accountName;
	}

	public void setAccountName(String accountName) 
	{
		this.accountName = accountName;
	}

	@JsonIgnore
	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	
	
	
	
	
	
	
	
	
	

}
