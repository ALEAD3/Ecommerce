package com.tuapp.grocery_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.tuapp.grocery_backend.service.CarritoService;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public List<Map<String, Object>> verCarrito() {
        return carritoService.obtenerCarrito();
    }

    @GetMapping("/total")
    public double total() {
        return carritoService.obtenerTotal();
    }

    @DeleteMapping
    public void vaciar() {
        carritoService.vaciarCarrito();
    }
}