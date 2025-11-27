package uni.fis.multimedia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import uni.fis.multimedia.dto.MultimediaResponseDTO;
import uni.fis.multimedia.entity.MultimediaEntity;
import uni.fis.multimedia.repository.MultimediaRepository;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;


@Service
@Transactional
class MultimediaServiceImpl implements MultimediaService {

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

            return result != null && result.contains("OK");

        } catch (Exception e) {
            throw new RuntimeException("Error al escanear con ClamAV", e);
        }
    }


    @Override
    public MultimediaEntity guardarArchivo(MultipartFile archivo) throws IOException {

        // VALIDAR MIME TYPE
        String tipo = archivo.getContentType();
        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            throw new RuntimeException("Tipo de archivo no permitido: " + tipo);
        }

        // VALIDAR EXTENSIÓN
        String nombreOriginal = archivo.getOriginalFilename().toLowerCase();
        if (!(nombreOriginal.endsWith(".png") ||
                nombreOriginal.endsWith(".jpg") ||
                nombreOriginal.endsWith(".jpeg") ||
                nombreOriginal.endsWith(".pdf") ||
                nombreOriginal.endsWith(".mp3") ||
                nombreOriginal.endsWith(".mp4"))) {
            throw new RuntimeException("Extensión no permitida");
        }

        // ESCANEAR CON CLAMAV
        byte[] bytes = archivo.getBytes();
        if (!escanear(bytes)) {
            throw new RuntimeException("El archivo contiene un virus y fue bloqueado");
        }

        String nombreGuardado = System.currentTimeMillis() + "_" + nombreOriginal;

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        File destino = new File(UPLOAD_DIR + nombreGuardado);
        archivo.transferTo(destino);

        String urlPublica = "/uploads/" + nombreGuardado;

        // GUARDAR EN BD
        MultimediaEntity m = new MultimediaEntity();
        m.setUrl(urlPublica);
        m.setTipoArchivo(tipo);

        return multimediaRepository.save(m);
    }


    @Override
    public List<MultimediaEntity> findAll() {
        return multimediaRepository.findAll();
    }

    @Override
    public MultimediaResponseDTO obtenerImagen(Long id) {
        MultimediaEntity multimedia = multimediaRepository.findMultimediaEntityById(id);

        if (multimedia == null) {
            throw new RuntimeException("Multimedia not found");
        }

        MultimediaResponseDTO dto = new MultimediaResponseDTO();
        dto.setId(multimedia.getId());
        dto.setUrl(multimedia.getUrl());
        dto.setTipoArchivo(multimedia.getTipoArchivo());

        return dto;
    }

}