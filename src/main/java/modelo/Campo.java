package modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
    private final int linha;
    private final int coluna;

    private boolean aberto = false;
    private boolean minado = false;
    private boolean marcado = false;

    private List<Campo> vizinhos = new ArrayList<>();
    private List<CampoObservador> observadores = new ArrayList<>();

    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public void registrarObservador(CampoObservador observador){
        observadores.add(observador);
    }

    private void notificarObservadores(CampoEvento evento){
        observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
    }

    /**
     * Esse metodo recebe como parametro um objeto Campo para ser adicionado
     * na lista vizinhos, caso o objeto passado seja vizinho
     * do objeto no qual o método foi invocado, o método retorna true e adiciona
     * o Campo vizinho na lista vizinhos, caso o objeto nao seja vizinho o método
     * retorna false e o objeto passado nao é adicionado na lista vizinhos.
     * @param vizinho
     * @return boolean
     */
    boolean adicionarVizinho(Campo vizinho){
        boolean linhaDif = linha != vizinho.linha;
        boolean colunaDif = coluna != vizinho.coluna;
        boolean diagonal = linhaDif && colunaDif;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        if(deltaGeral == 1 && !diagonal){
            vizinhos.add(vizinho);
            return true;
        }else if (deltaGeral == 2 && diagonal){
            vizinhos.add(vizinho);
            return true;
        }else{
            return false;
        }
    }

    public void alternarMarcacao(){
        if(!aberto) {
            marcado = !marcado;

//            if(marcado) {
//                notificarObservadores(CampoEvento.MARCAR);
//            } else{
//                notificarObservadores(CampoEvento.DESMARCAR);
//            }

            notificarObservadores(marcado ? CampoEvento.MARCAR : CampoEvento.DESMARCAR);
        }
    }

    public boolean abrir(){
        if(!aberto && !marcado){
            if(minado){
                notificarObservadores(CampoEvento.EXPLODIR);
                return true;
            }

            setAberto(true);

            if(vizinhancaSegura()){
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean vizinhancaSegura(){
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    void minar(){
            minado = true;
    }

    public boolean isMarcado(){
        return marcado;
    }

    public boolean isAberto(){
        return aberto;
    }

    void setAberto(boolean aberto){
        this.aberto = aberto;

        if (aberto) {
            notificarObservadores(CampoEvento.ABRIR);
        }
    }
    public boolean isMinado(){ return minado; }

    boolean isFechado(){
        return !isAberto();
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    boolean objetivoAlcancado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    public int minasNaVizinhanca(){
        return (int) vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;

        notificarObservadores(CampoEvento.REINICIAR);
    }

//    public String toString(){
//        if(marcado) {
//            return "X";
//        } else if(aberto && minado){
//            return "*";
//        } else if(aberto && minasNaVizinhanca() > 0){
//            return Long.toString(minasNaVizinhanca());
//        } else if(aberto) {
//            return " ";
//        } else {
//            return "?";
//        }
//    }

}
