package uni.fis.multimedia.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import uni.fis.multimedia.dto.MultimediaResponseDTO;
import uni.fis.multimedia.entity.MultimediaEntity;
import uni.fis.multimedia.repository.MultimediaRepository;
import uni.fis.multimedia.exception.MultimediaExceptions.*;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MultimediaServiceImpl implements MultimediaService {

    private static final Logger logger = LoggerFactory.getLogger(MultimediaServiceImpl.class);
    
    private final MultimediaRepository multimediaRepository;

    private static final String UPLOAD_DIR = "/uploads/";

    // Tipos permitidos
    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "image/png",
            "image/jpeg",
            "application/pdf",
            "audio/mpeg",
            "video/mp4"
    );

    public MultimediaServiceImpl(MultimediaRepository multimediaRepository) {
        this.multimediaRepository = multimediaRepository;
    }

    public boolean escanear(byte[] data) {
        logger.info("Iniciando escaneo antivirus para archivo de {} bytes", data.length);
        
        try (Socket socket = new Socket("clamav", 3310)) {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            out.write("zINSTREAM\0".getBytes());

            int offset = 0;
            while (offset < data.length) {
                int chunk = Math.min(2048, data.length - offset);

                // Tamaño del chunk (4 bytes)
                byte[] size = ByteBuffer.allocate(4).putInt(chunk).array();
                out.write(size);

                // Chunk de datos
                out.write(data, offset, chunk);
                offset += chunk;
            }

            // Enviar 0 para finalizar el stream
            out.write(ByteBuffer.allocate(4).putInt(0).array());
            out.flush();

            // Leer respuesta
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String result = reader.readLine();

            boolean escaneoExitoso = result != null && result.contains("OK");
            
            if (escaneoExitoso) {
                logger.info("Escaneo antivirus completado: ARCHIVO LIMPIO");
            } else {
                logger.warn("Escaneo antivirus completado: ARCHIVO INFECTADO - {}", result);
            }
            
            return escaneoExitoso;

        } catch (IOException e) {
            logger.error("Error de conexión con ClamAV: {}", e.getMessage(), e);
            throw new ClamAVConnectionException("No se pudo conectar con el servicio antivirus", e);
        } catch (Exception e) {
            logger.error("Error inesperado durante el escaneo antivirus: {}", e.getMessage(), e);
            throw new VirusScanException("Error durante el escaneo de seguridad del archivo", e);
        }
    }

    @Override
    public MultimediaEntity guardarArchivo(MultipartFile archivo) {
        logger.info("Iniciando proceso de guardado para archivo: {}", archivo.getOriginalFilename());
        
        // Validar que el archivo no esté vacío
        if (archivo.isEmpty()) {
            logger.warn("Intento de guardar archivo vacío: {}", archivo.getOriginalFilename());
            throw new EmptyFileException("El archivo no puede estar vacío");
        }

        // Validar nombre del archivo
        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.trim().isEmpty()) {
            logger.warn("Intento de guardar archivo sin nombre");
            throw new InvalidFileNameException("El archivo debe tener un nombre válido");
        }

        nombreOriginal = nombreOriginal.toLowerCase();
        logger.debug("Procesando archivo: {} con tipo: {}", nombreOriginal, archivo.getContentType());

        // VALIDAR MIME TYPE
        String tipo = archivo.getContentType();
        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            logger.warn("Tipo de archivo no permitido: {} para archivo: {}", tipo, nombreOriginal);
            throw new InvalidFileTypeException("Tipo de archivo no permitido: " + tipo + 
                    ". Tipos permitidos: PNG, JPG, JPEG, PDF, MP3, MP4");
        }

        // VALIDAR EXTENSIÓN
        if (!(nombreOriginal.endsWith(".png") ||
                nombreOriginal.endsWith(".jpg") ||
                nombreOriginal.endsWith(".jpeg") ||
                nombreOriginal.endsWith(".pdf") ||
                nombreOriginal.endsWith(".mp3") ||
                nombreOriginal.endsWith(".mp4"))) {
            logger.warn("Extensión no permitida para archivo: {}", nombreOriginal);
            throw new InvalidFileTypeException("Extensión de archivo no permitida. " +
                    "Extensiones permitidas: .png, .jpg, .jpeg, .pdf, .mp3, .mp4");
        }

        // Crear directorio si no existe
        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            Files.createDirectories(uploadPath);
            logger.debug("Directorio de uploads verificado/creado: {}", UPLOAD_DIR);
        } catch (IOException e) {
            logger.error("No se pudo crear el directorio de uploads: {}", UPLOAD_DIR, e);
            throw new FileStorageException("Error al crear el directorio de almacenamiento", e);
        }

        File destino = null;
        try {
            // ESCANEAR CON CLAMAV
            byte[] bytes = archivo.getBytes();
            logger.debug("Archivo leído en memoria, tamaño: {} bytes", bytes.length);
            
            if (!escanear(bytes)) {
                logger.error("Archivo bloqueado por contener virus: {}", nombreOriginal);
                throw new InfectedFileException("El archivo contiene un virus y fue bloqueado por seguridad");
            }

            String nombreGuardado = System.currentTimeMillis() + "_" + nombreOriginal;
            logger.debug("Nombre generado para guardar: {}", nombreGuardado);

            // Guardar archivo en sistema de archivos
            destino = new File(UPLOAD_DIR + nombreGuardado);
            archivo.transferTo(destino);
            logger.info("Archivo guardado exitosamente en: {}", destino.getAbsolutePath());

            String urlPublica = "/uploads/" + nombreGuardado;

            // GUARDAR EN BD
            MultimediaEntity m = new MultimediaEntity();
            m.setUrl(urlPublica);
            m.setTipoArchivo(tipo);

            MultimediaEntity entityGuardada = multimediaRepository.save(m);
            logger.info("Archivo guardado en BD con ID: {}", entityGuardada.getId());
            return entityGuardada;

        } catch (IOException e) {
            logger.error("Error de E/S al procesar el archivo: {}", nombreOriginal, e);
            throw new FileStorageException("Error al procesar el archivo", e);
        } catch (Exception e) {
            logger.error("Error inesperado al guardar archivo: {}", nombreOriginal, e);
            
            // Limpiar archivo físico si se creó pero falló después
            if (destino != null && destino.exists()) {
                try {
                    if (destino.delete()) {
                        logger.info("Archivo físico eliminado después de error: {}", destino.getAbsolutePath());
                    } else {
                        logger.warn("No se pudo eliminar el archivo físico después de error: {}", destino.getAbsolutePath());
                    }
                } catch (SecurityException secEx) {
                    logger.warn("No se tiene permiso para eliminar el archivo físico: {}", destino.getAbsolutePath());
                }
            }
            
            // Relanzar excepciones específicas
            if (e instanceof InfectedFileException) {
                throw (InfectedFileException) e;
            }
            throw new FileStorageException("Error inesperado al procesar el archivo", e);
        }
    }

    @Override
    public List<MultimediaEntity> findAll() {
        logger.info("Buscando todos los archivos multimedia");
        try {
            List<MultimediaEntity> result = multimediaRepository.findAll();
            logger.info("Se encontraron {} archivos multimedia", result.size());
            return result;
        } catch (Exception e) {
            logger.error("Error al obtener listado de archivos multimedia", e);
            throw new DataAccessException("Error al recuperar los archivos multimedia", e);
        }
    }

    @Override
    public MultimediaResponseDTO obtenerImagen(Long id) {
        logger.info("Buscando multimedia con ID: {}", id);
        
        if (id == null || id <= 0) {
            logger.warn("ID inválido proporcionado: {}", id);
            throw new InvalidIdException("ID de multimedia inválido");
        }

        MultimediaEntity multimedia = multimediaRepository.findMultimediaEntityById(id);

        if (multimedia == null) {
            logger.warn("No se encontró multimedia con ID: {}", id);
            throw new MultimediaNotFoundException("Multimedia no encontrado con ID: " + id);
        }

        logger.debug("Multimedia encontrado: ID={}, Tipo={}, URL={}", 
                    multimedia.getId(), multimedia.getTipoArchivo(), multimedia.getUrl());

        MultimediaResponseDTO dto = new MultimediaResponseDTO();
        dto.setId(multimedia.getId());
        dto.setUrl(multimedia.getUrl());
        dto.setTipoArchivo(multimedia.getTipoArchivo());

        return dto;
    }
}