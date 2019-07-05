package com.example.demo.service.impl;

import com.example.demo.config.LibraryProperties;
import com.example.demo.domain.dto.AuthorForm;
import com.example.demo.domain.dto.BookForm;
import com.example.demo.domain.Author;
import com.example.demo.domain.Book;
import com.example.demo.domain.Publisher;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.AuthorService;
import com.example.demo.service.BookService;
import com.example.demo.service.PublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final LibraryProperties properties;
    private final BookRepository repository;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    @Override
    public List<Book> findAll() {
        log.info("Request to find all books");
        return repository.findAll();
    }

    @Override
    public Book findByIsbn(String isbn) {
        return repository.findByIsbn(isbn);
    }

    @Override
    public Book save(BookForm bookForm) {
        log.info("Request to save book with isbn '{}'", bookForm.getIsbn());
        List<Author> authors = createAuthors(bookForm);
        Publisher publisher = createPublisher(bookForm);

        authors = authorService.saveAll(authors);
        publisher = publisherService.save(publisher);

        Book book = createBook(bookForm, authors, publisher);

        return repository.save(book);
    }

    @Override
    public void delete(BookForm bookForm) {
        repository.deleteById(bookForm.getIsbn());
    }

    @Override
    public BookForm createBookForm(Book book) {
        BookForm bookForm = new BookForm();
        bookForm.setIsbn(book.getIsbn());
        bookForm.setName(book.getName());
        bookForm.setPublisher(book.getPublisher() != null ? book.getPublisher().getName() : "No publisher");
        bookForm.setPublishYear(book.getPublishYear());
        bookForm.setCopies(book.getCopies());

        List<AuthorForm> authorForms = new ArrayList<>();
        book.getAuthors().forEach(author -> authorForms.add(authorService.createAuthorForm(author)));
        bookForm.setAuthors(authorForms);

        return bookForm;
    }

    private Book createBook(BookForm bookForm, List<Author> authors, Publisher publisher) {

        Book book = new Book();
        book.setIsbn(bookForm.getIsbn());
        book.setName(bookForm.getName());
        book.setPublishYear(bookForm.getPublishYear());
        book.setCopies(bookForm.getCopies());
        book.setAuthors(authors);
        book.setPublisher(publisher);
        book.setPicture(bookForm.getPicture());

        return book;
    }

    private List<Author> createAuthors(BookForm bookForm) {
        List<Author> authors = new ArrayList<>();
        for(AuthorForm authorForm : bookForm.getAuthors()) {
            Author author = new Author();
            author.setFirstName(authorForm.getFirstName());
            author.setLastName(authorForm.getLastName());
            authors.add(author);
        }

        return authors;
    }

    private Publisher createPublisher(BookForm bookForm) {
        Publisher publisher = new Publisher();
        publisher.setName(bookForm.getPublisher());

        return publisher;
    }
}
