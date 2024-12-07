import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { Usuario } from '../../modelos/usuario';

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
  constructor(private fb: FormBuilder, private usuarioService: UsuarioService)
  {
    this.publicacionForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(50)]],
      descripcion: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(256)]],
      fecha_creacion: ['', Validators.required],
      fecha_actualizacion: ['', Validators.required],
      tag: ['', Validators.required],
      usuario: ['', Validators.required],
    });
  }
  ngOnInit(): void {
    this.cargarUsuarios();
  }
  cargarUsuarios() {
    this.usuarioService.getUsuarios().subscribe((data) => {
      this.usuarios = data;
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
}
