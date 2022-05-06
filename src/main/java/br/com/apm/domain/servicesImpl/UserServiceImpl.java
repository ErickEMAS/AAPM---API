package br.com.apm.domain.servicesImpl;

import br.com.apm.data.repositories.CarteiraRepository;
import br.com.apm.domain.dto.*;
import br.com.apm.domain.enums.CodeType;
import br.com.apm.domain.models.Carteira;
import br.com.apm.domain.service.UserService;
import br.com.apm.data.repositories.RoleRepository;
import br.com.apm.data.repositories.UserRepository;
import br.com.apm.domain.models.Role;
import br.com.apm.domain.models.UserAPI;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Override
    public String signUp(SignUpDTO signUpDTO) {

        if (signUpDTO.getCpf() == null)
            throw new IllegalArgumentException("Campo CPF Obrigatório");

        UserAPI user = userRepository.findByCpf(signUpDTO.getCpf());

        if (user != null){
            if (!user.isAccountNonLocked())
                throw new IllegalArgumentException("Usuário bloqueado");
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        if (signUpDTO.getId() != null)
            throw new IllegalArgumentException("Campo Id não dever ser informado");

        if (signUpDTO.getPassword() != null)
            throw new IllegalArgumentException("Campo Senha não dever ser informado nessa etapa");


        if (signUpDTO.getPasswordConfirm() != null)
            throw new IllegalArgumentException("Campo Confirmar Senha não dever ser informado nessa etapa");

        UserAPI _user = userRepository.findByEmail(signUpDTO.getEmail());

        if (_user != null)
            throw new IllegalArgumentException("O e-mail informado já possui um cadastro no sistema");


        Role role = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        Carteira newCarteira = new Carteira();
        newCarteira.setSellers(new ArrayList<>());
        newCarteira = carteiraRepository.save(newCarteira);

        UserAPI newUser = new UserAPI();
        newUser.setCpf(signUpDTO.getCpf());
        newUser.setEmail(signUpDTO.getEmail());
        newUser.setFullName(signUpDTO.getFullName());
        newUser.setRoles(roles);
        newUser.setCarteira(newCarteira);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(false);

        newUser = userRepository.save(newUser);

        newCarteira.setOwner(newUser);
        newCarteira = carteiraRepository.save(newCarteira);

        return "Usuário cadastrado com sucesso";
    }

    @Override
    public String signUpAdmin(SignUpDTO signUpDTO) {
        if (signUpDTO.getCpf() == null)
            throw new IllegalArgumentException("Campo CPF Obrigatório");

        UserAPI user = userRepository.findByCpf(signUpDTO.getCpf());

        if (user != null){
            if (!user.isAccountNonLocked())
                throw new IllegalArgumentException("Usuário bloqueado");
            throw new IllegalArgumentException("Usuário já cadastrado");
        }


        if (signUpDTO.getId() != null)
            throw new IllegalArgumentException("Campo Id não dever ser informado");

        if (signUpDTO.getPassword() != null)
            throw new IllegalArgumentException("Campo Senha não dever ser informado nessa etapa");


        if (signUpDTO.getPasswordConfirm() != null)
            throw new IllegalArgumentException("Campo Confirmar Senha não dever ser informado nessa etapa");

        UserAPI _user = userRepository.findByEmail(signUpDTO.getEmail());

        if (_user != null)
            throw new IllegalArgumentException("O e-mail informado já possui um cadastro no sistema");


        Role role = roleRepository.findByName("ROLE_ADMIN");
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        UserAPI newUser = new UserAPI();
        newUser.setCpf(signUpDTO.getCpf());
        newUser.setEmail(signUpDTO.getEmail());
        newUser.setFullName(signUpDTO.getFullName());
        newUser.setRoles(roles);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(false);

        newUser = userRepository.save(newUser);

        return "Usuário cadastrado com sucesso";
    }

    @Override
    public Page<UserDTO> getUsers(String roleName, Pageable pageable) {
        if (roleName == null) return userRepository.findAll(pageable).map(UserDTO::toUserDTO);

        Page<UserDTO> pageUsers =  pageUsers = userRepository.findByRoles_Name(roleName, pageable).map(UserDTO::toUserDTO);

        return pageUsers;
    }


    @Override
    public UserDTO signUpStepOne(String cpf) {
        UserAPI user = userRepository.findByCpf(cpf);

        if (user == null)
            throw new IllegalArgumentException("Nenhuma conta cadastrada com o CPF informado foi localizado no banco de dados");

        if (!user.isAccountNonLocked())
            throw new IllegalArgumentException("Conta encerrada");

        if (user.isEmailConfirmed())
            throw new IllegalArgumentException("Usuário já cadastrado");

        return UserDTO.toUserDTO(user);
    }

    @Override
    public UserDTO signUpStepTwo(SignUpDTO signUpDTO) {
        UserAPI user = userRepository.findById(signUpDTO.getId()).get();

        if (!user.isAccountNonLocked())
            throw new IllegalArgumentException("Conta encerrada");

        if (user.isEmailConfirmed())
            throw new IllegalArgumentException("Usuário já cadastrado");

        if (signUpDTO.getCpf().intern() != user.getCpf().intern())
            throw new IllegalArgumentException("O CPF não pode ser alterado");

        if (signUpDTO.getEmail() == null)
            throw new IllegalArgumentException("Não foi possível concluir o cadastro. Campo e-mail, obrigatório");

        if (signUpDTO.getFullName() == null)
            throw new IllegalArgumentException("Não foi possível concluir o cadastro. Campo nome, obrigatório");

        UserAPI _user = userRepository.findByEmail(signUpDTO.getEmail());

        if (_user != null)
            if (_user.getId().toString().intern() != user.getId().toString().intern())
                throw new IllegalArgumentException("O e-mail informado já possui um cadastro no sistema");

        verifyPassword(user, signUpDTO.getPassword(), signUpDTO.getPasswordConfirm());


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setEmail(signUpDTO.getEmail());
        user.setFullName(signUpDTO.getFullName());
        user.setPassword(encoder.encode(signUpDTO.getPassword()));
        user.setCode(codeGenerator());
        user.setEnabled(true);
        user.setCodeType(CodeType.EMAIL_CONFIRM);

        sendEmail(user);

        user.setValidityCode(LocalDateTime.now().plusMinutes(5));

        user = userRepository.save(user);

        return UserDTO.toUserDTO(user);
    }

    @Override
    public String confirmEmail(ConfirmCodeDTO confirmEmailDTO) {
        UserAPI user = userRepository.findByEmail(confirmEmailDTO.getEmail());

        if (user.isEmailConfirmed())
            throw new IllegalArgumentException("E-mail já foi confirmado");

        verifyCode(user, confirmEmailDTO.getCode(), CodeType.EMAIL_CONFIRM);

        user.setEmailConfirmed(true);
        user.setCode(null);
        user.setCodeType(null);
        user.setValidityCode(null);
        userRepository.save(user);

        return "E-mail Confirmado com sucesso";
    }

    @Override
    public String sendCode(ConfirmCodeDTO confirmEmailDTO) {
        UserAPI user = userRepository.findByEmail(confirmEmailDTO.getEmail());

        if (user == null)
            throw new IllegalArgumentException("Usuário não localizado");

        if (user.isEmailConfirmed() && confirmEmailDTO.getCodeType() == CodeType.EMAIL_CONFIRM)
            throw new IllegalArgumentException("E-mail já foi confirmado");

        user.setCode(codeGenerator());
        user.setCodeType(confirmEmailDTO.getCodeType());

        sendEmail(user);

        user.setValidityCode(LocalDateTime.now().plusMinutes(5));

        user = userRepository.save(user);

        return "Código enviado por e-mail: " + user.getCode(); //Remover código depois de implementar e-mail service
    }

    @Override
    public String changePasswordStepOne(ChangePasswordDTO changePasswordDTO) {
        UserAPI user = userRepository.findByEmail(changePasswordDTO.getEmail());

        verifyCode(user, changePasswordDTO.getCode(), CodeType.PASSWORD_CHANGE);

        user.setValidityCode(LocalDateTime.now().plusMinutes(20));
        userRepository.save(user);

        return "Código confirmado com sucesso";
    }

    @Override
    public String changePasswordStepTwo(ChangePasswordDTO changePasswordDTO) {
        UserAPI user = userRepository.findByEmail(changePasswordDTO.getEmail());

        verifyCode(user, changePasswordDTO.getCode(), CodeType.PASSWORD_CHANGE);

        verifyPassword(user, changePasswordDTO.getPassword(), changePasswordDTO.getPasswordConfirm());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setCode(null);
        user.setCodeType(null);
        user.setValidityCode(null);
        user.setPassword(encoder.encode(changePasswordDTO.getPassword()));
        userRepository.save(user);

        return "Senha Alterada com sucesso";
    }

    @Override
    public UserDTO changeEmail(ChangeEmaildDTO changeEmaildDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        user = userRepository.findById(user.getId()).get();

        verifyCode(user, changeEmaildDTO.getCode(), CodeType.EMAIL_CHANGE);

        if (user.getEmail().intern() == changeEmaildDTO.getNewEmail().intern())
            throw new IllegalArgumentException("O novo e-mail não pode ser igual ao o e-mail já cadastrado");

        user.setEmail(changeEmaildDTO.getNewEmail());
        user.setCode(null);
        user.setCodeType(null);
        user.setValidityCode(null);
        user.setEmailConfirmed(false);
        userRepository.save(user);

        return UserDTO.toUserDTO(user);
    }

    @Override
    public String deleteUser(UUID userId) {
        UserAPI user = userRepository.findById(userId).get();

        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        user.setEmailConfirmed(false);
        user.setEnabled(false);
        user.setPassword(null);
        user.setCarteira(null);

        user = userRepository.save(user);

        Carteira carteira = carteiraRepository.findByOwner_Id(user.getId());

        if (carteira.getSellers().size() < 1){
            carteiraRepository.delete(carteira);

            return "Usuário Desativado";
        }

        carteira.setOwner(null);

        carteiraRepository.save(carteira);

        return "Usuário Desativado";
    }

    @Override
    public String reactivateUser(ReactivateUserDTO reactivateUserDTO) {
        UserAPI user = new UserAPI();

        if (reactivateUserDTO.getId() != null)
            user = userRepository.findById(reactivateUserDTO.getId()).get();

        if (reactivateUserDTO.getCpf() != null)
            user = userRepository.findByCpf(reactivateUserDTO.getCpf());

        if (reactivateUserDTO.getEmail() != null)
            user = userRepository.findByEmail(reactivateUserDTO.getEmail());

        return "Usuário reativado";
    }

    public void sendEmail(UserAPI user){
        String emailSubject = "App Agente Parceiro Magalu - Código para ";
        String emailText = "Olá Agente! \n Conforme solicitado segue seu codigo para ";

        if (user.getCodeType() == CodeType.EMAIL_CONFIRM) {
            emailSubject += "Confirmação de E-mail";
            emailText += "confirmação de e-mail \n Código: ";
        }

        if (user.getCodeType() == CodeType.EMAIL_CHANGE) {
            emailSubject += "alteração de E-mail";
            emailText += "alteração de E-mail \n Código: ";
        }

        if (user.getCodeType() == CodeType.PASSWORD_CHANGE) {
            emailSubject += "alteração de senha";
            emailText += "alteração de senha \n Código: ";
        }

        emailText += user.getCode();

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("agente.parceiro.magalu@gmail.com");
            email.setTo(user.getEmail());
            email.setSubject(emailSubject);
            email.setText(emailText);
            javaMailSender.send(email);
        } catch (MailException emailError) {
            throw new IllegalArgumentException(emailError);
        }
    }



    private boolean verifyPassword(UserAPI user, String password, String passwordConfirm){
        if (password == null)
            throw new IllegalArgumentException("Não foi possível alterar a senha. Campo senha, obrigatório");

        if (passwordConfirm == null)
            throw new IllegalArgumentException("Não foi possível alterar a senha. Campo confirmar senha, obrigatório");

        if (password.length() < 8)
            throw new IllegalArgumentException("A senha precisa ter mais de 8 caracteres");

        if (passwordConfirm.intern() != password.intern())
            throw new IllegalArgumentException("A senha digitada é diferente da senha de confirmação");

        return true;
    }

    private boolean verifyCode(UserAPI user, String code, CodeType codeType){
        if (code == null)
            throw new IllegalArgumentException("Não existe código para ser confirmado");

        if (user.getValidityCode().isBefore(LocalDateTime.now())) {
            user.setCode(null);
            user.setCodeType(null);
            userRepository.save(user);
            throw new IllegalArgumentException("Código expirado");
        }

        if (user.getCodeType() != codeType) {
            user.setCode(null);
            user.setCodeType(null);
            userRepository.save(user);
            throw new IllegalArgumentException("Código inválido");
        }

        if (codeType != CodeType.EMAIL_CONFIRM) {
            if (!user.isEmailConfirmed()) {
                user.setCode(null);
                user.setCodeType(null);
                userRepository.save(user);
                throw new IllegalArgumentException("E-mail precisa ser confirmado para prosseguir");
            }
        }

        if (code.intern() != user.getCode().intern())
            throw new IllegalArgumentException("Código inválido");

        return true;
    }

    private String codeGenerator(){
        Random random = new Random();
        String code = "";

        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(9);
            code = code + number;
        }

        return code;
    }

    public UserAPI addCarteiraToUser(UserAPI user){
        Carteira carteira = new Carteira();
        carteira.setSellers(new ArrayList<>());
        carteira.setOwner(user);
        carteira = carteiraRepository.save(carteira);

        user.setCarteira(carteira);
        user = userRepository.save(user);

        return user;
    }

}
