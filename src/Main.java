import javax.swing.JFrame;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        int rowCount = 21;
        int columCount = 19;
        int tileSize = 32;
        int boardWidth = columCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man"); //titulo
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //centralizar
        frame.setResizable(false); //impedir que a jenala se expanda
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fechar o jogo

        Pacman pacmanGame = new Pacman();
        frame.add(pacmanGame);
        frame.pack();
        pacmanGame.requestFocus();
        frame.setVisible(true);
    }
}