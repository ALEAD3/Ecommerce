package com.tuapp.grocery_backend.tools;

import com.tuapp.grocery_backend.model.Producto;
import com.tuapp.grocery_backend.service.CarritoService;
import org.springframework.stereotype.Component;

@Component
public class CarritoTool {

    private final ProductoRepository productoRepository;
    private final CarritoService carritoService;

    public CarritoTool(ProductoRepository productoRepository,
                       CarritoService carritoService) {
        this.productoRepository = productoRepository;
        this.carritoService = carritoService;
    }

    public String agregarAlCarrito(String nombre, int cantidad) {
        Producto p = productoRepository.findByNombre(nombre);

        if (p == null) return "Producto no encontrado.";
        if (p.getStock() < cantidad) return "No hay suficiente stock.";

        carritoService.agregar(p, cantidad);
        return cantidad + " unidades de " + nombre + " fueron agregadas al carrito.";
    }
}