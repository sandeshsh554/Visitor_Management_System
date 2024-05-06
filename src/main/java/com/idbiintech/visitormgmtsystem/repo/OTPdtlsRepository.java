package com.idbiintech.visitormgmtsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idbiintech.visitormgmtsystem.model.OTPdtls;

public interface OTPdtlsRepository extends JpaRepository<OTPdtls,Integer> 
{
	@Query(value="select secret_key from otp_dtls u where u.account_name = ?1 and u.emp_id = ?2",nativeQuery = true)
	public String chkseckeyexist(String accountname, int empid);

	

}
