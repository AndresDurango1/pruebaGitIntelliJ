package com.example.Quidpro.Quidpro.Servicios;
import com.example.Quidpro.Quidpro.Entidades.ImagenesUsuario;
import com.example.Quidpro.Quidpro.Excepciones.InvalidDataException;
import com.example.Quidpro.Quidpro.Repositorios.ImagenesUsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ImagenesUsuarioServicio {
    private static final String RUTA_BASE = "src/main/resources/static/images/imagesUsuarios/";
    private final ImagenesUsuarioRepositorio imagenesUsuarioRepositorio;
    @Autowired
    public ImagenesUsuarioServicio(ImagenesUsuarioRepositorio imagenesUsuarioRepositorio) {
        this.imagenesUsuarioRepositorio = imagenesUsuarioRepositorio;
    }
    //Metodo auxiliar para obtener la extension
    private String getExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        return lastIndex == -1 ? "" : filename.substring(lastIndex + 1);
    }
    // Metodo para Crear Registros
    public ImagenesUsuario guardarImagenes(MultipartFile archivo) {
        try {
            if (archivo.isEmpty()) {
                throw new InvalidDataException("El archivo se encuentra vacío");
            }
            String titulo = UUID.randomUUID().toString() + "." + getExtension(archivo.getOriginalFilename());
            Path path = Paths.get(RUTA_BASE);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path rutaDestino = path.resolve(titulo);
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
            ImagenesUsuario imagenesUsuario = new ImagenesUsuario();
            imagenesUsuario.setTitulo(titulo);
            imagenesUsuario.setUrl_imagenUsuario("imagesUsuarios/" + titulo);
            return imagenesUsuarioRepositorio.save(imagenesUsuario);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen de perfil: ", e);
        }
    }
    // Metodo para Consultar todos los Registros
    public List<ImagenesUsuario> consultarImagenes() {
        return imagenesUsuarioRepositorio.findAll();
    }
    // Metodo para Consultar un Registro por id
    public ImagenesUsuario consultarImagenPorId(int id) {
        return imagenesUsuarioRepositorio.findById(id)
                .orElseThrow(() -> new InvalidDataException("Imagen no encontrada con ID: " + id));
    }
    public ImagenesUsuario actualizarImagen(int id, MultipartFile archivo) {
        ImagenesUsuario imagenExistente = consultarImagenPorId(id);
        try {
            // Eliminar el archivo físico antiguo
            Path pathArchivoAntiguo = Paths.get(RUTA_BASE + imagenExistente.getTitulo());
            Files.deleteIfExists(pathArchivoAntiguo);

            // Crear y guardar la nueva imagen
            String titulo = UUID.randomUUID().toString() + "." + getExtension(archivo.getOriginalFilename());
            Path path = Paths.get(RUTA_BASE);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path rutaDestino = path.resolve(titulo);
            Files.copy(archivo.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

            // Actualizar campos del registro
            imagenExistente.setTitulo(titulo);
            imagenExistente.setUrl_imagenUsuario("imagesUsuarios/" + titulo);
        } catch (IOException e) {
            throw new RuntimeException("Error al actualizar la imagen: " + e.getMessage(), e);
        }
        return imagenesUsuarioRepositorio.save(imagenExistente);
    }
    // Metodo para Eliminar un Registro por Id
    public String eliminarImagen(int id) {
        ImagenesUsuario imagenEliminar = consultarImagenPorId(id);
        try {
            Path pathArchivo = Paths.get(RUTA_BASE + imagenEliminar.getTitulo());
            Files.deleteIfExists(pathArchivo);
            imagenesUsuarioRepositorio.delete(imagenEliminar);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo de la imagen", e);
        }
        return "Imagen Eliminada con éxito";
    }
}
