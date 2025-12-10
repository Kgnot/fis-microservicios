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

import java.io.File;
import java.io.IOException;
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

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

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

    @Override
    public MultimediaEntity guardarArchivo(MultipartFile archivo) {
        logger.info("Iniciando proceso de guardado para archivo: {}", archivo.getOriginalFilename());

        if (archivo.isEmpty()) {
            logger.warn("Intento de guardar archivo vacío");
            throw new EmptyFileException("El archivo no puede estar vacío");
        }

        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.trim().isEmpty()) {
            logger.warn("Intento de guardar archivo sin nombre");
            throw new InvalidFileNameException("El archivo debe tener un nombre válido");
        }

        nombreOriginal = nombreOriginal.toLowerCase();
        String tipo = archivo.getContentType();
        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            logger.warn("Tipo de archivo no permitido: {}", tipo);
            throw new InvalidFileTypeException(
                    "Tipo de archivo no permitido: " + tipo +
                    ". Tipos permitidos: PNG, JPG, JPEG, PDF, MP3, MP4"
            );
        }

        if (!(nombreOriginal.endsWith(".png") ||
                nombreOriginal.endsWith(".jpg") ||
                nombreOriginal.endsWith(".jpeg") ||
                nombreOriginal.endsWith(".pdf") ||
                nombreOriginal.endsWith(".mp3") ||
                nombreOriginal.endsWith(".mp4"))) {

            logger.warn("Extensión no permitida: {}", nombreOriginal);
            throw new InvalidFileTypeException(
                    "Extensión no permitida. Permitidas: .png, .jpg, .jpeg, .pdf, .mp3, .mp4"
            );
        }
        if (archivo.getSize() > MAX_FILE_SIZE) {
            logger.warn("Archivo demasiado grande: {} bytes. Límite: {}", archivo.getSize(), MAX_FILE_SIZE);
            throw new FileTooLargeException("El archivo excede el tamaño máximo permitido de 15 MB");
        }

        Path uploadPath = Paths.get(UPLOAD_DIR);
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            logger.error("Error creando directorio {}", UPLOAD_DIR, e);
            throw new FileStorageException("Error al crear directorio de almacenamiento", e);
        }

        File destino = null;
        try {
            String nombreGuardado = System.currentTimeMillis() + "_" + nombreOriginal;

            destino = new File(UPLOAD_DIR + nombreGuardado);
            archivo.transferTo(destino);

            String urlPublica = "/uploads/" + nombreGuardado;

            MultimediaEntity m = new MultimediaEntity();
            m.setUrl(urlPublica);
            m.setTipoArchivo(tipo);

            return multimediaRepository.save(m);

        } catch (IOException e) {
            logger.error("Error procesando archivo", e);

            if (destino != null && destino.exists()) {
                destino.delete();
            }

            throw new FileStorageException("Error al procesar el archivo", e);

        } catch (Exception e) {

            if (destino != null && destino.exists()) {
                destino.delete();
            }

            throw new FileStorageException("Error inesperado al procesar archivo", e);
        }
    }

    @Override
    public List<MultimediaEntity> findAll() {
        return multimediaRepository.findAll();
    }

    @Override
    public MultimediaResponseDTO obtenerImagen(Long id) {
        MultimediaEntity m = multimediaRepository.findMultimediaEntityById(id);

        if (m == null) {
            throw new MultimediaNotFoundException("Multimedia no encontrado con ID: " + id);
        }

        MultimediaResponseDTO dto = new MultimediaResponseDTO();
        dto.setId(m.getId());
        dto.setUrl(m.getUrl());
        dto.setTipoArchivo(m.getTipoArchivo());
        return dto;
    }

    public static class FileTooLargeException extends RuntimeException {
        public FileTooLargeException(String msg) { super(msg); }
    }
}
