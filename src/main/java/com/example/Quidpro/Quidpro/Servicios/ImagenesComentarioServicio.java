package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.ImagenesComentario;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Repositorios.ImagenesComentarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImagenesComentarioServicio {
    private static final String RUTA_BASE = "src/main/resources/static/images/imagesComentarios/";
    private final ImagenesComentarioRepositorio imagenesComentarioRepositorio;

    @Autowired
    public ImagenesComentarioServicio(ImagenesComentarioRepositorio imagenesComentarioRepositorio) {
        this.imagenesComentarioRepositorio = imagenesComentarioRepositorio;
    }
    // Metodo para guardar múltiples imágenes para un Comentario
    public List<ImagenesComentario> guardarImagenes(MultipartFile[] archivos) {
        if (archivos == null || archivos.length == 0) {
            throw new InvalidDataException("No se proporcionaron imágenes para guardar");
        }
        try {
            Path path = Paths.get(RUTA_BASE);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            List<ImagenesComentario> imagenesGuardadas = new ArrayList<>();
            for (MultipartFile archivo : archivos) {
                if (archivo.isEmpty()) {
                    continue;  // No procesar archivos vacíos
                }
                String titulo = UUID.randomUUID().toString() + "." + getExtension(archivo.getOriginalFilename());
                Path rutaDestino = path.resolve(titulo);
                Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
                ImagenesComentario imagen = new ImagenesComentario();
                imagen.setTitulo(titulo);
                imagen.setUrl_imagenComentario("imagesComentarios/" + titulo);
                imagenesGuardadas.add(imagen);
            }
            if (!imagenesGuardadas.isEmpty()) {
                imagenesComentarioRepositorio.saveAll(imagenesGuardadas);
            }
            return imagenesGuardadas;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las imágenes de la publicación: ", e);
        }
    }
    private String getExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        return lastIndex == -1 ? "" : filename.substring(lastIndex + 1);
    }
    //Metodo para actualizar una imagen de la publicacion
    public ImagenesComentario actualizarImagen(int id, MultipartFile archivo) {
        // Buscar la imagen por ID
        ImagenesComentario imagenExistente = imagenesComentarioRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));

        try {
            // Eliminar el archivo físico antiguo
            Path pathArchivoAntiguo = Paths.get(RUTA_BASE + imagenExistente.getTitulo());
            Files.deleteIfExists(pathArchivoAntiguo);

            // Guardar el nuevo archivo
            if (archivo != null && !archivo.isEmpty()) {
                String nuevoTitulo = UUID.randomUUID().toString() + "." + getExtension(archivo.getOriginalFilename());
                Path rutaDestino = Paths.get(RUTA_BASE).resolve(nuevoTitulo);
                Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

                // Actualizar los campos de la imagen
                imagenExistente.setTitulo(nuevoTitulo);
                imagenExistente.setUrl_imagenComentario("imagesPublicaciones/" + nuevoTitulo);
            }
            // Guardar la imagen actualizada
            return imagenesComentarioRepositorio.save(imagenExistente);
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar la imagen", e);
        }
    }
    // Metodo para Consultar todos los Registros
    public List<ImagenesComentario> consultarImagenes() {
        return imagenesComentarioRepositorio.findAll();
    }
    // Metodo para Consultar un Registro por id
    public ImagenesComentario consultarImagenPorId(int id) {
        return imagenesComentarioRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
    }
    // Metodo para Eliminar un Registro por Id
    public String eliminarImagen(int id) {
        ImagenesComentario imagenEliminar = consultarImagenPorId(id);
        try {
            Path pathArchivo = Paths.get(RUTA_BASE + imagenEliminar.getTitulo());
            Files.deleteIfExists(pathArchivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo de la imagen", e);
        }
        imagenesComentarioRepositorio.delete(imagenEliminar);
        return "Imagen eliminada de manera éxitosa";
    }
}
