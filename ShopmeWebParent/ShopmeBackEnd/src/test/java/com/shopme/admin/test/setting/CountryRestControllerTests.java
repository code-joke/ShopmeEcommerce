package com.shopme.admin.test.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CountryRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CountryRepository countryRepo;

    @Test
    @WithMockUser(username = "vinhtqph09311@gmail.com", password = "something", roles = "ADMIN")
    public void testListCountries() throws Exception {
        String url = "/countries/list";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);

        assertThat(countries).hasSizeGreaterThan(0);
    }

    @Test
    @WithMockUser(username = "vinhtqph09311@gmail.com", password = "something", roles = "ADMIN")
    public void testCreateCountry() throws Exception {
        String url = "/countries/save";
        String countryName = "V";
        String countryCode = "B";
        Country country = new Country(countryName, countryCode);

        String jsonRequest = objectMapper.writeValueAsString(country);

        MvcResult mvcResult = mockMvc.perform(post(url).contentType("application/json").content(jsonRequest).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Integer countryId = Integer.parseInt(response);

        Optional<Country> findById = countryRepo.findById(countryId);
        assertThat(findById.isPresent());

        Country savedCountry = findById.get();
        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "vinhtqph09311@gmail.com", password = "something", roles = "ADMIN")
    public void testUpdateCountry() throws Exception {
        String url = "/countries/save";
        Integer countryId = 252;
        String countryName = "Vi";
        String countryCode = "B";
        Country country = new Country(countryId, countryName, countryCode);

        String jsonRequest = objectMapper.writeValueAsString(country);

        mockMvc.perform(post(url).contentType("application/json").content(jsonRequest).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(countryId)))
                .andDo(print());

        Optional<Country> findById = countryRepo.findById(countryId);
        assertThat(findById).isPresent();

        Country savedCountry = findById.get();
        assertThat(savedCountry.getName()).isEqualTo(countryName);
    }

    @Test
    @WithMockUser(username = "vinhtqph09311@gmail.com", password = "something", roles = "ADMIN")
    public void testDeleteCountry() throws Exception {
        Integer countryId = 252;
        String url = "/countries/delete/" + countryId;

        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        Optional<Country> findById = countryRepo.findById(countryId);
        assertThat(findById).isNotPresent();
    }
}
