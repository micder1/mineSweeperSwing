package visao;

import modelo.Campo;
import modelo.CampoEvento;
import modelo.CampoObservador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

    private Campo campo;

    private final Color BG_PADRAO = new Color(184, 184, 184);
    private final Color BG_ABERTO = new Color(160, 160, 160);
    private final Color BG_MARCADO = new Color(8, 179, 247);
    private final Color BG_EXPLODIR = new Color(189, 66, 68);
    private final Color TXT_VERDE = new Color(0, 100, 0);

    public BotaoCampo(Campo campo){
        this.campo = campo;
        setBorder(BorderFactory.createBevelBorder(0));

        setBackground(BG_PADRAO);
        setOpaque(true);

        addMouseListener(this);

        campo.registrarObservador(this);
    }

    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        switch (evento){
            case ABRIR:
                aplicarEstiloAbrir();
            break;
            case MARCAR:
                aplicarEstiloMarcar();
            break;
            case EXPLODIR:
                aplicarEstiloExplodir();
            break;
            default:
                aplicarEstiloPadrao();
        }

        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    private void aplicarEstiloPadrao() {
        setBackground(BG_PADRAO);
        setBorder(BorderFactory.createBevelBorder(0));
        setText("");
    }

    private void aplicarEstiloExplodir() {
        setBackground(BG_EXPLODIR);
        setForeground(Color.white);
        setText("X");
    }

    private void aplicarEstiloMarcar() {
        setBackground(BG_MARCADO);
        setForeground(Color.black);
        setText("o");
    }

    private void aplicarEstiloAbrir() {

        if(campo.isMinado()){
            setBackground(BG_EXPLODIR);
            return;
        }

        setBackground(BG_ABERTO);
        setBorder(BorderFactory.createLineBorder(Color.gray));

        switch (campo.minasNaVizinhanca()){
            case 1:
                setForeground(TXT_VERDE);
                break;
            case 2:
                setForeground(Color.blue);
                break;
            case 3:
                setForeground(Color.yellow);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.red);
                break;
            default:
                setForeground(Color.pink);
        }

        String val = !campo.vizinhancaSegura() ? campo.minasNaVizinhanca() + "" : "";
        setText(val);
    }

    //eventos mouse
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == 1){
            campo.abrir();
        }else {
            campo.alternarMarcacao();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
