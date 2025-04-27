package com.example.realestate.enums;

public enum TypeRealEstate {
    HOUSE("House"),
    APARTMENT("Apartment"),
    OFFICE("Office");

    private final String label;

    TypeRealEstate(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
