package org.example.locationservice.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum TSType {
    PICKUP,
    DESTINATION
}
@Getter
@Setter
@NoArgsConstructor
public class TSSuggestionsDto {
    private String searchToken;
    private String q;
    private TSType type;

    public TSSuggestionsDto(String searchToken, String q, TSType type) {
        this.searchToken = searchToken;
        this.q = q;
        this.type = type;
    }
}
