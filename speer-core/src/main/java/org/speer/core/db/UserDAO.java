package org.speer.core.db;

import io.dropwizard.hibernate.AbstractDAO;
import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.speer.core.entities.Note;
import org.speer.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    @Inject
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public User findById(Long id) {
        return get(id);
    }

    public User findByUsername(String username) {
            return  (User) namedQuery("User.findByUsername")
                    .setParameter("username", username)
                    .uniqueResult();

    }

    public User create(User user) {
        user.setNotes(new ArrayList<>());
        return persist(user);
    }

    public void update(User user){
        currentSession().merge(user);
    }

    public List<Note> getAllNotesForUser(String username) {
        User user = findByUsername(username);
        if (user != null) {
            return user.getNotes();
        }
        throw new RuntimeException("Invalid User!!");
    }
}
