package com.dsa.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A group of performance tables produced by one run of the System Performance Report menu,
 * kept so that "System Overview" can show the most recent report.
 */
public class PerformanceReport {

    private final LocalDateTime createdAt = LocalDateTime.now();
    private final Map<String, List<OperationResult>> sections = new LinkedHashMap<>();

    public void addSection(String title, List<OperationResult> rows) {
        sections.put(title, Collections.unmodifiableList(new ArrayList<>(rows)));
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** Section title mapped to its rows, in the order the sections were added. */
    public Map<String, List<OperationResult>> getSections() {
        return Collections.unmodifiableMap(sections);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }
}
