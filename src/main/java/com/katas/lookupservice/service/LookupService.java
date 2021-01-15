package com.katas.lookupservice.service;


import com.katas.lookupservice.domain.Lookup;
import com.katas.lookupservice.repository.LookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LookupService {

    private final LookupRepository lookupRepository;

    @Autowired
    public LookupService(LookupRepository lookupRepository) {
        this.lookupRepository = lookupRepository;
    }

    public List<Lookup> findAllByCategory(String category) {
        log.info("Call to: LookupService.findAllByCategory()");
        return this.lookupRepository.findByCategoryAndDeletedFalseOrderByOrdinalAsc(category);
    }

    public Optional<Lookup> findById(Long id) {
        log.info("Call to: LookupService.findById()");
        return this.lookupRepository.findById(id);
    }

    public Optional<Lookup> findByNameAndCategory(String name, String category) {
        log.info("Call to: LookupService.findByName()");
        return this.lookupRepository.findByNameAndCategory(name, category);
    }

    public List<Lookup> findByNamesAndCategory(List<String> names, String category) {
        log.info("Call to: LookupService.findByNames()");
        return this.lookupRepository.findByNameAndCategory(names, category);
    }

    public Lookup findByOrdinalAndCategory(Long ordinal, String category) {
        return this.lookupRepository.findByOrdinalAndCategory(ordinal, category)
                .orElseThrow(() -> new RuntimeException(String.format("Lookup with ordinal %1d and category %2s not found", ordinal, category)));
    }

    public Lookup save(Lookup lookup) {
        return this.lookupRepository.save(lookup);
    }

    public List<Lookup> saveAll(List<Lookup> lookups) {
        return this.lookupRepository.saveAll(lookups);
    }
}
