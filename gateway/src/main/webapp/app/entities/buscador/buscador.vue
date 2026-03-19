<template>
  <div class="container-fluid min-vh-100 page-container" @click="cerrarListas">
    <div class="container pt-4 pb-5">
      
      <div class="d-flex justify-content-between align-items-center mb-4 no-print">
        <h2 class="mb-0 color-institucional-dark fw-bold">Buscador General de Recetas</h2>
        
        <router-link to="/historico" class="btn btn-outline-institucional shadow-sm">
          <font-awesome-icon icon="list" class="me-2" /> Ir a Historial de Recetas
        </router-link>
      </div>

      <div class="row no-print">
        <div class="col-md-4">
          <div class="card p-3 shadow-sm buscador-card border-institucional-light" @click.stop>
            <label class="color-institucional-dark"><b>Paciente:</b></label>
            <input
              v-model="pacienteQuery"
              @input="buscarPacientes"
              @focus="buscarPacientes" 
              placeholder="ID o nombre..."
              class="form-control focus-institucional"
              autocomplete="off"
            />
            <ul v-if="pacientes && pacientes.length" class="list-group resultados-dropdown">
              <li class="list-group-item list-group-item-action" v-for="p in pacientes" :key="p.id" @click="seleccionarPaciente(p)">
                {{ p.id }} - {{ p.nombre }} {{ p.apellidoP }}
              </li>
            </ul>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card p-3 shadow-sm buscador-card border-institucional-light" @click.stop>
            <label class="color-institucional-dark"><b>Médico:</b></label>
            <input
              v-model="medicoQuery"
              @input="buscarMedicos"
              @focus="buscarMedicos"
              placeholder="ID o nombre..."
              class="form-control focus-institucional"
              autocomplete="off"
            />
            <ul v-if="medicos && medicos.length" class="list-group resultados-dropdown">
              <li class="list-group-item list-group-item-action" v-for="m in medicos" :key="m.id" @click="seleccionarMedico(m)">
                {{ m.id }} - {{ m.nombre }}
              </li>
            </ul>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card p-3 shadow-sm buscador-card border-institucional-light" @click.stop>
            <label class="color-institucional-dark"><b>Medicamento:</b></label>
            <input
              v-model="medicamentoQuery"
              @input="buscarMedicamentos"
              @focus="buscarMedicamentos"
              placeholder="Escriba para buscar..."
              class="form-control focus-institucional"
              autocomplete="off"
            />
            <ul v-if="medicamentos && medicamentos.length" class="list-group resultados-dropdown">
              <li class="list-group-item list-group-item-action d-flex justify-content-between align-items-center" v-for="m in medicamentos" :key="m.id" @click="seleccionarMedicamento(m)">
                <div>
                  <span class="fw-bold">{{ m.id }} - {{ m.nombre }}</span><br>
                  <small class="text-muted">Stock: {{ m.cantidad }}</small>
                </div>
                <span v-if="m.controlado" class="badge bg-warning text-dark">Controlado</span>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div class="card mt-4 shadow-sm printable-area border-0 content-card">
        <div class="card-header bg-institucional text-white text-center py-2 no-print">
          <h6 class="mb-0 fw-bold uppercase-tracking">Resumen de Receta</h6>
        </div>

        <div class="only-print text-center mb-4">
          <h3 class="color-institucional fw-bold text-uppercase">Receta Médica</h3>
          <p class="text-muted small">Servicios de Salud - Comprobante de Entrega</p>
          <hr />
        </div>

        <div class="card-body p-3">
          <h6 class="color-institucional mb-2 fw-bold border-bottom pb-1">Paciente</h6>
          <table class="table table-sm table-bordered mb-3">
            <tr>
              <th class="bg-light" style="width: 80px">ID</th>
              <td>{{ receta.pacienteId || '---' }}</td>
              <th class="bg-light" style="width: 90px">Nombre</th>
              <td>{{ receta.pacienteNombre || 'No seleccionado' }}</td>
            </tr>
            <tr>
              <th class="bg-light">Edad</th>
              <td>{{ receta.edad || '---' }}</td>
              <th class="bg-light">Sexo</th>
              <td>{{ receta.sexo || '---' }}</td>
            </tr>
          </table>

          <h6 class="color-institucional mb-2 fw-bold border-bottom pb-1">Médico</h6>
          <table class="table table-sm table-bordered mb-3">
            <tr>
              <th class="bg-light" style="width: 80px">ID</th>
              <td>{{ receta.medicoId || '---' }}</td>
              <th class="bg-light" style="width: 90px">Nombre</th>
              <td>{{ receta.medicoNombre || 'No seleccionado' }}</td>
            </tr>
            <tr>
              <th class="bg-light">Especialidad</th>
              <td colspan="3">{{ receta.especialidad || '---' }}</td>
            </tr>
          </table>

          <h6 class="color-institucional mb-2 fw-bold border-bottom pb-1">Medicamentos Seleccionados</h6>
          <table class="table table-sm table-bordered align-middle text-center mb-3">
            <thead class="table-light">
              <tr>
                <th style="width: 40px">#</th>
                <th>Medicamento</th>
                <th style="width: 100px">Cant.</th>
                <th style="width: 80px" class="no-print">Stock</th>
                <th style="width: 130px" class="no-print">Disponibilidad</th>
                <th>Observaciones</th>
                <th style="width: 120px" class="no-print">Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="receta.medicamentos.length === 0">
                <td colspan="7" class="text-muted py-3 text-center">No hay medicamentos agregados</td>
              </tr>
              <tr v-for="(med, index) in receta.medicamentos" :key="med._uid" 
                  :class="{'table-danger': med.medicamentoId && med.cantidad > med.stock, 'table-warning': med.controlado}">
                <td>{{ index + 1 }}</td>
                <td class="text-start">
                  <span v-if="!med.editando">
                    <b v-if="med.controlado" class="text-danger no-print">⚠️ </b>{{ med.nombre }}
                  </span>
                  <input v-else v-model="med.nombre" class="form-control form-control-sm"/>
                </td>
                <td>
                  <span v-if="!med.editando">{{ med.cantidad }}</span>
                  <input v-else type="number" v-model.number="med.cantidad" min="1" class="form-control form-control-sm text-center mx-auto" style="width: 70px"/>
                </td>
                <td class="no-print"><span :class="{'text-danger fw-bold': med.stock === 0}">{{ med.stock }}</span></td>
                <td class="no-print">
                  <span v-if="med.controlado" class="badge bg-dark">NARCÓTICO</span>
                  <span v-else-if="med.stock >= med.cantidad && med.stock > 0" class="badge bg-success">Disponible</span>
                  <span v-else class="badge bg-danger">{{ med.stock === 0 ? 'Sin stock' : 'Insuficiente' }}</span>
                </td>
                <td class="text-start">
                  <span v-if="!med.editando">{{ med.indicaciones }}</span>
                  <input v-else v-model="med.indicaciones" class="form-control form-control-sm" placeholder="Indicaciones..."/>
                </td>
                <td class="no-print">
                  <button v-if="!med.editando" class="btn btn-outline-institucional btn-sm me-1" @click="editarMedicamento(index)">Editar</button>
                  <button v-else class="btn btn-success btn-sm me-1" @click="guardarEdicion(index)">OK</button>
                  <button class="btn btn-outline-danger btn-sm" @click="eliminarMedicamento(index)">X</button>
                </td>
              </tr>
            </tbody>
          </table>

          <div class="only-print mt-5">
            <div class="row text-center mt-5">
              <div class="col-6"><div class="border-top mx-4 mt-4 pt-1 small">Firma del Médico</div></div>
              <div class="col-6"><div class="border-top mx-4 mt-4 pt-1 small">Sello de Farmacia</div></div>
            </div>
          </div>
        </div>

        <div class="card-footer bg-light text-center py-3 no-print">
          <button class="btn btn-institucional-light btn-lg shadow-sm text-white me-3" :disabled="receta.medicamentos.length === 0" @click="imprimirBorrador">
            <font-awesome-icon icon="print" class="me-1" /> Imprimir Vista Previa
          </button>
          <button class="btn btn-institucional-dark btn-lg shadow-sm text-white" :disabled="receta.medicamentos.length === 0 || !receta.pacienteId || !receta.medicoId" @click="registrarHistorico">
            <font-awesome-icon icon="save" class="me-1" /> Registrar y Finalizar
          </button>
        </div>
      </div>
    </div>

    <div v-if="mostrarModalAdmin" class="modal-overlay">
      <div class="modal-content-custom shadow-lg border-institucional-dark">
        <div class="modal-header-custom bg-institucional-dark text-white p-3 text-center">
          <h5 class="mb-0">Autorización Requerida</h5>
        </div>
        <div class="p-4">
          <p class="text-center">Se detectaron <b>NARCÓTICOS</b>.<br>Ingrese credenciales de administrador.</p>
          <div class="mb-3">
            <label class="form-label small fw-bold">Usuario:</label>
            <input type="text" v-model="adminCredentials.usuario" class="form-control focus-institucional" />
          </div>
          <div class="mb-4">
            <label class="form-label small fw-bold">Contraseña:</label>
            <input type="password" v-model="adminCredentials.password" class="form-control focus-institucional" @keyup.enter="validarYAutorizarAdmin" />
          </div>
          <div class="d-flex justify-content-between">
            <button class="btn btn-secondary" @click="mostrarModalAdmin = false">Cancelar</button>
            <button class="btn btn-institucional-dark text-white" @click="validarYAutorizarAdmin">Autorizar y Guardar</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./buscador.component.ts"></script>

