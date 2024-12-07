import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutComponent } from './componentes/layout/layout.component';
import { HeaderComponent } from './componentes/header/header.component';
import { FooterComponent } from './componentes/footer/footer.component';
import { LoginComponent } from './auth/login/login.component';
import { HomeComponent } from './componentes/home/home.component';
import { EmprendimientoComponent } from './componentes/emprendimiento/emprendimiento.component';
import { CrearEmprendimientoComponent } from './componentes/crear-emprendimiento/crear-emprendimiento.component';
import { UsuarioComponent } from './componentes/usuario/usuario.component';
import { CrearUsuarioComponent } from './componentes/crear-usuario/crear-usuario.component';
import { PublicacionesComponent } from './componentes/publicaciones/publicaciones.component';
import { ComentariosComponent } from './componentes/comentarios/comentarios.component';
import { CrearPublicacionComponent } from './componentes/crear-publicacion/crear-publicacion.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LayoutComponent,
    EmprendimientoComponent,
    UsuarioComponent,
    PublicacionesComponent,
    ComentariosComponent,
    CrearUsuarioComponent,
    CrearEmprendimientoComponent,
    LoginComponent,
    CrearPublicacionComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule,
    NgSelectModule
  ],
  providers: [
    provideClientHydration(withEventReplay())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
