package com.app.todos.application.service.Todos;

import com.app.todos.domain.Todos.DTOs.TodosRequestDTO;
import com.app.todos.domain.Todos.DTOs.TodosStatusDTO;
import com.app.todos.domain.Todos.DTOs.TodosUpdateDTO;
import com.app.todos.domain.Todos.Todo;
import com.app.todos.domain.Users.User;
import com.app.todos.resources.repository.Todos.TodosRepo;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.Auth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class TodosService {

    @Autowired
    private TodosRepo todosRepo;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepo userRepo;

    public Todo get(BigInteger id, String token) {
        Todo t = todosRepo.findById(id).orElseThrow();
        User u = userRepo.findById(t.getUser_id()).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return null;
        return todosRepo.findById(id).orElseThrow();
    }

    public List<Todo> getAll(BigInteger user_id, String token, int page, int size) {
        User u = userRepo.findById(user_id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return null;
        Page<Todo> todoPage = todosRepo.getUserTodos(user_id, PageRequest.of(page, size));
        return todoPage.toList();
    }

    public List<Todo> getDone(BigInteger id, String token) {
        User u = userRepo.findById(id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return null;
        return todosRepo.getDone(id);
    }

    public List<Todo> getProgress(BigInteger id, String token) {
        User u = userRepo.findById(id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return null;
        return todosRepo.getInProgress(id);
    }

    public List<Todo> getPending(BigInteger id, String token) {
        User u = userRepo.findById(id).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return null;
        return todosRepo.getPending(id);
    }

    public void newTodo(TodosRequestDTO data) {
        Todo t = new Todo(
            data.user_id(), data.client(), data.title(), data.description(), data.link(), data.due(), data.priority()
        );
        todosRepo.save(t);
    }

    public void update(BigInteger id, TodosUpdateDTO data, String token) {
        Todo t = todosRepo.findById(id).orElseThrow();
        User u = userRepo.findById(t.getUser_id()).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return;
        t.setTitle(data.title());
        t.setClient(data.client());
        t.setDescription(data.description());
        t.setLink(data.link());
        t.setDue(data.due());
        t.setPriority(data.priority());
        todosRepo.save(t);
    }

    public void updateStatus(BigInteger id, TodosStatusDTO data, String token) {
        Todo t = todosRepo.findById(id).orElseThrow();
        User u = userRepo.findById(t.getUser_id()).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return;
        t.setStatus(data.status());
        todosRepo.save(t);
    }

    public void delete(BigInteger id, String token) {
        Todo t = todosRepo.findById(id).orElseThrow();
        User u = userRepo.findById(t.getUser_id()).orElseThrow();
        if (!tokenService.validate(token).equals(u.getEmail())) return;
        todosRepo.deleteById(id);
    }
}
