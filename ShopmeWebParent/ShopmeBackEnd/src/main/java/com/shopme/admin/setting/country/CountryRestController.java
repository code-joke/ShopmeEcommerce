package com.shopme.admin.setting.country;

import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryRestController {

    @Autowired
    private CountryRepository countryRepo;

    @GetMapping("/list")
    public List<Country> listCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    @PostMapping("/save")
    public String save(@RequestBody Country country) {
        Country savedCountry = countryRepo.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        countryRepo.deleteById(id);
    }
}

