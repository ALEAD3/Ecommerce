package com.tuapp.grocery_backend.service;

import com.tuapp.grocery_backend.model.Producto;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {

    public void agregar(Producto producto, int cantidad) {
        // Simulación: solo actualiza stock
        producto.setStock(producto.getStock() - cantidad);
    }
}