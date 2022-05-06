package br.com.apm.application.controllers;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.UserAPI;
import br.com.apm.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sign-up")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> signUp(@RequestBody SignUpDTO userWithCPF) {
        try {
            return ResponseEntity.ok(userService.signUp(userWithCPF));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    @PostMapping("/sign-up-admin")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> signUpAdmin(@RequestBody SignUpDTO userWithCPF) {
        try {
            return ResponseEntity.ok(userService.signUpAdmin(userWithCPF));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/delete-user")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> deleteUser(@RequestBody UUID userId) {
        try {
            return ResponseEntity.ok(userService.deleteUser(userId));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/reactivate-user")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> reactivateUser(@RequestBody ReactivateUserDTO reactivateUserDTO) {
        try {
            return ResponseEntity.ok(userService.reactivateUser(reactivateUserDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/get-users")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Object> getAdmins(@RequestParam(value = "roleName", required = false) String roleName, Pageable pageable) {
        try {
            return ResponseEntity.ok(userService.getUsers(roleName, pageable));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/sign-up-step1")
    public ResponseEntity<Object> signUpStepOne(@RequestParam("cpf") String cpf) {
        try {
            return ResponseEntity.ok(userService.signUpStepOne(cpf));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/sign-up-step2")
    public ResponseEntity<Object> signUpStepTwo(@RequestBody SignUpDTO signupDTO) {
        try {
            return ResponseEntity.ok(userService.signUpStepTwo(signupDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<Object> confirmEmail(@RequestBody ConfirmCodeDTO confirmEmailDTO) {
        try {
            return ResponseEntity.ok(userService.confirmEmail(confirmEmailDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/send-code")
    public ResponseEntity<Object> sendCode(@RequestBody ConfirmCodeDTO confirmEmailDTO) {
        try {
            return ResponseEntity.ok(userService.sendCode(confirmEmailDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/change-password-step1")
    public ResponseEntity<Object> changePasswordStepOne(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            return ResponseEntity.ok(userService.changePasswordStepOne(changePasswordDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/change-password-step2")
    public ResponseEntity<Object> changePasswordStepTwo(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            return ResponseEntity.ok(userService.changePasswordStepTwo(changePasswordDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/change-email")
    public ResponseEntity<Object> changeEmail(@RequestBody ChangeEmaildDTO changeEmaildDTO) {
        try {
            return ResponseEntity.ok(userService.changeEmail(changeEmaildDTO));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

