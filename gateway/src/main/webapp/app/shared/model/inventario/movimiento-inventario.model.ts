import { type IInventario } from '@/shared/model/inventario/inventario.model';

import { type TipoMovimiento } from '@/shared/model/enumerations/tipo-movimiento.model';
export interface IMovimientoInventario {
  id?: number;
  tipoMovimiento?: keyof typeof TipoMovimiento;
  cantidad?: number;
  fecha?: Date;
  observacion?: string | null;
  inventario?: IInventario | null;
}

export class MovimientoInventario implements IMovimientoInventario {
  constructor(
    public id?: number,
    public tipoMovimiento?: keyof typeof TipoMovimiento,
    public cantidad?: number,
    public fecha?: Date,
    public observacion?: string | null,
    public inventario?: IInventario | null,
  ) {}
}
