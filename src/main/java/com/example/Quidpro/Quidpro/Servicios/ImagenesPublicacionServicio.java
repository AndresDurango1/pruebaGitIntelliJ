package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Repositorios.ImagenesPublicacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ImagenesPublicacionServicio {
    private static final String RUTA_BASE = "src/main/resources/static/images/imagesPublicaciones/";
    private final ImagenesPublicacionRepositorio imagenesPublicacionRepositorio;
    @Autowired
    public ImagenesPublicacionServicio(ImagenesPublicacionRepositorio imagenesPublicacionRepositorio) {
        this.imagenesPublicacionRepositorio = imagenesPublicacionRepositorio;
    }
    // Metodo para guardar múltiples imágenes para una publicación
    public List<ImagenesPublicacion> guardarImagenes(MultipartFile[] archivos) {
        if (archivos == null || archivos.length == 0) {
            throw new InvalidDataException("No se proporcionaron imágenes para guardar");
        }
        try {
            Path path = Paths.get(RUTA_BASE);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            List<ImagenesPublicacion> imagenesGuardadas = new ArrayList<>();
            for (MultipartFile archivo : archivos) {
                if (!archivo.isEmpty()) {
                    String titulo = UUID.randomUUID().toString() + "." + getExtension(archivo.getOriginalFilename());
                    Path rutaDestino = path.resolve(titulo);
                    Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
                    ImagenesPublicacion imagen = new ImagenesPublicacion();
                    imagen.setTitulo(titulo);
                    imagen.setUrl_imagenPublicacion("imagesPublicaciones/" + titulo);
                }
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
    // Metodo para Consultar todos los Registros
    public List<ImagenesPublicacion> consultarImagenes() {
        return imagenesPublicacionRepositorio.findAll();
    }
    // Metodo para Consultar un Registro por id
    public ImagenesPublicacion consultarImagenPorId(int id) {
        return imagenesPublicacionRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
    }
    //Metodo para actualizar una imagen de la publicacion
    public ImagenesPublicacion actualizarImagen(int id, MultipartFile archivo) {
        // Buscar la imagen por ID
        ImagenesPublicacion imagenExistente = imagenesPublicacionRepositorio.findById(id)
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
                imagenExistente.setUrl_imagenPublicacion("imagesPublicaciones/" + nuevoTitulo);
            }

            // Guardar la imagen actualizada
            return imagenesPublicacionRepositorio.save(imagenExistente);
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar la imagen", e);
        }
    }
    // Metodo para Eliminar un Registro por Id
    public String eliminarImagen(int id) {
        ImagenesPublicacion imagenAEliminar = consultarImagenPorId(id);
        // Eliminar el archivo físico del sistema de archivos
        try {
            Path pathArchivo = Paths.get(RUTA_BASE + imagenAEliminar.getTitulo());
            Files.deleteIfExists(pathArchivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo de la imagen", e);
        }
        imagenesPublicacionRepositorio.delete(imagenAEliminar);
        return "Imagen Eliminada de manera éxitosa";
    }
}
