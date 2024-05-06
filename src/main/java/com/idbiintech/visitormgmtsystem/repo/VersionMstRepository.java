package com.idbiintech.visitormgmtsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idbiintech.visitormgmtsystem.model.VerMstDtls;
import com.idbiintech.visitormgmtsystem.model.VersionMst;
import com.idbiintech.visitormgmtsystem.model.VisitorDtls;
import java.util.List;


public interface VersionMstRepository extends JpaRepository<VersionMst,Integer>
{
	
	public List<VersionMst> findByAppname(String appname);
	
	
	@Query(value="select * from vermst_dtls vr where vr.appname=?1 and vr.deviceos=?2", nativeQuery=true)
	public List<VersionMst> getappdtls(String appname, String deviceos);

	
	

}
