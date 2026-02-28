package com.tuapp.grocery_backend.tools;

import com.tuapp.grocery_backend.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoTool {

    private final ProductoRepository productoRepository;

    public ProductoTool(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public String consultarProducto(String nombre) {
        Producto p = productoRepository.findByNombre(nombre);
        if (p == null) return "No encontré ese producto.";

        return "El producto " + p.getNombre() +
               " cuesta $" + p.getPrecio() +
               " y hay " + p.getStock() + " unidades disponibles.";
    }
}