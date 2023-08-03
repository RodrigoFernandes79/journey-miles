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
    private String name;
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
        if (dataInputUpdate.photo() != null) {
            this.photo = dataInputUpdate.photo();
        }
        if (dataInputUpdate.price() != null) {
            this.price = BigDecimal.valueOf(dataInputUpdate.price().doubleValue());
        }
    }
}
