import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { atLeastOneSelected } from '../../validaciones/emprendimiento-validator';
import { EmprendimientoService } from '../../services/emprendimiento.service';
import { DepartamentoService } from '../../services/departamento.service';
import { CiudadService } from '../../services/ciudad.service';
import { UsuarioService } from '../../services/usuario.service';
import { SectorService } from '../../services/sector.service';
import { EstadoService } from '../../services/estado.service';


@Component({
  selector: 'app-crear-emprendimiento',
  standalone: false,
  templateUrl: './crear-emprendimiento.component.html',
  styleUrl: './crear-emprendimiento.component.css'
})
export class CrearEmprendimientoComponent implements OnInit {
  emprendimientoForm: FormGroup;

  departamentos: any[] = [];
  ciudades: any[] = [];
  emprendedores: any[] = [];
  sectores: any[] = [];
  estados: any[] = [];
  constructor(private fb: FormBuilder, private emprendimientoService: EmprendimientoService, private departamentoService: DepartamentoService, private ciudadService: CiudadService, private usuariosService: UsuarioService, private sectoresService: SectorService, private estadosService: EstadoService)
  {
    this.emprendimientoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50), Validators.pattern('[A-Za-z\s]+')]],
      descripcion: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(256), Validators.pattern("^[A-Za-z0-9\\s.,;:!?@#$%&*()_\\-+=\\/']+$")]],
      fecha_creacion: ['', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
      fecha_actualizacion: ['', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]],
      departamento: ['', Validators.required],
      ciudad: ['', Validators.required],
      emprendedores: [[], [atLeastOneSelected]],
      sectores: [[], [atLeastOneSelected]],
      estados: [[], [atLeastOneSelected]],
    });
    console.log(this.emprendimientoForm);
  }
  ngOnInit(): void {
    this.cargarDepartamentos();
    this.cargarEmprendedores();
    this.cargarSectores();
    this.cargarEstados();

      // Depuración del estado inicial de los campos
      console.log('Estado de emprendedores:', this.emprendimientoForm.get('emprendedores')?.errors);
      console.log('Estado de sectores:', this.emprendimientoForm.get('sectores')?.errors);
      console.log('Estado de estados:', this.emprendimientoForm.get('estados')?.errors);

        // Monitorear cambios en los valores de los controles
        this.emprendimientoForm.get('emprendedores')?.valueChanges.subscribe(value => {
          console.log('Emprendedores seleccionados:', value);
        });
        this.emprendimientoForm.get('sectores')?.valueChanges.subscribe(value => {
          console.log('Sectores seleccionados:', value);
        });
        this.emprendimientoForm.get('estados')?.valueChanges.subscribe(value => {
          console.log('Estados seleccionados:', value);
        });
  }

  //Metodos auxiliares para cargar información en el formulario de emprendimiento
  cargarDepartamentos() {
    this.departamentoService.getDepartamentos().subscribe((data) => {
      console.log('Departamentos:', data);
      this.departamentos = data;
    });
  }
  onDepartamentoChange() {
    const departamentoId = this.emprendimientoForm.get('departamento')?.value;
    this.cargarCiudades(departamentoId);
    console.log(this.emprendimientoForm);
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
  cargarEmprendedores() {
    this.usuariosService.getUsuarios().subscribe((data) => {
      console.log('Usuarios:', data);
      this.emprendedores = data;
    });
  }
  cargarSectores() {
    this.sectoresService.getSectores().subscribe((data) =>{
      console.log('Sectores:', data);
      this.sectores = data;
    })
  }
  cargarEstados() {
    this.estadosService.getEstados().subscribe((data) =>{
      console.log('Estados:', data);
      this.estados = data;
    })
  }

  //Método para crear un nuevo emprendimiento en la base de datos
  crearEmprendimiento() {
    if (this.emprendimientoForm.valid) {
      const formData = new FormData();
      formData.append('nombre', this.emprendimientoForm.get('nombre')?.value);
      formData.append('descripcion', this.emprendimientoForm.get('descripcion')?.value);
      formData.append('fecha_creacion', this.emprendimientoForm.get('fecha_creacion')?.value);
      formData.append('fecha_actualizacion', this.emprendimientoForm.get('fecha_actualizacion')?.value);
      formData.append('idUsarios', this.emprendimientoForm.get('emprendedores')?.value);
      formData.append('idDepartamento', this.emprendimientoForm.get('departamento')?.value);
      formData.append('idCiudad', this.emprendimientoForm.get('ciudad')?.value);
      formData.append('idSectores', this.emprendimientoForm.get('sectores')?.value);
      formData.append('idEstado', this.emprendimientoForm.get('estados')?.value);
      console.log('Form Data:', formData);
      this.emprendimientoService.createEmprendimiento(formData).subscribe(
        (response) => {
          console.log('Emprendimiento creado:', response);
        },
        (error) => {
          console.error('Error al crear el emprendimiento:', error);
        }
      );
    } else {
      console.log('Formulario inválido:', this.emprendimientoForm);
    }
  }

}

