package com.katas.lookupservice.repository;

import com.katas.lookupservice.domain.Lookup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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

    @Test
    public void whenFindBy_thenReturnNull() {
        Optional<Lookup> emailLookup = lookupRepository.findByNameAndCategory("EMAIL", "EMAIL_CONFIGURATION");
        assertTrue(emailLookup.isEmpty());

        Optional<Lookup> smsLookup = lookupRepository.findByNameAndCategory("SMS", "NOTIFICATIONS");
        assertTrue(smsLookup.isEmpty());
    }

    @Test
    public void whenFindByNameAndCategory_thenReturnLookup() {
        Lookup newEmailLookup = lookup("EMAIL", "EMAIL_CONFIGURATION", "system@demo.com", 1L);
        lookupRepository.save(newEmailLookup);

        Optional<Lookup> emailLookup = lookupRepository.findByNameAndCategory("EMAIL", "EMAIL_CONFIGURATION");
        emailLookup.ifPresent(existing -> assertEquals(newEmailLookup, existing));
    }

    @Test
    public void whenFindByOrdinal_thenReturnLookup() {
        Lookup newEmailLookup = lookup("SMS", "NOTIFICATIONS", "sms_url", 2L);
        lookupRepository.save(newEmailLookup);

        Optional<Lookup> emailLookup = lookupRepository.findByOrdinalAndCategory(2L, "NOTIFICATIONS");
        emailLookup.ifPresent(existing -> assertEquals(newEmailLookup, existing));
    }

    @Test
    public void whenFindByCategoryAndExisting_thenReturnAllLookups() {
        lookupRepository.save(lookup("SMS", "NOTIFICATIONS", "sms_url", 2L));
        lookupRepository.save(lookup("EMAIL", "NOTIFICATIONS", "email_url", 3L));
        lookupRepository.save(lookup("CONFIG", "CONFIGURATIONS", "some_config", 4L));

        List<Lookup> lookups = lookupRepository.findByCategoryAndDeletedFalseOrderByOrdinalAsc("NOTIFICATIONS");
        assertFalse(lookups.isEmpty());

        assertTrue(lookups.stream().noneMatch(item -> item.getCategory().equals("CONFIGURATIONS")));
    }

    private Lookup lookup(String name, String category, String value, Long ordinal) {
        Lookup lookup = new Lookup();

        lookup.setName(name);
        lookup.setCategory(category);
        lookup.setValue(value);
        lookup.setOrdinal(ordinal);
        lookup.setModifiedBy(1L);
        lookup.setDeleted(false);
        return lookup;
    }
}
