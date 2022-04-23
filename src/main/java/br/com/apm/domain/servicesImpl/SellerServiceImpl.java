package br.com.apm.domain.servicesImpl;

import br.com.apm.data.repositories.*;
import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.*;
import br.com.apm.domain.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private DynamicFieldRepository dynamicFieldRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SellerFieldRepository sellerFieldRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private CheckListVisitaRepository checkListVisitaRepository;

    @Autowired
    private DynamicQuestionCheckListRepository dynamicQuestionCheckListRepository;

    @Autowired
    private QuestionCheckListRepository questionCheckListRepository;

    @Override
    public DynamicField addField(DynamicField dynamicField) {
        if (dynamicField.getName() == null)
            throw new IllegalArgumentException("Campo Nome é obrigatório");

        if (dynamicField.getType() == null)
            throw new IllegalArgumentException("Campo Tipo é obrigatório");

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

        Carteira fndCarteira = carteiraRepository.findByOwner_Id(user.getId());

        if (user.getCarteira() == null){
            Carteira carteira = new Carteira();
            carteira.setSellers(new ArrayList<>());
            carteira.setOwner(user);
            carteira = carteiraRepository.save(carteira);

            user.setCarteira(carteira);
            user = userRepository.save(user);
        }

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

        seller = sellerRepository.save(seller);

        seller = updateSellerFields(seller);

        return seller;
    }

    @Override
    public DynamicQuestionCheckList addQuestionChecklist(DynamicQuestionCheckList dynamicQuestionCheckList) {

        if (dynamicQuestionCheckList.getFieldUpdate() == null)
            throw new IllegalArgumentException("Um campo dinamico deve ser relacionado a essa questão");

        if (dynamicQuestionCheckList.getQuestion() == null)
            throw new IllegalArgumentException("Campo pergunta é obrigátorio");

        DynamicField dynamicField = new DynamicField();

        if (dynamicQuestionCheckList.getFieldUpdate().getId() != null)
            dynamicField = dynamicFieldRepository.findById(dynamicQuestionCheckList.getFieldUpdate().getId()).get();

        if (dynamicQuestionCheckList.getFieldUpdate().getId() == null)
            dynamicField = addField(dynamicQuestionCheckList.getFieldUpdate());

        dynamicQuestionCheckList.setQuestion(dynamicQuestionCheckList.getQuestion());
        dynamicQuestionCheckList.setActive(dynamicQuestionCheckList.isActive());
        dynamicQuestionCheckList.setAnswerRequired(dynamicQuestionCheckList.isAnswerRequired());
        dynamicQuestionCheckList.setAlternatives(dynamicQuestionCheckList.getAlternatives());
        dynamicQuestionCheckList.setFieldUpdate(dynamicField);

        dynamicQuestionCheckList = dynamicQuestionCheckListRepository.save(dynamicQuestionCheckList);

        return dynamicQuestionCheckList;
    }

    @Override
    public CheckListVisita startChecklist(Seller seller) {
        Seller _seller = sellerRepository.findById(seller.getId()).get();

        if (_seller == null)
            throw new IllegalArgumentException("Seller não localizado");

        _seller = updateSellerFields(_seller);

        CheckListVisita newCheckListVisita = new CheckListVisita();

        if (_seller.getCheckListVisitas().size() > 0)
            if (_seller.getCheckListVisitas().get(_seller.getCheckListVisitas().size() - 1).getDataVisita() == null)
                newCheckListVisita = _seller.getCheckListVisitas().get(_seller.getCheckListVisitas().size() - 1);

        newCheckListVisita.setSeller(_seller);

        checkListVisitaRepository.save(newCheckListVisita);

        newCheckListVisita = updateQuestionsCheckList(newCheckListVisita);

        return newCheckListVisita;
    }

    private Seller updateSellerFields(Seller seller){
        List<DynamicField> dynamicFields = dynamicFieldRepository.findAll();

        if (seller.getSellerFields() == null)
            seller.setSellerFields(new ArrayList<>());

        if (seller.getSellerFields().size() == dynamicFields.size()) return seller;

        boolean spotted;
        int sellerFielSize = seller.getSellerFields().size();
        SellerField sellerField;

        for (int i = 0; i < dynamicFields.size(); i++) {
            spotted = true;
            for (int j = 0; j < sellerFielSize; j++) {
                if (dynamicFields.get(i).getId() == seller.getSellerFields().get(j).getIdDynamicSellerRef()) {
                    spotted = false;
                    j = sellerFielSize;
                }
            }
            if (spotted) {
                sellerField = new SellerField();
                sellerField.setIdDynamicSellerRef(dynamicFields.get(i).getId());
                sellerField.setName(dynamicFields.get(i).getName());
                sellerField.setType(dynamicFields.get(i).getType());
                sellerField.setSeller(seller);
                sellerField = sellerFieldRepository.save(sellerField);

                seller.getSellerFields().add(sellerField);
            }
        }

        return sellerRepository.save(seller);
    }

    private CheckListVisita updateQuestionsCheckList(CheckListVisita checkListVisita){
        List<DynamicQuestionCheckList> dynamicQuestionCheckLists = dynamicQuestionCheckListRepository.findAll();

        if (checkListVisita.getQuestions() == null)
            checkListVisita.setQuestions(new ArrayList<>());

        if (dynamicQuestionCheckLists == null || checkListVisita.getQuestions().size() == dynamicQuestionCheckLists.size())
            return checkListVisita;

        boolean spotted;
        int questionCheckListSize = checkListVisita.getQuestions().size();
        QuestionCheckList questionCheckList;

        for (int i = 0; i < dynamicQuestionCheckLists.size(); i++) {
            spotted = true;
            for (int j = 0; j < questionCheckListSize; j++) {
                if (dynamicQuestionCheckLists.get(i).getId() == checkListVisita.getQuestions().get(j).getFieldUpdateID()) {
                    spotted = false;
                    j = questionCheckListSize;
                }
            }
            if (spotted) {
                questionCheckList = new QuestionCheckList();
                questionCheckList.setCheckListVisita(checkListVisita);
                questionCheckList.setQuestion(dynamicQuestionCheckLists.get(i).getQuestion());
                questionCheckList.setAlternatives(dynamicQuestionCheckLists.get(i).getAlternatives());
                questionCheckList.setAnswerRequired(dynamicQuestionCheckLists.get(i).isAnswerRequired());
                questionCheckList.setFieldUpdateID(dynamicQuestionCheckLists.get(i).getId());
                questionCheckList = questionCheckListRepository.save(questionCheckList);

                checkListVisita.getQuestions().add(questionCheckList);
            }
        }

        return checkListVisitaRepository.save(checkListVisita);
    }

}
