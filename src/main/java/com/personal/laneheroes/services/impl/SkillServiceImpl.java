package com.personal.laneheroes.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.laneheroes.dto.SkillJsonDTO;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.*;
import com.personal.laneheroes.enums.SkillType;
import com.personal.laneheroes.exception.EntityNotFoundException;
import com.personal.laneheroes.repositories.*;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.HeroService;
import com.personal.laneheroes.services.SkillService;
import com.personal.laneheroes.specifications.SkillSpecification;
import com.personal.laneheroes.utilities.ResponseMessages;
import com.personal.laneheroes.utilities.Utility;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    private final HeroRepository heroRepository;

    private final ObjectMapper objectMapper;

    private final HeroService heroService;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository, HeroRepository heroRepository, ObjectMapper objectMapper, HeroService heroService) {
        this.skillRepository = skillRepository;
        this.heroRepository = heroRepository;
        this.objectMapper = objectMapper;
        this.heroService = heroService;
    }

    @Value("${image-dir}")
    private String imageDir;

    @Override
    public ResponseWrapper<Skill> addSkill(SkillJsonDTO skill, MultipartFile imgFile) {
        Skill dbSkill = new Skill();
        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];

        if (!assignRelatedEntities(skill, dbSkill, ResponseMessages.ADD_FAIL, responseHolder)) {
            return (ResponseWrapper<Skill>) responseHolder[0];
        }

        Hero dbHero = heroService.getHeroById(Long.parseLong(skill.getHeroId())).getData();

        dbSkill.setSkillName(skill.getSkillName());
        dbSkill.setSkillSlot(skill.getSkillSlot());
        dbSkill.setSkillDescription(skill.getSkillDescription());
        dbSkill.setIsPassive(skill.getIsPassive());
        dbSkill.setIsUltimate(skill.getIsUltimate());
        dbSkill.setHero(dbHero);


        dbSkill.setSkillTypes(convertStringToSkillTypeList(skill.getSkillTypes()));

        boolean isDotaPassive = dbHero.getGame().getGameCode().equalsIgnoreCase("dota") && dbSkill.getSkillTypes().contains(SkillType.INNATE);
        if (isDotaPassive){
            dbSkill.setImgIcon("dota-innate.png");
        } else if (imgFile != null && !imgFile.isEmpty()){
            String name = dbHero.getHeroCode().split("_")[0];
            String folderPath = "skill"+ResponseMessages.DELIMITER+dbHero.getGame().getGameCode().toLowerCase()+ResponseMessages.DELIMITER+name.toLowerCase();


            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, folderPath);
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbSkill.setImgIcon(uploadResult.getData());
            }

        }

        skillRepository.save(dbSkill);
        return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE + " "
                + ResponseMessages.ADD_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbSkill);
    }

    @Override
    public ResponseWrapper<Skill> updateSkill(SkillJsonDTO skill, MultipartFile imgFile){
        Optional<Skill> skillPresence = skillRepository.findById(Long.parseLong(skill.getId()));
        if (skillPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        Skill dbSkill  = skillPresence.get();
        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];


        boolean isDotaPassive = false;

        if (!assignRelatedEntities(skill, dbSkill, ResponseMessages.UPDATE_FAIL, responseHolder)) {
            return (ResponseWrapper<Skill>) responseHolder[0];
        }

        if (skill.getSkillName() != null){
            dbSkill.setSkillName(skill.getSkillName());
        }

        if (skill.getSkillSlot() != null){
            dbSkill.setSkillSlot(skill.getSkillSlot());
        }

        if (skill.getSkillDescription() != null){
            dbSkill.setSkillDescription(skill.getSkillDescription());
        }

        if (skill.getIsPassive() != null){
            dbSkill.setIsPassive(skill.getIsPassive());
        }

        if (skill.getIsUltimate() != null){
            dbSkill.setIsPassive(skill.getIsUltimate());
        }

        if (skill.getSkillTypes() != null){
            dbSkill.setSkillTypes(convertStringToSkillTypeList(skill.getSkillTypes()));
        }



        if (skill.getHeroId() != null){
            dbSkill.setHero(heroService.getHeroById(Long.parseLong(skill.getHeroId())).getData());
            isDotaPassive = dbSkill.getHero().getGame().getGameCode().equalsIgnoreCase("dota") && dbSkill.getSkillTypes().contains(SkillType.INNATE);
        }

        if (isDotaPassive){
            dbSkill.setImgIcon("dota-innate.png");
        } else if (imgFile != null && !imgFile.isEmpty()){
            String name = dbSkill.getHero().getHeroCode().split("_")[0];
            String folderPath = "skill"+ResponseMessages.DELIMITER+dbSkill.getHero().getGame().getGameCode().toLowerCase()+ResponseMessages.DELIMITER+name.toLowerCase();
            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, folderPath);
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbSkill.setImgIcon(uploadResult.getData());
            }

        }
        skillRepository.save(dbSkill);
        return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE + " "
                + ResponseMessages.UPDATE_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbSkill);
    }

    @Override
    public ResponseWrapper<Skill> deleteSkill(Long id) {
        Optional<Skill> skillPresence = skillRepository.findById(id);
        if (skillPresence.isPresent()){
            Skill dbSkill = skillPresence.get();
            skillRepository.delete(dbSkill);
            return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, null);
        } else {
            return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE + " "
                    + ResponseMessages.DELETE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
    }


    @Override
    public ResponseWrapper<List<SkillJsonDTO>> getAllSkills() {
        List<Skill> list = skillRepository.findAll();
        List<SkillJsonDTO> dtos = new ArrayList<>();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.SKILL_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.SKILL_SINGLE.toLowerCase();
            }
            for (Skill s : list){
                dtos.add(convertSkillToDTO(s));
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, dtos);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, dtos);
        }
    }

    @Override
    public ResponseWrapper<SkillJsonDTO> getSkillById(Long id) {
        Optional<Skill> skillPresence = skillRepository.findById(id);
        if (skillPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE
                    + " " + ResponseMessages.NOT_FOUND,
                    ResponseMessages.FAIL_STATUS, null);
        }

        SkillJsonDTO dto = convertSkillToDTO(skillPresence.get());

        return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE
                + " " + ResponseMessages.FOUND,
                ResponseMessages.SUCCESS_STATUS, dto);
    }

    @Override
    public ResponseWrapper<PagedResponse<SkillJsonDTO>> searchSkills(String name, Long heroId, Pageable pageable) {
        Specification<Skill> spec = SkillSpecification.withFilters(name, heroId);
        Page<Skill> resultPage = skillRepository.findAll(spec, pageable);

        if (resultPage.hasContent()) {
            // Convert only if there's content
            Page<SkillJsonDTO> dtoPage = resultPage.map(this::convertSkillToDTO);
            PagedResponse<SkillJsonDTO> pagedResponse = new PagedResponse<>(dtoPage);

            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + resultPage.getNumberOfElements() + " ";
            if (resultPage.getNumberOfElements() > 1) {
                successMessage += ResponseMessages.SKILL_PLURAL.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.SKILL_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.SKILL_SINGLE.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.SKILL_SINGLE.toLowerCase();
            }

            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        } else {
            // Handle no content separately
            PagedResponse<SkillJsonDTO> emptyResponse = new PagedResponse<>(Page.empty(pageable));
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS, ResponseMessages.SUCCESS_STATUS, emptyResponse);
        }
    }

    @Override
    public ResponseWrapper<UploadResult> uploadSkillsFromExcel(String excelFile) {
        int totalAdded = 0;
        try (
                FileInputStream inputStream = new FileInputStream(excelFile);
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {
            List<Skill> skills = skillRepository.findAll();
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip header

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Skill skill = mapRowToSkill(row);

                if (!skillCopyCheck(skills, skill.getSkillName())) {
                    totalAdded++;
                    skillRepository.save(skill);
                }
            }
        } catch (Exception ex) {
            return new ResponseWrapper<>(ResponseMessages.BATCH_FAIL, ResponseMessages.FAIL_STATUS, UploadResult.error(ex.getMessage()));
        }

        return new ResponseWrapper<>(ResponseMessages.BATCH_SUCCESS, ResponseMessages.SUCCESS_STATUS, UploadResult.success(totalAdded));
    }

    @Override
    public void uploadInitSkillsFromJSON(String path) throws IOException {
        if (skillRepository.count() > 0) return;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);


        List<SkillJsonDTO> skillDTOs = objectMapper.readValue(inputStream, new TypeReference<>() {});

        List<Skill> skills = new ArrayList<>();

        for (SkillJsonDTO dto : skillDTOs) {
            Hero hero = heroRepository.findByHeroCodeIgnoreCase(dto.getHeroCode())
                    .orElseThrow(() -> new RuntimeException("Hero not found: " + dto.getHeroCode()));


            Skill skill = new Skill();

            skill.setSkillName(dto.getSkillName());
            skill.setSkillSlot(dto.getSkillSlot());
            skill.setSkillDescription(dto.getSkillDescription());
            skill.setIsPassive(dto.getIsPassive());
            skill.setIsUltimate(dto.getIsUltimate());
            skill.setImgIcon(dto.getImgIcon());
            skill.setSkillTypes(convertStringToSkillTypeList(dto.getSkillTypes()));
            skill.setHero(hero);

            skills.add(skill);
        }

        skillRepository.saveAll(skills);

    }

    private boolean skillCopyCheck(List<Skill> skills, String name) {
        for (Skill skill : skills) {
            if (skill.getSkillName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean assignRelatedEntities(SkillJsonDTO sourceDto, Skill target, String failMsg, ResponseWrapper<?>[] responseHolder) {
        if (sourceDto.getHeroCode() != null && !sourceDto.getHeroCode().isBlank()) {
            // Lookup hero using heroCode (String)
            Optional<Hero> hero = heroRepository.findByHeroCodeIgnoreCase(sourceDto.getHeroCode());
            if (hero.isEmpty()) {
                responseHolder[0] = new ResponseWrapper<>(
                        ResponseMessages.HERO_SINGLE + " " + failMsg + ": ID is invalid",
                        ResponseMessages.FAIL_STATUS,
                        null
                );
                return false;
            }
            target.setHero(hero.get());
        }

        return true;
    }

    private Skill mapRowToSkill(Row row) {
        Skill skill = new Skill();
        skill.setImgIcon("noimage.png");

        for (Cell cell : row) {
            int index = cell.getColumnIndex();
            String value = "";
            switch (cell.getCellType()){
                case STRING -> value = cell.getStringCellValue();
                case NUMERIC -> {
                    double numericValue = cell.getNumericCellValue();
                    value = (numericValue == Math.floor(numericValue))
                            ? String.valueOf((long) numericValue)  // No .0 if it's whole
                            : String.valueOf(numericValue);

                }
                default -> {
                    //Nothing
                }
            }



            switch (index) {
                case 0 -> skill.setSkillName(value);
                case 1 -> skill.setSkillDescription(value);
                case 2 -> skill.setSkillSlot(Integer.parseInt(value));
                case 3 -> skill.setImgIcon(value);
                case 4 -> skill.setIsPassive(value);
                case 5 -> skill.setIsUltimate(value);
                case 6 -> skill.setHero(fetchHero(value));
                case 7 -> skill.setSkillTypes(convertStringToSkillTypeList(value));
                default -> {
                    //Nothing
                }
            }
        }
        return skill;
    }

    private Hero fetchHero(String name) {
        return heroRepository.findByHeroCodeIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Hero", name));
    }


    private List<SkillType> convertStringToSkillTypeList(String value){
        List<SkillType> skillTypes = new ArrayList<>();
        if (value != null && !value.trim().isEmpty()) {
            String[] typeTokens = value.split(",");
            for (String token : typeTokens) {
                try {
                    SkillType type = SkillType.valueOf(token.trim().toUpperCase());
                    skillTypes.add(type);
                } catch (IllegalArgumentException e) {
                    // Optionally log or skip unknown enums
                }
            }
        }
        return skillTypes;
    }

    private SkillJsonDTO convertSkillToDTO(Skill skill) {
        SkillJsonDTO dto = new SkillJsonDTO();

        dto.setId(Long.toString(skill.getId()));
        dto.setSkillName(skill.getSkillName());
        dto.setSkillDescription(skill.getSkillDescription());
        dto.setSkillSlot(skill.getSkillSlot());
        dto.setImgIcon(skill.getImgIcon());
        dto.setIsPassive(skill.getIsPassive());
        dto.setIsUltimate(skill.getIsUltimate());

        // Skill types to comma-separated or list string
        dto.setSkillTypes(skill.getSkillTypes().stream()
                .map(Enum::name)
                .collect(Collectors.joining(",")));  // or use List<String> in DTO if you prefer);

        // Hero (store hero code or ID, up to you)
        if (skill.getHero() != null){
            dto.setHeroCode(skill.getHero().getHeroCode());
            dto.setHeroId(Long.toString(skill.getHero().getId()));
            dto.setHeroImgIcon(skill.getHero().getImgIcon());
        }

        return dto;
    }


}
