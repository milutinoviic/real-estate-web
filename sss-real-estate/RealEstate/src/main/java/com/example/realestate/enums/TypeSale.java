package com.example.realestate.enums;

public enum TypeSale {
    FOR_SALE("For Sale"),
    RENT("Rent");

    private final String label;

    TypeSale(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
