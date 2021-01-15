package com.katas.lookupservice.service;

import com.katas.lookupservice.domain.Lookup;
import com.katas.lookupservice.utils.LookupUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO this test case is not exhaustive
@SpringBootTest
public class LookupServiceTest {

    @Autowired
    private LookupService lookupService;

    private Long modifiedBy = 1L;

    @Test
    public void whenFindById_thenLookupShouldNotBeFound() {
        Optional<Lookup> found = lookupService.findById(100L);

        assertTrue(found.isEmpty());
    }

    @Test
    public void whenFindByNamesAndCategory_thenLookupShouldBeFound() {

        lookupService.save(LookupUtils.toLookup("EMAIL", "EMAIL_CONFIGURATION", "1", 1L, modifiedBy));
        lookupService.save(LookupUtils.toLookup("PASSWORD", "EMAIL_CONFIGURATION", "1", 2L, modifiedBy));

        List<Lookup> found = lookupService.findByNamesAndCategory(Arrays.asList("EMAIL", "PASSWORD"), "EMAIL_CONFIGURATION");

        assertTrue(found.size() > 0);
    }
}