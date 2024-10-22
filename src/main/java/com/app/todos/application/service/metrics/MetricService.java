package com.app.todos.application.service.metrics;

import com.app.todos.application.service.auth.TokenService;
import com.app.todos.application.service.todos.TodosService;
import com.app.todos.application.service.users.UserService;
import com.app.todos.domain.metrics.dtos.MetricsQntByPriorDto;
import com.app.todos.domain.metrics.dtos.MetricsQntByStatResponseDto;
import com.app.todos.domain.todos.Todo;
import com.app.todos.resources.repository.Todos.TodosRepo;
import com.app.todos.resources.repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MetricService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private TodosRepo todosRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private TodosService todosService;
    @Autowired
    private TokenService tokenService;

    /*
        DONE 1. Tarefas por Prioridade:
        Quantidade de tarefas categorizadas por prioridade (Alta, Média, Baixa).

        2. Tarefas por Data de Criação ou Conclusão:
        Distribuição das tarefas criadas ou finalizadas ao longo do tempo.
        Gráfico sugerido: Gráfico de linha ou gráfico de área para mostrar o volume de tarefas ao longo de dias,
        semanas ou meses.

        3. Tempo Médio de Conclusão:
        Tempo médio que as tarefas levam para serem concluídas, desde a criação até o estado "Concluído".
        Gráfico sugerido: Gráfico de barras com o tempo médio por tarefa.

        4. Tarefas Atrasadas:
        Quantidade de tarefas cuja data de conclusão ultrapassou o prazo (overdue).
        Gráfico sugerido: Gráfico de barras comparando tarefas no prazo e tarefas atrasadas.

        DONE 5. Tarefas por Usuário:
        Se houver vários usuários, seria interessante mostrar a quantidade de tarefas criadas, concluídas ou
        pendentes por usuário.
        Gráfico sugerido: Gráfico de barras para comparar o desempenho entre usuários.

        6. Taxa de Conclusão:
        Percentual de tarefas concluídas em relação ao total de tarefas.
        Gráfico sugerido: Gráfico de pizza ou gráfico de progresso.

        7. Tarefas por Cliente:
        Se os "To-dos" estão associados a diferentes clientes, você pode apresentar uma distribuição de tarefas por cliente.
        Gráfico sugerido: Gráfico de barras para visualização comparativa.

        8. Tarefas por Status ao Longo do Tempo:
        Evolução do status das tarefas ao longo do tempo (quantas tarefas foram concluídas, quantas ficaram pendentes, etc.).
        Gráfico sugerido: Gráfico de linha com diferentes linhas para cada status.

        9. Tarefas Canceladas:
        Se houver um status de "cancelado", seria relevante mostrar o número de tarefas canceladas e o motivo, se aplicável.
        Gráfico sugerido: Gráfico de barras ou pizza.

        10. Projeção de Tarefas Futuras:
        Número de tarefas cuja data de vencimento está no futuro (tarefas programadas).
        Gráfico sugerido: Gráfico de barras mostrando a quantidade de tarefas programadas para os próximos dias ou semanas.
     */

    public MetricsQntByStatResponseDto getTodosByUser(
            BigInteger user_id,
            String token,
            LocalDate from,
            LocalDate to
    ) {
        userService.validateUserToken(user_id, token);
        if (from == null) from = LocalDate.of(1900, 1, 1);
        if (to == null) to = LocalDate.now();
        Map<String, Long> longMap = todosRepo
            .findQuantityByStatus(user_id, from, to);
        return new MetricsQntByStatResponseDto(
            longMap.get("pending"),
            longMap.get("inProgress"),
            longMap.get("done"),
            longMap.get("total")
        );
    }

    public MetricsQntByPriorDto getTodosQuantityByPriority(BigInteger user_id, String token) {
        userService.validateUserToken(user_id, token);
        Map<String, Long> stringMap = todosRepo.findQuantityByPriority(user_id);
        return new MetricsQntByPriorDto(
            stringMap.get("high"),
            stringMap.get("medium"),
            stringMap.get("low")
        );
    }

    public List<Todo> get(
            BigInteger user_id,
            String token,
            String query,
            // status: 0 = pending, 1 = in progress, 2 = done
            String status,
            LocalDate from,
            LocalDate to
    ) {
        return todosRepo.massiveQuery(
            user_id,
            query,
            status != null && !status.isEmpty() ? Integer.parseInt(status) : null,
            from != null ? from : LocalDate.of(1900, 1, 1),
            to != null ? to : LocalDate.now()
        );
    }
}
