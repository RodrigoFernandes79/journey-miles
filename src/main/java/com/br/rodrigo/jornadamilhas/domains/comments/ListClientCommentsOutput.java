package com.br.rodrigo.jornadamilhas.domains.comments;

import java.util.List;

public record ListClientCommentsOutput(String photo, String comment, String username) {

    public ListClientCommentsOutput(Comment comment) {
        this(comment.getPhoto(), comment.getComment(), comment.getUsername());
    }

}
