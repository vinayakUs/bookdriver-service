package org.example.locationservice.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AutocompletePrediction {
    // Getters and Setters
    private String description;
    private String placeId;
    private List<String> types;
    private List<Term> terms;
    private List<MatchedSubstring> matchedSubstrings;
    private StructuredFormatting structuredFormatting;

    // Nested classes for structured data
    @Setter
    @Getter
    public static class Term {
        private int offset;
        private String value;

    }

    @Setter
    @Getter
    public static class MatchedSubstring {
        private int offset;
        private int length;

    }

    @Setter
    @Getter
    public static class StructuredFormatting {
        private String mainText;
        private List<MatchedSubstring> mainTextMatchedSubstrings;
        private String secondaryText;

    }
}
