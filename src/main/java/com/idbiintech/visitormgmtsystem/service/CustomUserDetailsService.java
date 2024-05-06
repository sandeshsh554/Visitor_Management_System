package com.idbiintech.visitormgmtsystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.idbiintech.visitormgmtsystem.model.MstVisitorDtls;
import com.idbiintech.visitormgmtsystem.model.User;
import com.idbiintech.visitormgmtsystem.repo.EmpMasterRepository;
import com.idbiintech.visitormgmtsystem.repo.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
	private final UserRepository userRepository;
	
	@Autowired
	EmpMasterRepository empMasterRepository;
	
	@Autowired
	private Environment env;
	
	MstVisitorDtls mstvst;
	
	UserDetails userDetails;
	
	String usrpass="";
	
	
    public CustomUserDetailsService(UserRepository userRepository) 
    {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String empid) throws UsernameNotFoundException 
    {
		Optional<MstVisitorDtls> mstempdtls = empMasterRepository.findById(Integer.parseInt(empid));
		if(mstempdtls.isPresent())
		{
		mstvst = mstempdtls.get();
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        usrpass = env.getProperty("usrpass");
        userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(String.valueOf(mstvst.getId()))
                        .password(usrpass)
                        .roles(mstvst.getUserRole())
                        .build();
		}
        return userDetails;
        
    }
}
