package com.br.rodrigo.jornadamilhas.repositories;

import com.br.rodrigo.jornadamilhas.domains.comments.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findCommentByClientId(Long id_client, Pageable pageable);
}
