package com.tuapp.grocery_backend.tools;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.HashMap;
import com.tuapp.grocery_backend.model.Producto;

@Repository
public class ProductoRepository {

    private final Map<String, Producto> productos = new HashMap<>();

    public ProductoRepository() {
        productos.put("Manzana", new Producto("Manzana", 5.0, 10));
        productos.put("Leche", new Producto("Leche", 20.0, 5));
    }

    public Producto findByNombre(String nombre) {
        return productos.entrySet()
                .stream()
                .filter(e -> e.getKey().equalsIgnoreCase(nombre))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    public Map<String, Producto> findAll() {
        return productos;
    }
}