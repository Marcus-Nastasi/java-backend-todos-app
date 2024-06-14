package com.app.todos.Services.Todos;

import com.app.todos.DTOs.Todos.NewTodoDTO;
import com.app.todos.DTOs.Todos.UpdateTodoDTO;
import com.app.todos.Models.Todos.Todo;
import com.app.todos.Repository.Todos.TodosRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class TodosService {

    @Autowired
    private TodosRepo todosRepo;

    public Todo get(BigInteger id) {
        return todosRepo.findById(id).orElseThrow();
    }

    public void newTodo(NewTodoDTO data) {
        Todo t = new Todo(data.user_id(), data.client(), data.description(), data.link(), data.due());
        todosRepo.save(t);
    }

    public void update(BigInteger id, UpdateTodoDTO data) {
        Todo t = todosRepo.findById(id).orElseThrow();

        t.setClient(data.client());
        t.setDescription(data.description());
        t.setLink(data.link());
        t.setDue(data.due());

        todosRepo.save(t);
    }

    public void delete(BigInteger id) {
        todosRepo.deleteById(id);
    }
}


