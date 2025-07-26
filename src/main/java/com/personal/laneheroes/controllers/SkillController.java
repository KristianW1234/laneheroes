package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.Skill;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.SkillService;
import com.personal.laneheroes.utilities.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/skill")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper<List<Skill>>> getAllSkills() {
        ResponseWrapper<List<Skill>> response = skillService.getAllSkills();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<Skill>> getOneSkill(@PathVariable Long id) {
        ResponseWrapper<Skill> response = skillService.getSkillById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Skill>> addSkill(
            @RequestPart("skill") Skill skill,
            @RequestPart(value="imgFile", required = false) MultipartFile imgFile) {

        ResponseWrapper<Skill> result = skillService.addSkill(skill, imgFile);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Skill>> updateSkill(
            @RequestPart("skill") Skill skill,
            @RequestPart(value="imgFile", required = false) MultipartFile imgFile) {

        ResponseWrapper<Skill> result = skillService.updateSkill(skill, imgFile);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<Skill>> deleteSkill(@PathVariable Long id) {
        ResponseWrapper<Skill> response = skillService.deleteSkill(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PagedResponse<Skill>>> searchSkills(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long heroId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {

        ResponseWrapper<PagedResponse<Skill>> response = skillService.searchSkills(name, heroId, Utility.setupPageable(page, size, sortBy, sortOrder));
        return ResponseEntity.ok(response);
    }
}
