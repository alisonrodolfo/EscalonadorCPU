/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author alison barreiro
 */
public class Main {

    public static List<Processo> proc_fila = new ArrayList<Processo>();

    public static void main(String[] args) {

        try {

            Main proj = new Main();
            proj.run(args);

        } catch (Exception e) {
            System.out.println("=/: " + e);
        }

    }

    public void run(String[] args) throws Exception {
        try {
            URL url = getClass().getResource("entrada.proj");
            int file_size = countLines(url.getPath()); //Conta o numero de linhas do arquivo
            //System.out.println("Numero de Linhas: " + file_size + "\n\n");
            try (FileReader file = new FileReader(url.getPath())) //Localizacao do arquivo
            {
                BufferedReader arq = new BufferedReader(file);

                CPU cpu = new CPU(proc_fila);
                /**
                 * Carrega o arquivo na CPU*
                 */
                memory_fetch(arq, file_size);

                /**
                 * CPU executa o programa*
                 */
                cpu.run();
                arq.close();
            }
        } catch (FileNotFoundException fnf) {
            System.out.println("Arquivo nao encontrado");
        }
    }

    public static int countLines(String filename) throws IOException {
        LineNumberReader reader = new LineNumberReader(new FileReader(filename));
        while (reader.readLine() != null);
        reader.close();
        return reader.getLineNumber();
    }

    public static void memory_fetch(BufferedReader br, int file_size) throws IOException {
        int i = 0;
        String line;

        while (i < file_size && br.ready()) {
            line = br.readLine();
            if (line.equals("")) {
                continue;
            }
            String dados_in[] = line.split(" ");
            proc_fila.add(new Processo(i, Integer.parseInt(dados_in[0]), Integer.parseInt(dados_in[1])));
            i++;
        }
    }

}
