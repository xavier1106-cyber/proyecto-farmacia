export interface IHistorico {
  id?: number;
  fechaEmision?: Date;
  folio?: string;
  pacienteId?: number;
  pacienteNombre?: string;
  pacienteCurp?: string | null;
  medicoId?: number;
  medicoNombre?: string | null;
  medicoEspecialidad?: string | null;
  usuarioQueRegistro?: string;
  medicamentos?: string;
  autorizo?: string | null;
  observaciones?: string | null;
  cantidad?: number | null;
}

export class Historico implements IHistorico {
  constructor(
    public id?: number,
    public fechaEmision?: Date,
    public folio?: string,
    public pacienteId?: number,
    public pacienteNombre?: string,
    public pacienteCurp?: string | null,
    public medicoId?: number,
    public medicoNombre?: string | null,
    public medicoEspecialidad?: string | null,
    public usuarioQueRegistro?: string,
    public medicamentos?: string,
    public autorizo?: string | null,
    public observaciones?: string | null,
    public cantidad?: number | null,
  ) {}
}
