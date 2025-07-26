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
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

@Service
@Transactional
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    private final HeroRepository heroRepository;

    private final ObjectMapper objectMapper;

    private final HeroService heroService;

    @Value("${image-dir}")
    private String imageDir;

    @Override
    public ResponseWrapper<Skill> addSkill(Skill skill, MultipartFile imgFile) {
        Skill dbSkill = new Skill();
        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];

        if (!assignRelatedEntities(skill, dbSkill, ResponseMessages.ADD_FAIL, responseHolder)) {
            return (ResponseWrapper<Skill>) responseHolder[0];
        }

        Hero dbHero = heroService.getHeroById(skill.getHero().getId()).getData();

        dbSkill.setSkillName(skill.getSkillName());
        dbSkill.setSkillSlot(skill.getSkillSlot());
        dbSkill.setSkillDescription(skill.getSkillDescription());
        dbSkill.setIsPassive(skill.getIsPassive());
        dbSkill.setIsUltimate(skill.getIsUltimate());
        dbSkill.setSkillTypes(skill.getSkillTypes());

        boolean isDotaPassive = dbHero.getGame().getGameCode().equalsIgnoreCase("dota") && skill.getSkillTypes().contains(SkillType.INNATE);
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
    public ResponseWrapper<Skill> updateSkill(Skill skill, MultipartFile imgFile){
        Optional<Skill> skillPresence = skillRepository.findById(skill.getId());
        if (skillPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        Skill dbSkill  = skillPresence.get();
        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];




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
            dbSkill.setSkillTypes(skill.getSkillTypes());
        }

        Hero dbHero = heroService.getHeroById(dbSkill.getHero().getId()).getData();

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
    public ResponseWrapper<List<Skill>> getAllSkills() {
        List<Skill> list = skillRepository.findAll();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.SKILL_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.SKILL_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, list);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, list);
        }
    }

    @Override
    public ResponseWrapper<Skill> getSkillById(Long id) {
        Optional<Skill> skillPresence = skillRepository.findById(id);
        return skillPresence.map(
                        skill -> new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE
                                + " " + ResponseMessages.FOUND,
                                ResponseMessages.SUCCESS_STATUS, skill))
                .orElseGet(() -> new ResponseWrapper<>(ResponseMessages.SKILL_SINGLE
                        + " " + ResponseMessages.NOT_FOUND,
                        ResponseMessages.FAIL_STATUS, null));
    }

    @Override
    public ResponseWrapper<PagedResponse<Skill>> searchSkills(String name, Long heroId, Pageable pageable) {
        Specification<Skill> spec = SkillSpecification.withFilters(name, heroId);
        Page<Skill> resultPage = skillRepository.findAll(spec, pageable);
        PagedResponse<Skill> pagedResponse = new PagedResponse<>(resultPage);

        if (resultPage.hasContent()) {
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + resultPage.getNumberOfElements() + " ";
            if (resultPage.getNumberOfElements() > 1) {
                successMessage += ResponseMessages.SKILL_PLURAL.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.SKILL_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.SKILL_SINGLE.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.SKILL_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS, ResponseMessages.SUCCESS_STATUS, pagedResponse);
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
            Hero hero = heroRepository.findByHeroCodeIgnoreCase(dto.hero)
                    .orElseThrow(() -> new RuntimeException("Hero not found: " + dto.hero));


            Skill skill = new Skill();

            skill.setSkillName(dto.skillName);
            skill.setSkillSlot(dto.skillSlot);
            skill.setSkillDescription(dto.skillDescription);
            skill.setIsPassive(dto.isPassive);
            skill.setIsUltimate(dto.isUltimate);
            skill.setSkillTypes(convertStringToSkillTypeList(dto.skillTypes));
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

    private boolean assignRelatedEntities(Skill source, Skill target, String failMsg, ResponseWrapper<?>[] responseHolder) {
        if (source.getHero() != null) {
            Optional<Hero> hero = Utility.getValidEntityById(
                    heroRepository, source.getHero().getId(),
                    ResponseMessages.HERO_SINGLE, failMsg, responseHolder);
            if (hero.isEmpty()) return false;
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


}
