import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Random;
import javax.swing.*;

public class Pacman extends JPanel implements ActionListener, KeyListener  {
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startX;
        int startY;
        char direcao = 'U';
        int velocidadeX = 0;
        int velocidadeY = 0;

        Block(Image image, int x, int y, int width, int height){
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        void uptadeDirection(char direcao){
            char prevDirection = this.direcao;
            this.direcao = direcao;
            atualizarVelocidade();
            this.x += this.velocidadeX;
            this.y += this.velocidadeY;
            for (Block wall : walls){
                if (colisao(this, wall)) {
                    this.x -= this.velocidadeX;
                    this.y -= this.velocidadeY;
                    this.direcao = prevDirection;
                    atualizarVelocidade();
                }
            }
        }
        void atualizarVelocidade(){
            if(this.direcao == 'U'){
                this.velocidadeX = 0;
                this.velocidadeY = -tileSize/4; //32/4 = 8 pixels
            }
            else if (this.direcao == 'D'){
                this.velocidadeX = 0;
                this.velocidadeY = tileSize/4;
            }
            else if (this.direcao == 'L'){
                this.velocidadeX = - tileSize / 4;
                this.velocidadeY = 0;
            }
            else if (this.direcao == 'R'){
                this.velocidadeX = tileSize / 4;
                this.velocidadeY = 0;
            }
        }
    }
    private int rowCount = 21;
    private int columCount = 19;
    private int tileSize = 32;
    private int boardWidth = columCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image redGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;

    private Image pacmanUpImage;
    private Image pacmanDownImage;
    private Image pacmanLeftImage;
    private Image pacmanRightImage;

    //X = parede, O = pula, P = pac man, ' ' = comida
    //fantasma: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X                 X",
            "X XX X XXXXX X XX X",
            "X    X       X    X",
            "XXXX XXXX XXXX XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXrXX X XXXX",
            "O       bpo       O",
            "XXXX X XXXXX X XXXX",
            "OOOX X       X XOOO",
            "XXXX X XXXXX X XXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X  X     P     X  X",
            "XX X X XXXXX X X XX",
            "X    X   X   X    X",
            "X XXXXXX X XXXXXX X",
            "X                 X",
            "XXXXXXXXXXXXXXXXXXX"
    };
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] direcoes = {'U', 'D', 'L', 'R'};
    Random random = new Random();

//construtor
    Pacman() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        //carregar imagens
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();

        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        mapa();
        for (Block ghost : ghosts){
            char novaDirecao = direcoes[random.nextInt(4)];
            ghost.uptadeDirection(novaDirecao);
        }
        //o tempo que leva para começar a contar o timer, milisegundos entre frames
        gameLoop = new Timer(50, this); //20 fps (1000/50)
        gameLoop.start();
    }

    public void mapa() {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for (int r = 0; r < rowCount; r++){
            for (int c = 0; c < columCount; c++){
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') {
                    Block wall = new Block(wallImage, x, y,tileSize, tileSize);
                    walls.add(wall);
                }
                else if(tileMapChar == 'b'){//fanstasma azul
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'o'){//fanstasma laranja
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'r'){//fanstasma vermelho
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'p'){//fanstasma rosa
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if(tileMapChar == 'P'){ //pacman
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }
                else if(tileMapChar == ' '){ //comida
                   Block food = new Block(null, x + 14, y + 14, 4, 4 );
                   foods.add(food);
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);//pacman

        for (Block ghost : ghosts){
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for(Block wall : walls){
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for(Block food : foods){
            g.fillRect(food.x, food.y, food.width, food.height);
        }
    }

    public void move(){
        pacman.x += pacman.velocidadeX;
        pacman.y += pacman.velocidadeY;
//fazer o pacman atravessar o mapa
        if (pacman.x < 0) pacman.x = boardWidth - tileSize;
        else if (pacman.x >= boardWidth) pacman.x = 0;
        if (pacman.y < 0) pacman.y = boardHeight - tileSize;
        else if (pacman.y >= boardHeight) pacman.y = 0;

        //checar colisao
        for(Block wall : walls){
            if (colisao(pacman, wall)){
                pacman.x -= pacman.velocidadeX;//se houver colisao. passo para tras
                pacman.y -= pacman.velocidadeY;
                break;
            }
        }

        //colisao para os fanstasmas / fazer trocar a direçao deles caso haja colisa
        for (Block ghost : ghosts){
            ghost.x += ghost.velocidadeX;
            ghost.y += ghost.velocidadeY;
        //fazer o pacman atravessar o mapa
            if (ghost.x < 0) ghost.x = boardWidth - tileSize;
            else if (ghost.x >= boardWidth) ghost.x = 0;
            if (ghost.y < 0) ghost.y = boardHeight - tileSize;
            else if (ghost.y >= boardHeight) ghost.y = 0;
            for (Block wall: walls)
            {
                if(colisao(ghost, wall)) {
                    ghost.x -= ghost.velocidadeX;
                    ghost.y -= ghost.velocidadeY;
                    char novaDirecao = direcoes[random.nextInt(4)];
                    ghost.uptadeDirection(novaDirecao);
                }
            }
        }
    }
    //colisao com a parde
    public boolean colisao(Block a, Block b){
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }


        public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        }

        public void keyTyped(KeyEvent e){
        }
        public void keyPressed(KeyEvent e){
        }
        public void keyReleased(KeyEvent e){
            System.out.println("KeyEvent: " + e.getKeyCode());
            if(e.getKeyCode() == KeyEvent.VK_UP){
                pacman.uptadeDirection('U');
            }
            else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                pacman.uptadeDirection('D');
            }
             else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                pacman.uptadeDirection('L');
            }
              else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                pacman.uptadeDirection('R');
            }
              //imagem do pacman
              if (pacman.direcao == 'U') {
                  pacman.image = pacmanUpImage;
            }
              else if (pacman.direcao == 'D') {
                pacman.image = pacmanDownImage;
            }
              else if (pacman.direcao == 'L') {
                pacman.image = pacmanLeftImage;
            }
              else if (pacman.direcao == 'R') {
                pacman.image = pacmanRightImage;
            }

    }
}
