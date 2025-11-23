package uni.fis.pago.Security;

import java.io.IOException;
import java.util.Optional;

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
import uni.fis.pago.Entity.OrdenCompra;
import uni.fis.pago.Entity.OrdenItem;
import uni.fis.pago.Entity.Pago;
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
        
        // 1. EXCEPCIÓN: crearOrdenCompra es ahora el endpoint libre de validación de ID
        if (method.equals("POST") && path.endsWith("/api/pago/crearOrdenCompra")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Definir endpoints protegidos
        // Incluimos POST crearPago aquí para que pase por validación
        boolean isPagoEndpoint = 
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerPago/\\d+.*")) ||
            (method.equals("POST") && path.contains("/api/pago/crearPago")); // Asume ruta tipo /crearPago/{id}
        
        boolean isOrdenCompraEndpoint = 
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerOrdenCompra/\\d+.*")) ||
            (method.equals("DELETE") && path.matches(".*/api/pago/EliminarOrdenCompra/\\d+.*")) ||
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerOrdenCompra/\\d+/OrdenesItems.*"));

        boolean isOrdenItemEndpoint = 
            (method.equals("GET") && path.matches(".*/api/pago/ObtenerOrdenItem/\\d+.*")) ||
            (method.equals("DELETE") && path.matches(".*/api/pago/EliminarOrdenItem/\\d+.*"));

        if (!isPagoEndpoint && !isOrdenCompraEndpoint && !isOrdenItemEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autenticado");
                return;
            }

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Integer userId = userPrincipal.getUserId();
            
            boolean authorized = false;

            if (isPagoEndpoint) {
                authorized = verifyPagoOwnership(path, userId, response);
            } else if (isOrdenCompraEndpoint) {
                authorized = verifyOrdenCompraOwnership(path, userId, response);
            } else if (isOrdenItemEndpoint) {
                authorized = verifyOrdenItemOwnership(path, userId, response);
            }
            
            if (authorized) {
                filterChain.doFilter(request, response);
            }

        } catch (Exception e) {
            log.error("Error en filtro ownership", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno");
        }
    }

    private boolean verifyPagoOwnership(String path, Integer userId, HttpServletResponse response) 
            throws IOException {
        // Detectar marcador: puede ser obtener o crear
        String marker = path.contains("/ObtenerPago/") ? "/ObtenerPago/" : "/crearPago/";
        
        Integer pagoId = extractIdFromPath(path, marker);
        if (pagoId == null) return sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID de pago no encontrado en la URL");
        
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        
        if (pagoOpt.isEmpty()) {
            return sendError(response, HttpServletResponse.SC_NOT_FOUND, "Pago no encontrado");
        }

        if (!pagoOpt.get().getIdUsuario().equals(userId)) {
            return sendError(response, HttpServletResponse.SC_FORBIDDEN, "No tienes permiso sobre este pago");
        }
        
        return true;
    }

    private boolean verifyOrdenCompraOwnership(String path, Integer userId, HttpServletResponse response) 
            throws IOException {
        String marker = path.contains("/EliminarOrdenCompra/") ? "/EliminarOrdenCompra/" : "/ObtenerOrdenCompra/";
        Integer ordenCompraId = extractIdFromPath(path, marker);
        
        if (ordenCompraId == null) return sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        
        Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(ordenCompraId);
        if (ordenOpt.isEmpty()) return sendError(response, HttpServletResponse.SC_NOT_FOUND, "Orden no encontrada");

        Integer idPago = ordenOpt.get().getIdPago();
        if (idPago == null) return sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Orden sin pago asociado");

        Optional<Pago> pagoOpt = pagoRepository.findById(idPago);
        if (pagoOpt.isEmpty() || !pagoOpt.get().getIdUsuario().equals(userId)) {
            return sendError(response, HttpServletResponse.SC_FORBIDDEN, "No autorizado");
        }
        
        return true;
    }

    private boolean verifyOrdenItemOwnership(String path, Integer userId, HttpServletResponse response) 
            throws IOException {
        String marker = path.contains("/EliminarOrdenItem/") ? "/EliminarOrdenItem/" : "/ObtenerOrdenItem/";
        Integer ordenItemId = extractIdFromPath(path, marker);
        
        if (ordenItemId == null) return sendError(response, HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        
        Optional<OrdenItem> itemOpt = ordenItemRepository.findById(ordenItemId);
        if (itemOpt.isEmpty()) return sendError(response, HttpServletResponse.SC_NOT_FOUND, "Item no encontrado");

        Integer idOrden = itemOpt.get().getIdOrdenCompra();
        if (idOrden == null) return sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Item huérfano");

        Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(idOrden);
        if (ordenOpt.isEmpty()) return sendError(response, HttpServletResponse.SC_NOT_FOUND, "Orden asociada no encontrada");

        Integer idPago = ordenOpt.get().getIdPago();
        Optional<Pago> pagoOpt = pagoRepository.findById(idPago);

        if (pagoOpt.isEmpty() || !pagoOpt.get().getIdUsuario().equals(userId)) {
            return sendError(response, HttpServletResponse.SC_FORBIDDEN, "No autorizado");
        }
        
        return true;
    }

    private Integer extractIdFromPath(String path, String marker) {
        try {
            int startIndex = path.indexOf(marker);
            if (startIndex == -1) return null;
            
            String afterMarker = path.substring(startIndex + marker.length());
            int endIndex = afterMarker.indexOf('/');
            String idStr = endIndex == -1 ? afterMarker : afterMarker.substring(0, endIndex);
            
            return Integer.parseInt(idStr);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean sendError(HttpServletResponse response, int status, String message) throws IOException {
        sendErrorResponse(response, status, message);
        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) 
            throws IOException {
        if(!response.isCommitted()) {
            response.setStatus(status);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
        }
    }
}