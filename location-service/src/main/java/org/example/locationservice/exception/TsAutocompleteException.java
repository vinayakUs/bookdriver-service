package org.example.locationservice.exception;

public class TsAutocompleteException extends RuntimeException {
    private String query;
    private String token;
    public TsAutocompleteException(String message,String query) {
        super(String.format("Autocomplete error for q=[%s]: %s", query,message));
    }

}
