package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.model.Author;

public class AuthorService implements IAuthorService {
    /* Author Service */
    private static AuthorService service;
    /* class that holds the author */
    private final Author author;

    public static AuthorService getInstance() {
        if (service == null)
            service = new AuthorService();
        return service;
    }

    /* this constructor takes the user name via property */
    private AuthorService() {
        String username = System.getProperty("user.name");
        author = new Author(username);
    }

    public Author getAuthor() {
        return author;
    }
}
