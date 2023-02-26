package com.shopme.site.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepo;

    public List<Setting> listAllSettings() {
        return (List<Setting>) settingRepo.findAll();
    }

    public List<Setting> getGeneralSettings() {
       return settingRepo.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    }

}
