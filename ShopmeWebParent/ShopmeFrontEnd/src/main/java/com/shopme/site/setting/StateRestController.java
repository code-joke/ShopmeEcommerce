package com.shopme.site.setting;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StateRestController {

    @Autowired
    private StateRepository stateRepo;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> listStates = stateRepo.findByCountryOrderByNameAsc(new Country(countryId));

        return listStates.stream().map(state ->
                new StateDTO(state.getId(), state.getName()))
                .collect(Collectors.toList());
    }

}
