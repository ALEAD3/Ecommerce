package com.tuapp.grocery_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.tuapp.grocery_backend.model.Producto;
import com.tuapp.grocery_backend.repository.ProductoRepository;

@Service
public class AssistantService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoService carritoService;

    public String chat(String message) {
        if (message == null || message.isBlank()) {
            return "¿Podrías escribir tu consulta nuevamente? 😊";
        }

        String msg = message.toLowerCase().trim();
        List<Producto> productos = productoRepository.findAll();

        // ===== SALUDOS =====
        if (msg.matches(".*\\b(hola|buenas|hey|hi|hello)\\b.*")) {
            return "¡Hola! 👋 Soy tu asistente virtual. Puedes preguntarme por productos o hacer pedidos.";
        }

        // ===== PEDIDO DINÁMICO =====
        if (msg.matches(".*\\b(agrega|agregar|quiero|comprar|llevar)\\b.*")) {

            // Dividir por comas o "y"
            String[] partes = msg.split(",| y ");
            StringBuilder respuesta = new StringBuilder();

            for (String parte : partes) {
                parte = parte.trim();
                if (parte.isEmpty()) continue;

                int cantidad = detectarCantidadNumero(parte);
                boolean agregado = false;

                // Buscar por variedad
                for (Producto p : productos) {
                    if (p.getVariedad() != null && parte.contains(p.getVariedad().toLowerCase())) {
                        respuesta.append(agregarAlCarrito(p, cantidad, p.getVariedad())).append("\n");
                        agregado = true;
                        break;
                    }
                }

                // Si no encontró variedad, buscar por nombre base (singular/plural)
                if (!agregado) {
                    for (Producto p : productos) {
                        String nombre = p.getNombre().toLowerCase();
                        if (parte.contains(nombre) || parte.contains(nombre + "s")) {
                            respuesta.append(agregarAlCarrito(p, cantidad, p.getNombre())).append("\n");
                            agregado = true;
                            break;
                        }
                    }
                }

                if (!agregado) {
                    respuesta.append("No encontré el producto: ").append(parte).append("\n");
                }
            }

            return respuesta.toString().trim();
        }

        // ===== CONSULTA DE VARIEDADES =====
        if (msg.matches(".*\\b(tipo|variedad|qué tipo)\\b.*")) {
            for (Producto p : productos) {
                if (msg.contains(p.getNombre().toLowerCase())) {
                    List<Producto> encontrados = productoRepository.findByNombreContainingIgnoreCase(p.getNombre());
                    if (encontrados == null || encontrados.isEmpty()) {
                        return "No encontré variedades disponibles.";
                    }

                    StringBuilder respuesta = new StringBuilder();
                    respuesta.append("Tenemos las siguientes variedades de ")
                             .append(p.getNombre())
                             .append(":\n");

                    for (Producto prod : encontrados) {
                        respuesta.append("- ")
                                 .append(prod.getVariedad() != null ? prod.getVariedad() : "Sin variedad")
                                 .append(" ($")
                                 .append(prod.getPrecio())
                                 .append(")\n");
                    }

                    return respuesta.toString();
                }
            }
        }

        // ===== BÚSQUEDA SIMPLE =====
        for (Producto p : productos) {
            String nombre = p.getNombre().toLowerCase();
            if (msg.contains(nombre) || msg.contains(nombre + "s")) {
                if (p.getStock() == null || p.getStock() <= 0) {
                    return "Actualmente no tenemos " + p.getNombre() + " en stock, pero podemos procesar tu pedido 📦.";
                }

                return "Tenemos disponible " + p.getNombre() + " a $" + p.getPrecio() + " 🛒";
            }
        }

        return "No logré entender tu solicitud 🤔 ¿Podrías reformularla?";
    }

    // =========================
    // MÉTODO AUXILIAR PARA AGREGAR AL CARRITO
    // =========================
    private String agregarAlCarrito(Producto p, int cantidad, String displayName) {
        if (p.getStock() == null || p.getStock() < cantidad) {
            if (p.getStock() != null && p.getStock() > 0) {
                carritoService.agregar(p, p.getStock());
                return "Solo quedan " + p.getStock() + " unidad(es) de " + displayName + ", las agregué al carrito 📦";
            }
            return "No hay suficiente stock de " + displayName + " 📦";
        }

        carritoService.agregar(p, cantidad);
        return "Perfecto 🙌 Agregué " + cantidad + " unidad(es) de " + displayName + " al carrito.";
    }

    // =========================
    // DETECTAR CANTIDAD
    // =========================
    private int detectarCantidadNumero(String msg) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return 1; // fallback seguro
            }
        }
        return 1; // si no hay número, asumimos 1
    }
}