package modelo;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro implements CampoObservador{
    private int linhas;
    private int colunas;
    private int minas;

    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.linhas = linhas;
        this.colunas = colunas;
        this.minas = minas;
        
        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void abrir(int linha, int coluna){
        try{
            campos.parallelStream()
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(c -> c.abrir());
        } catch (Exception e){
            campos.forEach(c -> c.setAberto(true));
            throw e;
        }
    }

    public void alternarMarcacao(int linha, int coluna){
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.alternarMarcacao());
    }

    private void associarVizinhos() {
        for(Campo c1 : campos){
            for(Campo c2 : campos){
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void gerarCampos() {
        for(int i = 0 ; i < linhas; i++){
            for(int j = 0 ; j < colunas ; j++){
                Campo campo = new Campo(i, j);
                campo.registrarObservador(this);
                campos.add(new Campo(i, j));
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0;

        do{
            int random = (int) (Math.random() * campos.size());
            campos.get(random).minar();
            minasArmadas = campos.stream().filter(c -> c.isMinado()).count();
        }while(minasArmadas < minas);
    }

    public boolean objetivoAlcancado(){
        return campos.stream().allMatch(c -> c.objetivoAlcancado());
    }

    public void reiniciar() {
        campos.stream().forEach(c -> c.reiniciar());
        sortearMinas();
    }

    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento){
        if(evento == CampoEvento.EXPLODIR){
            System.out.println("perdeu");
        }else if(objetivoAlcancado()){
            System.out.println("ganhou");
        }
    }

}
