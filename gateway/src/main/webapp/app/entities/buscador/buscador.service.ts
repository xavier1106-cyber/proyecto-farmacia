import axios from "axios";

const pacienteApi = "services/paciente/api/pacientes";
const medicoApi = "services/medico/api/medicos";
const inventarioApi = "services/inventario/api/inventarios";

export default class BuscadorService {

  async buscarMedicamentos(query: string): Promise<any[]> {
    try {
      //Añadimos size=2000 para que no se detenga en los primeros 20 registros de la DB
      const res = await axios.get(`${inventarioApi}?size=2000`);
      const data = res.data || [];

      //  Si no hay texto en el buscador, devolvemos los 40 (o todos los que existan)
      if (!query) return data;

      return data.filter((med: any) =>
        med.id.toString().includes(query) ||
        (med.nombre && med.nombre.toLowerCase().includes(query.toLowerCase()))
      );
    } catch (error) {
      console.error("Error cargando medicamentos:", error);
      return [];
    }
  }

  // Se aplica la misma lógica para pacientes y médicos para que también aparezcan todos
  async buscarPacientes(query: string): Promise<any[]> {
    const res = await axios.get(`${pacienteApi}?size=2000`);
    if (!query) return res.data;
    return res.data.filter((p: any) => 
      p.id.toString().includes(query) || 
      (p.nombre && p.nombre.toLowerCase().includes(query.toLowerCase()))
    );
  }

  async buscarMedicos(query: string): Promise<any[]> {
    const res = await axios.get(`${medicoApi}?size=2000`);
    if (!query) return res.data;
    return res.data.filter((m: any) => 
      m.id.toString().includes(query) || 
      (m.nombre && m.nombre.toLowerCase().includes(query.toLowerCase()))
    );
  }
}