import { defineComponent, nextTick } from "vue";
import BuscadorService from "./buscador.service";
import HistoricoService from "@/entities/historico/historico/historico.service";
import InventarioService from "@/entities/inventario/inventario/inventario.service";
//traemos el service de movimiento
import MovimientoInventarioService from "@/entities/inventario/movimiento-inventario/movimiento-inventario.service";

const buscarService = new BuscadorService();
const historicoService = new HistoricoService();
const inventarioService = new InventarioService();

//creamos la estancia del servicio
const movimientoInventarioService = new MovimientoInventarioService();




export default defineComponent({
  name: "Buscador",
  data() {
    return {
      // Queries de búsqueda principal
      pacienteQuery: "",
      medicoQuery: "",
      medicamentoQuery: "",

      // Resultados de búsqueda principal
      pacientes: [] as any[],
      medicos: [] as any[],
      medicamentos: [] as any[],

      // Objeto de receta actual
      receta: {
        pacienteId: null as number | null,
        pacienteNombre: "",
        edad: "" as string | number,
        sexo: "",
        medicoId: null as number | null,
        medicoNombre: "",
        especialidad: "",
        medicamentos: [] as any[],
        tipo: "Normal",
        fechaEmision: new Date().toLocaleDateString(),
        estado: "Borrador"
      },

      // Modal de Autorización Admin
      mostrarModalAdmin: false,
      adminCredentials: {
        usuario: "",
        password: ""
      }
    };
  },
  methods: {
    // --- LÓGICA DE BÚSQUEDA Y SELECCIÓN ---
    async buscarPacientes() {
      this.pacientes = await buscarService.buscarPacientes(this.pacienteQuery);
    },

    async buscarMedicos() {
      this.medicos = await buscarService.buscarMedicos(this.medicoQuery);
    },

    async buscarMedicamentos() {
      this.medicamentos = await buscarService.buscarMedicamentos(this.medicamentoQuery);
    },

    seleccionarPaciente(p: any) {
      this.receta.pacienteId = p.id;
      this.receta.pacienteNombre = `${p.nombre} ${p.apellidoP || ''}`;
      this.receta.edad = p.edad || "N/A";
      this.receta.sexo = p.sexo || "N/A";
      this.pacientes = [];
      this.pacienteQuery = "";
    },

    seleccionarMedico(m: any) {
      this.receta.medicoId = m.id;
      this.receta.medicoNombre = m.nombre;
      this.receta.especialidad = m.especialidad || "General";
      this.medicos = [];
      this.medicoQuery = "";
    },

    seleccionarMedicamento(m: any) {
      this.receta.medicamentos.push({
        medicamentoId: m.id,
        nombre: m.nombre,
        cantidad: 1,
        stock: m.cantidad || 0,
        controlado: m.controlado || false,
        indicaciones: "",
        editando: false,
        _uid: Math.random().toString(36).substr(2, 9)
      });
      this.medicamentos = [];
      this.medicamentoQuery = "";
    },

    // --- GESTIÓN DE TABLA DE MEDICAMENTOS ---
    cerrarListas() {
      this.pacientes = [];
      this.medicos = [];
      this.medicamentos = [];
    },

    eliminarMedicamento(index: number) {
      this.receta.medicamentos.splice(index, 1);
    },

    editarMedicamento(index: number) {
      this.receta.medicamentos[index].editando = true;
    },

    guardarEdicion(index: number) {
      this.receta.medicamentos[index].editando = false;
    },

    validarStock(index: number) {
      const med = this.receta.medicamentos[index];
      if (med.cantidad > med.stock) {
        alert(`Atención: La cantidad solicitada de ${med.nombre} supera el stock disponible (${med.stock}).`);
        med.cantidad = med.stock;
      }
    },

    // --- PROCESO DE GUARDADO E IMPRESIÓN ---
    imprimirBorrador() {
      window.print();
    },

    async registrarHistorico() {
      const tieneControlados = this.receta.medicamentos.some(m => m.controlado);
      if (tieneControlados) {
        this.mostrarModalAdmin = true;
      } else {
        await this.ejecutarGuardadoFinal();
      }
    },

    async validarYAutorizarAdmin() {
      if (this.adminCredentials.usuario === "admin" && this.adminCredentials.password === "admin") {
        this.mostrarModalAdmin = false;
        await this.ejecutarGuardadoFinal(this.adminCredentials.usuario);
      } else {
        alert("Credenciales de administrador no válidas.");
      }
    },

    async ejecutarGuardadoFinal(adminAutorizador?: string) {
      try {
        const usuarioSesion = localStorage.getItem("usuario") || "usuario_anonimo";
        const autorizo = adminAutorizador || usuarioSesion;

        const listaMedsParaHistorico = this.receta.medicamentos.map(med => ({
          id: med.medicamentoId,
          nombre: med.nombre,
          cantidad: med.cantidad,
          controlado: med.controlado,
          observaciones: med.indicaciones 
        }));

        const dataHistorico = {
          fechaEmision: new Date().toISOString(),
          folio: `FOL-${Date.now()}`,
          pacienteId: this.receta.pacienteId,
          pacienteNombre: this.receta.pacienteNombre,
          medicoId: this.receta.medicoId,
          medicoNombre: this.receta.medicoNombre,
          medicoEspecialidad: this.receta.especialidad, 
          usuarioQueRegistro: usuarioSesion,
          autorizo: autorizo,
          medicamentos: JSON.stringify(listaMedsParaHistorico), 
          cantidad: listaMedsParaHistorico.reduce((acc, cur) => acc + (Number(cur.cantidad) || 0), 0),
          observaciones: `Receta con ${listaMedsParaHistorico.length} productos.`
        };

        // 1. Guardar Histórico
        await historicoService.create(dataHistorico);

        // 2. Descontar Inventario
        for (const med of this.receta.medicamentos) {

          if (!med.medicamentoId) continue;

          //Traer inventario
          const inv = await inventarioService.find(med.medicamentoId);

          const stockAnterior = inv.cantidad;

          // Descontar inventario
          inv.cantidad = (inv.cantidad || 0) - med.cantidad;
          await inventarioService.update(inv);

          //Crear movimiento
          const movimiento = {
            tipoMovimiento: "SALIDA",                  // TipoMovimiento enum
            cantidad: med.cantidad,                     // Cantidad que se descuenta
            fecha: new Date().toISOString().split('T')[0], // LocalDate como YYYY-MM-DD
            observacion: `Salida por receta ${dataHistorico.folio}`, // tu referencia
            inventario: { id: med.medicamentoId }       // ManyToOne relación con Inventario
          };

          await movimientoInventarioService.create(movimiento);
        }

        alert("Receta registrada con éxito.");
        
        const deseaImprimir = confirm("¿Desea imprimir el comprobante de la receta ahora?");
        if (deseaImprimir) {
          window.print();
        }

        await this.limpiarPantalla();
        
      } catch (error) {
        console.error("Error en el proceso de guardado:", error);
        alert("Ocurrió un error al procesar la receta.");
      }
    },

    async limpiarPantalla() {
      await nextTick();
      this.pacienteQuery = "";
      this.medicoQuery = "";
      this.medicamentoQuery = "";
      this.receta.pacienteId = null;
      this.receta.pacienteNombre = "";
      this.receta.edad = "";
      this.receta.sexo = "";
      this.receta.medicoId = null;
      this.receta.medicoNombre = "";
      this.receta.especialidad = "";
      this.receta.medicamentos = [];
      this.adminCredentials = { usuario: "", password: "" };
    }
  }
});