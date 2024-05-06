package com.idbiintech.visitormgmtsystem.model;


import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name=  "visitor_dtls")
public class VisitorDtls 
{
	
	public  VisitorDtls()
	{
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visitor_dtls_seq")
	@SequenceGenerator(name = "visitor_dtls_seq", sequenceName = "visitor_dtls_seq", allocationSize = 1, initialValue = 1)
	@Column(name="visitor_id")
	private Integer visitorid;
	
	@Column(name="name")
	private String visitorname;
	
	@Column(name="company")
	private String companyname;
	
	
	@Column(name="address")
	private String address;
	
	@Column(name="mobile_no")
	private String mobileno;
	
	@Column(name="purpose")
	private String purpose;
	
	@Column(name="email_id")
	private String emailid;
	
	@Column(name="identity_proof_image")
	private String identityproofimage;
	
	@Column(name="empid")
	private String empid;
	
		
	@Column(name="declined_Reason")
	private String declinedreason;
	
	@Column(name="request_status")
	private String requestStatus;
	
	
	private String create_date;
	
	private String intime;
	
	private String outtime;
	
	@Column(name="strcapimage_encode")
	private String strcapimageenccode;
	
    
	private String security_id;
	
	@JsonIgnore
	@Column(name="vstid")
	private Integer vstid;
	
	
	public Integer getVisitorid() 
	{
		return visitorid;
	}

	public void setVisitorid(Integer visitorid) 
	{
		this.visitorid = visitorid;
	}
	
	

	public String getVisitorname() 
	{
		return visitorname;
	}

	public void setVisitorname(String visitorname) 
	{
		this.visitorname = visitorname;
	}

	public String getCompanyname() 
	{
		return companyname;
	}

	public void setCompanyname(String companyname) 
	{
		this.companyname = companyname;
	}

	
	public String getMobileno() 
	{
		return mobileno;
	}

	
	public void setMobileno(String mobileno) 
	{
		this.mobileno = mobileno;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getIdentityproofimage() {
		return identityproofimage;
	}

	public void setIdentityproofimage(String identityproofimage) {
		this.identityproofimage = identityproofimage;
	}



	public String getAddress() 
	{
		return address;
	}

	public void setAddress(String address) 
	{
		this.address = address;
	}

	public String getDeclinedreason() {
		return declinedreason;
	}

	public void setDeclinedreason(String declinedreason) {
		this.declinedreason = declinedreason;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	
	
 
	public String getSecurity_id() {
		return security_id;
	}

	public void setSecurity_id(String security_id) {
		this.security_id = security_id;
	}



	public String getStrcapimageenccode() {
		return strcapimageenccode;
	}

	public void setStrcapimageenccode(String strcapimageenccode) {
		this.strcapimageenccode = strcapimageenccode;
	}



	public String getOuttime() {
		return outtime;
	}

	public void setOuttime(String outtime) {
		this.outtime = outtime;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}
	
	
	

	
	  public Integer getVstid() {
		return vstid;
	}

	public void setVstid(Integer vstid) {
		this.vstid = vstid;
	}
	
	
	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public JSONObject toJSON() {
	        JSONObject jo = new JSONObject();
	        jo.put("visitorid", getVisitorid());
	        jo.put("visitorname", getVisitorname());
	        jo.put("companyname", getCompanyname());
	        jo.put("address", getAddress());
	        jo.put("mobileno", getMobileno());
	        jo.put("purpose", getPurpose());
	        jo.put("emailid", getEmailid());
	        jo.put("identityproofimage", getIdentityproofimage());
	        jo.put("empid", getEmpid());
	        jo.put("declinedreason", getDeclinedreason());
	        jo.put("requestStatus", getRequestStatus());
	        jo.put("create_date", getCreate_date());
	        jo.put("intime", getIntime());
	        jo.put("outtime", getOuttime());
	        jo.put("strcapimageenccode", getStrcapimageenccode());
	        return jo;
	    }

	@Override
	public String toString() {
		return "VisitorDtls [visitorid=" + visitorid + ", visitorname=" + visitorname + ", companyname=" + companyname
				+ ", address=" + address + ", mobileno=" + mobileno + ", purpose=" + purpose + ", emailid=" + emailid
				+ ", identityproofimage=" + identityproofimage + ", empid=" + empid + ", declinedreason="
				+ declinedreason + ", requestStatus=" + requestStatus + ", create_date=" + create_date + ", intime="
				+ intime + ", outtime=" + outtime + ", strcapimageenccode=" + strcapimageenccode + ", security_id="
				+ security_id + ", vstid=" + vstid + "]";
	}

	public VisitorDtls(Integer visitorid, String visitorname, String companyname, String address, String mobileno,
			String purpose, String emailid, String identityproofimage, String empid, String declinedreason,
			String requestStatus, String create_date, String intime, String outtime, String strcapimageenccode,
			String security_id, Integer vstid) {
		super();
		this.visitorid = visitorid;
		this.visitorname = visitorname;
		this.companyname = companyname;
		this.address = address;
		this.mobileno = mobileno;
		this.purpose = purpose;
		this.emailid = emailid;
		this.identityproofimage = identityproofimage;
		this.empid = empid;
		this.declinedreason = declinedreason;
		this.requestStatus = requestStatus;
		this.create_date = create_date;
		this.intime = intime;
		this.outtime = outtime;
		this.strcapimageenccode = strcapimageenccode;
		this.security_id = security_id;
		this.vstid = vstid;
	}
	
	
}
