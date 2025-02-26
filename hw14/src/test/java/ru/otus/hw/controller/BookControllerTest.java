package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Book;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.UsersService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UsersService usersService;

    private Book testBook;

    @BeforeEach
    public void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetListPage_Admin() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetListPage_User() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetListPage_Unauthorized() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testEditPage_Admin() throws Exception {
        when(bookService.findById(1L)).thenReturn(Optional.of(testBook));

        mockMvc.perform(get("/book-edit").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testEditPage_User() throws Exception {
        mockMvc.perform(get("/book-edit").param("id", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testEditPage_Unauthorized() throws Exception {
        mockMvc.perform(get("/book-edit").param("id", "1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteBook_Admin() throws Exception {
        mockMvc.perform(get("/delete").param("id", "1"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteBook_User() throws Exception {
        mockMvc.perform(get("/delete").param("id", "1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteBook_Unauthorized() throws Exception {
        mockMvc.perform(get("/delete").param("id", "1"))
                .andExpect(status().isUnauthorized());
    }
}
