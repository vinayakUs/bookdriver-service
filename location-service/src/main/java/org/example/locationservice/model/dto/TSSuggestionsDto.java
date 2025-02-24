package org.example.locationservice.model.dto;


import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private String searchToken;

    @NotNull
    private String q;
    @NotNull
    private TSType type;

    public TSSuggestionsDto(String searchToken, String q, TSType type) {
        this.searchToken = searchToken;
        this.q = q;
        this.type = type;
    }
}
