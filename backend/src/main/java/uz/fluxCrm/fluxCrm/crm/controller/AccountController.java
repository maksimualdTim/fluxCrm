package uz.fluxCrm.fluxCrm.crm.controller;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import uz.fluxCrm.fluxCrm.crm.dto.RegisterDto;
import uz.fluxCrm.fluxCrm.crm.service.AccountService;


@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
    private final AccountService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
    }
    
    
}
