package org.speer.core.service;

import jakarta.inject.Inject;
import org.speer.core.db.NoteDAO;
import org.speer.core.db.UserDAO;
import org.speer.core.entities.Note;
import org.speer.core.entities.User;

import java.util.List;
import java.util.stream.Collectors;


public class NoteServiceImpl implements NoteService {

    private final UserDAO userDAO;
    private final NoteDAO noteDAO;

    @Inject
    public NoteServiceImpl(UserDAO userDAO, NoteDAO noteDAO) {
        this.userDAO = userDAO;
        this.noteDAO = noteDAO;
    }

    private void validateUser(User user, Note note){
        if(user.getNotes().contains(note)){
            return;
        }
        throw new RuntimeException("User not authorised!!");
    }

    @Override
    public Note getNote(User user, Long noteId){
        try {
            Note note = noteDAO.findById(noteId);
            validateUser(user, note);
            return note;

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void createNote(User user, Note note){
        noteDAO.create(note);
        user.addNote(note);
        userDAO.update(user);
    }

    @Override
    public void updateNote(User user, Note note){
        Note originalNote = noteDAO.findById(note.getNote_id());
        validateUser(user, originalNote);
        user.getNotes().remove(originalNote);
        user.addNote(note);
        userDAO.update(user);
        noteDAO.update(note);
    }

    @Override
    public void deleteNote(User user, Long noteId){
        try {
            Note note = noteDAO.findById(noteId);
            validateUser(user, note);
            user.getNotes().remove(note);
            userDAO.update(user);
            noteDAO.delete(note);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void shareNote(String username, Long noteId, User user){
        try {
            Note note = noteDAO.findById(noteId);
            validateUser(user, note);
            User sharedUser = userDAO.findByUsername(username);
            if(!sharedUser.getNotes().contains(note)){
                sharedUser.addNote(note);
                userDAO.update(sharedUser);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Note> searchNotes(User user, String query){
        List<Note> noteList = user.getNotes();
        return noteList
                .stream()
                .filter(note-> note.getTitle().contains(query) || note.getContent().contains(query))
                .collect(Collectors.toList());

    }




}
