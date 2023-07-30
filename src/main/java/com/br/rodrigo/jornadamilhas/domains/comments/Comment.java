package com.br.rodrigo.jornadamilhas.domains.comments;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String photo;
    private String username;
    private String comment;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public Comment(ClientCommentRegistration clientCommentRegistration) {
        this.comment = clientCommentRegistration.comment();
    }
}
