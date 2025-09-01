package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.SkillJsonDTO;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Skill;
import com.personal.laneheroes.response.ResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SkillService {
    ResponseWrapper<Skill> addSkill(SkillJsonDTO skill, MultipartFile imgFile);

    ResponseWrapper<Skill> updateSkill(SkillJsonDTO skill, MultipartFile imgFile);

    ResponseWrapper<Skill> deleteSkill(Long id);

    ResponseWrapper<List<SkillJsonDTO>> getAllSkills();

    ResponseWrapper<SkillJsonDTO> getSkillById(Long id);

    ResponseWrapper<PagedResponse<SkillJsonDTO>> searchSkills(String name, Long heroId, Pageable pageable);

    ResponseWrapper<UploadResult> uploadSkillsFromExcel(String excelFile);

    void uploadInitSkillsFromJSON(String path) throws IOException;
}
