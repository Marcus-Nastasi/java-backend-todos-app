package com.app.todos.application.service.todos;

import com.app.todos.application.service.users.UserService;
import com.app.todos.domain.todos.DTOs.TodosPageResponseDto;
import com.app.todos.domain.todos.DTOs.TodosRequestDTO;
import com.app.todos.domain.todos.DTOs.TodosStatusDTO;
import com.app.todos.domain.todos.DTOs.TodosUpdateDTO;
import com.app.todos.domain.todos.Todo;
import com.app.todos.web.handler.exception.AppException;
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
    private UserService userService;

    public TodosPageResponseDto getAll(
            BigInteger user_id,
            String token,
            String query,
            String client,
            // status: 0 = pending, 1 = in progress, 2 = done
            String status,
            // priority: 0 = low, 1 = medium, 2 = high
            String priority,
            LocalDate from,
            LocalDate to,
            LocalDate due,
            int page,
            int size
    ) {
        userService.validateUserToken(user_id, token);
        Page<Todo> todoPage = todosRepo.getUserTodos(
            user_id,
            query,
            client,
            status != null && !status.isEmpty() ? Integer.parseInt(status) : null,
            priority != null && !priority.isEmpty() ? Integer.parseInt(priority) : null,
            from != null ? from : LocalDate.of(1900, 1, 1),
            to != null ? to : LocalDate.now(),
            due != null ? due : LocalDate.of(2500, 1, 1),
            PageRequest.of(page, size)
        );
        return this.mapToTodosPageResponseDto(todoPage);
    }

    public Todo get(BigInteger id, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Todo not found"));
        userService.validateUserToken(t.getUser_id(), token);
        return t;
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
        userService.validateUserToken(t.getUser_id(), token);
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
        userService.validateUserToken(t.getUser_id(), token);
        t.setStatus(data.status());
        t.setLast_updated(LocalDate.now());
        todosRepo.save(t);
        return t;
    }

    public Todo delete(BigInteger id, String token) {
        Todo t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        userService.validateUserToken(t.getUser_id(), token);
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
