package com.idbiintech.visitormgmtsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name=  "vermst_dtls")
public class VerMstDtls 
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;
	
	private String androidvercode;
	
	private String androidvername;
	
	private String androidlink;
	
	private String androidreldate;
	
	
	private String iosvercode;
	
	private String iosvername;
	
	private String ioslink;
	
	private String iosreldate;

	private String deviceos;
	
	private String appname;


	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getAndroidvercode() {
		return androidvercode;
	}

	public void setAndroidvercode(String androidvercode) {
		this.androidvercode = androidvercode;
	}

	public String getAndroidvername() {
		return androidvername;
	}

	public void setAndroidvername(String androidvername) {
		this.androidvername = androidvername;
	}

	public String getAndroidlink() {
		return androidlink;
	}

	public void setAndroidlink(String androidlink) {
		this.androidlink = androidlink;
	}

	public String getAndroidreldate() {
		return androidreldate;
	}

	public void setAndroidreldate(String androidreldate) {
		this.androidreldate = androidreldate;
	}

	public String getIosvercode() {
		return iosvercode;
	}

	public void setIosvercode(String iosvercode) {
		this.iosvercode = iosvercode;
	}

	public String getIosvername() {
		return iosvername;
	}

	public void setIosvername(String iosvername) {
		this.iosvername = iosvername;
	}

	public String getIoslink() {
		return ioslink;
	}

	public void setIoslink(String ioslink) {
		this.ioslink = ioslink;
	}

	public String getIosreldate() {
		return iosreldate;
	}

	public void setIosreldate(String iosreldate) {
		this.iosreldate = iosreldate;
	}

	public String getDeviceos() {
		return deviceos;
	}

	public void setDeviceos(String deviceos) {
		this.deviceos = deviceos;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	
	

}
