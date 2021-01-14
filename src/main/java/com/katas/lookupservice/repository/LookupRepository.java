package com.katas.lookupservice.repository;

import com.katas.lookupservice.domain.Lookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LookupRepository extends JpaRepository<Lookup, Long> {
    List<Lookup> findByCategoryAndDeletedFalseOrderByOrdinalAsc(String category);

    Optional<Lookup> findByNameAndCategory(String name, String category);

    Optional<Lookup> findByOrdinalAndCategory(Long value, String category);
}
