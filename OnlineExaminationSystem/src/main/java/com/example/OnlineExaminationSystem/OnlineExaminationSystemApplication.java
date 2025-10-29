package com.example.OnlineExaminationSystem;

import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories
public class OnlineExaminationSystemApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(OnlineExaminationSystemApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Create default admin user if not exists
		if (userRepository.findByUsername("admin").isEmpty()) {
			User admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.setRole("ADMIN");
			admin.setEmail("admin@example.com");
			userRepository.save(admin);
		}
	}
}
