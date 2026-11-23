import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TeclaSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Primeiro botão");
            JLabel visor = new JLabel("_");
            JButton tecla = new JButton("7");

            // O código entre chaves só roda quando alguém clica na tecla.
            tecla.addActionListener(evento -> {
                visor.setText("7");
            });

            JPanel painel = new JPanel();
            painel.add(visor);
            painel.add(tecla);
            janela.setContentPane(painel);
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setSize(260, 120);
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }
}
