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

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public String getListPage(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/book-edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("book not found with id = %s", id)));
        model.addAttribute("book", book);
        return "book-editor";
    }

    @PostMapping("/book-edit")
    public String savePerson(Book book) {
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Book deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting book: " + e.getMessage());
        }
        return "redirect:/books";
    }


    @GetMapping("/book-create")
    public String createBook(Model model) {
        model.addAttribute("book", new Book());
        return "book-create";
    }

    @PostMapping("/book-create")
    public String saveNewBook(@Validated Book book) {
        bookService.save(book);
        return "redirect:/books";
    }
}
