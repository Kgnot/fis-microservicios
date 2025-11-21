package uni.fis.pago.Security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uni.fis.pago.Repository.OrdenCompraRepository;
import uni.fis.pago.Repository.OrdenItemRepository;
import uni.fis.pago.Repository.PagoRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagoOwnershipFilter extends OncePerRequestFilter {

    private final PagoRepository pagoRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenItemRepository ordenItemRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        
        log.info("=== OWNERSHIP FILTER === Path: {}, Method: {}", path, method);
        
        // Endpoints que NO requieren verificación de ownership (son creaciones)
        if ((method.equals("POST") && path.equals("/api/pago/crearPago")) ||
            (method.equals("POST") && path.equals("/api/pago/crearOrdenCompra")) ||
            (method.equals("POST") && path.equals("/api/pago/crearOrdenItem"))) {
            log.info("Endpoint de creación, no requiere verificación de ownership");
            filterChain.doFilter(request, response);
            return;
        }

        // Endpoints que SÍ requieren verificación de ownership
        boolean requiresOwnershipCheck = 
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerPago/\\d+.*")) ||
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerOrdenCompra/\\d+.*")) ||
            (method.equals("DELETE") && path.matches(".*/api/pago/EliminarOrdenCompra/\\d+.*")) ||
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerOrdenItem/\\d+.*")) ||
            (method.equals("DELETE") && path.matches(".*/api/pago/EliminarOrdenItem/\\d+.*")) ||
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerOrdenCompra/\\d+/OrdenesItems.*"));

        if (!requiresOwnershipCheck) {
            log.info("No requiere verificación de ownership, continuando...");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Obtener el userId del token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null) {
                log.error("Authentication es null");
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autenticado");
                return;
            }
            
            if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
                log.error("Principal no es instancia de UserPrincipal");
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autenticado correctamente");
                return;
            }

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Integer userId = userPrincipal.getUserId();
            
            log.info("UserId del token: {}", userId);

            // Verificar ownership según el tipo de endpoint
            if (path.contains("/ObtenerPago/")) {
                if (!verifyPagoOwnership(path, userId, response)) return;
                
            } else if (path.contains("/ObtenerOrdenCompra/") && !path.contains("/OrdenesItems")) {
                if (!verifyOrdenCompraOwnership(path, userId, response)) return;
                
            } else if (path.contains("/EliminarOrdenCompra/")) {
                if (!verifyOrdenCompraOwnership(path, userId, response)) return;
                
            } else if (path.contains("/ObtenerOrdenItem/")) {
                if (!verifyOrdenItemOwnership(path, userId, response)) return;
                
            } else if (path.contains("/EliminarOrdenItem/")) {
                if (!verifyOrdenItemOwnership(path, userId, response)) return;
                
            } else if (path.contains("/OrdenesItems")) {
                if (!verifyOrdenCompraOwnership(path, userId, response)) return;
            }
            
            log.info("Verificación de ownership exitosa para userId {}", userId);

        } catch (Exception e) {
            log.error("Error inesperado en la verificación de ownership", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                            "Error al verificar permisos: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean verifyPagoOwnership(String path, Integer userId, HttpServletResponse response) 
            throws IOException {
        Integer pagoId = extractIdFromPath(path, "/ObtenerPago/");
        
        if (pagoId == null) {
            log.error("No se pudo extraer pagoId de la ruta: {}", path);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                            "No se pudo identificar el pago en la URL");
            return false;
        }
        
        var pagoOpt = pagoRepository.findById(pagoId);
        
        if (pagoOpt.isEmpty()) {
            log.warn("Pago con id {} no encontrado", pagoId);
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Pago no encontrado");
            return false;
        }

        var pago = pagoOpt.get();
        Integer idUsuario = pago.getIdUsuario(); // Asume que Pago tiene un campo idUsuario
        
        log.info("IdUsuario del pago: {}", idUsuario);

        if (!idUsuario.equals(userId)) {
            log.warn("Usuario {} intentó acceder al pago {} que pertenece al usuario {}", 
                     userId, pagoId, idUsuario);
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, 
                            "No tienes permiso para acceder a este pago");
            return false;
        }
        
        return true;
    }

    private boolean verifyOrdenCompraOwnership(String path, Integer userId, HttpServletResponse response) 
            throws IOException {
        Integer ordenCompraId = extractIdFromPath(path, 
            path.contains("/EliminarOrdenCompra/") ? "/EliminarOrdenCompra/" : "/ObtenerOrdenCompra/");
        
        if (ordenCompraId == null) {
            log.error("No se pudo extraer ordenCompraId de la ruta: {}", path);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                            "No se pudo identificar la orden de compra en la URL");
            return false;
        }
        
        var ordenCompraOpt = ordenCompraRepository.findById(ordenCompraId);
        
        if (ordenCompraOpt.isEmpty()) {
            log.warn("Orden de compra con id {} no encontrada", ordenCompraId);
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, 
                            "Orden de compra no encontrada");
            return false;
        }

        var ordenCompra = ordenCompraOpt.get();
        Integer idUsuario = ordenCompra.getIdUsuario(); // Asume que OrdenCompra tiene un campo idUsuario
        
        log.info("IdUsuario de la orden de compra: {}", idUsuario);

        if (!idUsuario.equals(userId)) {
            log.warn("Usuario {} intentó acceder a la orden de compra {} que pertenece al usuario {}", 
                     userId, ordenCompraId, idUsuario);
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, 
                            "No tienes permiso para acceder a esta orden de compra");
            return false;
        }
        
        return true;
    }

    private boolean verifyOrdenItemOwnership(String path, Integer userId, HttpServletResponse response) 
            throws IOException {
        Integer ordenItemId = extractIdFromPath(path, 
            path.contains("/EliminarOrdenItem/") ? "/EliminarOrdenItem/" : "/ObtenerOrdenItem/");
        
        if (ordenItemId == null) {
            log.error("No se pudo extraer ordenItemId de la ruta: {}", path);
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                            "No se pudo identificar el item en la URL");
            return false;
        }
        
        var ordenItemOpt = ordenItemRepository.findById(ordenItemId);
        
        if (ordenItemOpt.isEmpty()) {
            log.warn("Orden item con id {} no encontrado", ordenItemId);
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Item no encontrado");
            return false;
        }

        var ordenItem = ordenItemOpt.get();
        // Asume que OrdenItem tiene una relación con OrdenCompra
        var ordenCompra = ordenItem.getOrdenCompra(); 
        
        if (ordenCompra == null) {
            log.error("OrdenItem {} no tiene una OrdenCompra asociada", ordenItemId);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                            "Error en la estructura de datos");
            return false;
        }
        
        Integer idUsuario = ordenCompra.getIdUsuario();
        
        log.info("IdUsuario del item (a través de orden de compra): {}", idUsuario);

        if (!idUsuario.equals(userId)) {
            log.warn("Usuario {} intentó acceder al item {} que pertenece al usuario {}", 
                     userId, ordenItemId, idUsuario);
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, 
                            "No tienes permiso para acceder a este item");
            return false;
        }
        
        return true;
    }

    private Integer extractIdFromPath(String path, String marker) {
        try {
            int startIndex = path.indexOf(marker);
            if (startIndex == -1) return null;
            
            String afterMarker = path.substring(startIndex + marker.length());
            
            // Extraer solo los dígitos hasta el siguiente '/' o hasta el final
            int endIndex = afterMarker.indexOf('/');
            String idStr = endIndex == -1 ? afterMarker : afterMarker.substring(0, endIndex);
            
            return Integer.parseInt(idStr);
        } catch (Exception e) {
            log.error("Error al extraer ID de la ruta: {}", path, e);
            return null;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) 
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}