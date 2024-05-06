package com.idbiintech.visitormgmtsystem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.idbiintech.visitormgmtsystem.auth.JwtUtil;
import com.idbiintech.visitormgmtsystem.model.ApiResponse;
import com.idbiintech.visitormgmtsystem.model.ApiResponse2;
import com.idbiintech.visitormgmtsystem.model.LoginOTPResponse;
import com.idbiintech.visitormgmtsystem.model.LoginResponse;
import com.idbiintech.visitormgmtsystem.model.MstVisitorDtls;
import com.idbiintech.visitormgmtsystem.model.Note;
import com.idbiintech.visitormgmtsystem.model.OTPdtls;
import com.idbiintech.visitormgmtsystem.model.RegstrationDtls;
import com.idbiintech.visitormgmtsystem.model.User;
import com.idbiintech.visitormgmtsystem.model.VersionMst;
import com.idbiintech.visitormgmtsystem.model.VisitorDtls;
import com.idbiintech.visitormgmtsystem.service.VersionService;
import com.idbiintech.visitormgmtsystem.service.VisitorMgmtService;
import com.idbiintech.visitormgmtsystem.serviceimpl.FirebaseMessagingService;
import com.idbiintech.visitormgmtsystem.util.AESCrypt;
import com.idbiintech.visitormgmtsystem.util.Util;

@RestController
public class VisitorMgmtController {

	public VisitorMgmtController() {
		this.authenticationManager = null;
	}

	@Autowired
	VisitorMgmtService visitormgmtservice;

	@Autowired
	FirebaseMessagingService firebaseService;

	@Autowired
	AESCrypt aescrypt;

	@Autowired
	private Environment env;

	@Autowired
	Util util;

	String status = "";

	String strusertoken = "";

	String vststatus = "";

	@Autowired
	Note note;

	Map<String, String> vstdata = new HashMap<>();

	Integer empcnt = 0;

	String errorMessage = "";

	String strencreq = "";

	String reqId1 = "";

	String encrq[] = null;

	String encreq1 = "";

	String encreq2 = "";

	String decreq1 = "";

	String decreq2 = "";

	String encresp = "";

	String strseckeyreq2 = "";

	VisitorDtls visitordtls;

	String successMessage = "";

	ObjectMapper mapper = new ObjectMapper();

	String strespdata = "";

	String token = "";

	String stradstatus = "";

	String strempid = "";

	String strpassword = "";

	String strusr1 = "";

	String strusr2 = "";

	String strpass1 = "";

	String strpass2 = "";
	
	String usrname="";

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	VersionService verservice;
	
	String usrpass = "";
	
	String strencdeckey="";
	
	boolean blpassexp = false ; 


