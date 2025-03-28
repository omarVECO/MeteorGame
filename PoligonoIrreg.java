// Proyecto 3
// Velázquez Cruz Omar Alejandro
// 7CM2

import java.util.ArrayList;
import java.util.List;

public class PoligonoIrreg {
    public List<Coordenada> vertices = new ArrayList<>();

    public void anadeVertice(Coordenada c) {
        vertices.add(c);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Vertices totales: " + vertices.size() + "\n");
        for (Coordenada c : vertices) {
            res.append(c).append("\n");
        }
        return res.toString();
    }
}