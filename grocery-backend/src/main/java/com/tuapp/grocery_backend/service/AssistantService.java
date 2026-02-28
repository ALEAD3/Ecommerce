package com.tuapp.grocery_backend.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.tuapp.grocery_backend.tools.ProductoTool;
import com.tuapp.grocery_backend.tools.CarritoTool;
import com.tuapp.grocery_backend.tools.ProductoRepository;

@Service
@RequiredArgsConstructor
public class AssistantService {

    private final ProductoRepository productoRepository;
    private final ProductoTool productoTool;
    private final CarritoTool carritoTool;

    public String chat(String message) {

        String msg = message.toLowerCase();

        // ✅ SALUDOS
        if (msg.contains("hola") || msg.contains("buenas") || msg.contains("hey")) {
            return "¡Hola! 👋 Soy tu asistente virtual. Puedes preguntarme por productos o disponibilidad.";
        }

        if (msg.contains("gracias")) {
            return "¡Con gusto! 😊 ¿Necesitas algo más?";
        }

        // 🔎 Buscar dinámicamente cualquier producto mencionado
        for (String nombreProducto : productoRepository.findAll().keySet()) {

            if (msg.contains(nombreProducto.toLowerCase())) {
                return productoTool.consultarProducto(nombreProducto);
            }
        }

        return "No encontré ese producto.";
    }
}