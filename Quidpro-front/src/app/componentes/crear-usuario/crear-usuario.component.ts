import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { DepartamentoService } from '../../services/departamento.service';
import { CiudadService } from '../../services/ciudad.service';
import { RolService } from '../../services/rol.service';

@Component({
  selector: 'app-crear-usuario',
  standalone: false,
  templateUrl: './crear-usuario.component.html',
  styleUrls: ['./crear-usuario.component.css']
})
export class CrearUsuarioComponent implements OnInit {
  usuarioForm: FormGroup;
  departamentos: any[] = [];
  ciudades: any[] = [];
  roles: any[] = [];
  imagen: File[] = [];
  previews: string[] = [];
  selectedFile: File | null = null;
  constructor(private fb: FormBuilder, private usuarioService: UsuarioService, private departamentoService: DepartamentoService, private ciudadService: CiudadService, private rolService: RolService)
  {
    this.usuarioForm = this.fb.group({
      nombres: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('^[A-Za-z]+( [A-Za-z]+)*$')]],
      apellidos: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('^[A-Za-z]+( [A-Za-z]+)*$')]],
      usuario: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('[a-zA-Z0-9]+')]],
      contrasena: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('[A-Za-z0-9\s\W]+')]],
      identificacion: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), Validators.pattern('[0-9]+')]],
      direccion: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('[A-Za-z0-9\W]+')]],
      correo: ['', [Validators.required, Validators.email, Validators.pattern('[a-zAA-Z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$')]],
      telefono: ['', [Validators.required, Validators.minLength(7), Validators.maxLength(10), Validators.pattern('^[0-9]{7,10}$')]],
      departamento: ['', Validators.required],
      ciudad: ['', Validators.required],
      rol: ['', Validators.required],
      imagen: ['', Validators.required]
    });
    console.log(this.usuarioForm);
  }


  ngOnInit(): void {
    this.cargarDepartamentos();
    this.cargarRoles();
  }
  cargarDepartamentos() {
    this.departamentoService.getDepartamentos().subscribe((data) => {
      console.log('Departamentos:', data);
      this.departamentos = data;
    });
  }
  onDepartamentoChange() {
    const departamentoId = this.usuarioForm.get('departamento')?.value;
    this.cargarCiudades(departamentoId);
    console.log(this.usuarioForm);
  }
  cargarCiudades(departamentoId: number) {
    console.log('Cargando ciudades para el departamento:', departamentoId);
    this.ciudadService.getCiudadesPorDepartamento(departamentoId).subscribe(
      (data) => {
        console.log('Ciudades recibidas:', data);
        this.ciudades = data;
      },
      (error) => {
        console.error('Error al cargar las ciudades:', error);
      }
    );
  }
  cargarRoles() {
    this.rolService.getRoles().subscribe((data) => {
      console.log('Roles:', data);
      this.roles = data;
    });
  }
  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0];
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
      this.imagen = validFiles;
      this.previews = previews;
      console.log('Imágenes válidas seleccionadas:', this.imagen);
    }
  }
  crearUsuario() {
    if (this.usuarioForm.valid) {
      const formData = new FormData();
      formData.append('nombres', this.usuarioForm.get('nombres')?.value);
      formData.append('apellidos', this.usuarioForm.get('apellidos')?.value);
      formData.append('usuario', this.usuarioForm.get('usuario')?.value);
      formData.append('contrasena', this.usuarioForm.get('contrasena')?.value);
      formData.append('identificacion', this.usuarioForm.get('identificacion')?.value);
      formData.append('direccion', this.usuarioForm.get('direccion')?.value);
      formData.append('correo', this.usuarioForm.get('correo')?.value);
      formData.append('telefono', this.usuarioForm.get('telefono')?.value);
      formData.append('idCiudad', this.usuarioForm.get('ciudad')?.value);
      formData.append('idRol', this.usuarioForm.get('rol')?.value);
      if (this.selectedFile) {
        formData.append('imagen', this.selectedFile);
      }
      console.log('Form Data:', formData);
      this.usuarioService.createUsuario(formData).subscribe(
        (response) => {
          console.log('Usuario creado:', response);
          alert("Usuario creado con exito");
        },
        (error) => {
          console.error('Error al crear usuario:', error);
          alert("Error al crear el usuario");
        }
      );
    } else {
      console.log('Formulario inválido:', this.usuarioForm);
    }
  }
}
