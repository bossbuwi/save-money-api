package com.paradox.savemoney.api.supabase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateItemRequest extends ItemBase {
}
