package com.aluracursos.desafiodepracticaspringboot;

import com.aluracursos.desafiodepracticaspringboot.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafiodepracticaspringbootApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DesafiodepracticaspringbootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();
		principal.mostrarMenu();
	}
}
