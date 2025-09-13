package com.paradox.savemoney.api.auth.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailAuthRequest extends AuthBase {
    @NotNull
    private String email;
}
