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
import java.time.LocalDate;

@Service
public class TodosService {

    @Autowired
    private TodosRepo todosRepo;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepo userRepo;

    private void validateUserToken(BigInteger user_id, String token) {
        User u = userRepo
            .findById(user_id)
            .orElseThrow(() -> new AppException("User not found"));
        if (!tokenService.validate(token).equals(u.getEmail()))
            throw new ForbiddenException("Invalid token");
    }

    public Todo get(BigInteger id, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Todo not found"));
        this.validateUserToken(t.getUser_id(), token);
        return t;
    }

    public TodosPageResponseDto getAll(
            BigInteger user_id,
            String token,
            String query,
            String status,
            int page,
            int size
    ) {
        this.validateUserToken(user_id, token);
        Page<Todo> todoPage = todosRepo.getUserTodos(
            user_id,
            query,
            status != null && !status.isEmpty() ? Integer.parseInt(status) : null,
            PageRequest.of(page, size)
        );
        return this.mapToTodosPageResponseDto(todoPage);
    }

    public TodosPageResponseDto getDone(BigInteger user_id, String token, int page, int size) {
        this.validateUserToken(user_id, token);
        Page<Todo> todoPage = todosRepo.getDone(user_id, PageRequest.of(page, size));
        return this.mapToTodosPageResponseDto(todoPage);
    }

    public TodosPageResponseDto getProgress(BigInteger user_id, String token, int page, int size) {
        this.validateUserToken(user_id, token);
        Page<Todo> todoPage = todosRepo.getInProgress(user_id, PageRequest.of(page, size));
        return this.mapToTodosPageResponseDto(todoPage);
    }

    public TodosPageResponseDto getPending(BigInteger user_id, String token, int page, int size) {
        this.validateUserToken(user_id, token);
        Page<Todo> todoPage = todosRepo.getPending(user_id, PageRequest.of(page, size));
        return this.mapToTodosPageResponseDto(todoPage);
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
        this.validateUserToken(t.getUser_id(), token);
        t.setTitle(data.title());
        t.setClient(data.client());
        t.setDescription(data.description());
        t.setLink(data.link());
        t.setDue(data.due());
        t.setPriority(data.priority());
        t.setLast_updated(LocalDate.now());
        todosRepo.save(t);
        return t;
    }

    public Todo updateStatus(BigInteger id, TodosStatusDTO data, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Todo not found"));
        this.validateUserToken(t.getUser_id(), token);
        t.setStatus(data.status());
        t.setLast_updated(LocalDate.now());
        todosRepo.save(t);
        return t;
    }

    public Todo delete(BigInteger id, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        this.validateUserToken(t.getUser_id(), token);
        todosRepo.deleteById(id);
        return t;
    }

    private TodosPageResponseDto mapToTodosPageResponseDto(Page<Todo> todoPage) {
        return new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todoPage.toList()
        );
    }
}
