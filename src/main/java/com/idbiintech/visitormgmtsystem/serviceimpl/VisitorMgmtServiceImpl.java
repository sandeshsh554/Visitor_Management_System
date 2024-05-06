package com.idbiintech.visitormgmtsystem.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.format.datetime.standard.DateTimeContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idbiintech.visitormgmtsystem.model.LoginOTPResponse;
import com.idbiintech.visitormgmtsystem.model.LoginResponse;
import com.idbiintech.visitormgmtsystem.model.MstVisitorDtls;
import com.idbiintech.visitormgmtsystem.model.OTPdtls;
import com.idbiintech.visitormgmtsystem.model.RegstrationDtls;
import com.idbiintech.visitormgmtsystem.model.VisitorDtls;
import com.idbiintech.visitormgmtsystem.repo.EmpMasterRepository;
import com.idbiintech.visitormgmtsystem.repo.OTPdtlsRepository;
import com.idbiintech.visitormgmtsystem.repo.RegistrationRepository;
import com.idbiintech.visitormgmtsystem.repo.VisitorMgmtRepository;
import com.idbiintech.visitormgmtsystem.service.VisitorMgmtService;
import com.idbiintech.visitormgmtsystem.util.Util;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VisitorMgmtServiceImpl implements VisitorMgmtService {

	@Autowired
	VisitorMgmtRepository visitormgmtrepo;

	@Autowired
	EmpMasterRepository empMasterRepository;

	@Autowired
	RegistrationRepository registrationRepo;

	@Autowired
	OTPdtlsRepository otpdtlsrepository;

	@Autowired
	LoginResponse loginresponse;

	@Autowired
	LoginOTPResponse loginOTPResponse;

	@Autowired
	OTPdtls otpdtls;

	VisitorDtls visitrodtls;

	String strotp = "";

	String strkey = "";

	String strgenotp;

	@Autowired
	Util util;

	Integer vistcnt = null;

	@Autowired
	HttpServletRequest req;

	MstVisitorDtls addMasterEmp;

	String struniqno;

	String strusrrole;

	String strvalotp;

	Integer empcnt = 0;
	

	List<MstVisitorDtls> emplst = new ArrayList<MstVisitorDtls>();

	public ResponseEntity<String> visitordtls(@RequestHeader String visitorname, @RequestHeader String companyname,
			@RequestHeader String location, @RequestHeader String mobileno) {
		System.out.println(" In visitor details service impl ");
		visitrodtls = new VisitorDtls();
		visitrodtls.setVisitorname(visitorname);
		visitrodtls.setCompanyname(companyname);
		visitrodtls.setAddress(location);
		visitrodtls.setMobileno(mobileno);
		visitormgmtrepo.save(visitrodtls);
		return new ResponseEntity<String>("VisitorDetails Saved Successfully", HttpStatus.OK);
	}

	public ResponseEntity<String> vstdtls(VisitorDtls visitordtls1)

	{
		visitormgmtrepo.save(visitordtls1);
		return new ResponseEntity<String>("VisitorDetails Saved Successfully", HttpStatus.OK);
	}

	public VisitorDtls visitordtls(VisitorDtls visitordtls) throws IOException 
	{
		Integer empcnt = empMasterRepository.chkempcnt(Integer.parseInt(visitordtls.getEmpid()));

		vistcnt = visitormgmtrepo.chkvstid(datetostring());

		if (vistcnt == null) {
			visitordtls.setVstid(1);
		}

		else 
		{
			System.out.println("vistcnt--------------" + vistcnt);
			vistcnt = vistcnt + 1;
			System.out.println("vistcnt-------11111------------" + vistcnt);
			visitordtls.setVstid(vistcnt);
		}

		struniqno = datetostring() + "_" + visitordtls.getVstid();
		
		System.out.println("struniqno----------------"+struniqno);
		
		
		String ip = InetAddress.getLocalHost().getHostAddress();
		String ip1 = InetAddress.getLoopbackAddress().getHostAddress();
		int intport = req.getServerPort();
		String strscheme = req.getScheme();
		System.out.println("strscheme--------------" + strscheme);
		String strurl = strscheme + "://" + ip + ":" + String.valueOf(intport) + "/encvisitormgmt/public/images/";
		System.out.println("strurl-------------------" + strurl);
		String empid = visitordtls.getEmpid();
		// conofencstrtoimage(visitordtls.getIdentityproofimage(),
		// visitordtls.getStrcapimageenccode(),struniqno);
		System.out.println("struniqno--------------" + struniqno);
		// conidencodetoimage(visitordtls.getIdentityproofimage(),struniqno);

		conidencodetoimage(visitordtls.getIdentityproofimage(), struniqno);
		convisencodetoimage(visitordtls.getStrcapimageenccode(), struniqno);
		String strimagecode = "visit_" + struniqno + ".jpg";
		String strident = "ident_" + struniqno + ".jpg";

		MstVisitorDtls mstvstdtls = findDetailsById(Integer.parseInt(empid));
		visitordtls.setIdentityproofimage(strurl + strident);
		visitordtls.setStrcapimageenccode(strurl + strimagecode);
		visitordtls.setCreate_date(datetostring());
		visitordtls.setIntime(timetostring());
		visitordtls.setRequestStatus("pending");
		VisitorDtls vstdls = visitormgmtrepo.save(visitordtls);

		
		
		
		
		try 
		{
			if (empcnt == null) 
			{
				Optional<MstVisitorDtls> mstempdtls = empMasterRepository.findById(Integer.parseInt(visitordtls.getEmpid()));
				if (mstempdtls.isPresent()) 
				{
					MstVisitorDtls mstemployeedtls = mstempdtls.get();
					mstemployeedtls.setEmpcnt(1);
					empMasterRepository.save(mstemployeedtls);
				}
			}

			else 
			{
				
				Optional<MstVisitorDtls> mstempdtls = empMasterRepository.findById(Integer.parseInt(visitordtls.getEmpid()));
				if (mstempdtls.isPresent()) 
				{
					MstVisitorDtls mstemployeedtls = mstempdtls.get();
					mstemployeedtls.setEmpcnt(mstemployeedtls.getEmpcnt() + 1);
					empMasterRepository.save(mstemployeedtls);
				}

			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return vstdls;
	}

	@Override
	public MstVisitorDtls findDetailsById(int id) {
		return empMasterRepository.findById(id).orElseThrow(null);
	}

	@Override
	public ResponseEntity<List> findDetailsByEmpName(String empname) {
		List<MstVisitorDtls> lstmstvst = empMasterRepository.findByEmpname(empname);
		return new ResponseEntity<List>(lstmstvst, HttpStatus.OK);
	}

	@Override
	public void addMasterEmployee2(MstVisitorDtls mstVisitorDtls) {
		empMasterRepository.save(mstVisitorDtls);
	}

	@Override
	public ResponseEntity<List> findAllEmpDetails() {
		List<MstVisitorDtls> lstmstvstdtls = new ArrayList<>();
		lstmstvstdtls = empMasterRepository.findAll();

		empMasterRepository.findAll().forEach(lstmstvstdtls::add);
		System.out.println("lstmstvstdtls---------------" + lstmstvstdtls);
		return new ResponseEntity<List>(lstmstvstdtls, HttpStatus.OK);
	}

	public List<MstVisitorDtls> getempdtls() {
		List<MstVisitorDtls> lstmstvstdtls = new ArrayList<>();
		lstmstvstdtls = empMasterRepository.findAll();
		return lstmstvstdtls;
	}

	public List<VisitorDtls> findvstdtlsbyempid(String empid) {
		// List<VisitorDtls> lstempvstdlts =
		// visitormgmtrepo.findByEmpidOrderByVisitoridDesc(empid);
		List<VisitorDtls> lstempvstdlts = null;

		try {
			Integer id = Integer.parseInt(empid);
			Optional<MstVisitorDtls> mstempdtls = empMasterRepository.findById(id);

			if (mstempdtls.isPresent()) {
				MstVisitorDtls mstemployeedtls = mstempdtls.get();
				strusrrole = mstemployeedtls.getUserRole();
				mstemployeedtls.setEmpcnt(0);
				empMasterRepository.save(mstemployeedtls);
			}

			if (strusrrole.equals("employee")) {
				// lstempvstdlts = visitormgmtrepo.findByEmpidOrderByVisitoridDesc(empid);//
				// findFirst10ByEmpidOrderByVisitoridDesc

				lstempvstdlts = visitormgmtrepo.findFirst100ByEmpidOrderByVisitoridDesc(empid);
			}

			else {
				lstempvstdlts = visitormgmtrepo.findAllByOrderByVisitoridDesc();

			}
			System.out.println("list size -----------" + lstempvstdlts.size());
		}

		catch (Exception e) 
		{
			e.printStackTrace();

		}
		return lstempvstdlts;
	}

	public VisitorDtls findvstdtls(String empid, Integer Id) {
		VisitorDtls lstempvstdlts = visitormgmtrepo.findByEmpidAndVisitorid(empid, Id);
		return lstempvstdlts;
	}

	@Override
	public Optional<VisitorDtls> updtvstdtls(String empid, Integer Id, String reqstatus, String declinedreason) {
		Optional<VisitorDtls> vstdtls = visitormgmtrepo.findByVisitoridAndEmpid(Id, empid);
		if (vstdtls.isPresent()) {
			VisitorDtls visitordtls = vstdtls.get();
			visitordtls.setRequestStatus(reqstatus);
			visitordtls.setDeclinedreason(declinedreason);
			visitormgmtrepo.save(visitordtls);
		}
		return vstdtls;
	}

	public LoginResponse registrationtls(RegstrationDtls registrationdtls) {

		MstVisitorDtls mstvistdtls = null;
		MstVisitorDtls mstemployeedtls = null;
		try 
		{
			Integer empid = Integer.parseInt(registrationdtls.getEmpId());
			try 
			{
				Optional<MstVisitorDtls> mstempdtls = empMasterRepository.findById(empid);
				if (mstempdtls.isPresent()) 
				{
					
					mstemployeedtls = mstempdtls.get();
					loginresponse.setId(registrationdtls.getId());
					loginresponse.setEmpname(mstemployeedtls.getEmpname());
					loginresponse.setEmpId(registrationdtls.getEmpId());
					loginresponse.setUserRole(mstemployeedtls.getUserRole());
					loginresponse.setDepartment(mstemployeedtls.getDepartment());
					loginresponse.setDesignation(mstemployeedtls.getDesignation());
					loginresponse.setMobileno(mstemployeedtls.getMobileno());
					loginresponse.setFirebaseToken(registrationdtls.getFirebaseToken());
					loginresponse.setDeviceOS(registrationdtls.getDeviceOS());
					loginresponse.setImeiNumber(registrationdtls.getImeiNumber());
					loginresponse.setVersionCode(registrationdtls.getVersionCode());
					loginresponse.setVersionName(registrationdtls.getVersionName());
					loginresponse.setEmailid(mstemployeedtls.getEmailid());
					
					
					
					mstemployeedtls.setVersionCode(registrationdtls.getVersionCode());
					mstemployeedtls.setVersionName(registrationdtls.getVersionName());
					mstemployeedtls.setImeiNumber(registrationdtls.getImeiNumber());
					mstemployeedtls.setFirebaseToken(registrationdtls.getFirebaseToken());
					mstemployeedtls.setDeviceOS(registrationdtls.getDeviceOS());
					mstemployeedtls.setEmppass(registrationdtls.getPassword());					
					empMasterRepository.save(mstemployeedtls);
				}
				
				else
				{
					loginresponse.setId(registrationdtls.getId());
					loginresponse.setEmpname("");
					loginresponse.setEmpId(registrationdtls.getEmpId());
					loginresponse.setUserRole("");
					loginresponse.setDepartment("IT");
					loginresponse.setDesignation("");
					loginresponse.setMobileno("");
					loginresponse.setFirebaseToken(registrationdtls.getFirebaseToken());
					loginresponse.setDeviceOS(registrationdtls.getDeviceOS());
					loginresponse.setImeiNumber(registrationdtls.getImeiNumber());
					loginresponse.setVersionCode(registrationdtls.getVersionCode());
					loginresponse.setVersionName(registrationdtls.getVersionName());
					loginresponse.setEmailid("");
				}
			}

			catch (Exception e) 
			{

				e.printStackTrace();
				return loginresponse;

			}

		}

		catch (Exception e) {
			e.printStackTrace();

		}

		finally 
		{
			if (mstvistdtls != null) 
			{
				mstvistdtls = null;
			}
			
			if (mstemployeedtls != null) 
			{
				mstemployeedtls = null;
			}

		}

		System.out.println("Login response obj -------------" + loginresponse);
		return loginresponse;
	}

	public LoginOTPResponse fetchregdtsls(RegstrationDtls registrationdtls) 
	{
		try 
		{
			Integer empid = Integer.parseInt(registrationdtls.getEmpId());
			otpdtls.setAccountName("i ATITHI");
			otpdtls.setEmpid(empid);
			otpdtls.setOtp(registrationdtls.getOtp());
			strvalotp = validateotp(otpdtls);
			if (strvalotp.equalsIgnoreCase("invalid otp")) 
			{
				System.out.println("Incorrect OTP");
				loginOTPResponse.setMsg("Incorrect OTP");
			}

			else 
			{
				loginOTPResponse.setMsg("1");
			}
			
			MstVisitorDtls mstvistdtls = findDetailsById(empid);
			loginOTPResponse.setEmpId(registrationdtls.getEmpId());
			loginOTPResponse.setMobileNumber(mstvistdtls.getMobileno());
			loginOTPResponse.setUserRole(mstvistdtls.getUserRole());
			loginOTPResponse.setName(mstvistdtls.getEmpname());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return loginOTPResponse;
	}

	public void conofencstrtoimage(String stridentproofimage, String strcapimgcode, String struniqno) {
		try {

			ObjectMapper mapper1 = new ObjectMapper();
			ObjectMapper mapper2 = new ObjectMapper();
			
			// String stridntprfimg =
			// mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(stridentproofimage.replace("\n","").replace("\\/","/"));
			// String strimgcode =
			// mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(strcapimgcode.replace("\n","").replace("\\/","/")
			// );

			String stridntprfimg = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(stridentproofimage);
			String strimgcode = mapper2.writerWithDefaultPrettyPrinter().writeValueAsString(strcapimgcode);
			Base64 decoder = new Base64();
			byte[] imgBytes = decoder.decode(stridntprfimg);
			byte[] imgBytes1 = decoder.decode(strimgcode);

			try (FileOutputStream osf = new FileOutputStream("public/images/idenimg_" + struniqno + ".jpg");
					FileOutputStream osf1 = new FileOutputStream("public/images/visimg" + struniqno + ".jpg")) 
			{
				osf.write(imgBytes);
				osf1.write(imgBytes1);
				osf.flush();
				osf1.flush();
			}

		}

		catch (Exception e) 
		{
			e.printStackTrace();
		}

		finally {

		}

	}

	public void conidencodetoimage(String stridentproofimage, String struniqno) {

		ObjectMapper mapper1 = new ObjectMapper();
		try {
			String stridntprfimg = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(stridentproofimage);
			Base64 decoder = new Base64();
			byte[] imgintBytes = decoder.decode(stridntprfimg);

			try (FileOutputStream osf = new FileOutputStream("public/images/ident_" + struniqno + ".jpg")) 
			{
				osf.write(imgintBytes);
				osf.flush();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (mapper1 != null) {
				mapper1 = null;

			}

		}

	}

	public void convisencodetoimage(String strvistimage, String struniqno) {

		ObjectMapper mapper = new ObjectMapper();
		try 
		{
			String strvstimg = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(strvistimage);
			Base64 decoder = new Base64();
			byte[] imgvstbyt = decoder.decode(strvstimg);

			try (FileOutputStream osf1 = new FileOutputStream("public/images/visit_" + struniqno + ".jpg")) 
			{
				osf1.write(imgvstbyt);
				osf1.flush();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			if (mapper != null) {
				mapper = null;

			}

		}

	}

	public String generateuniqno() {
		SecureRandom scrno = new SecureRandom();
		int result = scrno.nextInt(1000000);
		String resultStr = result + "";
		if (resultStr.length() != 2)
			for (int x = resultStr.length(); x < 6; x++)
				resultStr = "0" + resultStr;
		return resultStr;

	}

	/*
	 * public static void main(String[] args) { timetostring(); datetostring();
	 * 
	 * }
	 */

	public static String timetostring() {
		LocalDateTime currentLocalDateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String formattedDateTime = currentLocalDateTime.format(dateTimeFormatter);
		System.out.println("Formatted LocalDateTime in String format : " + formattedDateTime);
		return formattedDateTime;
	}

	public static String datetostring()   
	{
		LocalDate date = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		String formattedDate = date.format(dateTimeFormatter);
		
		
		
		
		
		
		return formattedDate;
	}

	@Override
	public Optional<VisitorDtls> updtvstouttime(String empid, Integer Id) {
		Optional<VisitorDtls> vstdtls = visitormgmtrepo.findByVisitoridAndEmpid(Id, empid);
		if (vstdtls.isPresent()) 
		{
			VisitorDtls visitordtls = vstdtls.get();
			visitordtls.setOuttime(timetostring());
			visitormgmtrepo.save(visitordtls);
		}
		return vstdtls;
	}

	@Override
	public String chkseckeyexist(String accountname, int empid) {
		return otpdtlsrepository.chkseckeyexist(accountname, empid);
	}

	@Override
	public void saveseckeydtls(OTPdtls otpdtls) {
		otpdtlsrepository.save(otpdtls);
	}

	@PostMapping("/generateotp")
	public String generateotp(OTPdtls otpdtls) throws Exception {
		String code = null;
		String secretKey = null;
		try {
			String stractname = otpdtls.getAccountName();
			int v_empId = otpdtls.getEmpid();
			String secreteKeyPresent = chkseckeyexist(stractname, v_empId);

			if (secreteKeyPresent == null) {
				secretKey = util.generateSecretKey();
				code = util.getTOTPCode(secretKey);
				otpdtls.setSecretKey(secretKey);
				String email = stractname;
				String companyName = "IDBIINTECH";
				saveseckeydtls(otpdtls);
				return secretKey;
			} else {
				secretKey = secreteKeyPresent;
				code = util.getTOTPCode(secreteKeyPresent);
				otpdtls.setSecretKey(secreteKeyPresent);
				saveseckeydtls(otpdtls);
			}

		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return secretKey;
	}

	@PostMapping("/validateotp")
	public String validateotp(OTPdtls otpdtls) throws Exception {

		String code = "";

		String otpstatus = "";

		try {
			String strentrotp = otpdtls.getOtp();
			System.out.println("strentrotp---------------" + strentrotp);

			String stractname = otpdtls.getAccountName();

			System.out.println("stractname--------------" + stractname);
			int empId = otpdtls.getEmpid();
			System.out.println("empid-----------" + empId);

			String secreteKeyPresent = chkseckeyexist(stractname, empId);

			if (secreteKeyPresent != null) {
				code = util.getTOTPCode(secreteKeyPresent);

				System.out.println("code------------" + code);

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
			return "invalid otp";

		}

	}

	public Integer getempcnt(Integer empid) {
		try {
			Optional<MstVisitorDtls> mstempdtls = empMasterRepository.findById(empid);
			if (mstempdtls.isPresent()) {
				MstVisitorDtls mstemployeedtls = mstempdtls.get();
				empcnt = mstemployeedtls.getEmpcnt();
				if (empcnt == null) {
					empcnt = 0;

				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();

		}

		return empcnt;

	}

	public List<MstVisitorDtls> getemplstbyrole(String role) 
	{
		emplst = empMasterRepository.findByUserRoleOrderByEmpname(role);
		return emplst;
	}

	public String chkempvststatus(VisitorDtls visitordtls) 
	{
		String reqstatus = visitormgmtrepo.chkempvststatus(visitordtls.getEmpid(),visitordtls.getVisitorname().toLowerCase(), 
				                                           visitordtls.getMobileno(), datetostring(), "pending");
		return reqstatus;
	}

	public static String getlastweekdate() 
	{
		LocalDate lastweekStartDate = LocalDate.now().minusWeeks(1);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		String formattedDate = lastweekStartDate.format(dateTimeFormatter);
		return formattedDate;
	}

	public static void main(String[] args) throws Exception 
	{
		
		String startDateString = "08-12-2017";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String s = LocalDate.parse(startDateString, formatter).format(formatter2);
	    System.out.println("s----------"+s);
		datetostring();
	}

}
