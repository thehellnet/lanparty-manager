package org.thehellnet.lanparty.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thehellnet.lanparty.manager.exception.NotFoundException;
import org.thehellnet.lanparty.manager.model.persistence.Setting;
import org.thehellnet.lanparty.manager.repository.SettingRepository;

@Service
@Transactional
public class SettingService {

    private final SettingRepository settingRepository;

    @Autowired
    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Transactional(readOnly = true)
    public boolean getBoolean(String param) {
        return Boolean.parseBoolean(get(param).getValue());
    }

    @Transactional(readOnly = true)
    public int getInteger(String param) {
        return Integer.parseInt(get(param).getValue());
    }

    @Transactional(readOnly = true)
    public long getLong(String param) {
        return Long.parseLong(get(param).getValue());
    }

    @Transactional(readOnly = true)
    public double getDouble(String param) {
        return Double.parseDouble(get(param).getValue());
    }

    @Transactional(readOnly = true)
    public String getString(String param) {
        return get(param).getValue();
    }

    @Transactional
    public void put(String param, Object value) {
        Setting setting = settingRepository.findByParam(param);
        if (setting == null) {
            setting = new Setting(param);
        }
        setting.setValue(String.valueOf(value));
        settingRepository.save(setting);
    }

    private Setting get(String param) {
        Setting setting = settingRepository.findByParam(param);
        if (setting == null) {
            throw new NotFoundException(String.format("Setting \"%s\" not found", param));
        }

        return setting;
    }
}
