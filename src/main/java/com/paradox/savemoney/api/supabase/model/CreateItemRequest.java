package com.paradox.savemoney.api.supabase.model;

import jakarta.validation.constraints.NotNull;

public class CreateItemRequest extends ItemBase {

    @NotNull
    @Override
    public Double getAmount() {
        return super.getAmount();
    }
}
