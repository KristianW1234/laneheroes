package com.personal.laneheroes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResult {
    private int savedCount;
    private String errorMessage;


    public static UploadResult success(int count) {
        return new UploadResult(count, null);
    }

    public static UploadResult error(String message) {
        return new UploadResult(0, message);
    }

    public boolean isSuccess() {
        return errorMessage == null;
    }

    // Getters and setters
}