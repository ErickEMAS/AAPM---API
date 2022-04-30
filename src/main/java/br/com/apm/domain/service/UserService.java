package br.com.apm.domain.service;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.UserAPI;
import org.apache.catalina.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String signUp(SignUpDTO signUpDTO);

    UserDTO signUpStepOne(String cpf);

    UserDTO signUpStepTwo(SignUpDTO signUpDTO);

    String confirmEmail(ConfirmCodeDTO confirmEmailDTO);

    String sendCode(ConfirmCodeDTO confirmEmailDTO);

    String changePasswordStepOne(ChangePasswordDTO changePasswordDTO);

    String changePasswordStepTwo(ChangePasswordDTO changePasswordDTO);

    UserDTO changeEmail(ChangeEmaildDTO changeEmaildDTO);

    UserDTO updateUser(UpdateUserDTO updateUserDTO);

    String deleteUser(UUID userId);

    String reactivateUser(ReactivateUserDTO reactivateUserDTO);

    String signUpAdmin(SignUpDTO userWithCPF);

    List<UserAPI> getAdmins(String roleName);
}
