package com.mehedi.controller;

import com.mehedi.dto.BookSearchByPrefixDTO;
import com.mehedi.entity.Book;
import com.mehedi.exception.BookNotFoundException;
import com.mehedi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long bookId,
            @RequestBody Book updatedBook
    ) {
        try {
            Book updated = bookService.updateBook(bookId, updatedBook);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (BookNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to update the book.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        try {
            bookService.deleteBook(bookId);
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        } catch (BookNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to delete the book. " + ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to fetch books." + ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Long bookId) {
        try {
            Optional<Book> book = bookService.findBookById(bookId);

            if (book.isPresent()) {
                return new ResponseEntity<>(book.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Book not found with id: " + bookId, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to fetch the book with id: " + bookId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBooksByTitle(@RequestParam("title") String title) {
        try {
            List<Book> books = bookService.searchBooksByTitle(title);
            if (!books.isEmpty()) {
                return new ResponseEntity<>(books, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No books found with the title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to search for books with title: " + title, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}