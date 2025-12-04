package com.rolapet.Moderacion.Service;

import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class ModeracionService {

    /** Lista thread-safe para almacenar palabras prohibidas en memoria */
    private final List<PalabraProhibida> palabrasProhibidas = new CopyOnWriteArrayList<>();

    /** Generador incremental para IDs de palabras prohibidas */
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    /**
     * Carga inicial de palabras prohibidas cuando arranca el servicio.
     * Se ejecuta automáticamente por la anotación @PostConstruct.
     */
    @PostConstruct
    public void inicializarPalabras() {
        log.info("Inicializando lista de palabras prohibidas...");

        // Cada llamada agrega una palabra con descripción y la activa en el sistema
        // Secciones agrupadas por categorías de moderación

        // ========== SPAM Y CONTENIDO NO DESEADO ==========
        agregarPalabraProhibida("spam", "Contenido repetitivo no deseado");
        agregarPalabraProhibida("publicidad", "Promoción no autorizada");
        agregarPalabraProhibida("clickbait", "Contenido engañoso para clicks");
        agregarPalabraProhibida("bot", "Actividad automatizada sospechosa");

        // ========== FRAUDE Y ESTAFAS ==========
        agregarPalabraProhibida("estafa", "Intento de fraude");
        agregarPalabraProhibida("fraude", "Actividad fraudulenta");
        agregarPalabraProhibida("phishing", "Intento de robo de información");
        agregarPalabraProhibida("piramidal", "Esquema piramidal ilegal");
        agregarPalabraProhibida("scam", "Estafa en línea");
        agregarPalabraProhibida("dinero fácil", "Promesa fraudulenta de dinero");
        agregarPalabraProhibida("gana dinero rápido", "Esquema de dinero sospechoso");

        // ========== SEGURIDAD Y HACKEO ==========
        agregarPalabraProhibida("hack", "Actividad de hackeo");
        agregarPalabraProhibida("hackear", "Intento de vulnerar seguridad");
        agregarPalabraProhibida("crackear", "Romper protecciones de software");
        agregarPalabraProhibida("keylogger", "Software malicioso");
        agregarPalabraProhibida("malware", "Software dañino");
        agregarPalabraProhibida("virus", "Software malicioso");
        agregarPalabraProhibida("ransomware", "Software de extorsión");

        // ========== DROGAS Y SUSTANCIAS ILEGALES ==========
        agregarPalabraProhibida("drogas", "Sustancias ilegales");
        agregarPalabraProhibida("narcóticos", "Sustancias controladas");
        agregarPalabraProhibida("marihuana venta", "Venta ilegal de sustancias");
        agregarPalabraProhibida("cocaína", "Droga ilegal");
        agregarPalabraProhibida("comprar drogas", "Comercio ilegal");

        // ========== ARMAS Y VIOLENCIA ==========
        agregarPalabraProhibida("armas", "Comercio ilegal de armas");
        agregarPalabraProhibida("bomba", "Amenaza de violencia");
        agregarPalabraProhibida("explosivo", "Material peligroso");
        agregarPalabraProhibida("asesinar", "Incitación a la violencia");
        agregarPalabraProhibida("matar", "Amenaza grave");

        // ========== CONTENIDO PARA ADULTOS ==========
        agregarPalabraProhibida("xxx", "Contenido explícito");
        agregarPalabraProhibida("porno", "Contenido adulto");
        agregarPalabraProhibida("sexo", "Contenido inapropiado");
        agregarPalabraProhibida("desnudos", "Contenido no permitido");

        // ========== DISCRIMINACIÓN Y ODIO ==========
        agregarPalabraProhibida("racista", "Discurso de odio");
        agregarPalabraProhibida("discriminación", "Contenido discriminatorio");
        agregarPalabraProhibida("xenofobia", "Odio por nacionalidad");
        agregarPalabraProhibida("homofobia", "Discriminación LGBTQ+");
        agregarPalabraProhibida("nazi", "Ideología de odio");

        // ========== ACOSO Y BULLYING ==========
        agregarPalabraProhibida("acoso", "Hostigamiento");
        agregarPalabraProhibida("bullying", "Intimidación");
        agregarPalabraProhibida("amenaza", "Contenido amenazante");
        agregarPalabraProhibida("stalkear", "Acoso persistente");
        agregarPalabraProhibida("doxxing", "Publicación de información privada");

        // ========== INFORMACIÓN PERSONAL ==========
        agregarPalabraProhibida("contraseña", "Información de seguridad");
        agregarPalabraProhibida("cvv", "Datos de tarjeta bancaria");

        // ========== CONTENIDO ENGAÑOSO ==========
        agregarPalabraProhibida("noticia falsa", "Desinformación");
        agregarPalabraProhibida("fake news", "Información falsa");
        agregarPalabraProhibida("conspiración", "Teoría no verificada");
        agregarPalabraProhibida("cura milagrosa", "Afirmación médica falsa");

        // ========== ACTIVIDADES ILEGALES ==========
        agregarPalabraProhibida("lavado de dinero", "Actividad financiera ilegal");
        agregarPalabraProhibida("evasión fiscal", "Delito financiero");
        agregarPalabraProhibida("falsificación", "Producción de documentos falsos");
        agregarPalabraProhibida("contrabando", "Comercio ilegal");
        agregarPalabraProhibida("tráfico", "Actividad ilegal de transporte");

        // ========== MANIPULACIÓN DE PLATAFORMA ==========
        agregarPalabraProhibida("comprar seguidores", "Manipulación de métricas");
        agregarPalabraProhibida("comprar likes", "Fraude de engagement");
        agregarPalabraProhibida("comprar reviews", "Opiniones falsas");
        agregarPalabraProhibida("likes automáticos", "Bot de interacciones");

        log.info("Lista inicializada con {} palabras prohibidas", palabrasProhibidas.size());
    }

    /**
     * Valida el contenido enviado por el usuario verificando si contiene palabras prohibidas.
     *
     * @param request Datos del contenido a evaluar
     * @return DTO con resultado de aprobación, mensaje y número de infracciones
     */
    public ModeracionResponseDTO validarContenido(ModeracionRequestDTO request) {
        String contenido = request.getContenido().toLowerCase();
        List<String> palabrasDetectadas = new ArrayList<>();

        // Revisa si el contenido contiene alguna palabra prohibida activa
        for (PalabraProhibida palabra : palabrasProhibidas) {
            if (palabra.getActiva() && contenido.contains(palabra.getPalabra().toLowerCase())) {
                palabrasDetectadas.add(palabra.getPalabra());
                log.warn("Palabra prohibida detectada: '{}'", palabra.getPalabra());
            }
        }

        // Si no se detectan palabras, contenido es aprobado
        if (palabrasDetectadas.isEmpty()) {
            log.info("Contenido aprobado para usuario: {}", request.getUsuarioId());
            return new ModeracionResponseDTO(true, "Contenido aprobado ✓", 0);
        }

        // Si hay coincidencias, se rechaza el contenido
        log.warn("Contenido rechazado. Palabras detectadas: {}", palabrasDetectadas);
        return new ModeracionResponseDTO(
                false,
                "Tu publicación contiene lenguaje inapropiado: " + String.join(", ", palabrasDetectadas),
                palabrasDetectadas.size()
        );
    }

    /**
     * Registra una nueva palabra prohibida.
     * Valida duplicados y entradas vacías.
     */
    public PalabraProhibida agregarPalabraProhibida(String palabra, String descripcion) {
        log.info("Agregando palabra prohibida: '{}'", palabra);

        if (palabra == null || palabra.trim().isEmpty()) {
            throw new IllegalArgumentException("La palabra no puede estar vacía");
        }

        String palabraLower = palabra.trim().toLowerCase();

        // Validar duplicado
        boolean existe = palabrasProhibidas.stream()
                .anyMatch(p -> p.getPalabra().equalsIgnoreCase(palabraLower));

        if (existe) {
            throw new IllegalArgumentException("La palabra '" + palabra + "' ya está registrada");
        }

        // Crear objeto
        PalabraProhibida nueva = new PalabraProhibida();
        nueva.setId(idGenerator.getAndIncrement());
        nueva.setPalabra(palabraLower);
        nueva.setDescripcion(descripcion);
        nueva.setActiva(true);

        palabrasProhibidas.add(nueva);
        log.info("Palabra '{}' agregada con ID: {}", palabraLower, nueva.getId());

        return nueva;
    }

    /**
     * Actualiza la palabra prohibida según ID.
     */
    public PalabraProhibida actualizarPalabraProhibida(Integer id, String nuevaPalabra, String descripcion) {
        log.info("Actualizando palabra con ID: {}", id);

        PalabraProhibida palabra = palabrasProhibidas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Palabra no encontrada con ID: " + id));

        palabra.setPalabra(nuevaPalabra.toLowerCase());
        palabra.setDescripcion(descripcion);

        log.info("Palabra actualizada: '{}'", nuevaPalabra);

        return palabra;
    }

    /**
     * Elimina una palabra por su ID.
     */
    public void eliminarPalabraProhibida(Integer id) {
        log.info("Eliminando palabra con ID: {}", id);

        boolean eliminada = palabrasProhibidas.removeIf(p -> p.getId().equals(id));

        if (!eliminada) {
            throw new RuntimeException("Palabra no encontrada con ID: " + id);
        }

        log.info("Palabra eliminada exitosamente");
    }

    /**
     * Desactiva una palabra prohibida sin eliminarla.
     */
    public PalabraProhibida desactivarPalabra(Integer id) {
        log.info("Desactivando palabra con ID: {}", id);

        PalabraProhibida palabra = buscarPorId(id);
        palabra.setActiva(false);

        log.info("Palabra '{}' desactivada", palabra.getPalabra());
        return palabra;
    }

    /**
     * Activa una palabra previamente desactivada.
     */
    public PalabraProhibida activarPalabra(Integer id) {
        log.info("Activando palabra con ID: {}", id);

        PalabraProhibida palabra = buscarPorId(id);
        palabra.setActiva(true);

        log.info("Palabra '{}' activada", palabra.getPalabra());
        return palabra;
    }

    /**
     * Lista todas las palabras, activas e inactivas.
     */
    public List<PalabraProhibida> listarTodasLasPalabras() {
        log.debug("Listando {} palabras prohibidas", palabrasProhibidas.size());
        return new ArrayList<>(palabrasProhibidas);
    }

    /**
     * Lista únicamente palabras activas.
     */
    public List<PalabraProhibida> listarPalabrasActivas() {
        List<PalabraProhibida> activas = palabrasProhibidas.stream()
                .filter(PalabraProhibida::getActiva)
                .toList();

        log.debug("Se encontraron {} palabras activas", activas.size());
        return activas;
    }

    /**
     * Busca palabra por su ID.
     */
    public PalabraProhibida buscarPorId(Integer id) {
        return palabrasProhibidas.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Palabra no encontrada con ID: " + id));
    }
}
