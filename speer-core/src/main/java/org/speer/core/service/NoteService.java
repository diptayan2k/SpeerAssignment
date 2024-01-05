package org.speer.core.service;

import org.speer.core.entities.Note;
import org.speer.core.entities.User;

import java.util.List;

public interface NoteService {
    Note getNote(User user, Long noteId);

    void createNote(User user, Note note);

    void updateNote(User user, Note note);

    void deleteNote(User user, Long noteId);

    void shareNote(String username, Long noteId, User user);

    List<Note> searchNotes(User user, String query);
}
