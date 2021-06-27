package edu.albert.studycards.authserver.security;

import edu.albert.studycards.authserver.domain.persistent.UserAccountPersistent;
import edu.albert.studycards.authserver.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserAccountRepository userAccRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccountPersistent userAcc = userAccRepo.findByEmail(username)
			                                .orElseThrow(() -> new UsernameNotFoundException("Account doesn't exists"));
		
		return SecurityAccount.fromUser(userAcc);
	}
	
	
}