package com.assignment;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

public class Models {
    public record Person(int id, String name) {}
    
    public record CreatePersonRequest(String name) {}
    
    public record KnowsRequest(Integer a, Integer b) {}
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PathResponse(boolean connected, Integer hops, List<Person> path) {}
    
    public record ErrorResponse(String error) {}
}
