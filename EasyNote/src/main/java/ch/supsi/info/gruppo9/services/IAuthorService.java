package ch.supsi.info.gruppo9.services;

import ch.supsi.info.gruppo9.model.Author;

public interface IAuthorService extends IApplicationService {
    /* function that returns the author */
    Author getAuthor();
}
