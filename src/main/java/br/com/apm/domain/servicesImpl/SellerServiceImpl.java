package br.com.apm.domain.servicesImpl;

import br.com.apm.data.repositories.*;
import br.com.apm.domain.dto.*;
import br.com.apm.domain.models.*;
import br.com.apm.domain.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private FAQRepository faqRepository;

    @Autowired
    private HuntingRepository huntingRepository;

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

        Carteira carteira;

        if (sellerDTO.getCnpj() == null)
                throw new IllegalArgumentException("Campo CNPJ é obrigatório");

        Seller seller =  sellerRepository.findByCnpj(sellerDTO.getCnpj());

        if (seller != null)
            throw new IllegalArgumentException("CNPJ já cadastrado");

        if (user.getCarteira() == null){
            carteira = new Carteira();
            carteira.setSellers(new ArrayList<>());
            carteira.setOwner(user);
            carteira = carteiraRepository.save(carteira);

            user.setCarteira(carteira);
            user = userRepository.save(user);
        }

        carteira = carteiraRepository.findByOwner_Id(user.getId());


        seller = new Seller();

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
        seller.setCarteira(carteira);

        seller = sellerRepository.save(seller);
        seller = updateSellerFields(seller);

        carteira.getSellers().add(seller);
        carteiraRepository.save(carteira);

        return seller;
    }

    @Override
    public String addSellerList(List<SellerDTO> sellerDTOS) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();
        String errorCNPJ = "";
        int totalSave = 0;

        Carteira carteira;


        for (SellerDTO sellerDTO : sellerDTOS) {
            boolean save = true;

            if (sellerDTO.getCnpj() == null)
                save = false;

            Seller seller =  sellerRepository.findByCnpj(sellerDTO.getCnpj());

            if (seller != null){
                errorCNPJ = errorCNPJ + seller.getCnpj() + "\n";
                save = false;
            }

            if (user.getCarteira() == null){
                carteira = new Carteira();
                carteira.setSellers(new ArrayList<>());
                carteira.setOwner(user);
                carteira = carteiraRepository.save(carteira);

                user.setCarteira(carteira);
                user = userRepository.save(user);
            }

            if (save) {
                carteira = carteiraRepository.findByOwner_Id(user.getId());

                seller = new Seller();

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
                seller.setCarteira(carteira);

                seller = sellerRepository.save(seller);
                seller = updateSellerFields(seller);

                carteira.getSellers().add(seller);
                carteiraRepository.save(carteira);

                totalSave++;
            }

        }

        return "Sellers salvos: " + totalSave + "\nSellers já cadastrados:\n" + errorCNPJ;
    }

    @Override
    public Carteira getCarteira(UUID agenteId) {
        return carteiraRepository.findByOwner_Id(agenteId);
    }

    @Override
    public Seller updateSeller(Seller seller) {
        Seller _seller = sellerRepository.findById(seller.getId()).get();

        _seller.setCnpj(seller.getCnpj());
        _seller.setHelenaSellerCode(seller.getHelenaSellerCode());
        _seller.setNome(seller.getNome());
        _seller.setTelefone(seller.getTelefone());
        _seller.setEmail(seller.getEmail());
        _seller.setCidade(seller.getCidade());
        _seller.setUf(seller.getUf());
        _seller.setCep(seller.getCep());
        _seller.setEndereco(seller.getEndereco());
        _seller.setNumero(seller.getNumero());
        _seller.setComplemento(seller.getComplemento());

        List<SellerField> sellerFields = new ArrayList<>();

        for (SellerField _sellerField : _seller.getSellerFields()) {
            for (SellerField sellerField : seller.getSellerFields()) {
                if (_sellerField.getId().toString().intern() == sellerField.getId().toString().intern()) {
                    _sellerField.setValue(sellerField.getValue());
                }
            }
            sellerFields.add(_sellerField);
        }

        _seller.setSellerFields(sellerFields);

        _seller = sellerRepository.save(_seller);
        return _seller;
    }

    @Override
    public Page<Carteira> getCarteiraWithoutOwner(Pageable pageable) {
        return carteiraRepository.findByOwnerIsNull(pageable);
    }

    @Override
    public Carteira tranferCarteira(TranferCarteiraDTO tranferCarteiraDTO) {
        UserAPI user = userRepository.findById(tranferCarteiraDTO.getUserId()).get();
        Carteira carteira = carteiraRepository.findById(tranferCarteiraDTO.getCarteiraId()).get();

        if (!user.getActive())
            throw new IllegalArgumentException("Carteira não localizada");

        if (carteira == null)
            throw new IllegalArgumentException("Carteira não localizada");

        if (user.getCarteira() == null){
            user.setCarteira(carteira);
            userRepository.save(user);

            carteira.setOwner(user);
            carteira = carteiraRepository.save(carteira);

            return carteira;
        }

        if (user.getCarteira().getSellers().size() < 1){
            Carteira _carteira = carteiraRepository.findByOwner_Id(user.getId());

            user.setCarteira(carteira);
            userRepository.save(user);

            carteira.setOwner(user);
            carteira = carteiraRepository.save(carteira);

            carteiraRepository.delete(_carteira);

            return carteira;
        }

        Carteira userCarteira = user.getCarteira();

        for (Seller seller : carteira.getSellers()) {
            seller.setCarteira(userCarteira);
            seller = sellerRepository.save(seller);

            userCarteira.getSellers().add(seller);
        }

        carteira = carteiraRepository.findById(carteira.getId()).get();
        carteiraRepository.delete(carteira);

        user.setCarteira(userCarteira);

        userRepository.save(user);

        carteira = user.getCarteira();

        return carteira;
    }

    @Override
    public Page<DynamicField> getFields(Pageable pageable) {
        return dynamicFieldRepository.findAll(pageable);
    }

    @Override
    public Page<DynamicQuestionCheckList> getQuestions(String status, Pageable pageable) {
        if (status.intern() == "desativado".intern())
            return dynamicQuestionCheckListRepository.findByActiveFalse(pageable);

        if (status.intern() == "ativado".intern())
            return dynamicQuestionCheckListRepository.findByActiveTrue(pageable);

        return dynamicQuestionCheckListRepository.findAll(pageable);
    }

    @Override
    public FAQ addFAQ(FAQ faq) {
        if (faq.getQuestion() == null)
            throw new IllegalArgumentException("Campo Pergunta é obrigatório");

        if (faq.getAnswer() == null)
            throw new IllegalArgumentException("Campo Resposta é obrigatório");

        return faqRepository.save(faq);
    }

    @Override
    public FAQ updateFAQ(FAQ faq) {
        if (faq.getId() == null)
            throw new IllegalArgumentException("Id é obrigatório");

        if (faq.getAnswer() == null || faq.getQuestion() == null)
            throw new IllegalArgumentException("Nenhum campo informado para alteração");

        FAQ _faq = faqRepository.findById(faq.getId()).get();

        if (faq.getAnswer() != null)
            _faq.setAnswer(faq.getAnswer());

        if (faq.getQuestion() != null)
            _faq.setQuestion(faq.getQuestion());

        return faqRepository.save(_faq);
    }

    @Override
    public Page<FAQ> getFAQS(String search, Pageable pageable) {
        if (search == null) search = "";

        return faqRepository.findByQuestionContainingIgnoreCaseOrAnswerContainingIgnoreCase(search, search, pageable);
    }

    @Override
    public Hunting addHunting(Hunting hunting) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        hunting.setUser(user);
        hunting = huntingRepository.save(hunting);

        if (user.getHuntings() ==  null)
            user.setHuntings(new ArrayList<>());

        user.getHuntings().add(hunting);
        userRepository.save(user);

        return hunting;
    }

    @Override
    public Hunting updateHunting(Hunting hunting) {
        if (hunting.getId() == null)
            throw new IllegalArgumentException("Id é obrigatório");


        Hunting _hunting = huntingRepository.findById(hunting.getId()).get();

        if (_hunting == null)
            throw new IllegalArgumentException("Hunting não localizado");

        if (hunting.getCnpj() != null)
            _hunting.setCnpj(hunting.getCnpj());

        if (hunting.getNome() != null)
            _hunting.setNome(hunting.getNome());

        if (hunting.getTelefone() != null)
            _hunting.setTelefone(hunting.getTelefone());

        if (hunting.getEmail() != null)
            _hunting.setEmail(hunting.getEmail());

        if (hunting.getCidade() != null)
            _hunting.setCidade(hunting.getCidade());

        if (hunting.getUf() != null)
            _hunting.setUf(hunting.getUf());

        if (hunting.getCep() != null)
            _hunting.setCep(hunting.getCep());

        if (hunting.getEndereco() != null)
            _hunting.setEndereco(hunting.getEndereco());

        if (hunting.getNumero() != null)
            _hunting.setNumero(hunting.getNumero());

        if (hunting.getComplemento() != null)
            _hunting.setComplemento(hunting.getComplemento());

        return huntingRepository.save(_hunting);
    }

    @Override
    public Page<Hunting> getHuntings(String nome, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        if (nome == null ) nome = "";

        return huntingRepository.findByUser_idAndNomeContainingIgnoreCase(user.getId(), nome, pageable);
    }

    @Override
    public DynamicQuestionCheckList updateQuestionChecklist(DynamicQuestionCheckList dynamicQuestionCheckList) {
        if (dynamicQuestionCheckList.getId() == null)
            throw new IllegalArgumentException("Id é obrigatório");


        DynamicQuestionCheckList _dynamicQuestionCheckList = dynamicQuestionCheckListRepository
                .findById(dynamicQuestionCheckList.getId()).get();

        if (_dynamicQuestionCheckList == null)
            throw new IllegalArgumentException("Questão não localizada");

        if (dynamicQuestionCheckList.getQuestion() != null)
            _dynamicQuestionCheckList.setQuestion(dynamicQuestionCheckList.getQuestion());

        if (dynamicQuestionCheckList.getFieldUpdate() != null)
            _dynamicQuestionCheckList.setFieldUpdate(dynamicQuestionCheckList.getFieldUpdate());

        if (dynamicQuestionCheckList.isAnswerRequired() != _dynamicQuestionCheckList.isAnswerRequired())
            _dynamicQuestionCheckList.setAnswerRequired(dynamicQuestionCheckList.isAnswerRequired());

        if (dynamicQuestionCheckList.isActive() != _dynamicQuestionCheckList.isActive())
            _dynamicQuestionCheckList.setActive(dynamicQuestionCheckList.isActive());

        if (dynamicQuestionCheckList.getFieldUpdate() != null){
            DynamicField dF = dynamicFieldRepository.save(dynamicQuestionCheckList.getFieldUpdate());
            _dynamicQuestionCheckList.setFieldUpdate(dF);
        }


        if (dynamicQuestionCheckList.getAlternatives() != null) {
            List<Alternative> alternatives = new ArrayList<>();

            for (Alternative alternative : dynamicQuestionCheckList.getAlternatives()) {
                if (alternative.getId() != null)
                    alternative = alternativeRepository.save(alternative);

                alternatives.add(alternative);
            }

            _dynamicQuestionCheckList.setAlternatives(alternatives);
        }

        _dynamicQuestionCheckList = dynamicQuestionCheckListRepository.save(_dynamicQuestionCheckList);

        boolean noFind = false;
        for (Alternative alternative : dynamicQuestionCheckList.getAlternatives()) {
            for (Alternative _alternative : _dynamicQuestionCheckList.getAlternatives()) {
                if (_alternative.getId().toString().intern() == alternative.getId().toString().intern())
                    noFind = false;
            }
            if (noFind)
                alternativeRepository.delete(alternative);
        }

        return _dynamicQuestionCheckList;
    }

    @Override
    public List<Seller> setVisitItinerary(List<Seller> sellers) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        List<Seller> _sellers = new ArrayList<>();

        for (Seller _seller : user.getCarteira().getSellers()) {
            for (Seller seller : sellers) {
                if (seller.getId().toString().intern() == _seller.getId().toString().intern()){
                    _seller.setOrderVisitItinerary(seller.getOrderVisitItinerary());
                    sellerRepository.save(_seller);
                }
            }
            _sellers.add(_seller);
        }

        user.getCarteira().setSellers(_sellers);
        user = userRepository.save(user);

        return user.getCarteira().getSellers();
    }

    @Override
    public List<Seller> sellerVisiting(Seller seller) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Seller _seller = sellerRepository.findById(seller.getId()).get();

        _seller.setVisiting(true);
        sellerRepository.save(_seller);

        UserAPI user = (UserAPI) authentication.getPrincipal();

        return user.getCarteira().getSellers();
    }

    @Override
    public List<Seller> resetVisitItinerary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        for (Seller _seller : user.getCarteira().getSellers()) {
            _seller.setVisiting(false);
            sellerRepository.save(_seller);
        }

        userRepository.findById(user.getId());

        return user.getCarteira().getSellers();
    }

    @Override
    public List<CheckListVisita> getActivity(UUID sellerId) {
        Seller seller = sellerRepository.findById(sellerId).get();

        if (seller == null)
            throw new IllegalArgumentException("Seller não localizado");

        return checkListVisitaRepository.findBySeller_Id(seller.getId());
    }

    @Override
    public DynamicQuestionCheckList addQuestionChecklist(DynamicQuestionCheckList dynamicQuestionCheckList) {

        if (dynamicQuestionCheckList.getFieldUpdate() == null)
            throw new IllegalArgumentException("Um campo dinâmico deve ser relacionado a essa questão");

        if (dynamicQuestionCheckList.getQuestion() == null)
            throw new IllegalArgumentException("Campo pergunta é obrigátorio");

        DynamicField dynamicField = new DynamicField();

        if (dynamicQuestionCheckList.getFieldUpdate().getId() != null)
            dynamicField = dynamicFieldRepository.findById(dynamicQuestionCheckList.getFieldUpdate().getId()).get();

        if (dynamicField.getId() != null) {
            DynamicField _dynamicField = dynamicFieldRepository.getById(dynamicField.getId());

            if (_dynamicField != null)
                throw new IllegalArgumentException("Esté campo já tem uma pergunta relacionada");
        }

        if (dynamicQuestionCheckList.getFieldUpdate().getId() == null)
            dynamicField = addField(dynamicQuestionCheckList.getFieldUpdate());

        DynamicQuestionCheckList _dynamicQuestionCheckList = new DynamicQuestionCheckList();

        _dynamicQuestionCheckList.setQuestion(dynamicQuestionCheckList.getQuestion());
        _dynamicQuestionCheckList.setActive(dynamicQuestionCheckList.isActive());
        _dynamicQuestionCheckList.setAnswerRequired(dynamicQuestionCheckList.isAnswerRequired());
        _dynamicQuestionCheckList.setMultipleAlternative(dynamicQuestionCheckList.isMultipleAlternative());
        _dynamicQuestionCheckList.setFieldUpdate(dynamicField);

        _dynamicQuestionCheckList = dynamicQuestionCheckListRepository.save(_dynamicQuestionCheckList);



        _dynamicQuestionCheckList.setAlternatives(persistAlternatives(dynamicQuestionCheckList.getAlternatives()));
        _dynamicQuestionCheckList = dynamicQuestionCheckListRepository.save(_dynamicQuestionCheckList);

        return _dynamicQuestionCheckList;
    }

    @Override
    public CheckListVisita startChecklist(Seller seller) {
        Seller _seller = sellerRepository.findById(seller.getId()).get();

        if (_seller == null) throw new IllegalArgumentException("Seller não localizado");

        _seller = updateSellerFields(_seller);

        CheckListVisita newCheckListVisita = new CheckListVisita();

        if (_seller.getCheckListVisitas() == null) _seller.setCheckListVisitas(new ArrayList<>());

        List<CheckListVisita> checkListVisitas = _seller.getCheckListVisitas();

        if (checkListVisitas.size() > 0){
            checkListVisitas = checkListVisitaRepository.findBySeller_Id(_seller.getId());

            if (checkListVisitas.size() > 0)
                if (checkListVisitas.get(checkListVisitas.size() - 1).getDataVisita() == null)
                    return checkListVisitas.get(checkListVisitas.size() - 1);
        }

        newCheckListVisita.setSeller(_seller);

        newCheckListVisita = checkListVisitaRepository.save(newCheckListVisita);

        checkListVisitas.add(newCheckListVisita);

        seller.setCheckListVisitas(checkListVisitas);
        sellerRepository.save(_seller);

        newCheckListVisita = updateQuestionsCheckList(newCheckListVisita);

        return newCheckListVisita;
    }

    @Override
    public CheckListVisita answerChecklist(CheckListVisita checkListVisita) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        CheckListVisita checkListVisitaDB = checkListVisitaRepository.findById(checkListVisita.getId()).get();

        if (checkListVisitaDB == null)
            throw new IllegalArgumentException("Checklist não foi iniciado");

        if (checkListVisita.getDataVisita() == null)
            throw new IllegalArgumentException("Checklist não pode ser alterado" + LocalDateTime.now());

        checkListVisitaDB.setDataVisita(checkListVisita.getDataVisita());
        checkListVisitaDB.setQuestions(checkListVisita.getQuestions());
        checkListVisitaDB.setNomeAgente(user.getFullName());

        checkListVisita.setSeller(checkListVisitaDB.getSeller());
        updateSellerFiledValue(checkListVisita);

        checkListVisitaDB = checkListVisitaRepository.save(checkListVisitaDB);


        return checkListVisitaDB;
    }

    @Override
    public Seller getSeller(UUID sellerId) {
        Seller seller = sellerRepository.findById(sellerId).get();
        seller = updateSellerFields(seller);
        return seller;
    }

    @Override
    public Page<Seller> getSellers(String search, UUID tagId, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        for (Role role : user.getRoles() ) {
            if (role.getName().intern() == "ROLE_ADMIN".intern())
                return sellerRepository.findAll(pageable);
        }

        if (user.getCarteira() == null)
            throw new IllegalArgumentException("Agente não tem carteira");

        if (search == null) search = "";

        if (tagId == null)
            return sellerRepository.findByCarteira_idAndNomeContainingIgnoreCase(user.getCarteira().getId(), search, pageable);

        return sellerRepository.findByTags_idAndNomeContainingIgnoreCase(tagId, search, pageable);
    }

    @Override
    public Tag addTag(Tag tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        if (tag.getName() == null)
            throw new IllegalArgumentException("Campo Nome é obrigatório");

        for (Tag _tag : user.getTags()) {
            if (_tag.getName().intern() == tag.getName().intern())
                throw new IllegalArgumentException("Uma tag com esse nome já existe");
        }

        Tag newTag = new Tag();
        newTag.setName(tag.getName());
        newTag.setColor(tag.getColor());

        newTag = tagRepository.save(newTag);

        if (user.getTags() == null)
            user.setTags(new ArrayList<>());

        user.getTags().add(newTag);

        userRepository.save(user);

        return newTag;
    }

    @Override
    public Tag deleteTag(Tag tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        if (tag.getId() == null)
            throw new IllegalArgumentException("Id obrigatório");

        List<Tag> tags = new ArrayList<>();

        for (Tag _tag : user.getTags()) {
            if (_tag.getId().toString().intern() != tag.getId().toString().intern())
                tags.add(_tag);
        }

        user.setTags(tags);
        user = userRepository.save(user);

        Tag deleteTag = tagRepository.findById(tag.getId()).get();
        deleteTag.setUser(null);

        tagRepository.save(deleteTag);
        tagRepository.delete(deleteTag);

        return deleteTag;
    }

    @Override
    public Tag updateTag(Tag tag) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        if (tag.getId() == null) throw new IllegalArgumentException("Id obrigatório");

        Tag updateTag = tagRepository.findById(tag.getId()).get();
        updateTag.setName(tag.getName());
        updateTag.setColor(tag.getColor());

        for (Tag _tag : user.getTags()) {
            if (_tag.getName().intern() == updateTag.getName().intern())
                if (_tag.getId().toString().intern() != updateTag.getId().toString().intern())
                    throw new IllegalArgumentException("Uma tag com esse nome já existe");
        }

        updateTag = tagRepository.save(updateTag);

        return updateTag;
    }

    @Override
    public List<Tag> getTags() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAPI user = (UserAPI) authentication.getPrincipal();

        return user.getTags();
    }

    @Override
    public Seller addTagSeller(AddTagSellerDTO addTagSellerDTO) {
        Seller seller = sellerRepository.findById(addTagSellerDTO.getSellerId()).get();
        Tag tag = tagRepository.findById(addTagSellerDTO.getTagId()).get();

        List<Tag> sellerTags = new ArrayList<>();

        if (seller.getTags() != null) sellerTags = seller.getTags();

        sellerTags.add(tag);

        seller.setTags(sellerTags);

        return sellerRepository.save(seller);
    }

    private CheckListVisita updateSellerFiledValue(CheckListVisita checkListVisita){
        QuestionCheckList _questionCheckListDB;
        QuestionCheckList _questionCheckList;
        SellerField _sellerField;

        for (int i = 0; i < checkListVisita.getQuestions().size(); i++) {
            _questionCheckListDB = questionCheckListRepository.findById(checkListVisita.getQuestions().get(i).getId()).get();
            _questionCheckList = checkListVisita.getQuestions().get(i);
            String valueField = "";

            if (_questionCheckListDB.isAnswerRequired()) {
                boolean answered = false;

                if (!_questionCheckListDB.isMultipleAlternative()){
                    boolean v1 = false;
                    boolean v2 = false;
                    for (Alternative alternative : _questionCheckList.getAlternatives()) {
                        if (alternative.isChecked()){
                            v2 = v1 ? true : false;
                            v1 = true;
                        }
                    }

                    if (v2) throw new IllegalArgumentException("Essa questão não pode ter mais de uma resposta ("
                            + _questionCheckListDB.getQuestion() + ")");
                }

                for (Alternative alternative : _questionCheckList.getAlternatives()) {
                    if (alternative.isChecked())
                        answered = true;
                }

                if (_questionCheckList.getOthers() != null || _questionCheckList.getOthers() != "")
                    answered = true;

                if (!answered)
                    throw new IllegalArgumentException("A questão: " + _questionCheckListDB.getQuestion() + " é obrigatória");

            }

            List<Alternative> alternativeList = new ArrayList<>();

            for (Alternative alternative : _questionCheckList.getAlternatives()) {
                if (alternative.isChecked())
                        valueField = valueField + alternative.getName() + ", ";

                if (_questionCheckList.getOthers() != null && _questionCheckList.getOthers() != "")
                    valueField = valueField + _questionCheckList.getOthers();

                alternative = alternativeRepository.save(alternative);
                alternativeList.add(alternative);
            }

            Seller _seller = checkListVisita.getSeller();

            for (SellerField sellerField : _seller.getSellerFields()) {
                if (sellerField.getIdDynamicSellerRef().toString().intern() == _questionCheckListDB.getFieldUpdateID().toString().intern()){
                    sellerField.setValue(valueField);
                    sellerFieldRepository.save(sellerField);
                }
            }

            _questionCheckList.setAlternatives(alternativeList);
            _questionCheckList.setCheckListVisita(checkListVisita);
            questionCheckListRepository.save(_questionCheckList);

        }

        return checkListVisita;
    }

    private Seller updateSellerFields(Seller seller){
        List<DynamicField> dynamicFields = dynamicFieldRepository.findAll();

        if (seller.getSellerFields() == null)
            seller.setSellerFields(new ArrayList<>());

        //if (seller.getSellerFields().size() == dynamicFields.size()) return seller;

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
                    if (_dynamicField.getName().toString().intern() != _sellerField.getName().toString().intern()){
                        _sellerField.setName(_dynamicField.getName());
                        _sellerField = sellerFieldRepository.save(_sellerField);
                        seller.getSellerFields().set(j, _sellerField);
                    }
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

            newAlternative.setName(_alternative.getName());
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
                questionCheckList.setMultipleAlternative(dynamicQuestionCheckLists.get(i).isMultipleAlternative());

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
