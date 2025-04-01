package ru.otus.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {

    private String username;

    private List<String> roles;

    private String firstName;

    private String userId;
}