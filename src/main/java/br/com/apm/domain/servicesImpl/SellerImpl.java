package br.com.apm.domain.servicesImpl;

import br.com.apm.data.repositories.*;
import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.*;
import br.com.apm.domain.service.SellerService;
import br.com.apm.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class SellerImpl implements SellerService {

    @Autowired
    private DynamicFieldRepository dynamicFieldRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerFieldRepository sellerFieldRepository;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public DynamicField addField(DynamicField dynamicField) {
        DynamicField newDynamicField = new DynamicField();
        newDynamicField.setName(dynamicField.getName());
        newDynamicField.setType(dynamicField.getType());

        dynamicFieldRepository.save(newDynamicField);

        return newDynamicField;
    }

    @Override
    public Seller addSeller(SellerDTO sellerDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        if (user.getCarteira() == null)
            user = userService.addCarteiraToUser(user);

        Seller seller = new Seller();

        seller.setCnpj(sellerDTO.getCnpj());
        seller.setHelenaSellerCode(sellerDTO.getHelenaSellerCode());
        seller.setNome(sellerDTO.getNome());
        seller.setTelefone(sellerDTO.getTelefone());
        seller.setEmail(sellerDTO.getEmail());
        seller.setCidade(sellerDTO.getCidade());
        seller.setUf(sellerDTO.getUf());
        seller.setCep(sellerDTO.getCep());
        seller.setEndereco(sellerDTO.getEndereco());
        seller.setNumero(sellerDTO.getNumero());
        seller.setComplemento(sellerDTO.getComplemento());
        seller.setCadastro(sellerDTO.getCadastro());
        seller.setDataPedidoTeste(sellerDTO.getDataPedidoTeste());
        seller.setCarteira(user.getCarteira());
        seller.setSellerFields(updateSellerFields(seller.getSellerFields()));

        sellerRepository.save(seller);

        return null;
    }

    private List<SellerField> updateSellerFields(List<SellerField> sellerFields){
        List<DynamicField> dynamicFields = dynamicFieldRepository.findAll();

        if (sellerFields.size() == dynamicFields.size()) return sellerFields;

        boolean spotted = true;
        SellerField sellerField = new SellerField();

        for (int i = 0; i < dynamicFields.size(); i++) {
            spotted = true;
            for (int j = 0; j < sellerFields.size(); j++) {
                if (dynamicFields.get(i).getId() == sellerFields.get(j).getIdDynamicSellerRef()) {
                    spotted = false;
                    j = sellerFields.size();
                }
            }
            if (spotted) {
                sellerField = new SellerField();
                sellerField.setIdDynamicSellerRef(d);
            }
        }


        List<Seller> _sellers = new ArrayList<>();

    }

}
