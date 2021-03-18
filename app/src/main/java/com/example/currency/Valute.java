package com.example.currency;

public class Valute {
    public String valute;
    public Param param;


    public Valute(){

    }
}

class Param {

    public String id;
    public String numCode;
    public String charCode;
    public String nominal;
    public String name;
    public float value;
    public float previous;

    public Param(String id, String numCode, String charCode,
               String nominal, String name, float value, float previous){
        this.id=id;
        this.numCode=numCode;
        this.charCode=charCode;
        this.nominal=nominal;
        this.name=name;
        this.value=value;
        this.previous=previous;
    }
}