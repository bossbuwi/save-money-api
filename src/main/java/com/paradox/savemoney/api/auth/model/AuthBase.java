package com.paradox.savemoney.api.auth.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthBase {
    @NotNull
    private String password;
}
