// Proyecto 3
// Velázquez Cruz Omar Alejandro
// 7CM2

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MeteorGame extends JPanel {
    private final ArrayList<Meteor> meteors;
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    public MeteorGame(int meteorCount) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        meteors = new ArrayList<>();
        generateMeteors(meteorCount);
    }

    private void generateMeteors(int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int radius = random.nextInt(WIDTH / 16) + 10; // Radio máximo 1/16 del ancho
            Meteor meteor = new Meteor(WIDTH, HEIGHT, radius);
            meteors.add(meteor);
            new Thread(meteor).start(); // Crear un hilo para cada meteorito
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < meteors.size(); i++) {
            for (int j = i + 1; j < meteors.size(); j++) {
                meteors.get(i).checkCollision(meteors.get(j));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        checkCollisions(); // Verificar colisiones antes de dibujar

        for (Meteor meteor : meteors) {
            meteor.draw(g2d);
        }
    }

    public static void main(String[] args) {
        int meteorCount = 10; // Valor por defecto
        if (args.length > 0) {
            try {
                meteorCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("El argumento debe ser un número entero. Usando 10 meteoritos por defecto.");
            }
        }

        JFrame frame = new JFrame("Meteor Game");
        MeteorGame game = new MeteorGame(meteorCount);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Hilo para redibujar la pantalla
        new Timer(16, e -> game.repaint()).start(); // Aproximadamente 60 FPS
    }
}