export interface IMedico {
  id?: number;
  nombre?: string;
  especialidad?: string;
  telefono?: string | null;
  turno?: string | null;
  activo?: boolean;
}

export class Medico implements IMedico {
  constructor(
    public id?: number,
    public nombre?: string,
    public especialidad?: string,
    public telefono?: string | null,
    public turno?: string | null,
    public activo?: boolean,
  ) {
    this.activo = this.activo ?? false;
  }
}
