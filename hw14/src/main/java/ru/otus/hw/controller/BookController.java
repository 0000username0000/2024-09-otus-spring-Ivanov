package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.services.BookService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequiredArgsConstructor
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    @GetMapping("/books")
    public String getListPage(Model model) {
        logger.debug("Fetching list of books");
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book-edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        logger.debug("Editing book with id: {}", id);
        Book book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Book not found with id = %s", id)));
        model.addAttribute("book", book);
        return "book-editor";
    }

    @PostMapping("/book-edit")
    public String savePerson(Book book) {
        logger.debug("Saving edited book: {}", book);
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.debug("Deleting book with id: {}", id);
        try {
            bookService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Book deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting book with id: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error deleting book: " + e.getMessage());
        }
        return "redirect:/books";
    }

    @GetMapping("/book-create")
    public String createBook(Model model) {
        logger.debug("Creating new book");
        model.addAttribute("book", new Book());
        return "book-create";
    }

    @PostMapping("/book-create")
    public String saveNewBook(@Validated Book book) {
        logger.debug("Saving new book: {}", book);
        bookService.save(book);
        return "redirect:/books";
    }
}
