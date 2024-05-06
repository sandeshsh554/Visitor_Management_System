package com.idbiintech.visitormgmtsystem.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idbiintech.visitormgmtsystem.model.VisitorDtls;


public interface VisitorMgmtRepository extends JpaRepository<VisitorDtls,Integer>   
{

	
	public List<VisitorDtls> findByEmpidOrderByVisitoridDesc(String empid);
	  
	
	public List<VisitorDtls> findFirst100ByEmpidOrderByVisitoridDesc(String empid);

	
	
	public VisitorDtls findByEmpidAndVisitorid(String empid,Integer Id);
	
	
	public Optional<VisitorDtls> findByVisitoridAndEmpid(Integer Id,String empid); 
	
	
	public List<VisitorDtls> findAllByOrderByVisitoridDesc();
	
	
	@Query(value="select max(vstid) from visitor_dtls vst where vst.create_date=?", nativeQuery = true)
	public Integer chkvstid(String todaysdate);
	
	
	@Query(value="select request_status from visitor_dtls vst where vst.empid= ?1 "
			+ " and vst.name=?2 and vst.mobile_no=?3  and vst.create_date=?4 and vst.request_status=?5", nativeQuery = true)
    public String chkempvststatus( String empid, String visitorname, 
    		                       String mobileno, String todaysdate, String status);
	
	@Query(value="select * from visitor_dtls vst where vst.create_date=?1", nativeQuery=true)
	public List<VisitorDtls> vstlstweekdtls(String createdate);
	
	
}
