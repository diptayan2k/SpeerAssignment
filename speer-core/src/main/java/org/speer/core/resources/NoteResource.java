package org.speer.core.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.ResponseMetered;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.speer.core.entities.Note;
import org.speer.core.entities.User;
import org.speer.core.models.GenericResponse;
import org.speer.core.service.NoteService;
import org.speer.core.util.RateLimiter;

import java.util.List;

@Path("api/notes")
@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteResource {

    private final NoteService noteService;

    @Inject
    public NoteResource(NoteService noteService) {
        this.noteService = noteService;
    }

    @GET
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @UnitOfWork
    public GenericResponse<List<Note>> getNotes(@Auth User user){
        try {
            /**
                Ideally this is not a good way to implement this,
                but due to time crunch, implementing
                like this.
            **/
            RateLimiter.handleRateLimiting(user.getUsername());
            return GenericResponse
                    .<List<Note>>builder()
                    .success(true)
                    .data(user.getNotes())
                    .build();
        } catch (Exception e){
            return GenericResponse
                    .<List<Note>>builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @GET
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @Path("/{noteId}")
    @UnitOfWork
    public GenericResponse<Note> getNote(@PathParam("noteId") Long id, @Auth User user){
        try {
            RateLimiter.handleRateLimiting(user.getUsername());
            Note note = noteService.getNote(user, id);
            if(note == null){
                return GenericResponse
                        .<Note>builder()
                        .success(false)
                        .message("Note not found!!")
                        .build();
            }
            return GenericResponse
                    .<Note>builder()
                    .success(true)
                    .data(note)
                    .build();
        }catch (Exception e){
            return GenericResponse
                    .<Note>builder()
                    .success(false)
                    .message(e.getMessage())
                    .build();
        }

    }

    @POST
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @UnitOfWork
    public GenericResponse createNote(Note note, @Auth User user){
        try {
            RateLimiter.handleRateLimiting(user.getUsername());
            noteService.createNote(user, note);
            return GenericResponse
                    .builder()
                    .success(true)
                    .build();

        } catch (Exception e){
            return GenericResponse
                    .<List<Note>>builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @PUT
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @UnitOfWork
    public GenericResponse updateNote(Note note, @Auth User user){
        try {
            RateLimiter.handleRateLimiting(user.getUsername());
            noteService.updateNote(user, note);
            return GenericResponse
                    .builder()
                    .success(true)
                    .build();
        }catch (Exception e){
            return GenericResponse
                    .<List<Note>>builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @DELETE
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @Path("/{noteId}")
    @UnitOfWork
    public GenericResponse deleteNote(@PathParam("noteId") Long noteId, @Auth User user){
        try {
            RateLimiter.handleRateLimiting(user.getUsername());
            noteService.deleteNote(user, noteId);
            return GenericResponse
                    .builder()
                    .success(true)
                    .build();
        } catch (Exception e){
            return GenericResponse
                    .<List<Note>>builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @POST
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @Path("/{noteId}/{username}/share")
    @UnitOfWork
    public GenericResponse shareNote(@PathParam("username") String username, @PathParam("noteId") Long noteId, @Auth User user){
        try {
            RateLimiter.handleRateLimiting(user.getUsername());
            noteService.shareNote(username, noteId, user);
            return GenericResponse
                    .builder()
                    .success(true)
                    .build();
        } catch (Exception e){
            return GenericResponse
                    .<List<Note>>builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }
    }

    @GET
    @Timed
    @ExceptionMetered
    @ResponseMetered
    @UnitOfWork
    @Path("/search")
    public GenericResponse<List<Note> > searchNotes(@QueryParam("query") String query ,@Auth User user){
        try {
            RateLimiter.handleRateLimiting(user.getUsername());
            return GenericResponse
                    .<List<Note>>builder()
                    .success(true)
                    .data(noteService.searchNotes(user, query))
                    .build();
        } catch (Exception e){
            return GenericResponse
                    .<List<Note>>builder()
                    .message(e.getMessage())
                    .success(false)
                    .build();
        }
    }





}
