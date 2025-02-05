package ru.otus.hw.controller.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class AuthorRestControllerTest {

//    private MockMvc mockMvc;
//
//    @Mock
//    private AuthorServiceImpl authorServiceImpl;
//
//    @Mock
//    private AuthorDtoService authorDtoService;
//
//    @InjectMocks
//    private AuthorRestController authorRestController;
//
//    public AuthorRestControllerTest() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(authorRestController).build();
//    }
//
//    @Test
//    void testGetAllAuthors() throws Exception {
//        AuthorDto authorDto = new AuthorDto(1L, "John Doe");
//        when(authorServiceImpl.findAll()).thenReturn(Collections.singletonList(new Author()));
//        when(authorDtoService.toDtoList(anyList())).thenReturn(Collections.singletonList(authorDto));
//
//        mockMvc.perform(get("/api/authors")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
//
//        verify(authorServiceImpl, times(1)).findAll();
//        verify(authorDtoService, times(1)).toDtoList(anyList());
//    }
}
