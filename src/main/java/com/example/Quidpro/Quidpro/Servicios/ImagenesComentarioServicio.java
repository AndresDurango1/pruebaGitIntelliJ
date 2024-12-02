package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.Comentario;
import com.example.Quidpro.Quidpro.Entidades.ImagenesComentario;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Repositorios.ImagenesComentarioRepositorio;
import jakarta.transaction.Transactional;
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


@Service
public class ImagenesComentarioServicio {
    private static final String RUTA_BASE = "src/main/resources/static/images/imagesComentarios/";
    private final ImagenesComentarioRepositorio imagenesComentarioRepositorio;

    @Autowired
    public ImagenesComentarioServicio(ImagenesComentarioRepositorio imagenesComentarioRepositorio) {
        this.imagenesComentarioRepositorio = imagenesComentarioRepositorio;
    }
    // Metodo para Consultar un Registro por id
    public ImagenesComentario consultarImagenPorId(int id) {
        return imagenesComentarioRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
    }
    // Metodo para Consultar todos los Registros
    public List<ImagenesComentario> consultarImagenes() {
        return imagenesComentarioRepositorio.findAll();
    }
    // Metodo para guardar una nueva imágenes en un Comentario
    public List<ImagenesComentario> guardarImagenes(MultipartFile[] archivos, Comentario comentario) {
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
                if (!archivo.isEmpty()) {
                    // Generar un título único para evitar conflictos de nombres
                    String titulo = "comentario_"+comentario.getId()+"_"+archivo.getOriginalFilename();
                    Path rutaDestino = path.resolve(titulo);
                    Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
                    ImagenesComentario imagen = new ImagenesComentario();
                    imagen.setTitulo(titulo);
                    imagen.setUrl_imagenComentario("imagesComentarios/" + titulo);
                    // Asociar la imagen con el comentario
                    imagen.setComentario(comentario);
                    // Guardar la imagen en la base de datos
                    imagen = imagenesComentarioRepositorio.save(imagen);
                    imagenesGuardadas.add(imagen);
                }
            }
            return imagenesGuardadas;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar las imágenes de la publicación: ", e);
        }
    }
    //Metodo para actualizar una imagen de la publicacion
    public ImagenesComentario actualizarImagen(int id, MultipartFile archivo, Comentario comentario) {
        ImagenesComentario imagenExistente = imagenesComentarioRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
        try {
            // Eliminar el archivo físico antiguo
            Path pathArchivoAntiguo = Paths.get(RUTA_BASE + imagenExistente.getTitulo());
            Files.deleteIfExists(pathArchivoAntiguo);
            // Guardar el nuevo archivo
            if (archivo != null && !archivo.isEmpty()) {
                String nuevoTitulo = "comentario"+comentario.getId()+"_"+archivo.getOriginalFilename();
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
    // Metodo para eliminar varias imágenes
    @Transactional
    public void eliminarImagen(List<ImagenesComentario> imagenes) {
        for (ImagenesComentario imagen : imagenes) {
            eliminarImagenPorId(imagen.getId());
        }
    }
    private void eliminarImagenPorId(int id) {
        ImagenesComentario imagen = imagenesComentarioRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
        System.out.println("Eliminando imagen: " + imagen);
        try {
            // Eliminar archivo del sistema
            Path pathArchivo = Paths.get(RUTA_BASE + imagen.getTitulo());
            Files.deleteIfExists(pathArchivo);
            // Verificar existencia antes de eliminar
            if (imagenesComentarioRepositorio.existsById(id)) {
                imagenesComentarioRepositorio.deleteById(id);
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
