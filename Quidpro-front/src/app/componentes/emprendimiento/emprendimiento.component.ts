import { Component } from '@angular/core';
import { EmprendimientoService } from '../../services/emprendimiento.service';
import { Emprendimiento } from '../../modelos/emprendimiento';

@Component({
  selector: 'app-emprendimiento',
  standalone: false,
  templateUrl: './emprendimiento.component.html',
  styleUrl: './emprendimiento.component.css'
})
export class EmprendimientoComponent {
  emprendimientos: Emprendimiento[] = [];

  constructor(private emprendimientoService: EmprendimientoService) {}
  ngOnInit():void {
    this.emprendimientoService.getEmprendimientos().subscribe((data) => (this.emprendimientos = data));
    console.log(this.emprendimientos);
  }


}
