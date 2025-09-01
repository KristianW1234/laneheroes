package com.personal.laneheroes.dto;


public class UploadResult {
    private int savedCount;
    private String errorMessage;

    public UploadResult(int savedCount, String errorMessage) {
        this.savedCount = savedCount;
        this.errorMessage = errorMessage;
    }

    public int getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(int savedCount) {
        this.savedCount = savedCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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