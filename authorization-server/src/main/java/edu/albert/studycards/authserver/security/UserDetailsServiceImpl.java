package edu.albert.studycards.authserver.security;

import edu.albert.studycards.authserver.domain.interfaces.AccountPersistent;
import edu.albert.studycards.authserver.domain.interfaces.ClientPersistent;
import edu.albert.studycards.authserver.repository.AccountRepository;
import edu.albert.studycards.authserver.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private AccountRepository accountRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ClientPersistent client = clientRepo.findByEmail(username)
			                          .orElseThrow(() -> new UsernameNotFoundException("Client doesn't exists"));
		AccountPersistent account = accountRepo.findByClient_Email(username)
			                            .orElseThrow(() -> new UsernameNotFoundException("Account doesn't exists"));
		
		return SecurityAccount.fromUser(client, account);
	}
	
	
}