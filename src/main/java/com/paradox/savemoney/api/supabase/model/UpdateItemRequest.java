package com.paradox.savemoney.api.supabase.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateItemRequest extends ItemBase {
    @NotNull
    private Long _id;
}
