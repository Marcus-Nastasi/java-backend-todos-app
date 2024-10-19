package com.app.todos.application.service.todos;

import com.app.todos.domain.todos.DTOs.TodosPageResponseDto;
import com.app.todos.domain.todos.DTOs.TodosRequestDTO;
import com.app.todos.domain.todos.DTOs.TodosStatusDTO;
import com.app.todos.domain.todos.DTOs.TodosUpdateDTO;
import com.app.todos.domain.todos.Todo;
import com.app.todos.domain.users.User;
import com.app.todos.web.handler.exception.AppException;
import com.app.todos.web.handler.exception.ForbiddenException;
import com.app.todos.resources.repository.Todos.TodosRepo;
import com.app.todos.resources.repository.User.UserRepo;
import com.app.todos.application.service.auth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class TodosService {

    @Autowired
    private TodosRepo todosRepo;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepo userRepo;

    public Todo get(BigInteger id, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Todo not found"));
        User u = userRepo
            .findById(t.getUser_id())
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        return t;
    }

    private TodosPageResponseDto searchByTitle(
            BigInteger id,
            String query,
            int page,
            int size
    ) {
        Page<Todo> todoPage = todosRepo.searchByTitle(query, PageRequest.of(page, size));
        return new TodosPageResponseDto(
                todoPage.getPageable().getPageNumber(),
                size,
                todoPage.getTotalPages(),
                todoPage.toList()
        );
    }

    public TodosPageResponseDto getAll(
            BigInteger user_id,
            String token,
            String query,
            int page,
            int size
    ) {
        User u = userRepo
            .findById(user_id)
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("");
        if (query != null) {
            return this.searchByTitle(user_id, query, page, size);
        }
        Page<Todo> todoPage = todosRepo.getUserTodos(user_id, PageRequest.of(page, size));
        return new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            size,
            todoPage.getTotalPages(),
            todoPage.toList()
        );
    }

    public TodosPageResponseDto getDone(BigInteger id, String token, int page, int size) {
        User u = userRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        Page<Todo> todoPage = todosRepo.getDone(id, PageRequest.of(page, size));
        return new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            size,
            todoPage.getTotalPages(),
            todoPage.toList()
        );
    }

    public TodosPageResponseDto getProgress(BigInteger id, String token, int page, int size) {
        User u = userRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        Page<Todo> todoPage = todosRepo.getInProgress(id, PageRequest.of(page, size));
        return new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            size,
            todoPage.getTotalPages(),
            todoPage.toList()
        );
    }

    public TodosPageResponseDto getPending(BigInteger id, String token, int page, int size) {
        User u = userRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        Page<Todo> todoPage = todosRepo.getPending(id, PageRequest.of(page, size));
        return new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            size,
            todoPage.getTotalPages(),
            todoPage.toList()
        );
    }

    public Todo newTodo(TodosRequestDTO data) {
        Todo t = new Todo(
            data.user_id(),
            data.client(),
            data.title(),
            data.description(),
            data.link(),
            data.due(),
            data.priority()
        );
        todosRepo.save(t);
        return t;
    }

    public Todo update(BigInteger id, TodosUpdateDTO data, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Todo not found"));
        User u = userRepo
            .findById(t.getUser_id())
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        t.setTitle(data.title());
        t.setClient(data.client());
        t.setDescription(data.description());
        t.setLink(data.link());
        t.setDue(data.due());
        t.setPriority(data.priority());
        todosRepo.save(t);
        return t;
    }

    public Todo updateStatus(BigInteger id, TodosStatusDTO data, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        User u = userRepo
            .findById(t.getUser_id())
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        t.setStatus(data.status());
        todosRepo.save(t);
        return t;
    }

    public Todo delete(BigInteger id, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        User u = userRepo
            .findById(t.getUser_id())
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
        todosRepo.deleteById(id);
        return t;
    }
}
