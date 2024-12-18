import java.util.*;

public class Escalonador {

    public static void executarRoundRobin(List<Processo> filaProntos, int quantum) {
        System.out.println("Executando Round-Robin...");
        int tempoAtual = 0;
        List<Processo> processosConcluidos = new ArrayList<>();
    
        while (!filaProntos.isEmpty()) {
            Iterator<Processo> iterator = filaProntos.iterator();
            while (iterator.hasNext()) {
                Processo processo = iterator.next();
                int tempoExecucao = Math.min(processo.tempoRestante, quantum);
    
                System.out.printf("Tempo %d: Executando processo %s por %d ms\n", tempoAtual, processo.nome, tempoExecucao);
                processo.tempoRestante -= tempoExecucao;
                tempoAtual += tempoExecucao;
    
                for (Processo p : filaProntos) {
                    if (p != processo) {
                        p.tempoEspera += tempoExecucao;
                    }
                }
    
                if (processo.tempoRestante <= 0) {
                    processo.tempoTurnaround = tempoAtual;
                    System.out.printf("Processo %s concluído! Turnaround: %d ms, Espera: %d ms\n",
                            processo.nome, processo.tempoTurnaround, processo.tempoEspera);
                    iterator.remove();
                    processosConcluidos.add(processo); // Adiciona o processo a lista de concluídos
                }
            }
        }
    
        exibirEstatisticas(processosConcluidos); // Passa a lista de processos concluídos
    }
    

    public static void executarPrioridade(List<Processo> filaProntos) {
        System.out.println("Executando Prioridade...");
        int tempoAtual = 0;
        List<Processo> processosConcluidos = new ArrayList<>();
    
        filaProntos.sort(Comparator.comparingInt(p -> p.prioridade)); // Ordenar por prioridade
        while (!filaProntos.isEmpty()) {
            Processo processo = filaProntos.remove(0);
            System.out.printf("Tempo %d: Executando processo %s por %d ms\n", tempoAtual, processo.nome, processo.tempoRestante);
    
            tempoAtual += processo.tempoRestante;
    
            for (Processo p : filaProntos) {
                p.tempoEspera += processo.tempoRestante;
            }
    
            processo.tempoTurnaround = tempoAtual;
            System.out.printf("Processo %s concluído! Turnaround: %d ms, Espera: %d ms\n",
                    processo.nome, processo.tempoTurnaround, processo.tempoEspera);
            processosConcluidos.add(processo); // Adiciona o processo a lista de concluídos
        }
    
        exibirEstatisticas(processosConcluidos); // Passa a lista de processos concluídos
    }
    

    private static void exibirEstatisticas(List<Processo> processosConcluidos) {
        System.out.println("\nEstatísticas finais:");
        int totalEspera = 0, totalTurnaround = 0, processos = processosConcluidos.size();
    
        for (Processo processo : processosConcluidos) {
            totalEspera += processo.tempoEspera;
            totalTurnaround += processo.tempoTurnaround;
        }
    
        if (processos > 0) {
            System.out.printf("Tempo médio de espera: %.2f ms\n", (double) totalEspera / processos);
            System.out.printf("Tempo médio de turnaround: %.2f ms\n", (double) totalTurnaround / processos);
        } else {
            System.out.println("Nenhum processo foi executado.");
        }
    }

}
    