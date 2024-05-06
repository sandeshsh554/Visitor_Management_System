package com.idbiintech.visitormgmtsystem.service;

import java.util.List;

import com.idbiintech.visitormgmtsystem.model.VersionMst;

public interface VersionService 
{
	
  public List<VersionMst> findByAppname(String appname);
  
  public List<VersionMst> getappdtls(String appname,String deviceOS);
  
  

}
