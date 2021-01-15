package com.katas.lookupservice.repository;

import com.katas.lookupservice.domain.Lookup;
import com.katas.lookupservice.utils.LookupUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LookupRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LookupRepository lookupRepository;
    private Long modifiedBy = 1L;

    @Test
    public void whenFindBy_thenReturnNull() {
        Optional<Lookup> emailLookup = lookupRepository.findByNameAndCategory("EMAIL", "EMAIL_CONFIGURATION");
        assertTrue(emailLookup.isEmpty());

        Optional<Lookup> smsLookup = lookupRepository.findByNameAndCategory("SMS", "NOTIFICATIONS");
        assertTrue(smsLookup.isEmpty());
    }

    @Test
    public void whenFindByNameAndCategory_thenReturnLookup() {
        Lookup newEmailLookup = LookupUtils.toLookup("EMAIL", "EMAIL_CONFIGURATION", "system@demo.com", 1L, modifiedBy);
        lookupRepository.save(newEmailLookup);

        Optional<Lookup> emailLookup = lookupRepository.findByNameAndCategory("EMAIL", "EMAIL_CONFIGURATION");
        emailLookup.ifPresent(existing -> assertEquals(newEmailLookup, existing));
    }

    @Test
    public void whenFindByOrdinal_thenReturnLookup() {
        Lookup newEmailLookup = LookupUtils.toLookup("SMS", "NOTIFICATIONS", "sms_url", 2L, modifiedBy);
        lookupRepository.save(newEmailLookup);

        Optional<Lookup> emailLookup = lookupRepository.findByOrdinalAndCategory(2L, "NOTIFICATIONS");
        emailLookup.ifPresent(existing -> assertEquals(newEmailLookup, existing));
    }

    @Test
    public void whenFindByCategoryAndExisting_thenReturnAllLookups() {
        lookupRepository.save(LookupUtils.toLookup("SMS", "NOTIFICATIONS", "sms_url", 2L, modifiedBy));
        lookupRepository.save(LookupUtils.toLookup("EMAIL", "NOTIFICATIONS", "email_url", 3L, modifiedBy));
        lookupRepository.save(LookupUtils.toLookup("CONFIG", "CONFIGURATIONS", "some_config", 4L, modifiedBy));

        List<Lookup> lookups = lookupRepository.findByCategoryAndDeletedFalseOrderByOrdinalAsc("NOTIFICATIONS");
        assertFalse(lookups.isEmpty());

        assertTrue(lookups.stream().noneMatch(item -> item.getCategory().equals("CONFIGURATIONS")));
    }

    @Test
    public void whenFindByNamesAndCategory_thenReturnAllLookups() {
        lookupRepository.save(LookupUtils.toLookup("SMS", "NOTIFICATIONS", "sms_url", 2L, modifiedBy));
        lookupRepository.save(LookupUtils.toLookup("EMAIL", "NOTIFICATIONS", "email_url", 3L, modifiedBy));
        lookupRepository.save(LookupUtils.toLookup("CONFIG", "CONFIGURATIONS", "some_config", 4L, modifiedBy));

        List<Lookup> lookups = lookupRepository.findByNameAndCategory(Arrays.asList("SMS", "EMAIL", "CONFIG"), "NOTIFICATIONS");
        assertEquals(2, lookups.size());

        assertTrue(lookups.stream().noneMatch(item -> item.getCategory().equals("CONFIGURATIONS")));
    }

}
