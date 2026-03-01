package com.example.Backend.service;

import com.example.Backend.model.Settings;
import com.example.Backend.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    public Settings getOrCreateSettings(Long userId) {
        return settingsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Settings settings = new Settings();
                    settings.setUserId(userId);
                    return settingsRepository.save(settings);
                });
    }

    public Settings updateSettings(Long userId, Settings updatedSettings) {
        Settings settings = getOrCreateSettings(userId);
        settings.setDefaultTone(updatedSettings.getDefaultTone());
        settings.setDefaultImageStyle(updatedSettings.getDefaultImageStyle());
        settings.setContentFrequency(updatedSettings.getContentFrequency());
        settings.setAutoSchedule(updatedSettings.getAutoSchedule());
        settings.setAiSuggestions(updatedSettings.getAiSuggestions());
        settings.setEmailPost(updatedSettings.getEmailPost());
        settings.setEmailFail(updatedSettings.getEmailFail());
        settings.setEmailWeekly(updatedSettings.getEmailWeekly());
        settings.setEmailTips(updatedSettings.getEmailTips());
        settings.setPushPost(updatedSettings.getPushPost());
        settings.setPushEngagement(updatedSettings.getPushEngagement());
        settings.setDefaultPostingTime(updatedSettings.getDefaultPostingTime());
        settings.setTimeZone(updatedSettings.getTimeZone());
        return settingsRepository.save(settings);
    }

    public void deleteSettings(Long userId) {
        settingsRepository.findByUserId(userId).ifPresent(settingsRepository::delete);
    }
}
