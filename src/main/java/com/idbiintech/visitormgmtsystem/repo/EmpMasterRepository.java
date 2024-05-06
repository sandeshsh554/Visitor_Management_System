package com.idbiintech.visitormgmtsystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idbiintech.visitormgmtsystem.model.MstVisitorDtls;

public interface EmpMasterRepository extends JpaRepository<MstVisitorDtls,Integer> {

	
	public List<MstVisitorDtls> findByEmpname(String empname);
	
	
	@Query(value="select empcnt from mst_emp_dtls mst where mst.id = ?1",nativeQuery = true)
	public Integer chkempcnt(Integer empid);
	
	
	public List<MstVisitorDtls> findByUserRoleOrderByEmpname(String role);
	
}
