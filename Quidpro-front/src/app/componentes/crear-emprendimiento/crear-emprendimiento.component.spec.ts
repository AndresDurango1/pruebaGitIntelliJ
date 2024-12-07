import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrearEmprendimientoComponent } from './crear-emprendimiento.component';

describe('CrearEmprendimientoComponent', () => {
  let component: CrearEmprendimientoComponent;
  let fixture: ComponentFixture<CrearEmprendimientoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CrearEmprendimientoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CrearEmprendimientoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
