/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author alison barreiro
 */
public class Processo implements Comparable<Processo> {

    private int procID;
    private int tempodeChegada;
    private int duracaodeProcesso;
    private int decrementadorTempo;

    public Processo(int procID, int tempodeChegada, int duracaodeProcesso) {
        this.procID = procID;
        this.tempodeChegada = tempodeChegada;
        this.duracaodeProcesso = duracaodeProcesso;
        this.decrementadorTempo = duracaodeProcesso;
    }

    public int getProcID() {
        return procID;
    }

    public void setProcID(int procID) {
        this.procID = procID;
    }

    public int getTempodeChegada() {
        return tempodeChegada;
    }

    public void setTempodeChegada(int tempodeChegada) {
        this.tempodeChegada = tempodeChegada;
    }

    public int getDuracaodeProcesso() {
        return duracaodeProcesso;
    }

    public void setDuracaodeProcesso(int duracaodeProcesso) {
        this.duracaodeProcesso = duracaodeProcesso;
    }

    public int getDecrementadorTempo() {
        return decrementadorTempo;
    }

    public void setDecrementadorTempo(int decrementadorTempo) {
        this.decrementadorTempo = decrementadorTempo;
    }


    /**
     * Ordena a lista de acordo com a duracao e tempo de chegada
     *
     * @param Processo
     */
    public int compareTo(Processo p) {
        if ((tempodeChegada < p.getTempodeChegada() || tempodeChegada == p.getTempodeChegada())
                && (getDuracaodeProcesso() < p.getDuracaodeProcesso())) {
            return -1;
        } else if ((tempodeChegada > p.getTempodeChegada() || (tempodeChegada == p.getTempodeChegada())
                && getDuracaodeProcesso() > p.getDuracaodeProcesso())) {
            return 1;
        } else {
            return 0;
        }
    }

}
