package com.br.rodrigo.jornadamilhas.domains.client;

import com.br.rodrigo.jornadamilhas.domains.address.Address;
import com.br.rodrigo.jornadamilhas.domains.comments.Comment;
import com.br.rodrigo.jornadamilhas.domains.destination.Destination;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String cpf;
    private String photo;
    private String password;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Embedded
    private Address address;
    @ManyToMany()
    @JoinTable(name = "clients-destinations",
            joinColumns = {@JoinColumn(name = "client_id")},
            inverseJoinColumns = {@JoinColumn(name = "destination_id")})
    private List<Destination> destinations = new ArrayList<>();

    public Client(ClientDataInput dataInput) {
        this.username = dataInput.username();
        this.email = dataInput.email();
        this.cpf = dataInput.cpf();
        this.password = dataInput.repeatPassword();

    }

    public void dataUpdateClient(ClientDataInputUpdate clientDataInputUpdate) {
        if (clientDataInputUpdate.photo() != null) {
            this.photo = clientDataInputUpdate.photo();
        }
        if (clientDataInputUpdate.phoneNumber() != null) {
            this.phoneNumber = clientDataInputUpdate.phoneNumber();
        }
        if (clientDataInputUpdate.dataAddress() != null) {
            if (this.address == null) {
                this.address = new Address(clientDataInputUpdate.dataAddress());
            }
            this.address.dataUpdateAddress(clientDataInputUpdate.dataAddress());
        }
    }
}

