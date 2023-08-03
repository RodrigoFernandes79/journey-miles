package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.comments.ClientCommentOutput;
import com.br.rodrigo.jornadamilhas.domains.comments.ClientCommentRegistration;
import com.br.rodrigo.jornadamilhas.domains.comments.DeleteCommentInput;
import com.br.rodrigo.jornadamilhas.domains.comments.ListClientCommentsOutput;
import com.br.rodrigo.jornadamilhas.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentService commentService;


    @PostMapping
    @Transactional
    public ResponseEntity<ClientCommentOutput> registerComment(
            @Valid @RequestBody ClientCommentRegistration clientCommentRegistration,
            UriComponentsBuilder uriComponentsBuilder) {

        var comment = commentService.registerComment(clientCommentRegistration);
        var uri = uriComponentsBuilder.path("/comments/{id}")
                .buildAndExpand(comment.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClientCommentOutput(comment));
    }

    @GetMapping
    public ResponseEntity<Page<ListClientCommentsOutput>> listAllComments(
            @PageableDefault(size = 6, sort = {"username"}) Pageable pageable
    ) {
        Page<ListClientCommentsOutput> commentOutput = commentService.listAllComments(pageable);


        return ResponseEntity.ok().body(commentOutput);
    }

    @GetMapping("/{id_client}")
    public ResponseEntity<Page<ListClientCommentsOutput>> findCommentByIdClient(
            @PathVariable Long id_client,
            @PageableDefault(size = 6, sort = "username") Pageable pageable) {
        Page<ListClientCommentsOutput> commentsOutput = commentService.findCommentByIdClient(
                id_client, pageable);

        return ResponseEntity.ok().body(commentsOutput);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<ClientCommentOutput> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody ClientCommentRegistration clientCommentRegistration) {

        var comment = commentService.updateComment(id, clientCommentRegistration);

        return ResponseEntity.ok().body(new ClientCommentOutput(comment));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable Long id,
            @RequestBody DeleteCommentInput deleteCommentInput) {
        commentService.deleteComment(id, deleteCommentInput);
        Map<String, String> message = new HashMap<>();
        message.put("Message: ", "Successfully Deleted Comment");
        return ResponseEntity.ok().body(message);
    }
}
