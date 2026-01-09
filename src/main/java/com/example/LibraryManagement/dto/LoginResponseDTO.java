package com.example.LibraryManagement.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor       // Needed for Jackson deserialization
@AllArgsConstructor      // Needed for builder to work properly
public class LoginResponseDTO {
    private String token;    // mandatory
    private String username; // optional
    private Set<String> roles; // optional
}
