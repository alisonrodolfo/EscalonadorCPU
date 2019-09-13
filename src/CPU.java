/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alison barreiro
 */
public class CPU {

    private final List<Processo> proc_fila;

    public CPU(List<Processo> mem_inst) {
        this.proc_fila = mem_inst;

    }

    public void run() {

        FCFS fcfs = new FCFS(proc_fila);
        SJF sjf = new SJF(proc_fila);
        RR rr = new RR(proc_fila);

    }

    /* Varre todos os processos para procurar o menor tempo */
    public int menorTempodeChegada(List<Processo> processos) {
        int menor = 99999999;
        for (Processo proc : processos) {
            if (proc.getTempodeChegada() < menor) {
                menor = proc.getTempodeChegada();
            }

        }
        return menor;
    }

    public class FCFS {

        private List<Processo> exProcessos;

        public FCFS(List<Processo> processos) {
            try {
                exProcessos = new ArrayList<>(processos);
                double tempoRetorno = 0, tempoResposta = 0, tempoEspera = 0;
                int totalProcessos = processos.size();
                // Recebe o menor tempo de chagada 
                int retorno = menorTempodeChegada(processos);

                // enquanto existir processos na fila de prontos
                while (!exProcessos.isEmpty()) {
                    //   Tempo transcorrido desde que se lança um processo
                    //    (entra na fila de prontos) até que finalize sua execução.
                    //
                    Processo proc = exProcessos.remove(0); // Recebe o primeiro processo e remove ele da fila.
                    retorno += proc.getDuracaodeProcesso(); // retorno recebe ele mais a duração do processo.
                    tempoRetorno += (retorno - proc.getTempodeChegada()); // Formula do tempo de rotorno  - tempo de chegada.
                    tempoEspera += (retorno - proc.getTempodeChegada() - proc.getDuracaodeProcesso()); // Formula definida: TEP = (REP - CHP - TAM);
                    tempoResposta += (retorno - proc.getTempodeChegada() - proc.getDuracaodeProcesso()); // Formula definida: TEP = (REP - CHP - TAM);
                }

                System.out.println(format("FCFS: %.1f %.1f %.1f ", tempoRetorno / totalProcessos, tempoResposta / totalProcessos, tempoEspera / totalProcessos));

            } catch (Exception e) {
            }
        }

    }

    /*
    
    MESMO CÓDIDO DO *FCFS* APENAS EXECUTA O QUICK SORTE ANTES PARA ORDENAR OS PROCESSOS
    
    
     */
    public class SJF {

        private List<Processo> exProcessos = new ArrayList<Processo>();

