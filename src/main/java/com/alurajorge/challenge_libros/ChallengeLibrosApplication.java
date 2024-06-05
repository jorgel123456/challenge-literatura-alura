package com.alurajorge.challenge_libros;

import com.alurajorge.challenge_libros.principal.Principal;
import com.alurajorge.challenge_libros.repository.AutorRepository;
import com.alurajorge.challenge_libros.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLibrosApplication  implements CommandLineRunner {
	@Autowired
	private LibrosRepository librosRepository;
	@Autowired
	private AutorRepository  autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLibrosApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal=new Principal(librosRepository, autorRepository);
		principal.muestraElMenu();
	}
}
