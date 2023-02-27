package com.shopme.site.setting;

import com.shopme.common.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {

    public List<Country> findAllByOrderByNameAsc();
}
