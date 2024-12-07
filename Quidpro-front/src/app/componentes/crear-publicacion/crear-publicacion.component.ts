import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../modelos/usuario';
import { PublicacionService } from '../../services/publicacion.service';

@Component({
  selector: 'app-crear-publicacion',
  standalone: false,
  templateUrl: './crear-publicacion.component.html',
  styleUrls: ['./crear-publicacion.component.css']
})
export class CrearPublicacionComponent {
  publicacionForm: FormGroup;
  usuarios: Usuario[] = [];
  imagenes: File[] = [];
  previews: string[] = [];
  constructor(private fb: FormBuilder, private usuarioService: UsuarioService, private publicacionService: PublicacionService)
  {
    this.publicacionForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(50), Validators.pattern(/^[A-Za-z\u00C0-\u00FF0-9\s.,;:!?@#$%&*()_\-+=\/']+$/)]],
      descripcion: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(256), Validators.pattern(/^[A-Za-z\u00C0-\u00FF0-9\s.,;:!?@#$%&*()_\-+=\/']+$/)]],
      fecha_creacion: ['', Validators.required],
      fecha_actualizacion: ['', Validators.required],
      tag: ['', [Validators.required, Validators.pattern(/^(#[A-Za-z\u00C0-\u00FF0-9]+)(\s#[A-Za-z\u00C0-\u00FF0-9]+)*$/)]],
      usuario: ['', Validators.required],
    });
  }
  ngOnInit(): void {
    this.cargarUsuarios();
  }
  cargarUsuarios() {
    this.usuarioService.getUsuarios().subscribe((data) => {
      this.usuarios = data.map((e: any) => ({
        ...e,
        nombreCompleto: `${e.nombres} ${e.apellidos}` // Agrega el campo nombreCompleto
      }));
      console.log('Usuarios procesados:', this.usuarios);
    });
  }
    onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const validFiles: File[] = [];
      const previews: string[] = [];

      Array.from(input.files).forEach((file) => {
        if (file.type.startsWith('image/') && file.size <= 2 * 1024 * 1024) {
          validFiles.push(file);
          previews.push(URL.createObjectURL(file));
        } else {
          console.warn(`Archivo no válido: ${file.name}`);
        }
      });
      this.imagenes = validFiles;
      this.previews = previews;
      console.log('Imágenes válidas seleccionadas:', this.imagenes);
    }
  }
  //Metodo para crear una publicacion en la base de datos
  crearPublicacion() {
    if(this.publicacionForm.valid) {
      console.log('Valores del formulario:', this.publicacionForm.value);
      const formData = new FormData();
      formData.append('titulo', this.publicacionForm.get('titulo')?.value);
      formData.append('descripcion', this.publicacionForm.get('descripcion')?.value);
      formData.append('fecha_creacion', this.publicacionForm.get('fecha_creacion')?.value);
      formData.append('fecha_actualizacion', this.publicacionForm.get('fecha_actualizacion')?.value);
      formData.append('tag', this.publicacionForm.get('tag')?.value);
      formData.append('idUsuario', this.publicacionForm.get('usuario')?.value);
      this.imagenes.forEach((imagen, index) => {
        formData.append('imagenes', imagen);
      });
      this.publicacionService.crearPublicacion(formData).subscribe(
        (response) => {
          console.log('Publicación creada:', response);
          alert('Publicación creada conxito');
        },
        (error) => {
          console.error('Error al crear la publicación:', error);
          alert('Error al crear la publicación');
        }
      )
    }
  }
}
