package com.idbiintech.visitormgmtsystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idbiintech.visitormgmtsystem.model.RegstrationDtls;

public interface RegistrationRepository extends JpaRepository<RegstrationDtls,Integer> 
{

public List<RegstrationDtls> findByEmpId(String empid);



}
