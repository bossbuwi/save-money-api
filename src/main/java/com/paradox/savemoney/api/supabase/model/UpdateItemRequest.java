package com.paradox.savemoney.api.supabase.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateItemRequest extends ItemBase {
    private long id;
}
