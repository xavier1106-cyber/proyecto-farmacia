export interface IPaciente {
  id?: number;
  curp?: string;
  nombre?: string;
  fechaNacimiento?: Date;
  sexo?: string;
  numeroSeguroSocial?: string | null;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public curp?: string,
    public nombre?: string,
    public fechaNacimiento?: Date,
    public sexo?: string,
    public numeroSeguroSocial?: string | null,
  ) {}
}
