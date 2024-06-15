package com.app.todos.Services.Todos;

import com.app.todos.DTOs.Todos.NewTodoDTO;
import com.app.todos.DTOs.Todos.UpdateTodoDTO;
import com.app.todos.Models.Todos.Todo;
import com.app.todos.Models.Users.User;
import com.app.todos.Repository.Todos.TodosRepo;
import com.app.todos.Repository.User.UserRepo;
import com.app.todos.Services.Auth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Todo get(BigInteger id) {
        return todosRepo.findById(id).orElseThrow();
    }

    public void newTodo(NewTodoDTO data) {
        Todo t = new Todo(data.user_id(), data.client(), data.title(), data.description(), data.link(), data.due(), data.priority());
        todosRepo.save(t);
    }

    public void update(BigInteger id, UpdateTodoDTO data, String token) {
        Todo t = todosRepo.findById(id).orElseThrow();
        User u = userRepo.findById(t.getUser_id()).orElseThrow();

        if (!tokenService.validate(token).equals(u.getEmail())) return;

        t.setClient(data.client());
        t.setDescription(data.description());
        t.setLink(data.link());
        t.setDue(data.due());

        todosRepo.save(t);
    }

    public void delete(BigInteger id) {
        todosRepo.deleteById(id);
    }

    public List<Todo> getAll(BigInteger user_id) {
        return todosRepo.getUserTodos(user_id);
    }
}


