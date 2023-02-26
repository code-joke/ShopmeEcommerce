package com.shopme.admin.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/states")
public class StateRestController {

    @Autowired
    private StateRepository stateRepo;

    @GetMapping("/list_by_country/{id}")
    public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> listStates = stateRepo.findByCountryOrderByNameAsc(new Country(countryId));

        return listStates.stream().map(state ->
                new StateDTO(state.getId(), state.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping("/save")
    public String save(@RequestBody State state) {
        State savedState = stateRepo.save(state);
        return String.valueOf(savedState.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        stateRepo.deleteById(id);
    }
}
