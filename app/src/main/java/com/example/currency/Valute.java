package com.example.currency;

import com.google.gson.JsonElement;

public class Valute {
    public String valute;
    public String id;
    public String numCode;
    public String charCode;
    public int nominal;
    public String name;
    public double value;
    public double previous;

    public Valute(JsonElement id, JsonElement numCode, JsonElement charCode,
                  JsonElement nominal, JsonElement name, JsonElement value, JsonElement previous){
        this.id = id.getAsString();
        this.numCode = numCode.getAsString();
        this.charCode = charCode.getAsString();
        this.nominal = nominal.getAsInt();
        this.name = name.getAsString();
        this.value = value.getAsFloat();
        this.previous = previous.getAsFloat();
    }
}
