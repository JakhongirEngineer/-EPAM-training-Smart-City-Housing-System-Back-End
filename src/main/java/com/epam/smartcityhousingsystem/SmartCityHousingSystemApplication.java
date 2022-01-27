package com.epam.smartcityhousingsystem;

import com.epam.smartcityhousingsystem.entities.HMACSecretKey;
import com.epam.smartcityhousingsystem.repositories.AdminRepository;
import com.epam.smartcityhousingsystem.repositories.HMACSecretKeyRepository;
import com.epam.smartcityhousingsystem.repositories.RoleRepository;
import com.epam.smartcityhousingsystem.repositories.UserRepository;
import com.epam.smartcityhousingsystem.security.Admin;
import com.epam.smartcityhousingsystem.security.Role;
import com.epam.smartcityhousingsystem.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

/**
 * SmartCityHousingSystemApplication is the entrypoint for the application.
 *
 * @author Jakhongir Rasulov
 * @project smartcity-housing-system
 */

@RequiredArgsConstructor
@SpringBootApplication
public class SmartCityHousingSystemApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final HMACSecretKeyRepository hmacSecretKeyRepository;

    public static void main(String[] args) {
        SpringApplication.run(SmartCityHousingSystemApplication.class, args);
    }

    /**
     * Creates roles, users, hmac keys, and populates
     * database with them
     */
    @Override
    public void run(String... args) throws Exception {

        Role role1 = new Role();
        Role role2 = new Role();
        role1.setName("ADMIN");
        role2.setName("RESIDENT");
        roleRepository.save(role1);
        roleRepository.save(role2);

        Admin admin = new Admin();
        admin.setEmail("admin@gmail.com");
        admin.setEnabled(true);
        admin.setFirstName("admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setLastName("lastName");
        admin.setSignedUp(true);
        admin.setTokenExpired(false);
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        admin.setRoles(roles);
        adminRepository.save(admin);

        User user = new User();
        Set<Role> firstUserRoles = new HashSet<>();
        firstUserRoles.add(role1);
        firstUserRoles.add(role2);
        user.setRoles(firstUserRoles);
        user.setEnabled(true);
        user.setSignedUp(true);
        user.setTokenExpired(false);
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("user");
        user.setLastName("user");
        userRepository.save(user);

        HMACSecretKey citizenAccount = new HMACSecretKey();
        citizenAccount.setComponentName("CITIZEN_ACCOUNT");
        citizenAccount.setSecretKey("citizenAccountKey");
        hmacSecretKeyRepository.save(citizenAccount);

        HMACSecretKey housingSystem = new HMACSecretKey();
        housingSystem.setComponentName("HOUSING_SYSTEM");
        housingSystem.setSecretKey("housingSystemKey");
        hmacSecretKeyRepository.save(housingSystem);

        HMACSecretKey cityManagement = new HMACSecretKey();
        cityManagement.setComponentName("CITY_MANAGEMENT");
        cityManagement.setSecretKey("cityManagementSecretKey");
        hmacSecretKeyRepository.save(cityManagement);

    }
}
