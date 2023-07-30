package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.comments.ClientCommentRegistration;
import com.br.rodrigo.jornadamilhas.domains.comments.Comment;
import com.br.rodrigo.jornadamilhas.domains.comments.DeleteCommentInput;
import com.br.rodrigo.jornadamilhas.domains.comments.ListClientCommentsOutput;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.repositories.ClientRepository;
import com.br.rodrigo.jornadamilhas.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CommentRepository commentRepository;

    public Comment registerComment(ClientCommentRegistration clientCommentRegistration) {
        var client = clientRepository.findById(clientCommentRegistration.client_id())
                .orElseThrow(() -> new DataNotFoundException(
                        "Username " + clientCommentRegistration.client_id() + " doesn't exists"));
        var comment = new Comment(clientCommentRegistration);
        comment.setClient(client);
        comment.setUsername(client.getUsername());
        comment.setPhoto(client.getPhoto());
        commentRepository.save(comment);
        return comment;


    }

    public Page<ListClientCommentsOutput> listAllComments(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        if (comments.isEmpty()) {
            throw new DataNotFoundException("No comments have been added yet");
        }
        Page<ListClientCommentsOutput> commentsOutput = comments.map(ListClientCommentsOutput::new);
        return commentsOutput;
    }

    public Page<ListClientCommentsOutput> findCommentByIdClient(
            Long id_client, Pageable pageable) {
        var verifyClient = clientRepository.findById(id_client)
                .orElseThrow(() -> new DataNotFoundException(
                        "Client Id " + id_client + " not found"));
        Page<Comment> comments = commentRepository.findCommentByClientId(id_client, pageable);
        if (comments.isEmpty()) {
            throw new DataNotFoundException("No comments have been added yet");
        }
        Page<ListClientCommentsOutput> commentsOutput = comments.map(ListClientCommentsOutput::new);
        return commentsOutput;

    }

    public Comment updateComment(Long id,
                                 ClientCommentRegistration clientCommentRegistration) {
        var verifyComment = commentRepository.findById(id);

        if (verifyComment.isEmpty()) {
            throw new DataNotFoundException("Commentary " + id +
                    " doesn't exists");
        }
        var verifyClient = clientRepository
                .findById(clientCommentRegistration.client_id());
        if (verifyClient.isPresent()) {
            verifyComment.get().setComment(clientCommentRegistration.comment());
            return verifyComment.get();
        }
        throw new DataNotFoundException("Client does not exists or commentary not from client");
    }

    public void deleteComment(Long id, DeleteCommentInput deleteCommentInput) {
        var verifyComment = commentRepository.findById(id);
        if (verifyComment.isEmpty()) {
            throw new DataNotFoundException("Comment does not exist");
        }
        var verifyClient = clientRepository.findById(
                deleteCommentInput.client_id());
        if (verifyClient.isEmpty()) {
            throw new DataNotFoundException("Client does not exist");
        }
        commentRepository.delete(verifyComment.get());
    }

    public List<Comment> showRandomComments() {
        var comments = commentRepository.findAll();
        if (comments.isEmpty()) {

            throw new DataNotFoundException("No comments have been added yet");
        }
        Collections.shuffle(comments);
        List<Comment> result = new ArrayList<>();
        Set<String> namesSet = new HashSet<>();

        // Percorrer os comentários embaralhados para selecionar 3 depoimentos com nomes únicos
        for (Comment comment : comments) {
            if (!namesSet.contains(comment.getUsername())) {
                result.add(comment);
                namesSet.add(comment.getUsername());
                if (result.size() == 3) {
                    break;
                }
            }
        }

        if (result.size() < 3) {
            // Se não houver 3 depoimentos com nomes únicos, adicione os depoimentos restantes
            int remainingComments = 3 - result.size();
            for (Comment comment : comments) {
                if (!result.contains(comment)) {
                    result.add(comment);
                    remainingComments--;
                    if (remainingComments == 0) {
                        break;
                    }
                }
            }
        }

        return result;

    }


}

