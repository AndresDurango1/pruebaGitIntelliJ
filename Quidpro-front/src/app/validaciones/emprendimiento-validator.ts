import { AbstractControl, ValidationErrors } from '@angular/forms';

export function atLeastOneSelected(control: AbstractControl): ValidationErrors | null {
  const value = control.value;
  if (Array.isArray(value) && value.length > 0) {
    return null; // Válido
  }
  return { atLeastOne: true }; // Inválido
}
