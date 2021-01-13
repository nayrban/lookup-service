package com.katas.lookupservice.repository;

import com.katas.lookupservice.domain.Lookup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LookupRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LookupRepository lookupRepository;

    @Test
    public void whenFindByNameAndCategory_thenReturnNotFound() {
        Lookup emailLookup = lookupRepository.findByNameAndCategory("EMAIL", "EMAIL_CONFIGURATION");
        Assertions.assertNull(emailLookup);
    }
}