<style scoped>
/* FONDO INSTITUCIONAL */
.page-container {
  background-image: url('/content/images/fondo.jpeg');
  background-repeat: no-repeat;
  background-position: center;
  background-attachment: fixed;
  background-size: cover;
  position: relative;
}

/* Capa de transparencia para legibilidad */
.page-container::before {
  content: "";
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background-color: rgba(255, 255, 255, 0.92); 
  z-index: 0;
}

.container {
  position: relative;
  z-index: 1;
}

/* COLORES INSTITUCIONALES */
.color-institucional { color: #9b2247 !important; }
.color-institucional-dark { color: #611232 !important; }
.bg-institucional { background-color: #9b2247 !important; }
.bg-institucional-dark { background-color: #611232 !important; }

/* BOTONES */
.btn-institucional-light { background-color: #9b2247; border-color: #9b2247; }
.btn-institucional-light:hover { background-color: #7a1b38; }
.btn-institucional-dark { background-color: #611232; border-color: #611232; }
.btn-institucional-dark:hover { background-color: #4a0d26; }
.btn-outline-institucional { color: #9b2247; border-color: #9b2247; background-color: transparent; }
.btn-outline-institucional:hover { background-color: #9b2247; color: white; }

/* BORDES E INPUTS */
.border-institucional-light { border-color: rgba(155, 34, 71, 0.3) !important; }
.border-institucional-dark { border: 2px solid #611232; }
.focus-institucional:focus { border-color: #9b2247; box-shadow: 0 0 0 0.25rem rgba(155, 34, 71, 0.25); }

.content-card { background-color: rgba(255, 255, 255, 0.95); }
.uppercase-tracking { text-transform: uppercase; letter-spacing: 1px; }

/* OTROS */
.buscador-card { position: relative; }
.resultados-dropdown {
  position: absolute; top: 75px; left: 0; width: 100%; z-index: 1050;
  max-height: 280px; overflow-y: auto; background: white;
  border: 1px solid #ddd; border-radius: 6px; box-shadow: 0 8px 16px rgba(0,0,0,0.15);
}

.table-danger td { background-color: #f8d7da !important; }
.table-warning td { background-color: #fff3cd !important; border-left: 5px solid #ffc107; }

.modal-overlay {
  position: fixed; top: 0; left: 0; width: 100vw; height: 100vh;
  background-color: rgba(0, 0, 0, 0.7); display: flex; justify-content: center; align-items: center; z-index: 2000;
}
.modal-content-custom { background-color: white; width: 95%; max-width: 450px; border-radius: 12px; overflow: hidden; }

@media print {
  .no-print { display: none !important; }
  .only-print { display: block !important; }
  .page-container::before, .page-container { background-image: none !important; background-color: white !important; }
  .printable-area { border: none !important; box-shadow: none !important; width: 100% !important; margin: 0 !important; }
}
</style>