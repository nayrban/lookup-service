package com.katas.lookupservice.utils;

import com.katas.lookupservice.domain.Lookup;

import java.util.List;
import java.util.Optional;

public class LookupUtils {

    public static Lookup toLookup(String name, String category, String value, Long ordinal, Long modifiedBy) {
        Lookup lookup = new Lookup();

        lookup.setName(name);
        lookup.setCategory(category);
        lookup.setValue(value);
        lookup.setOrdinal(ordinal);
        lookup.setModifiedBy(modifiedBy);
        lookup.setDeleted(false);
        return lookup;
    }

    public static Lookup findConfigurationOrThrow(List<Lookup> emailConfiguration, String name) {
        return findConfiguration(emailConfiguration, name)
                .orElseThrow(() -> new RuntimeException(String.format("%s Configuration Not Found", name)));
    }

    public static Optional<Lookup> findConfiguration(List<Lookup> lookups, String name) {
        return lookups.stream().filter(lookup -> lookup.getName().equals(name)).findFirst();
    }
}
