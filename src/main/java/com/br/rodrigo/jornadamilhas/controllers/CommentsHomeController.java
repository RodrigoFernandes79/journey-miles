package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.comments.Comment;
import com.br.rodrigo.jornadamilhas.domains.comments.ListClientCommentsOutput;
import com.br.rodrigo.jornadamilhas.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/comments-home")
public class CommentsHomeController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<ListClientCommentsOutput>> showRandomComments() {
        List<Comment> randomComments = commentService.showRandomComments();
        List<ListClientCommentsOutput> listRandomComments = randomComments
                .stream().map(ListClientCommentsOutput::new).collect(Collectors.toList());
        ;

        return ResponseEntity.ok().body(listRandomComments);
    }
}