	public VisitorMgmtController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) 
	{
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/visitordtls") // Done
	public ResponseEntity<ApiResponse> visitordtls(@RequestBody VisitorDtls visitordtls) 
	{
		try {
			String successMessage = "Success";
			errorMessage = "Failed";
			try {
				vststatus = visitormgmtservice.chkempvststatus(visitordtls);
			} catch (Exception e) {
				e.getMessage();
			}

			if (vststatus != null && vststatus != "") {
				if (vststatus.equalsIgnoreCase("pending")) {
					ApiResponse response = new ApiResponse(
							"Request has already been raised for the visitor for the given day", errorMessage, null);
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
			}

			VisitorDtls responseData = visitormgmtservice.visitordtls(visitordtls);
			MstVisitorDtls mstempdtls = visitormgmtservice.findDetailsById(Integer.parseInt(responseData.getEmpid()));
			strusertoken = mstempdtls.getFirebaseToken();
			ApiResponse successResponse = new ApiResponse(successMessage, "Success", responseData);
			JSONObject visitorData = responseData.toJSON();
			vstdata.put("visitorData", visitorData.toJSONString());
			vstdata.put("notificationType", "request");
			vstdata.put("title", "Your Visitor" + " " + visitordtls.getVisitorname() + " " + "is at gate");
			vstdata.put("image", responseData.getStrcapimageenccode());
			note.setSubject("Your Visitor" + " " + visitordtls.getVisitorname() + " " + "is at gate");
			note.setContent("Long press to accept/decline request");
			note.setImage(responseData.getStrcapimageenccode());
			note.setData(vstdata);
			System.out.println("Subject--" + note.getSubject());
			if (strusertoken != null && strusertoken != "") 
			{
				sendNotification(note, successMessage, strusertoken);
			}
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} 
		catch (Exception e) 
		{
			String errorMessage = "Failed";
			e.printStackTrace();
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/addMasterEmployee2")
	public void addMasterEmployee2(@RequestBody MstVisitorDtls data) {
		visitormgmtservice.addMasterEmployee2(data);
	}

	@GetMapping("/getAllEmpDtl")
	public ResponseEntity<List> getAllEmpDetails() 
	{
		return visitormgmtservice.findAllEmpDetails();
	}

	@GetMapping("/mstempdtls/empname/{empname}")
	public ResponseEntity<List> empDetailsByName(@PathVariable String empname) 
	{
		return visitormgmtservice.findDetailsByEmpName(empname);
	}

	@GetMapping("/mstempdtls/{id}")
	public ResponseEntity<ApiResponse> getempname(@PathVariable int id) 
	{
		try 
		{
			String successMessage = "Success";
			MstVisitorDtls responseData = visitormgmtservice.findDetailsById(id);
			ApiResponse successResponse = new ApiResponse(successMessage, successMessage, responseData);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} 
		catch (Exception e) 
		{
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}

	}

	@GetMapping("/getAllEmpDetail")
	public ResponseEntity<ApiResponse> getAllEmpDtls() {
		try {
			String successMessage = "Success";
			List<MstVisitorDtls> responseData = visitormgmtservice.getemplstbyrole("employee");
			ApiResponse successResponse = new ApiResponse(successMessage, successMessage, responseData);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (Exception e) {
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/getvistdtlsbyempid") // Done
	public ResponseEntity<ApiResponse> getvstdtlsbyempid(@RequestBody VisitorDtls visitordtls) {
		try 
		{
			String successMessage = "Visitor list fetched successfully.";
			List<VisitorDtls> responseData = visitormgmtservice.findvstdtlsbyempid(visitordtls.getEmpid());
			ApiResponse successResponse = new ApiResponse(successMessage, "success", responseData);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);

		} 
		catch (Exception e) 
		{
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}

	}

	@PostMapping("/getvstdtls") // Done
	public ResponseEntity<ApiResponse2> getvstdtls(@RequestBody VisitorDtls visitordtls) {
		try 
		{
			String successMessage = "Visitor data fetched successfully.";
			status = "Success";
			VisitorDtls responseData = visitormgmtservice.findvstdtls(visitordtls.getEmpid(),visitordtls.getVisitorid());
			MstVisitorDtls mstemployeedata = visitormgmtservice.findDetailsById(Integer.parseInt(visitordtls.getEmpid()));
			ApiResponse2 successResponse = new ApiResponse2(successMessage, status, responseData, mstemployeedata);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (Exception e) 
		{
			String errorMessage = "Failed";
			ApiResponse2 errorResponse = new ApiResponse2(errorMessage, errorMessage, null, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/updtvstdtls") // Done
	public ResponseEntity<ApiResponse> updtvstdtls(@RequestBody VisitorDtls visitordtls) {
		try {
			String successMessage = "Visitor data updated successfully.";
			status = "Success";
			Optional<VisitorDtls> responseData = visitormgmtservice.updtvstdtls(visitordtls.getEmpid(),
					visitordtls.getVisitorid(), visitordtls.getRequestStatus(), visitordtls.getDeclinedreason());
			ApiResponse successResponse = new ApiResponse(successMessage, status, responseData);
			VisitorDtls respdata = visitormgmtservice.findvstdtls(visitordtls.getEmpid(), visitordtls.getVisitorid());
			MstVisitorDtls mstempdtls = visitormgmtservice.findDetailsById(Integer.parseInt(respdata.getSecurity_id()));
			strusertoken = mstempdtls.getFirebaseToken();
			JSONObject visitorData = respdata.toJSON();
			vstdata.put("visitorData", visitorData.toJSONString());
			if (respdata.getRequestStatus() != null) {
				vstdata.put("notificationType", "confirm");
				vstdata.put("title",
						"Request has been accepted for Visitor Name:" + "" + respdata.getVisitorname() + "");
				note.setSubject("Request has been accepted for Visitor Name:" + "" + respdata.getVisitorname() + "");
			}

			else {
				vstdata.put("notificationType", "confirm");
				vstdata.put("title","Request has been rejected for Visitor Name:" + "" + respdata.getVisitorname() + "");
				note.setSubject("Request has been rejected for Visitor Name:" + "" + respdata.getVisitorname() + "");
			}
			vstdata.put("image", respdata.getStrcapimageenccode());
			note.setContent(respdata.getVisitorname());
			note.setImage(respdata.getStrcapimageenccode());
			note.setData(vstdata);
			if (strusertoken != null && strusertoken != "") 
			{
				sendNotificationtosecurity(note, successMessage, strusertoken);
			}
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (Exception e) 
		{
			e.printStackTrace();
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/loginapi")
	public ResponseEntity<ApiResponse> loginapi(@RequestBody RegstrationDtls registrationdtls) {
		try 
		{
			String successMessage = "Otp sent successfully - register device.";
			LoginResponse responseData = visitormgmtservice.registrationtls(registrationdtls);

			String strlogmobno = registrationdtls.getMobileno();
			String resmobno = responseData.getMobileno();

			if (!strlogmobno.equals(resmobno)) 
			{
				ApiResponse errorResponse = new ApiResponse("Invalid Credentials", "failure", null);
				return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
			}
			ApiResponse successResponse = new ApiResponse(successMessage, "Success", responseData);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (Exception e) 
		{
			e.printStackTrace();
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, "failure", null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/otpverified")
	public ResponseEntity<ApiResponse> otpverification(@RequestBody RegstrationDtls registrationdtls) {
		ApiResponse successResponse = null;
		String errorMessage = "Incorrect OTP";

		try {
			String successMessage = "User verified successfully.";
			LoginOTPResponse responseData = visitormgmtservice.fetchregdtsls(registrationdtls);

			if (responseData.getMsg().equalsIgnoreCase("1")) 
			{
				successResponse = new ApiResponse(successMessage, "Success", responseData);
				return ResponseEntity.status(HttpStatus.OK).body(successResponse);
			} 
			else 
			{
				successResponse = new ApiResponse(errorMessage, "failure", null);
				return ResponseEntity.status(HttpStatus.OK).body(successResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "Incorrect OTP";
			ApiResponse errorResponse = new ApiResponse(errorMessage, "failure", null);
			return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
		}
	}

	@PostMapping("/updtvstouttime") // Done
	public ResponseEntity<ApiResponse> updtvistouttime(@RequestBody VisitorDtls visitordtls) {
		try {
			String successMessage = "Visitor Out Time updated successfully.";
			status = "Success";
			Optional<VisitorDtls> responseData = visitormgmtservice.updtvstouttime(visitordtls.getEmpid(),
					visitordtls.getVisitorid());
			ApiResponse successResponse = new ApiResponse(successMessage, status, responseData);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		} catch (Exception e) {
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}

	}

	public VisitorMgmtController(FirebaseMessagingService firebaseService) {
		this.authenticationManager = null;
		this.firebaseService = firebaseService;
	}

	@RequestMapping("/sendnotification")
	@ResponseBody
	public String sendNotification(@RequestBody Note note, @RequestParam String topic, String usertoken)
			throws FirebaseMessagingException {
		System.out.println(" in send notification ");
		return firebaseService.sendNotification(note, topic, usertoken);
	}

	@RequestMapping("/sendnotificationtosecurity")
	@ResponseBody
	public String sendNotificationtosecurity(@RequestBody Note note, @RequestParam String topic, String usertoken)
			throws FirebaseMessagingException {
		System.out.println(" in security send notification ");
		return firebaseService.sendNotificationtosecurity(note, topic, usertoken);
	}

	@PostMapping("/generateotp")
	public String getEmployeeValue(@RequestBody OTPdtls otpdtls) throws Exception {
		String code = null;
		try {

			String stractname = otpdtls.getAccountName();
			int v_empId = otpdtls.getEmpid();
			String secreteKeyPresent = visitormgmtservice.chkseckeyexist(stractname, v_empId);
			String secretKey = null;

			if (secreteKeyPresent == null) {
				secretKey = util.generateSecretKey();
				code = util.getTOTPCode(secretKey);
				otpdtls.setSecretKey(secretKey);
				String email = stractname;
				String companyName = "IDBIINTECH";
				visitormgmtservice.saveseckeydtls(otpdtls);
				return secretKey;
			} else {
				code = util.getTOTPCode(secreteKeyPresent);
				otpdtls.setSecretKey(secreteKeyPresent);
				visitormgmtservice.saveseckeydtls(otpdtls);
			}

		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return code;
	}

	@PostMapping("/validateotp")
	public String enterEmployeeOTP(@RequestBody OTPdtls otpdtls) throws Exception {

		String code = "";

		String otpstatus = "";

		try {
			String strentrotp = otpdtls.getOtp();

			String stractname = otpdtls.getAccountName();

			int empId = otpdtls.getEmpid();

			String secreteKeyPresent = visitormgmtservice.chkseckeyexist(stractname, empId);

			if (secreteKeyPresent != null) {
				code = util.getTOTPCode(secreteKeyPresent);
				System.out.println(code);

				if (strentrotp.equals(code)) {
					otpstatus = "Success";
					return code;
				}

				else {
					otpstatus = "Fail";
					return "invalid otp";
				}

			} else {
				otpstatus = "Something went to Wrong";
				return "invalid otp";
			}

		} catch (Exception e)

		{
			e.printStackTrace();

		}

		return code;

	}

	@PostMapping("/getempcnt") // Done
	public ResponseEntity<ApiResponse> getempcnt(@RequestBody VisitorDtls visitordtls) {

		try {
			String successMessage = "Total Employee Count";
			empcnt = visitormgmtservice.getempcnt(Integer.parseInt(visitordtls.getEmpid()));
			ApiResponse successResponse = new ApiResponse(successMessage, "success", empcnt);
			return ResponseEntity.status(HttpStatus.OK).body(successResponse);
		}

		catch (Exception e) {
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}

	}

	@PostMapping("/encAddVisitorDetails")
	public String encdecvstdtls(@RequestBody JSONObject vstdtls, @RequestHeader String reqId)
			throws JsonMappingException, JsonProcessingException {
		try {

			String successMessage = "";
			String strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getseckey(reqId, encreq1);
			System.out.println("strseckeyreq2 in encdecvstdtls ------------" + strseckeyreq2);
			VisitorDtls visitordtls = util.getdecreq(reqId, encreq1, encreq2, strseckeyreq2);
			VisitorDtls responseData = visitormgmtservice.visitordtls(visitordtls);
			MstVisitorDtls mstempdtls = visitormgmtservice.findDetailsById(Integer.parseInt(responseData.getEmpid()));
			strusertoken = mstempdtls.getFirebaseToken();
			ApiResponse successResponse = new ApiResponse(successMessage, "Success", responseData);
			JSONObject visitorData = responseData.toJSON();
			vstdata.put("visitorData", visitorData.toJSONString());
			vstdata.put("notificationType", "request");
			vstdata.put("title", "Your Visitor" + " " + visitordtls.getVisitorname() + " " + "is at gate");
			vstdata.put("image", responseData.getStrcapimageenccode());
			note.setSubject("Your Visitor" + " " + visitordtls.getVisitorname() + " " + "is at gate");
			note.setContent("Long press to accept/decline request");
			note.setImage(responseData.getStrcapimageenccode());
			note.setData(vstdata);
			System.out.println("Subject--" + note.getSubject());
			if (strusertoken != null && strusertoken != "") 
			{
				sendNotification(note, successMessage, strusertoken);
			}
			strespdata = mapper.writeValueAsString(successResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			String decresp = aescrypt.decrypt(encresp, strseckeyreq2);
		}

		catch (Exception e) {
			e.printStackTrace();

		}

		return encresp;

	}

	@PostMapping("/encGetVisitorDetailsByEmployeeId")
	public String getencvstdtlsbyempid(@RequestBody JSONObject vstdtls, @RequestHeader String reqId)
			throws JsonProcessingException {
		try {
			strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getseckey(reqId, encreq1);
			visitordtls = util.getdecreq(reqId, encreq1, encreq2, strseckeyreq2);
			String successMessage = "Visitor list fetched successfully.";
			List<VisitorDtls> responseData = visitormgmtservice.findvstdtlsbyempid(visitordtls.getEmpid());
			ApiResponse successResponse = new ApiResponse(successMessage, "success", responseData);
			strespdata = mapper.writeValueAsString(successResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;

		} catch (Exception e) {
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			strespdata = mapper.writeValueAsString(errorResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		}

	}

	@PostMapping("/encGetVisitorDetails")
	public String getencvstdtls(@RequestBody JSONObject vstdtls, @RequestHeader String reqId)
			throws JsonProcessingException {
		try {
			strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getseckey(reqId, encreq1);
			visitordtls = util.getdecreq(reqId, encreq1, encreq2, strseckeyreq2);
			successMessage = "Visitor data fetched successfully.";
			status = "Success";
			VisitorDtls responseData = visitormgmtservice.findvstdtls(visitordtls.getEmpid(),
					visitordtls.getVisitorid());
			MstVisitorDtls mstemployeedata = visitormgmtservice
					.findDetailsById(Integer.parseInt(visitordtls.getEmpid()));
			ApiResponse2 successResponse = new ApiResponse2(successMessage, status, responseData, mstemployeedata);
			strespdata = mapper.writeValueAsString(successResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		} catch (Exception e) {
			String errorMessage = "Failed";
			ApiResponse2 errorResponse = new ApiResponse2(errorMessage, errorMessage, null, null);
			strespdata = mapper.writeValueAsString(errorResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		}

	}

	@PostMapping("/encUpdateVisitorDetails")
	public String encupdtvstdtls(@RequestBody JSONObject vstdtls, @RequestHeader String reqId)
			throws JsonProcessingException 
	{
		try 
		{
			successMessage = "Visitor data updated successfully.";
			status = "Success";
			strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getseckey(reqId, encreq1);
			visitordtls = util.getdecreq(reqId, encreq1, encreq2, strseckeyreq2);
			Optional<VisitorDtls> responseData = visitormgmtservice.updtvstdtls(visitordtls.getEmpid(),
					visitordtls.getVisitorid(), visitordtls.getRequestStatus(), visitordtls.getDeclinedreason());
			if (responseData.isPresent()) 
			{
				VisitorDtls vstdetails = responseData.get();
				ApiResponse successResponse = new ApiResponse(successMessage, status, vstdetails);
				VisitorDtls respdata = visitormgmtservice.findvstdtls(visitordtls.getEmpid(),visitordtls.getVisitorid());
				MstVisitorDtls mstempdtls = visitormgmtservice.findDetailsById(Integer.parseInt(respdata.getSecurity_id()));
				strusertoken = mstempdtls.getFirebaseToken();
				JSONObject visitorData = respdata.toJSON();
				vstdata.put("visitorData", visitorData.toJSONString());
				if (respdata.getRequestStatus().equalsIgnoreCase("accept")) 
				{
					vstdata.put("notificationType", "confirm");
					vstdata.put("title","Request has been accepted for Visitor Name:" + "" + respdata.getVisitorname() + "");
					note.setSubject("Request has been accepted for Visitor Name:" + "" + respdata.getVisitorname() + "");
				}

				else 
				{
					vstdata.put("notificationType", "confirm");
					vstdata.put("title","Request has been rejected for Visitor Name:" + "" + respdata.getVisitorname() + "");
					note.setSubject("Request has been rejected for Visitor Name:" + "" + respdata.getVisitorname() + "");
				}
				vstdata.put("image", respdata.getStrcapimageenccode());
				note.setContent(respdata.getVisitorname());
				note.setImage(respdata.getStrcapimageenccode());
				note.setData(vstdata);
				if (strusertoken != null && strusertoken != "") {
					sendNotificationtosecurity(note, successMessage, strusertoken);
				}
				strespdata = mapper.writeValueAsString(successResponse);
				encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			}
			return encresp;
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			strespdata = mapper.writeValueAsString(errorResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		}
	}

	@PostMapping("/encUpdateVisitorOutTime")
	public String encupdtvistouttime(@RequestBody JSONObject vstdtls, @RequestHeader String reqId)
			throws JsonProcessingException {
		try 
		{
			strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getseckey(reqId, encreq1);
			visitordtls = util.getdecreq(reqId, encreq1, encreq2, strseckeyreq2);
			String successMessage = "Visitor Out Time updated successfully.";
			status = "Success";
			Optional<VisitorDtls> responseData = visitormgmtservice.updtvstouttime(visitordtls.getEmpid(),visitordtls.getVisitorid());
			
			if (responseData.isPresent()) 
			{
				VisitorDtls visitordtls = responseData.get();
				ApiResponse successResponse = new ApiResponse(successMessage, status, visitordtls);
				strespdata = mapper.writeValueAsString(successResponse);
				encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
				String decresp = aescrypt.decrypt(encresp, strseckeyreq2);
			}
			return encresp;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			strespdata = mapper.writeValueAsString(errorResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		}

	}

	@PostMapping("/encGetEmployeeVisitorCount")
	public String encgetempcnt(@RequestBody JSONObject vstdtls, @RequestHeader String reqId)
			throws JsonProcessingException {
		try {
			strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getseckey(reqId, encreq1);
			visitordtls = util.getdecreq(reqId, encreq1, encreq2, strseckeyreq2);
			String successMessage = "Total Employee Count";
			usrpass = env.getProperty("usrpass");
			
			MstVisitorDtls mstempdtls = visitormgmtservice.findDetailsById(Integer.parseInt(visitordtls.getEmpid()));
			strpassword = aescrypt.decrypt(mstempdtls.getEmppass(),util.getencdeckey());
			
			strempid = visitordtls.getEmpid();
			strusr1 = env.getProperty("secusr1");
			strusr2 = env.getProperty("secusr2");
			strpass1 = env.getProperty("secpass1");
			strpass2 = env.getProperty("secpass2");
			
			empcnt = visitormgmtservice.getempcnt(Integer.parseInt(visitordtls.getEmpid()));
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(visitordtls.getEmpid()), usrpass));
			String usrname = authentication.getName();
			User user = new User(usrname, "");
			token = jwtUtil.createToken(user);
			
			if (!strempid.equalsIgnoreCase(strusr1) && !strempid.equalsIgnoreCase(strusr2)
				&& !strpassword.equalsIgnoreCase(strpass1) && !strpassword.equalsIgnoreCase(strpass2)) 
			{
				strempid = "INT" + strempid;
				stradstatus = util.getempdata(strempid, strpassword);
				System.out.println("stradstatus---------------"+stradstatus);
			}
			
			if (stradstatus.equalsIgnoreCase("failure")) 
			{ 
			   blpassexp=true;
			   ApiResponse errorResponse = new ApiResponse("Invalid Credentials", "failure", null,token,blpassexp); 
			   strespdata = mapper.writeValueAsString(errorResponse); 
			   encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			   String decdresp = aescrypt.decrypt(encresp, strseckeyreq2);
			   return encresp; 
			}
			
			ApiResponse successResponse = new ApiResponse(successMessage, "success", empcnt, token,blpassexp);
			strespdata = mapper.writeValueAsString(successResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			String decresp = aescrypt.decrypt(encresp, strseckeyreq2);
			return encresp;
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null,token,blpassexp);
			strespdata = mapper.writeValueAsString(errorResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		}

	}


	@PostMapping("/encVerifyOtp")
	public String encotpverf(@RequestBody JSONObject vstdtls, @RequestHeader String reqId)
			throws JsonProcessingException {

		ApiResponse successResponse = null;
		String errorMessage = "Incorrect OTP";
		try {
			strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getseckey(reqId, encreq1);
			JSONParser parser = new JSONParser();
			JSONObject decreq2 = null;

			try {
				decreq2 = (JSONObject) parser.parse(aescrypt.decrypt(encreq2, strseckeyreq2));
			} catch (ParseException e) {
				e.printStackTrace();
			}


			RegstrationDtls registrationdtls = mapper.readValue(decreq2.toJSONString(), RegstrationDtls.class);
			String successMessage = "User verified successfully.";
			LoginOTPResponse responseData = visitormgmtservice.fetchregdtsls(registrationdtls);
			usrpass = env.getProperty("usrpass");
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(responseData.getEmpId(), usrpass));
			String email = authentication.getName();
			User user = new User(email, "");
			token = jwtUtil.createToken(user);
			

			if (responseData.getMsg().equalsIgnoreCase("1")) 
			{
				successResponse = new ApiResponse(successMessage, "Success", responseData, token);
				strespdata = mapper.writeValueAsString(successResponse);
				encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
				String decresp = aescrypt.decrypt(encresp, strseckeyreq2);
				return encresp;
			}

			else {
				successResponse = new ApiResponse(errorMessage, "failure", null, token);
				strespdata = mapper.writeValueAsString(successResponse);
				encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
				String decresp = aescrypt.decrypt(encresp, strseckeyreq2);
				return encresp;
			}

		}

		catch (Exception e) {
			e.printStackTrace();
			errorMessage = "Incorrect OTP";
			ApiResponse errorResponse = new ApiResponse(errorMessage, "failure", null, token);
			strespdata = mapper.writeValueAsString(errorResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		}
	}

	@GetMapping("/encGetAllEmployeeDetails")
	public String encgetAllEmpDtls(@RequestHeader String reqId) throws JsonProcessingException {
		try {
			strseckeyreq2 = util.getstatickey(reqId);
			String successMessage = "Success";
			List<MstVisitorDtls> responseData = visitormgmtservice.getemplstbyrole("employee");
			ApiResponse successResponse = new ApiResponse(successMessage, successMessage, responseData);
			strespdata = mapper.writeValueAsString(successResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		} catch (Exception e) 
		{
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, errorMessage, null);
			strespdata = mapper.writeValueAsString(errorMessage);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		}
	}

	@PostMapping("/encRegisterUser")
	public String encadloginapi(@RequestBody JSONObject vstdtls, @RequestHeader String reqId) throws JsonProcessingException 
	{
		try 
		{
			strencreq = Util.getencreq(vstdtls);
			encrq = strencreq.split("-");
			encreq1 = encrq[0];
			encreq2 = encrq[1];
			strseckeyreq2 = util.getstatickey(reqId);
			JSONParser parser = new JSONParser();
			JSONObject decreq2 = null;
			try 
			{
				decreq2 = (JSONObject) parser.parse(aescrypt.decrypt(encreq2, strseckeyreq2));
			} 
			catch (ParseException e) 
			{
				e.printStackTrace();
			}
			
			ObjectMapper mapper1 = new ObjectMapper();
			strempid = decreq2.get("empId").toString();
			strpassword = decreq2.get("password").toString();
			strusr1 = env.getProperty("secusr1");
			strusr2 = env.getProperty("secusr2");
			strpass1 = env.getProperty("secpass1");
			strpass2 = env.getProperty("secpass2");
			if (!strempid.equalsIgnoreCase(strusr1) && !strempid.equalsIgnoreCase(strusr2)
				&& !strpassword.equalsIgnoreCase(strpass1) && !strpassword.equalsIgnoreCase(strpass2)) 
			{
				strempid = "INT" + strempid;
				stradstatus = util.getempdata(strempid, strpassword);
			}
			usrpass = env.getProperty("usrpass");
			strencdeckey = util.getencdeckey();
			RegstrationDtls registrationdtls = mapper1.readValue(decreq2.toJSONString(), RegstrationDtls.class);
			registrationdtls.setPassword(aescrypt.encrypt(strpassword,strencdeckey));
			LoginResponse responseData = visitormgmtservice.registrationtls(registrationdtls);
			if (stradstatus.equalsIgnoreCase("failure")) 
			{ 
			   ApiResponse errorResponse = new ApiResponse("Invalid Credentials","failure",null,null,true); 
			   strespdata = mapper.writeValueAsString(errorResponse); 
			   encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			   String decdresp = aescrypt.decrypt(encresp, strseckeyreq2);
			   return encresp; 
			}
			
			if (stradstatus.equalsIgnoreCase("Success"))
			{	
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(responseData.getEmpId(), usrpass));
			String usrname = authentication.getName();
			User user = new User(usrname, "");
			token = jwtUtil.createToken(user);
			}
			
			ApiResponse successResponse = new ApiResponse(successMessage, "Success", responseData, token);
			strespdata = mapper.writeValueAsString(successResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			return encresp;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			String errorMessage = "Failed";
			ApiResponse errorResponse = new ApiResponse(errorMessage, "failure", null, token);
			strespdata = mapper.writeValueAsString(errorResponse);
			encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
			String decresp = aescrypt.decrypt(encresp, strseckeyreq2);
			return encresp;
		}
	}

	

	@PostMapping("/encAppVersionInfo")
	public String versioninfo(@RequestBody JSONObject vstdtls, 
			@RequestHeader String reqId) throws JsonProcessingException, ParseException 
	{
		successMessage="success";
		strencreq = Util.getencreq1(vstdtls);
		encreq1 = strencreq;
		strseckeyreq2 = util.getstatickey(reqId);
		JSONParser parser = new JSONParser();
		JSONObject decreq2 = null;
		try 
		{
			decreq2 = (JSONObject) parser.parse(aescrypt.decrypt(encreq1, strseckeyreq2));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		 
		List<VersionMst> lstvrcode= new ArrayList<VersionMst>();
		try
		{
		lstvrcode=  verservice.getappdtls(decreq2.get("app_name").toString(), decreq2.get("device_os").toString());
		ApiResponse successResponse = new ApiResponse(successMessage, "Success", lstvrcode);
		strespdata = mapper.writeValueAsString(successResponse);
		encresp = aescrypt.encrypt(strespdata, strseckeyreq2);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return encresp;
	}
	

}