        public SJF(List<Processo> processos) {
            try {
                double tempoRetorno = 0, tempoResposta = 0, tempoEspera = 0;
                int totalProcessos = processos.size();
                int retorno = menorTempodeChegada(processos);

                quicksort(processos);

                while (!exProcessos.isEmpty()) {

                    //   Tempo transcorrido desde que se lança um processo
                    //    (entra na fila de prontos) até que finalize sua execução.
                    //
                    Processo proc = exProcessos.remove(0); // Recebe o primeiro processo e remove ele da fila.
                    retorno += proc.getDuracaodeProcesso(); // retorno recebe ele mais a duração do processo.
                    tempoRetorno += (retorno - proc.getTempodeChegada()); // Formula do tempo de rotorno  - tempo de chegada.
                    tempoEspera += (retorno - proc.getTempodeChegada() - proc.getDuracaodeProcesso()); // Formula definida: TEP = (REP - CHP - TAM);
                    tempoResposta += (retorno - proc.getTempodeChegada() - proc.getDuracaodeProcesso()); // Formula definida: TEP = (REP - CHP - TAM);
                }

                System.out.println(format("SJF: %.1f %.1f %.1f ", tempoRetorno / totalProcessos, tempoResposta / totalProcessos, tempoEspera / totalProcessos));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        private void quicksort(List<Processo> processos) {
            List<Processo> proc = new ArrayList<>(processos);
            int auxRetorno = 0, menor = 0, pivo = 0;
            // Ordena a lista para garantir a hierarquia de chegada
            Collections.sort(proc);
            exProcessos.add(proc.remove(0));
            auxRetorno = exProcessos.get(0).getDuracaodeProcesso();

            while (proc.size() > 1) {
                pivo = 0;
                for (int i = 1; i < proc.size(); i++) {
                    if (proc.get(pivo).getDuracaodeProcesso() <= proc.get(i).getDuracaodeProcesso() && proc.get(pivo).getTempodeChegada() < auxRetorno) {
                        menor = pivo;
                    } else if (proc.get(i).getTempodeChegada() < auxRetorno) {
                        pivo = i;
                        menor = i;
                    }
                }
                auxRetorno += proc.get(menor).getDuracaodeProcesso();
                exProcessos.add(proc.remove(menor));
            }
            // add o ultimo processo a lista, na ultima posicao
            if (proc.size() == 1) {
                exProcessos.add(proc.remove(0));
            }
        }

    }

    public class RR {

        private final int quantum = 2;

        // Fila dos processos
        private List<Processo> exProcessos = new ArrayList<>();
        
        // Lista de INTEGER dos temposdeChegada
        private List<Integer> temposChegada = new ArrayList<>();

        //  Map do ID do processo e tempodeResposta
        // Usado para somar apenas uma única vez cada tempo de resposa
        private Map<Integer, Integer> temposResposta = new HashMap<>();

        public RR(List<Processo> processos) {
            double tempoRetorno = 0, tempoResposta = 0, tempoEspera = 0;
            int totalProcessos = processos.size();
            int retorno = menorTempodeChegada(processos);
            
            // ordena os processos com mesmo tempo de Chegada.
            sortFirstCome(processos, retorno);

            while (!exProcessos.isEmpty()) {
                
                // Remove o primeiro processo da fila e adiciona no final ou não.
                Processo proc = exProcessos.remove(0);

                // intervalo entre a chegada ao sistema e inicio de sua execução;
                // Simples MAP para validar se já foi calculado o tempo de resposta do processo, para não repetir diversas vezes.
                if (!temposResposta.containsKey(proc.getProcID())) {
                    temposResposta.put(proc.getProcID(), retorno - proc.getTempodeChegada());
                }

                // Verifica se o processo é maior que o quantum de tempo de CPU
                // caso contrário, o processo é finalizado, visto que, já executou seus procedimentos.
                if (proc.getDecrementadorTempo() > quantum) {
                    proc.setDecrementadorTempo(proc.getDecrementadorTempo() - quantum);
                    
                    retorno += quantum;
                    
                    // Para garantir a Hierarquia do tempo de chagada.
                    sortFirstCome(processos, retorno);

                    // adiciona o processo no final da lista, pois não foi finalizado
                    // se  processo não terminou, ocorre uma preempção e o processo é inserido no fim da fila
                    exProcessos.add(proc);
                    
                    
                } else {
                    // Quando o processo é menor que quantum, retorno recebe o tempo restante e finaliza.
                    retorno += proc.getDecrementadorTempo();
                }

 
                //tempo transcorrido desde o momento em que o software entra e o instante em que termina sua execução
                if (!exProcessos.contains(proc)) {
                    // Quando o processo é removido da fila de execução
                    // tempoRetorno recebe a soma do tempo de retorno menos o tempo de chegado do processo.
                    tempoRetorno += retorno - proc.getTempodeChegada();
                    
                    
                    //soma dos períodos em que o processo estava no seu estado pronto.
                    
                    tempoEspera += (retorno - proc.getTempodeChegada() - proc.getDuracaodeProcesso()); // TEP = (REP - CHP - TAM)
                }
            }

            
            for (int relay : temposResposta.keySet()) {
                tempoResposta += temposResposta.get(relay);
            }

            System.out.println(format("RR: %.1f %.1f %.1f ", tempoRetorno / totalProcessos, tempoResposta / totalProcessos, tempoEspera / totalProcessos));

        }

        // Organiza de acordo com o tempo de chegada
        private void sortFirstCome(List<Processo> processos, int retorno) {
            
            int menor;

            for (Processo process : processos) {
                if (!temposChegada.contains(process.getTempodeChegada()) && process.getTempodeChegada() <= retorno) {
                    if (!exProcessos.contains(process)) {
                        
                        menor = process.getTempodeChegada();
                        temposChegada.add(menor);
                        for (Processo processo : processos) {
                            if (processo.getTempodeChegada() == menor) {
                                exProcessos.add(processo);
                            }
                        }
                   }
                }
            }
        }

    }

}
