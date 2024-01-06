package org.speer.core.db;

import io.dropwizard.hibernate.AbstractDAO;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.inject.Inject;
import org.hibernate.SessionFactory;
import org.speer.core.entities.Note;

import java.util.ArrayList;

public class NoteDAO extends AbstractDAO<Note> {

    @Inject
    public NoteDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Note findById(Long id) {
        return get(id);
    }

    public void create(Note note) {
        persist(note);
    }

    public void update(Note note) {
        currentSession().merge(note);
    }

    public void delete(Note note) {
        currentSession().remove(note);
    }
}
