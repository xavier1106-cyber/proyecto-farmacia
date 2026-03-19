export interface IInventario {
  id?: number;
  claveMedicamento?: string;
  nombre?: string;
  presentacion?: string;
  lote?: string;
  cantidad?: number;
  cantidadMinima?: number;
  fechaCaducidad?: Date;
  ubicacion?: string;
  controlado?: boolean;
}

export class Inventario implements IInventario {
  constructor(
    public id?: number,
    public claveMedicamento?: string,
    public nombre?: string,
    public presentacion?: string,
    public lote?: string,
    public cantidad?: number,
    public cantidadMinima?: number,
    public fechaCaducidad?: Date,
    public ubicacion?: string,
    public controlado?: boolean,
  ) {
    this.controlado = this.controlado ?? false;
  }
}
