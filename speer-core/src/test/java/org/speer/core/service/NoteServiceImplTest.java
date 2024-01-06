package org.speer.core.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.speer.core.db.NoteDAO;
import org.speer.core.db.UserDAO;
import org.speer.core.entities.Note;
import org.speer.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class NoteServiceImplTest {


    private UserDAO userDAO;
    private NoteDAO noteDAO;
    private NoteServiceImpl noteService;

    private User userRaju;
    private Note note1;
    private  Note note2;
    private Note note3;

    public NoteServiceImplTest() {
        this.userDAO = Mockito.mock(UserDAO.class);
        this.noteDAO = Mockito.mock(NoteDAO.class);
        noteService = new NoteServiceImpl(userDAO, noteDAO);
    }

    @Before
    public void setUp(){

        note1 = Note
                .builder()
                .note_id(1L)
                .title("note1")
                .content("This is note 1")
                .build();

        note2 = Note
                .builder()
                .note_id(2L)
                .title("note2")
                .content("This is note 2")
                .build();

        note3 = Note
                .builder()
                .note_id(3L)
                .title("note3")
                .content("This is note 3")
                .build();

        userRaju = User
                .builder()
                .id(1L)
                .name("Raju")
                .username("raju")
                .password("password1")
                .notes(List.of(note1, note2))
                .build();

        Mockito.when(noteDAO.findById(1L)).thenReturn(note1);
        Mockito.when(noteDAO.findById(2L)).thenReturn(note2);
        Mockito.when(noteDAO.findById(3L)).thenReturn(note3);
        Mockito.doNothing().when(noteDAO).create(Mockito.any(Note.class));
        Mockito.doNothing().when(noteDAO).delete(Mockito.any(Note.class));
        Mockito.doNothing().when(noteDAO).update(Mockito.any(Note.class));
        Mockito.doNothing().when(userDAO).update(Mockito.any(User.class));
        Mockito.doNothing().when(userDAO).update(Mockito.any(User.class));
    }

    @Test
    public void getNoteSuccessfulTest(){
        Note note = noteService.getNote(userRaju, 1L);
        Assert.assertEquals(note, note1);
    }

    @Test
    public void getNoteValidationFailedTest(){
        RuntimeException e = Assert.assertThrows(RuntimeException.class, ()-> noteService.getNote(userRaju, 3L));
        Assert.assertEquals(e.getMessage(), "User not authorised!!");

    }

    @Test
    public void createNoteTest(){

        User user = User
                .builder()
                .notes(new ArrayList<>())
                .build();
        noteService.createNote(user, note1);
    }

    @Test
    public void shareNoteTest(){

        User user1 = User
                .builder()
                .name("user1")
                .id(2L)
                .notes(new ArrayList<>())
                .build();

        Mockito.when(userDAO.findByUsername("user1")).thenReturn(user1);
        noteService.shareNote("user1",1L, userRaju);
        Assert.assertTrue(user1.getNotes().contains(note1));

    }

    @Test
    public void updateNoteTest(){

        List<Note> notes = new ArrayList<>();
        Note note = Note
                .builder()
                .note_id(3L)
                .title("note3")
                .content("This is note 3")
                .build();
        notes.add(note);
        notes.add(note1);

        Note newNote = Note
                        .builder()
                        .note_id(3L)
                        .title("note3")
                        .content("new content")
                        .build();
        notes.add(note);

        User user1 = User
                .builder()
                .name("user1")
                .id(2L)
                .notes(notes)
                .build();
        Assert.assertTrue(user1.getNotes().contains(note));
        noteService.updateNote(user1, newNote);
        Assert.assertTrue(user1.getNotes().contains(newNote));
    }

    @Test
    public void deleteNoteTest(){

        List<Note> notes = new ArrayList<>();
        notes.add(note2);
        notes.add(note1);


        User user1 = User
                .builder()
                .name("user1")
                .id(2L)
                .notes(notes)
                .build();
        Assert.assertTrue(user1.getNotes().contains(note2));
        noteService.deleteNote(user1, note2.getNote_id());
        Assert.assertFalse(user1.getNotes().contains(note2));
    }


}
