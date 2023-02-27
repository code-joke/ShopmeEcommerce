package com.shopme.site.customer;

import com.shopme.common.entity.Country;
import com.shopme.site.setting.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private CountryRepository countryRepo;

    public List<Country> listAllCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }
}
