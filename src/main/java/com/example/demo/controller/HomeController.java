package com.example.demo.controller;

import com.example.demo.config.LibraryProperties;
import com.example.demo.domain.Book;
import com.example.demo.domain.dto.BookForm;
import com.example.demo.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.utils.Constants.VIEW_CATALOG;
import static com.example.demo.utils.Constants.VIEW_HOME;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;


    @GetMapping(path = "/all-books")
    public ModelAndView getAllBooksPage(@RequestParam(value="isbn", required=false) String isbn) {
        ModelAndView model = new ModelAndView(VIEW_HOME);

        if (isbn != null) {
            Book book = bookService.findByIsbn(isbn);
            BookForm bookForm = bookService.createBookForm(book);
            model.addObject("book", bookForm);

            return model;
        }

        List<Book> bookList = bookService.findAll();
        model.addObject("bookList", bookList);
        model.addObject("book", new BookForm());

        return model;
    }

    @GetMapping(path = {"/home", "/"})
    public ModelAndView getCatalogPage() {
        ModelAndView modelAndView = new ModelAndView(VIEW_CATALOG);
        List<Book> books = bookService.findAll();
        List<BookForm> bookForms = new ArrayList<>();
        books.forEach(book -> {
            bookForms.add(bookService.createBookForm(book));
        });

        System.out.println("picture content: " + bookForms.get(0).getPictureContent());

        modelAndView.addObject("books", bookForms);

        return modelAndView;
    }





}
