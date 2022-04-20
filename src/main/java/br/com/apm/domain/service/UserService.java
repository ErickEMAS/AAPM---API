package br.com.apm.domain.service;

import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.UserAPI;

public interface UserService {

    String signUp(SignUpDTO signUpDTO);

    UserDTO signUpStepOne(SignUpDTO userWithCPF);

    UserDTO signUpStepTwo(SignUpDTO signUpDTO);

    String confirmEmail(ConfirmCodeDTO confirmEmailDTO);

    String sendCode(ConfirmCodeDTO confirmEmailDTO);

    String changePasswordStepOne(ChangePasswordDTO changePasswordDTO);

    String changePasswordStepTwo(ChangePasswordDTO changePasswordDTO);

    UserDTO changeEmail(ChangeEmaildDTO changeEmaildDTO);

    UserDTO updateUser(UpdateUserDTO updateUserDTO);
}
