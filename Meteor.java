// Proyecto 3
// Velázquez Cruz Omar Alejandro
// 7CM2

import java.awt.*;
import java.util.Random;

public class Meteor implements Runnable {
    private int x, y; // Coordenadas del centro del meteorito
    private int radius; // Radio del círculo circunscrito
    private int speed; // Velocidad del meteorito
    private final int screenWidth;
    private final int screenHeight;
    private PoligonoIrreg shape; // Forma del meteorito
    private int dx, dy; // Dirección de movimiento
    private boolean active = true; // Indica si el meteorito está activo

    public Meteor(int screenWidth, int screenHeight, int radius) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.radius = radius;

        Random random = new Random();
        this.x = random.nextInt(screenWidth);
        this.y = random.nextInt(screenHeight);

        // Velocidad inversamente proporcional al área (π * r^2)
        this.speed = Math.max(1, 1000 / (int) (Math.PI * radius * radius));

        // Dirección aleatoria
        this.dx = random.nextBoolean() ? 1 : -1;
        this.dy = random.nextBoolean() ? 1 : -1;

        // Generar forma irregular
        this.shape = generateRandomShape();
    }

    private PoligonoIrreg generateRandomShape() {
        PoligonoIrreg polygon = new PoligonoIrreg();
        Random random = new Random();
        int vertices = random.nextInt(5) + 3; // Entre 3 y 7 vértices
        for (int i = 0; i < vertices; i++) {
            double angle = 2 * Math.PI * i / vertices;
            double r = radius * (0.5 + random.nextDouble() * 0.5); // Variación en el radio
            double px = r * Math.cos(angle);
            double py = r * Math.sin(angle);
            polygon.anadeVertice(new Coordenada(px, py));
        }
        return polygon;
    }

    public void move() {
        x += dx * speed;
        y += dy * speed;

        // Reaparecer por el lado opuesto si sale de los bordes
        if (x - radius > screenWidth) x = -radius;
        if (x + radius < 0) x = screenWidth + radius;
        if (y - radius > screenHeight) y = -radius;
        if (y + radius < 0) y = screenHeight + radius;
    }

    public void checkCollision(Meteor other) {
        if (!this.active || !other.active) return;

        // Distancia entre los centros de los dos meteoritos
        double distance = Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        if (distance < this.radius + other.radius) {
            // Reducir el radio al 50%
            this.radius /= 2;
            other.radius /= 2;

            // Recalcular velocidad
            this.speed = Math.max(1, 1000 / (int) (Math.PI * this.radius * this.radius));
            other.speed = Math.max(1, 1000 / (int) (Math.PI * other.radius * other.radius));

            // Generar nuevas formas basadas en el nuevo radio
            this.shape = generateRandomShape();
            other.shape = generateRandomShape();

            // Si el radio es demasiado pequeño, desactivar el meteorito
            if (this.radius < 5) this.active = false;
            if (other.radius < 5) other.active = false;
        }
    }

    public void draw(Graphics2D g) {
        if (!active) return; // No dibujar si el meteorito está inactivo

        int[] xPoints = new int[shape.vertices.size()];
        int[] yPoints = new int[shape.vertices.size()];
        for (int i = 0; i < shape.vertices.size(); i++) {
            Coordenada c = shape.vertices.get(i);
            xPoints[i] = (int) (x + c.abcisa());
            yPoints[i] = (int) (y + c.ordenada());
        }
        g.setColor(Color.WHITE);
        g.drawPolygon(xPoints, yPoints, shape.vertices.size());
    }

    @Override
public void run() {
    while (active) {
        move();
        try {
            Thread.sleep(16); // Aproximadamente 60 FPS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    System.out.println("Hilo del meteorito terminado: " + Thread.currentThread().getName());
}
}