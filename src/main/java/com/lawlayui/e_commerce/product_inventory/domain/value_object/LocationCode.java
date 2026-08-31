package com.lawlayui.e_commerce.product_inventory.domain.value_object;

public record LocationCode(String value) {

    private static final String LOCATION_CODE_PATTERN = "^[A-Za-z0-9]+-[A-Za-z0-9]+-[A-Za-z0-9]+-[A-Za-z0-9]+-[A-Za-z0-9]+$";

    public LocationCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("LocationCode cannot be null or blank");
        }
        if (!value.matches(LOCATION_CODE_PATTERN)) {
            throw new IllegalArgumentException("Invalid Location Code format. Expected format: [Warehouse]-[Area]-[Rack]-[Height]-[Bin] (e.g., CGK01-PICK-12-04-01)");
        }
    }
}
