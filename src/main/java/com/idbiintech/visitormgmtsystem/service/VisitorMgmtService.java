package com.idbiintech.visitormgmtsystem.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.idbiintech.visitormgmtsystem.model.LoginOTPResponse;
import com.idbiintech.visitormgmtsystem.model.LoginResponse;
import com.idbiintech.visitormgmtsystem.model.MstVisitorDtls;
import com.idbiintech.visitormgmtsystem.model.OTPdtls;
import com.idbiintech.visitormgmtsystem.model.RegstrationDtls;
import com.idbiintech.visitormgmtsystem.model.VisitorDtls;

public interface VisitorMgmtService 
{

	public ResponseEntity<String> visitordtls(@RequestHeader String visitorname, @RequestHeader String companyname, @RequestHeader String location,@RequestHeader String mobileno);

	
	public ResponseEntity<String> vstdtls(VisitorDtls visitordtls);

	
	public MstVisitorDtls findDetailsById(int name);
	
	
	public ResponseEntity<List> findDetailsByEmpName(String name);
	
	
	public void addMasterEmployee2(MstVisitorDtls mstVisitorDtls);
	
	
	public ResponseEntity<List> findAllEmpDetails();
	
	 
	public VisitorDtls visitordtls(VisitorDtls visitordtls) throws IOException;
	
	
	public List<MstVisitorDtls> getempdtls(); 
	
	
	public List<VisitorDtls> findvstdtlsbyempid(String empid);
	
	
	public VisitorDtls findvstdtls(String empid, Integer Id); 
	
	
	public Optional<VisitorDtls> updtvstdtls(String empid, Integer Id, String reqstatus, String declinedreason);
	
	
	public Optional<VisitorDtls> updtvstouttime(String empid, Integer Id ); 

	
	
	public LoginResponse registrationtls(RegstrationDtls registrationdtls);
	
	
	public LoginOTPResponse fetchregdtsls(RegstrationDtls registrationdtls);
	
	
	public String chkseckeyexist(String accountname, int empid);
	
	
	public void saveseckeydtls(OTPdtls otpdtls);
	
	
	public Integer getempcnt(Integer empid);
	
	
	public List<MstVisitorDtls> getemplstbyrole(String role) ;
	
	
	public String chkempvststatus(VisitorDtls visitordtls);

	


	
}
