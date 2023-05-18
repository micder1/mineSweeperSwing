package visao;

import modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class PainelTabuleiro extends JPanel {

    public PainelTabuleiro(Tabuleiro tab) {
        setLayout(new GridLayout(tab.getLinhas(), tab.getColunas()));

        tab.forEachCampo(c -> add(new BotaoCampo(c)));

        tab.registrarObservador(e -> {
            SwingUtilities.invokeLater(() -> {
//                if(e.isGanhou()){
//                    JOptionPane.showMessageDialog(this, "Voce ganhou!");
//                } else {
//                    JOptionPane.showMessageDialog(this, "Voce perdeu");
//                }
                JOptionPane.showMessageDialog(this, e.isGanhou() ? "Voce ganhou!" : "Voce perdeu");

                tab.reiniciar();
            });
        });
    }
}
