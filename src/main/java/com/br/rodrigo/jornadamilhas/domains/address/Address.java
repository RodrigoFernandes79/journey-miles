package com.br.rodrigo.jornadamilhas.domains.address;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String streetOrAvenue;
    private String district;
    private String zipCode;
    private String number;
    private String apartment;
    private String city;
    private String state;


    public Address(DataAddress dataAddress) {
        this.streetOrAvenue = dataAddress.streetOrAvenue();
        this.district = dataAddress.district();
        this.zipCode = dataAddress.zipCode();
        this.number = dataAddress.number();
        this.apartment = dataAddress.apartment();
        this.city = dataAddress.city();
        this.state = dataAddress.state();
    }

    public void dataUpdateAddress(DataAddress dataAddress) {
        if (dataAddress.streetOrAvenue() != null) {
            this.streetOrAvenue = dataAddress.streetOrAvenue();
        }
        if (dataAddress.district() != null) {
            this.district = dataAddress.district();
        }
        if (dataAddress.zipCode() != null) {
            this.zipCode = dataAddress.zipCode();
        }
        if (dataAddress.number() != null) {
            this.number = dataAddress.number();
        }
        if (dataAddress.apartment() != null) {
            this.apartment = dataAddress.apartment();
        }
        if (dataAddress.city() != null) {
            this.city = dataAddress.city();
        }
        if (dataAddress.state() != null) {
            this.state = dataAddress.state();
        }
    }
}
