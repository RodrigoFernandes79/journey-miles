package com.br.rodrigo.jornadamilhas.domains.comments;

public record ClientCommentOutput(String username, String photo, String comment) {

    public ClientCommentOutput(Comment comment) {
        this(comment.getUsername(), comment.getPhoto(), comment.getComment());
    }

}
