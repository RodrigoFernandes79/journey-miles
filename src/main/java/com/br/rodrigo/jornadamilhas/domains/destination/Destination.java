package com.br.rodrigo.jornadamilhas.domains.destination;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    private String photo2;
    private String name;
    private String metaDescription;
    private String textDescription;
    private BigDecimal price;
    @ManyToMany(mappedBy = "destinations")
    @JsonIgnore
    private List<Client> clients = new ArrayList<>();

    public Destination(DestinationDataInput dataInput) {
        this.photo = dataInput.photo();
        this.name = dataInput.name();
        this.price = BigDecimal.valueOf(dataInput.price().doubleValue());
    }

    public void dataUpdateDestination(DestinationDataInputUpdate dataInputUpdate) {
        if (dataInputUpdate.photo_1() != null) {
            this.photo = dataInputUpdate.photo_1();
        }
        if (dataInputUpdate.photo_2() != null) {
            this.photo2 = dataInputUpdate.photo_2();
        }
        if (dataInputUpdate.metaDescription() != null) {
            this.metaDescription = dataInputUpdate.metaDescription();
        }
        if (dataInputUpdate.textDescription() != null) {
            this.textDescription = dataInputUpdate.textDescription();
        }
        if (dataInputUpdate.price() != null) {
            this.price = BigDecimal.valueOf(dataInputUpdate.price().doubleValue());
        }
    }
}
