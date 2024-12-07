package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.ImagenesPublicacion;
import com.example.Quidpro.Quidpro.Entidades.Publicacion;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Repositorios.ImagenesPublicacionRepositorio;
import jakarta.transaction.Transactional;
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
    // Metodo para consultar una imagen por su ID
    public ImagenesPublicacion consultarImagenPorId(int id) {
        return imagenesPublicacionRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
    }
    // Metodo para consultar todas las imágenes
    public List<ImagenesPublicacion> consultarImagenes() {
        return imagenesPublicacionRepositorio.findAll();
    }
    // Metodo para guardar una nueva imagen
    public List<ImagenesPublicacion> guardarImagenes(MultipartFile[] archivos, Publicacion publicacion) {
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
                    // Generar un título único para evitar conflictos de nombres
                    //String titulo = "publicacion_"+publicacion.getId()+"_"+archivo.getOriginalFilename(); /*nombre original de la imagen*/
                    String titulo = UUID.randomUUID().toString() + "." + getExtension(archivo.getOriginalFilename()); /*nombre aleatorio*/
                    // Ruta de destino para guardar la imagen
                    Path rutaDestino = path.resolve(titulo);
                    Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
                    // Crear la entidad `ImagenesPublicacion`
                    ImagenesPublicacion imagen = new ImagenesPublicacion();
                    imagen.setTitulo(titulo);
                    imagen.setUrl_imagenPublicacion("imagesPublicaciones/" + titulo);
                    // Asociar la imagen con la publicación
                    imagen.setPublicacion(publicacion);
                    // Guardar la imagen en la base de datos
                    imagen = imagenesPublicacionRepositorio.save(imagen);
                    imagenesGuardadas.add(imagen);
                }
            }
            return imagenesGuardadas;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las imágenes: ", e);
        }
    }
    // Metodo para actualizar una imagen existente
    public ImagenesPublicacion actualizarImagen(int id, MultipartFile archivo, Publicacion publicacion) {
        ImagenesPublicacion imagenExistente = imagenesPublicacionRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
        try {
            // Eliminar el archivo físico antiguo
            Path pathArchivoAntiguo = Paths.get(RUTA_BASE + imagenExistente.getTitulo());
            Files.deleteIfExists(pathArchivoAntiguo);
            // Guardar el nuevo archivo
            if (archivo != null && !archivo.isEmpty()) {
                String nuevoTitulo = "publicacion_"+publicacion.getId()+"_"+archivo.getOriginalFilename();
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
    // Metodo para eliminar varias imágenes
    @Transactional
    public void eliminarImagen(List<ImagenesPublicacion> imagenes) {
        for (ImagenesPublicacion imagen : imagenes) {
            eliminarImagenPorId(imagen.getId());
        }
    }
    private void eliminarImagenPorId(int id) {
        ImagenesPublicacion imagen = imagenesPublicacionRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
        System.out.println("Eliminando imagen: " + imagen);
        try {
            // Eliminar archivo del sistema
            Path pathArchivo = Paths.get(RUTA_BASE + imagen.getTitulo());
            Files.deleteIfExists(pathArchivo);
            // Verificar existencia antes de eliminar
            if (imagenesPublicacionRepositorio.existsById(id)) {
                imagenesPublicacionRepositorio.deleteById(id);
                System.out.println("Imagen eliminada con ID: " + id);
            } else {
                System.out.println("La imagen ya no existe en la base de datos.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen", e);
        }
    }
    // Metodo auxiliar para obtener la extensión del archivo
    private String getExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        return lastIndex == -1 ? "" : filename.substring(lastIndex + 1);
    }
}
