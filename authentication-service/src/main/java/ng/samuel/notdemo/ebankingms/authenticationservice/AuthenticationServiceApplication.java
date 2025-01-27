package ng.samuel.notdemo.ebankingms.authenticationservice;

import lombok.extern.slf4j.Slf4j;
import ng.samuel.notdemo.ebankingms.authenticationservice.entity.Role;
import ng.samuel.notdemo.ebankingms.authenticationservice.entity.User;
import ng.samuel.notdemo.ebankingms.authenticationservice.enums.Gender;
import ng.samuel.notdemo.ebankingms.authenticationservice.repository.RoleRepository;
import ng.samuel.notdemo.ebankingms.authenticationservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@EnableFeignClients
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
@EnableDiscoveryClient
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!roleRepository.existsBy()) {
                log.info("No roles found");
                try {
                    Role userRole = Role.builder().name("USER").description("USER").build();
                    Role adminRole = Role.builder().name("ADMIN").description("ADMIN").build();
                    Role superAdminRole = Role.builder().name("SUPER_ADMIN").description("SUPER_ADMIN").build();
                    roleRepository.save(userRole);
                    roleRepository.save(adminRole);
                    roleRepository.save(superAdminRole);
                    log.info("Roles 'USER', 'MODERATOR', 'ADMIN' and 'SUPER_ADMIN' created successfully'");
                } catch (Exception e) {
                    log.error("Error while creating roles", e);
                }
            }
            User user = userRepository.findByUsername("ADMINISTRATOR").orElse(null);
            if (user == null) {
                log.info("No super admin found");
                try {
                    User superAdmin = new User();
                    superAdmin.setCin("Administrator CIN");
                    superAdmin.setFirstname("Administrator's First Name");
                    superAdmin.setLastname("Administrator's Last Name");
                    superAdmin.setNationality("Administrator Nationality");
                    superAdmin.setDateOfBirth(LocalDate.of(1994, 1, 22));
                    superAdmin.setPlaceOfBirth("Administrator's Birthday");
                    superAdmin.setGender(Gender.MALE);
                    superAdmin.setUsername("ADMINISTRATOR");
                    superAdmin.setEmail("samuelgbenga972@gmail.com");
                    superAdmin.setPasswordNeedToBeModified(Boolean.TRUE);
                    //String defaultPassword = UUID.randomUUID().toString();
                    String defaultPassword = "samuel";
                    superAdmin.setPassword(passwordEncoder.encode(defaultPassword));
                    superAdmin.setEnabled(true);
                    User savedUser = userRepository.save(superAdmin);
                    log.info("SUPER ADMIN created successfully with password '{}'", defaultPassword);
                    List<Role> roleList = roleRepository.findAll();
                    savedUser.setRoles(roleList);
                    User updatedUser = userRepository.save(savedUser);
                    log.info("User updated successfully with roles '{}'", updatedUser.getRoles());
                } catch (Exception e) {
                    log.error("Error while creating super admin user", e);
                }
            }
        };
    }
}
