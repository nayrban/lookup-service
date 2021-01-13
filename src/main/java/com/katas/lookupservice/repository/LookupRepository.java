package com.katas.lookupservice.repository;

import com.katas.lookupservice.domain.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LookupRepository extends JpaRepository<Lookup, Long> {
    List<Lookup> findByCategoryAndDeletedFalseOrderByOrdinalAsc(String category);

    Lookup findByNameAndCategory(String name, String category);

    Lookup findByOrdinalAndCategory(Long value, String category);
}
