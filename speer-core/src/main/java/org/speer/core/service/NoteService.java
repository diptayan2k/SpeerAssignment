package org.speer.core.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.speer.core.db.NoteDAO;
import org.speer.core.db.UserDAO;
import org.speer.core.entities.Note;
import org.speer.core.entities.User;

import java.util.List;
import java.util.Optional;


public class NoteService {

    private  final UserDAO userDAO;
    private final NoteDAO noteDAO;

    @Inject
    public NoteService(UserDAO userDAO, NoteDAO noteDAO) {
        this.userDAO = userDAO;
        this.noteDAO = noteDAO;
    }

    private void validateUser(User user, Note note){
        if(note.getCreatedBy().getUsername().equals(user.getUsername())){
            return;
        }
        throw new RuntimeException("User not authorised!!");
    }

    public Note getNote(User user, Long noteId){
        try {
            Note note = noteDAO.findById(noteId);
            validateUser(user, note);
            return note;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    public void createNote(User user, Note note){
        note.setCreatedBy(user);
        user.addNote(note);
        noteDAO.create(note);
        userDAO.update(user);
    }

    public void updateNote(User user, Note note){
        validateUser(user, note);
        note.setCreatedBy(user);
        noteDAO.update(note);
    }

    public void deleteNote(User user, Long noteId){
        try {
            Note note = noteDAO.findById(noteId);
            validateUser(user, note);
            noteDAO.delete(note);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void shareNote(List<String> userIds, Long noteId, User user){
        try {
            Note note = noteDAO.findById(noteId);
            validateUser(user, note);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }




}
