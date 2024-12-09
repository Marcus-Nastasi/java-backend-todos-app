package com.app.todos.application.usecases.metrics;

import com.app.todos.application.gateway.todos.TodosGateway;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.domain.metrics.MetricsResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

public class MetricUseCase {
    private final TodosGateway todosGateway;
    private final UserUseCase userUseCase;

    public MetricUseCase(TodosGateway todosGateway, UserUseCase userUseCase) {
        this.todosGateway = todosGateway;
        this.userUseCase = userUseCase;
    }

    /*
        GRAPH DONE 1. Tarefas por Prioridade:
        Quantidade de tarefas categorizadas por prioridade (Alta, Média, Baixa).

        DONE 2. Tarefas por Data de Criação ou Conclusão:
        Distribuição das tarefas criadas ou finalizadas ao longo do tempo.
        Gráfico sugerido: Gráfico de linha ou gráfico de área para mostrar o volume de tarefas ao longo de dias,
        semanas ou meses.

        GRAPH DONE 4. Tarefas Atrasadas:
        Quantidade de tarefas cuja data de conclusão ultrapassou o prazo (overdue).
        Gráfico sugerido: Gráfico de barras comparando tarefas no prazo e tarefas atrasadas.

        DONE 5. Tarefas por Usuário:
        Se houver vários usuários, seria interessante mostrar a quantidade de tarefas criadas, concluídas ou
        pendentes por usuário.
        Gráfico sugerido: Gráfico de barras para comparar o desempenho entre usuários.

        DONE 6. Taxa de Conclusão:
        Percentual de tarefas concluídas em relação ao total de tarefas.
        Gráfico sugerido: Gráfico de pizza ou gráfico de progresso.

        DONE 7. Tarefas por Cliente:
        Se os "To-dos" estão associados a diferentes clientes, você pode apresentar uma distribuição de tarefas por cliente.
        Gráfico sugerido: Gráfico de barras para visualização comparativa.

        DONE 10. Projeção de Tarefas Futuras:
        Número de tarefas cuja data de vencimento está no futuro (tarefas programadas).
        Gráfico sugerido: Gráfico de barras mostrando a quantidade de tarefas programadas para os próximos dias ou semanas.

        3. Tempo Médio de Conclusão:
        Tempo médio que as tarefas levam para serem concluídas, desde a criação até o estado "Concluído".
        Gráfico sugerido: Gráfico de barras com o tempo médio por tarefa.

        ? 8. Tarefas por Status ao Longo do Tempo:
        Evolução do status das tarefas ao longo do tempo (quantas tarefas foram concluídas, quantas ficaram pendentes, etc.).
        Gráfico sugerido: Gráfico de linha com diferentes linhas para cada status.
     */

    public MetricsResponse get(
            BigInteger user_id,
            String token,
            String client,
            LocalDate from,
            LocalDate to
    ) {
        userUseCase.validateUserToken(user_id, token);
        Map<String, BigDecimal> metricsMap = todosGateway.getMetrics(
            user_id,
            client,
            from != null ? from : LocalDate.of(1900, 1, 1),
            to != null ? to : LocalDate.now()
        );
        return new MetricsResponse(
            Long.parseLong(String.valueOf(metricsMap.get("total"))),
            Long.parseLong(String.valueOf(metricsMap.get("high"))),
            Long.parseLong(String.valueOf(metricsMap.get("medium"))),
            Long.parseLong(String.valueOf(metricsMap.get("low"))),
            Long.parseLong(String.valueOf(metricsMap.get("pending"))),
            Long.parseLong(String.valueOf(metricsMap.get("inprogress"))),
            Long.parseLong(String.valueOf(metricsMap.get("done"))),
            Long.parseLong(String.valueOf(metricsMap.get("overdue"))),
            Long.parseLong(String.valueOf(metricsMap.get("future"))),
            metricsMap.get("completion_rate")
        );
    }
}
