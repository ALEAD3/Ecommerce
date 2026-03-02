package com.tuapp.grocery_backend.tools;

import org.springframework.stereotype.Component;
import java.util.List; // 🔥 IMPORT QUE FALTABA

import com.tuapp.grocery_backend.model.Producto;
import com.tuapp.grocery_backend.repository.ProductoRepository;
import com.tuapp.grocery_backend.service.CarritoService;

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

        // Buscar por nombre base
        List<Producto> productos =
                productoRepository.findByNombreIgnoreCase(nombre);

        Producto p = null;

        if (!productos.isEmpty()) {
            p = productos.get(0); // tomar el primero
        }

        // Si no encontró por nombre, intentar por variedad
        if (p == null) {
            p = productoRepository.findByVariedadIgnoreCase(nombre);
        }

        if (p == null) {
            return "Producto no encontrado.";
        }

        if (p.getStock() < cantidad) {
            return "No hay suficiente stock.";
        }

        carritoService.agregar(p, cantidad);

        return cantidad + " unidades de " + p.getNombre()
                + " fueron agregadas al carrito.";
    }
}