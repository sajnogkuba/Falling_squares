package finalPro;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
public class S27505Sajnog26c extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(S27505Sajnog26c::new);
    }
    S27505Sajnog26c() {
        this.setMinimumSize(new Dimension(640, 640));

        JPanel menu = new JPanel(new BorderLayout());
        JPanel menuCenter = new JPanel(new GridLayout(3, 1));
        JButton easy = new JButton("Easy");
        easy.setBackground(new Color(97, 232, 119));
        easy.setForeground(new Color(0, 100, 0));
        easy.setOpaque(true);
        JButton medium = new JButton("Medium");
        medium.setBackground(new Color(255, 247, 0));
        medium.setForeground(Color.ORANGE);
        medium.setOpaque(true);
        JButton hard = new JButton("Hard");
        hard.setBackground(Color.RED);
        hard.setForeground(new Color(150, 0, 10));
        hard.setOpaque(true);
        JLabel label = new JLabel("SELECT MODE");
        label.setBorder(new TitledBorder(""));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        menuCenter.add(easy);
        menuCenter.add(medium);
        menuCenter.add(hard);
        menu.add(label, BorderLayout.NORTH);
        menu.add(menuCenter, BorderLayout.CENTER);

        JLabel south = new JLabel("Score");
        south.setHorizontalAlignment(SwingConstants.CENTER);
        south.setBorder(new TitledBorder("Score"));

        easy.addActionListener(e -> {
            Board board = new Board(this);
            board.addSleep = 600;
            board.moveSleep = 14;
            this.remove(menu);
            this.add(south, BorderLayout.SOUTH);
            this.add(board);
            this.setVisible(false);
            this.setVisible(true);
            board.repaint();

            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    synchronized (Board.squares){
                        for(Square square : Board.squares){
                            if((square.visible && e.getX() >= square.x) && (e.getX() <= (square.x + square.size)) && (e.getY() >= square.y) && (e.getY() <= (square.y + square.size))) {
                                square.visible = false;
                                Board.points++;
                            }
                        }
                    }
                }
            });
        });
        medium.addActionListener(e -> {
            Board board = new Board(this);
            board.addSleep = 500;
            board.moveSleep = 12;
            this.remove(menu);
            this.add(south, BorderLayout.SOUTH);
            this.add(board);
            this.setVisible(false);
            this.setVisible(true);
            board.repaint();

            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    synchronized (Board.squares){
                        for(Square square : Board.squares){
                            if((square.visible && e.getX() >= square.x) && (e.getX() <= (square.x + square.size)) && (e.getY() >= square.y) && (e.getY() <= (square.y + square.size))) {
                                square.visible = false;
                                Board.points++;
                            }
                        }
                    }
                }
            });
        });
        hard.addActionListener(e -> {
            Board board = new Board(this);
            board.addSleep = 400;
            board.moveSleep = 7;
            this.remove(menu);
            this.add(south, BorderLayout.SOUTH);
            this.add(board);
            this.setVisible(false);
            this.setVisible(true);
            board.repaint();

            board.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    synchronized (Board.squares){
                        for(Square square : Board.squares){
                            if((square.visible && e.getX() >= square.x) && (e.getX() <= (square.x + square.size)) && (e.getY() >= square.y) && (e.getY() <= (square.y + square.size))) {
                                square.visible = false;
                                Board.points++;
                            }
                        }
                    }
                }
            });
        });
        Font font = label.getFont();
        font = font.deriveFont(font.getSize() + 5f);
        label.setFont(font);

        this.add(menu, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        Thread scoreThread = new Thread(() -> {
            while (true){
                String text = "Current score: " + (int)((Board.points/Board.maxScore) * 100) + '%';
                south.setText(text);
            }
        });

        scoreThread.start();

    }
}
class Board extends JPanel {
    int addSleep = 0;
    int moveSleep = 0;
    S27505Sajnog26c frame;
    static final List<Square> squares = new ArrayList<>();
    static double maxScore = 0;
    static double points = 0;
    Thread add = new Thread(() -> {
        while (true) {
            synchronized (squares) {
                squares.add(new Square(this.getHeight() / 10, this.getWidth()));
                squares.removeIf(s -> !s.visible);
            }
            maxScore++;
            try {
                Thread.sleep(addSleep);
            } catch (InterruptedException e) {
                break;
            }
            this.repaint();
        }
    });

    Thread move = new Thread(() -> {
        while (true) {
            synchronized (squares) {
                for (Square square : squares) {
                    square.y += 3;
                    if((square.y + square.size) >= this.getHeight()){
                        square.visible = false;
                    }
                }
            }
            try {
                Thread.sleep(moveSleep);
            } catch (InterruptedException e) {
                break;
            }
            this.repaint();
        }
    });
    Thread stop = new Thread(() -> {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        add.interrupt();
        move.interrupt();
        if((int)(points/maxScore * 100) > 80)
            JOptionPane.showMessageDialog(null, "You win");
        else
            JOptionPane.showMessageDialog(null, "You lost");
    });

    Board(S27505Sajnog26c frame) {
        this.setBackground(Color.ORANGE);
        this.frame = frame;
        add.start();
        move.start();
        stop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        synchronized (squares) {
            for (Square square : squares) {
                square.size = this.getHeight() / 10;
                if (square.visible)
                    square.draw(g);
            }
        }
    }
}

class Square {
    boolean visible = true;
    int size;
    int y = 0;
    int x;

    Square(int size, int panelWidth) {
        this.size = size;
        this.x = (int) ((Math.random() * 1000) % panelWidth) - size;
        if (this.x < 0) this.x = 0;
    }

    void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, size, size);
    }
}
