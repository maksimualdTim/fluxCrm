package uz.fluxCrm.fluxCrm.crm.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.config.TenantContext;
import uz.fluxCrm.fluxCrm.crm.dto.RegisterDto;
import uz.fluxCrm.fluxCrm.crm.entity.Account;
import uz.fluxCrm.fluxCrm.crm.entity.User;
import uz.fluxCrm.fluxCrm.crm.repository.AccountRepository;
import uz.fluxCrm.fluxCrm.crm.repository.UserRepository;

@Service
@AllArgsConstructor
public class AccountService {
    private final EmailService emailService;
    private final KeycloakService keycloakService;
    private final AccountRepository accountRepository;
    private final PipelineService pipelineService;
    private final UserRepository userRepository;


    public void register(RegisterDto registerDto) {
        Account account = new Account();
        account.setName(registerDto.getAccountName());
        account.setCurrency("RU");
        account.setLanguage("RU");
        account.setTimezone("Moscow");
        account.setSubdomain("test.fluxcrm.com");

        account = accountRepository.save(account);

        String password = PasswordGenerator.generatePassword(10);
        UUID uuid = keycloakService.createUser(registerDto.getFirstName(), registerDto.getLastName(), registerDto.getEmail(), password);

        TenantContext.setCurrentAccountId(account.getId());

        User user = new User();
        user.setEmail(registerDto.getEmail());
        user.setKeycloakId(uuid);
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());

        userRepository.save(user);
        pipelineService.createPipeline("Воронка", true);
        emailService.sendEmail(registerDto.getEmail(), "Registration", "Your password: " + password);
    }

    public class PasswordGenerator {

        private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
        private static final String NUMBERS = "0123456789";
        private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?/";

        private static final SecureRandom random = new SecureRandom();

        public static String generatePassword(int length) {
            if (length < 8) {
                throw new IllegalArgumentException("Password length must be at least 8 characters");
            }

            // Ensure the password includes at least one character from each category
            StringBuilder password = new StringBuilder();
            password.append(getRandomChar(UPPERCASE_LETTERS));
            password.append(getRandomChar(LOWERCASE_LETTERS));
            password.append(getRandomChar(NUMBERS));
            password.append(getRandomChar(SPECIAL_CHARACTERS));

            // Fill the remaining length with random characters from all categories
            String allCharacters = UPPERCASE_LETTERS + LOWERCASE_LETTERS + NUMBERS + SPECIAL_CHARACTERS;
            for (int i = 4; i < length; i++) {
                password.append(getRandomChar(allCharacters));
            }

            // Shuffle the characters to ensure randomness
            List<Character> shuffledPassword = new ArrayList<>();
            for (char c : password.toString().toCharArray()) {
                shuffledPassword.add(c);
            }
            Collections.shuffle(shuffledPassword);

            StringBuilder finalPassword = new StringBuilder();
            for (char c : shuffledPassword) {
                finalPassword.append(c);
            }

            return finalPassword.toString();
        }

        private static char getRandomChar(String characters) {
            return characters.charAt(random.nextInt(characters.length()));
        }
    }
}
