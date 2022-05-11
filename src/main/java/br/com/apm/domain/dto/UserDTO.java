package br.com.apm.domain.dto;

import br.com.apm.domain.models.Carteira;
import br.com.apm.domain.models.Role;
import br.com.apm.domain.models.UserAPI;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UserDTO {

    private UUID id;
    private String email;
    private String cpf;
    private String fullName;
    private boolean emailConfirmed;

    private List<String> roles;
    private int totalSeller;
    private Boolean active;
    private Boolean enabled;

    public static UserDTO toUserDTO(UserAPI user) {
        ModelMapper modelMapper = new ModelMapper();
        UserDTO dto = modelMapper.map(user, UserDTO.class);

        dto.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        dto.totalSeller = user.getCarteira() == null ? 0 : user.getCarteira().getSellers().size();

        return dto;
    }

}
