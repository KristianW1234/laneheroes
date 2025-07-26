package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Skill;
import com.personal.laneheroes.response.ResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SkillService {
    ResponseWrapper<Skill> addSkill(Skill game, MultipartFile imgFile);

    ResponseWrapper<Skill> updateSkill(Skill game, MultipartFile imgFile);

    ResponseWrapper<Skill> deleteSkill(Long id);

    ResponseWrapper<List<Skill>> getAllSkills();

    ResponseWrapper<Skill> getSkillById(Long id);

    ResponseWrapper<PagedResponse<Skill>> searchSkills(String name, Long heroId, Pageable pageable);

    ResponseWrapper<UploadResult> uploadSkillsFromExcel(String excelFile);

    void uploadInitSkillsFromJSON(String path) throws IOException;
}
