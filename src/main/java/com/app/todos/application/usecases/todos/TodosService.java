package com.app.todos.application.usecases.todos;

import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.adapters.output.todos.TodosPageResponseDto;
import com.app.todos.adapters.input.todos.TodosRequestDTO;
import com.app.todos.adapters.input.todos.TodosStatusDTO;
import com.app.todos.adapters.input.todos.TodosUpdateDTO;
import com.app.todos.infrastructure.entity.todos.TodoEntity;
import com.app.todos.application.exception.AppException;
import com.app.todos.infrastructure.persistence.todos.TodosRepo;
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
    private UserUseCase userService;

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
        Page<TodoEntity> todoPage = todosRepo.getUserTodos(
            user_id,
            query,
            client,
            status != null && !status.isEmpty() ? Integer.parseInt(status) : null,
            priority != null && !priority.isEmpty() ? Integer.parseInt(priority) : null,
            from != null ? from : LocalDate.of(1000, 1, 1),
            to != null ? to : LocalDate.of(2500, 1, 1),
            due != null ? due : LocalDate.of(2500, 1, 1),
            PageRequest.of(page, size)
        );
        return this.mapToTodosPageResponseDto(todoPage);
    }

    public TodoEntity get(BigInteger id, String token) {
        TodoEntity t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Todo not found"));
        userService.validateUserToken(t.getUser_id(), token);
        return t;
    }

    public TodoEntity newTodo(TodosRequestDTO data) {
        TodoEntity t = new TodoEntity(
            data.user_id(),
            data.client(),
            data.title(),
            data.description(),
            data.link(),
            LocalDate.now(),
            data.due(),
            data.priority(),
            LocalDate.now()
        );
        todosRepo.save(t);
        return t;
    }

    public TodoEntity update(BigInteger id, TodosUpdateDTO data, String token) {
        TodoEntity t = todosRepo
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

    public TodoEntity updateStatus(BigInteger id, TodosStatusDTO data, String token) {
        TodoEntity t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("Todo not found"));
        userService.validateUserToken(t.getUser_id(), token);
        t.setStatus(data.status());
        t.setLast_updated(LocalDate.now());
        todosRepo.save(t);
        return t;
    }

    public TodoEntity delete(BigInteger id, String token) {
        TodoEntity t = todosRepo
            .findById(id)
            .orElseThrow(() -> new AppException("User not found"));
        userService.validateUserToken(t.getUser_id(), token);
        todosRepo.deleteById(id);
        return t;
    }

    private TodosPageResponseDto mapToTodosPageResponseDto(Page<TodoEntity> todoPage) {
        return new TodosPageResponseDto(
            todoPage.getPageable().getPageNumber(),
            todoPage.getPageable().getPageSize(),
            todoPage.getTotalPages(),
            todoPage.toList()
        );
    }
}
