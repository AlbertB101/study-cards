package edu.albert.studycards.authserver.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ClientRepositoryTest {
	
	@Autowired
	ClientRepository clientRepo;
	
	
	
}
