import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './componentes/layout/layout.component';
import { HomeComponent } from './componentes/home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { EmprendimientoComponent } from './componentes/emprendimiento/emprendimiento.component';
import { CrearEmprendimientoComponent } from './componentes/crear-emprendimiento/crear-emprendimiento.component';
import { UsuarioComponent } from './componentes/usuario/usuario.component';
import { CrearUsuarioComponent } from './componentes/crear-usuario/crear-usuario.component';
import { PublicacionesComponent } from './componentes/publicaciones/publicaciones.component';
import { CrearPublicacionComponent } from './componentes/crear-publicacion/crear-publicacion.component';

const routes: Routes = [
  {
    path: '', component:LayoutComponent,
    children: [
      {
        path: '', component: HomeComponent
      },
      {
        path: 'emprendimientos', component: EmprendimientoComponent
      },
      {
        path:'usuarios', component: UsuarioComponent
      },
      {
        path: 'publicaciones', component: PublicacionesComponent
      },
      {
        path: 'crear-usuario', component: CrearUsuarioComponent
      },
      {
        path: 'crear-emprendimiento', component: CrearEmprendimientoComponent
      },
      {
        path: 'crear-publicacion', component: CrearPublicacionComponent
      },
      {
        path:'iniciar-sesion', component: LoginComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
