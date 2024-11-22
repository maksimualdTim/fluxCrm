package uz.fluxCrm.fluxCrm.crm.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.entity.Account;
import uz.fluxCrm.fluxCrm.crm.repository.AccountRepository;

@Component
@AllArgsConstructor
public class TenantContextFilter extends OncePerRequestFilter{
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Check authentication");
                System.out.println(authentication.getName());
            if(authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
                Long accountId = jwt.getClaim("current_account_id");
                System.out.println("account id " + accountId);
    
                if(accountId != null) {
                    Account account = accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account not found by ID: " + accountId));
                    TenantContext.setCurrentAccountId(accountId);
                }
            }
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
    
}
