package br.com.apm.domain.servicesImpl;

import br.com.apm.data.repositories.*;
import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.*;
import br.com.apm.domain.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private AlternativeRepository alternativeRepository;

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

        DynamicQuestionCheckList _dynamicQuestionCheckList = new DynamicQuestionCheckList();

        _dynamicQuestionCheckList.setQuestion(dynamicQuestionCheckList.getQuestion());
        _dynamicQuestionCheckList.setActive(dynamicQuestionCheckList.isActive());
        _dynamicQuestionCheckList.setAnswerRequired(dynamicQuestionCheckList.isAnswerRequired());
        _dynamicQuestionCheckList.setFieldUpdate(dynamicField);

        _dynamicQuestionCheckList = dynamicQuestionCheckListRepository.save(_dynamicQuestionCheckList);



        _dynamicQuestionCheckList.setAlternatives(persistAlternatives(dynamicQuestionCheckList.getAlternatives()));
        _dynamicQuestionCheckList = dynamicQuestionCheckListRepository.save(_dynamicQuestionCheckList);

        return _dynamicQuestionCheckList;
    }

    @Override
    public CheckListVisita startChecklist(Seller seller) {
        Seller _seller = sellerRepository.findById(seller.getId()).get();

        if (_seller == null)
            throw new IllegalArgumentException("Seller não localizado");

        _seller = updateSellerFields(_seller);

        CheckListVisita newCheckListVisita = new CheckListVisita();
        List<CheckListVisita> checkListVisitasDB = checkListVisitaRepository.findBySeller_Id(_seller.getId());

        if (checkListVisitasDB.size() > 0)
            if (checkListVisitasDB.get(checkListVisitasDB.size() - 1).getDataVisita() == null)
                newCheckListVisita = checkListVisitasDB.get(checkListVisitasDB.size() - 1);

        newCheckListVisita.setSeller(_seller);

        newCheckListVisita = checkListVisitaRepository.save(newCheckListVisita);

        newCheckListVisita = updateQuestionsCheckList(newCheckListVisita);

        return newCheckListVisita;
    }

    @Override
    public CheckListVisita answerChecklist(CheckListVisita checkListVisita) {
        CheckListVisita checkListVisitaDB = checkListVisitaRepository.findById(checkListVisita.getId()).get();

        if (checkListVisitaDB == null)
            throw new IllegalArgumentException("Checklist não foi iniciado");

        if (checkListVisitaDB.getDataVisita() != null)
            throw new IllegalArgumentException("Checklist não pode ser alterado");

        checkListVisitaDB.setDataVisita(LocalDateTime.now());
        checkListVisitaDB.setQuestions(checkListVisitaDB.getQuestions());

        updateSellerFiledValue(checkListVisita);

        checkListVisitaDB = checkListVisitaRepository.save(checkListVisitaDB);


        return checkListVisitaDB;
    }

    @Override
    public Seller getSeller(UUID sellerId) {
        Seller seller = sellerRepository.findById(sellerId).get();
        return seller;
    }

    private CheckListVisita updateSellerFiledValue(CheckListVisita checkListVisita){
        QuestionCheckList _questionCheckListDB;
        QuestionCheckList _questionCheckList;
        SellerField _sellerField;

        for (int i = 0; i < checkListVisita.getQuestions().size(); i++) {
            _questionCheckListDB = questionCheckListRepository.findById(checkListVisita.getQuestions().get(i).getId()).get();
            _questionCheckList = checkListVisita.getQuestions().get(i);

            if (_questionCheckListDB.isAnswerRequired()) {
                if (_questionCheckList.getAnswer() == null)
                    throw new IllegalArgumentException("Questão " + _questionCheckListDB.getQuestion() + " é obrigatório");
            }

            _sellerField = sellerFieldRepository.findByIdDynamicSellerRef(_questionCheckList.getFieldUpdateID());

            if (_sellerField == null)
                throw new IllegalArgumentException("Ops! aldeu errado, Sellerfild não foi localizado");

            _sellerField.setValue(_questionCheckList.getAnswer());
            sellerFieldRepository.save(_sellerField);
        }

        return checkListVisita;
    }

    private Seller updateSellerFields(Seller seller){
        List<DynamicField> dynamicFields = dynamicFieldRepository.findAll();

        if (seller.getSellerFields() == null)
            seller.setSellerFields(new ArrayList<>());

        if (seller.getSellerFields().size() == dynamicFields.size()) return seller;

        boolean spotted;
        SellerField sellerField;

        DynamicField _dynamicField;
        SellerField _sellerField;

        for (int i = 0; i < dynamicFields.size(); i++) {
            spotted = true;
            _dynamicField = dynamicFields.get(i);
            for (int j = 0; j < seller.getSellerFields().size(); j++) {
                _sellerField = seller.getSellerFields().get(j);
                if (_dynamicField.getId().toString().intern() == _sellerField.getIdDynamicSellerRef().toString().intern()) {
                    spotted = false;
                    j = seller.getSellerFields().size();
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

    private List<Alternative>  persistAlternatives(List<Alternative> _alternatives) {
        List<Alternative> alternatives = new ArrayList<>();

        for (Alternative _alternative: _alternatives  ) {
            Alternative newAlternative = new Alternative();

            newAlternative.setTittle(_alternative.getTittle());
            newAlternative = alternativeRepository.save(newAlternative);

            alternatives.add(newAlternative);
        }

        return alternatives;
    }

    private CheckListVisita updateQuestionsCheckList(CheckListVisita checkListVisita){
        List<DynamicQuestionCheckList> dynamicQuestionCheckLists = dynamicQuestionCheckListRepository.findByActiveTrue();

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
                if (dynamicQuestionCheckLists.get(i).getFieldUpdate().getId().toString().intern() ==
                        checkListVisita.getQuestions().get(j).getFieldUpdateID().toString().intern()) {
                    spotted = false;
                    j = questionCheckListSize;
                }
            }
            if (spotted) {
                questionCheckList = new QuestionCheckList();
                questionCheckList.setCheckListVisita(checkListVisita);
                questionCheckList.setQuestion(dynamicQuestionCheckLists.get(i).getQuestion());
                questionCheckList.setAnswerRequired(dynamicQuestionCheckLists.get(i).isAnswerRequired());
                questionCheckList.setFieldUpdateID(dynamicQuestionCheckLists.get(i).getFieldUpdate().getId());

                questionCheckList = questionCheckListRepository.save(questionCheckList);
                questionCheckList.setAlternatives(persistAlternatives(dynamicQuestionCheckLists.get(i).getAlternatives()));
                questionCheckList = questionCheckListRepository.save(questionCheckList);

                checkListVisita.getQuestions().add(questionCheckList);
            }
        }

        checkListVisita.setQuestions(questionCheckListRepository.findByCheckListVisita_id(checkListVisita.getId()));

        return checkListVisitaRepository.findById(checkListVisita.getId()).get();
    }

}
