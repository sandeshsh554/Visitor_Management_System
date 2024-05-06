package com.idbiintech.visitormgmtsystem.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idbiintech.visitormgmtsystem.model.VerMstDtls;
import com.idbiintech.visitormgmtsystem.model.VersionMst;
import com.idbiintech.visitormgmtsystem.repo.VersionMstRepository;
import com.idbiintech.visitormgmtsystem.service.VersionService;

@Service
public class VersionServiceImpl implements VersionService
{
	@Autowired
	private VersionMstRepository versionmstrepo;
	
	
	  public List<VersionMst> findByAppname(String appname)
	  {
		  return versionmstrepo.findByAppname(appname);
	  }
	  
	  public List<VersionMst> getappdtls(String appname,String deviceOS)
	  {
		  return versionmstrepo.getappdtls(appname,deviceOS);

		  
	  }


}
