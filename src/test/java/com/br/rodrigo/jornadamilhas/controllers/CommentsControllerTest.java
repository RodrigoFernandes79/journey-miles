package com.br.rodrigo.jornadamilhas.controllers;

import com.br.rodrigo.jornadamilhas.domains.client.Client;
import com.br.rodrigo.jornadamilhas.domains.comments.*;
import com.br.rodrigo.jornadamilhas.exceptions.DataNotFoundException;
import com.br.rodrigo.jornadamilhas.repositories.CommentRepository;
import com.br.rodrigo.jornadamilhas.services.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class CommentsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<ClientCommentRegistration> registrationJacksonTester;
    @Autowired
    private JacksonTester<ClientCommentOutput> outputJacksonTester;
    @Autowired
    private JacksonTester<Page<ListClientCommentsOutput>> listClientCommentsOutputJacksonTester;
    @Autowired
    private JacksonTester<DeleteCommentInput> deleteCommentInputJacksonTester;
    @MockBean
    private CommentService commentService;
    @MockBean
    private CommentRepository commentRepository;

    @Test
    @DisplayName("registerComment: Should return http 400 when information's are invalids")
    @WithMockUser
    void registerComment_scenario_1() throws Exception {
        var response = mvc.perform(post("/comments"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("registerComment: Should return http 201 when information's are valid")
    @WithMockUser
    void registerComment_scenario_2() throws Exception {
        // Arrange
        Long clientId = 1L;
        String commentText = "Enjoy Travel";
        // Create a valid client
        Client client = new Client();
        client.setUsername("David");
        // Set up the mock behavior to return a valid Comment
        Comment comment = new Comment();
        comment.setClient(client);
        comment.setUsername(client.getUsername());
        comment.setComment(commentText);

        when(commentService.registerComment(any())).thenReturn(comment);
        // Act
        var response = mvc.perform(
                        post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registrationJacksonTester.write(
                                        new ClientCommentRegistration(clientId, commentText)).getJson())
                )
                .andReturn().getResponse();
        // Assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var expectedOutput = new ClientCommentOutput(client.getUsername(), null, commentText);
        var expectedJson = outputJacksonTester.write(expectedOutput).getJson();

        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @DisplayName("listAllComments: Should return http 200 when information's are valid")
    @WithMockUser
    void listAllComments_scenario_2() throws Exception {
        //Arrange
        Pageable pageable = PageRequest.of(0, 6, Sort.by("username"));

        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setUsername("user1");
        Comment comment2 = new Comment();
        comment2.setUsername("user2");
        comments.add(comment1);
        comments.add(comment2);

        Page<ListClientCommentsOutput> page = new PageImpl<>(
                comments.stream().map(ListClientCommentsOutput::new).collect(Collectors.toList()));
        when(commentService.listAllComments(pageable)).thenReturn(page);
        //Act
        var result = mvc.perform(get("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "6")
                        .param("sort", "username"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        // Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());

        String responseJson = result.getContentAsString();
        String expectedJson = listClientCommentsOutputJacksonTester.write(page).getJson();
        JSONAssert.assertEquals(expectedJson, responseJson, false);
    }

    @Test
    @DisplayName("listAllComments: Should return http 404 when commentary not found")
    @WithMockUser
    void listAllComments_scenario_1() throws Exception {
        Pageable pageable = PageRequest.of(0, 6, Sort.by("username"));
        //Arrange
        List<Comment> comments = new ArrayList<>();
        Page<ListClientCommentsOutput> page = new PageImpl<>(
                comments.stream().map(ListClientCommentsOutput::new).collect(Collectors.toList()));
        when(commentService.listAllComments(pageable)).thenThrow(
                new DataNotFoundException("No comments have been added yet"));

        //Act
        var result = mvc.perform(get("/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        // Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("findCommentByIdClient: Should return http 200 when information's are valid")
    @WithMockUser
    void findCommentByIdClient_scenario_1() throws Exception {
        // Arrange
        Long id_client = 1L;
        Pageable pageable = PageRequest.of(0, 6, Sort.by("username"));

        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setUsername("user1");
        comment1.setComment("I Love Travel");
        comments.add(comment1);

        Page<ListClientCommentsOutput> page = new PageImpl<>(comments.stream()
                .map(ListClientCommentsOutput::new).collect(Collectors.toList()));
        when(commentService.findCommentByIdClient(comment1.getId(), pageable)).thenReturn(page);
        //Act
        var result = mvc.perform(get("/comments/{id_client}", id_client)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "6")
                        .param("sort", "username"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        String expectedJson = listClientCommentsOutputJacksonTester.write(page).getJson();
        String responseJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson, responseJson, false);
    }

    @Test
    @DisplayName("findCommentByIdClient: Should return http 404 when client not found")
    @WithMockUser
    void findCommentByIdClient_scenario_2() throws Exception {
        // Arrange
        Long id_client = null;
        Pageable pageable = PageRequest.of(0, 6, Sort.by("username"));
        when(commentService.findCommentByIdClient(null, pageable)).thenThrow(new DataNotFoundException(
                "Client Id " + id_client + " not found"));
        //Act
        var result = mvc.perform(get("/comments/{id_client}", id_client)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "6")
                        .param("sort", "username"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("updateComment: Should return http 200 ok when information's are valid ")
    @WithMockUser
    void updateComment_scenario_1() throws Exception {
        //Arrange
        Long client_id = 1L;
        Client client = new Client();
        client.setUsername("David");
        String comm = "Travel is awesome!";

        Comment comment = new Comment();
        comment.setUsername(client.getUsername());
        comment.setComment(comm);

        when(commentService.updateComment(any(), any())).thenReturn(comment);
        // Act
        var result = mvc.perform(patch("/comments/{id}", client_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJacksonTester.write(new ClientCommentRegistration(
                                client_id, comment.getComment())).getJson())
                )
                .andReturn()
                .getResponse();
        // Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());

        var commentOutput = new ClientCommentOutput(
                comment.getUsername(), comment.getPhoto(), comment.getComment());
        var expectedJson = outputJacksonTester.write(commentOutput).getJson();

        assertThat(result.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    @DisplayName("updateComment: Should return http 400 when information's are invalid ")
    @WithMockUser
    void updateComment_scenario_2() throws Exception {
        //Arrange
        Long client_id = 1L;
        // Act
        var result = mvc.perform(patch("/comments/{id}", client_id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        // Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deleteComment: Should return http 200 when information´s are valid")
    @WithMockUser
    void deleteComment_scenario_1() throws Exception {
        //Asserts
        Long comment_id = 1L;
        String comm = "Travel is awesome!";

        Client client = new Client();
        client.setId(1l);

        Comment comment = new Comment();
        comment.setId(comment_id);
        comment.setComment(comm);
        // Act
        var result = mvc.perform(delete("/comments/{id}", comment_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteCommentInputJacksonTester.write(new DeleteCommentInput(client.getId())).getJson()
                        ))
                .andReturn()
                .getResponse();
// Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    @DisplayName("deleteComment: Should return http 404 when comment not found")
    @WithMockUser
    void deleteComment_scenario_2() throws Exception {
        //Asserts
        Long comment_id = null;
        Client client = new Client();
        client.setId(1l);

        when(commentRepository.findById(null)).thenThrow(new DataNotFoundException("Comment does not exist"));

        // Act
        var result = mvc.perform(delete("/comments/{id}", comment_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteCommentInputJacksonTester.write(new DeleteCommentInput(client.getId())).getJson()
                        ))
                .andReturn()
                .getResponse();
// Assert
        assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }
}