<template>
  <div class="container mt-4 mb-5">
    <div class="row justify-content-center">
      <div class="col-md-10">
        <div v-if="historico" class="card shadow-lg border-0" id="seccion-receta">
          <!-- HEADER -->
          <div
            class="card-header bg-primary text-white d-flex justify-content-between align-items-center py-3 no-print"
          >
            <div>
              <h4 class="mb-0">
                <font-awesome-icon
                  icon="file-invoice"
                  class="text-info me-2"
                ></font-awesome-icon>
                Folio: <span class="fw-bold">{{ historico.folio }}</span>
              </h4>
              <small class="text-white-50">ID de Sistema: {{ historico.id }}</small>
            </div>
            <div class="text-end">
              <div class="fw-bold h5 mb-0">
                {{ formatDateLong(historico.fechaEmision) }}
              </div>
              <span class="badge bg-info text-dark shadow-sm">Registro Verificado</span>
            </div>
          </div>

          <!-- PRINT HEADER -->
          <div class="only-print text-center mb-4">
            <h2 class="text-primary fw-bold text-uppercase">
              Receta Médica / Comprobante de Entrega
            </h2>
            <p class="text-muted">
              Folio: {{ historico.folio }} | Fecha:
              {{ formatDateLong(historico.fechaEmision) }}
            </p>
            <hr />
          </div>

          <!-- BODY -->
          <div class="card-body p-4 bg-white">
            <!-- DATOS PACIENTE Y MÉDICO -->
            <div class="row mb-4">
              <div class="col-md-6 border-end border-primary-subtle printable-col">
                <h6
                  class="text-primary fw-bold text-uppercase border-bottom border-primary-subtle pb-2"
                >
                  <font-awesome-icon icon="user" class="me-2 no-print"></font-awesome-icon
                  >Datos del Paciente
                </h6>
                <p class="mb-1 text-secondary">
                  <strong>Nombre:</strong>
                  <span class="text-dark">{{ historico.pacienteNombre }}</span>
                </p>
                <p class="mb-1 text-secondary">
                  <strong>ID Paciente:</strong>
                  <span class="text-dark">{{ historico.pacienteId }}</span>
                </p>
                <p class="mb-0 text-secondary">
                  <strong>CURP:</strong>
                  <span class="text-muted">{{
                    historico.pacienteCurp || "No registrada"
                  }}</span>
                </p>
              </div>
              <div class="col-md-6 ps-md-4 printable-col">
                <h6
                  class="text-info-emphasis fw-bold text-uppercase border-bottom border-info-subtle pb-2"
                >
                  <font-awesome-icon
                    icon="user-md"
                    class="me-2 no-print"
                  ></font-awesome-icon
                  >Datos del Médico
                </h6>
                <p class="mb-1 text-secondary">
                  <strong>Nombre:</strong>
                  <span class="text-dark">{{ historico.medicoNombre }}</span>
                </p>
                <p class="mb-1 text-secondary">
                  <strong>Especialidad:</strong>
                  <span class="text-dark">{{
                    historico.medicoEspecialidad || "Médico General"
                  }}</span>
                </p>
                <p class="mb-0 text-secondary">
                  <strong>ID Médico:</strong>
                  <span class="text-dark">{{ historico.medicoId }}</span>
                </p>
              </div>
            </div>

            <!-- MEDICAMENTOS -->
            <div class="mt-4">
              <h6 class="text-primary fw-bold text-uppercase mb-3">
                <font-awesome-icon icon="pills" class="me-2 no-print"></font-awesome-icon
                >Medicamentos Entregados
              </h6>
              <div class="table-responsive shadow-sm">
                <table class="table table-hover table-bordered align-middle">
                  <thead class="table-primary text-primary-emphasis">
                    <tr>
                      <th style="width: 10%">ID</th>
                      <th>Nombre / Indicaciones</th>
                      <th class="text-center" style="width: 20%">Cantidad</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-if="esJsonValido(historico.medicamentos)">
                      <tr
                        v-for="med in parsearMeds(historico.medicamentos)"
                        :key="med.id"
                      >
                        <td class="text-muted small">{{ med.id }}</td>
                        <td>
                          <div class="fw-bold text-dark">{{ med.nombre }}</div>
                          <div
                            v-if="med.observaciones"
                            class="text-primary italic mt-1 small"
                          >
                            <font-awesome-icon
                              icon="info-circle"
                              class="me-1"
                            ></font-awesome-icon>
                            {{ med.observaciones }}
                          </div>
                        </td>
                        <td class="text-center">
                          <span
                            class="badge bg-primary px-3 py-2 rounded-pill print-badge"
                            >{{ med.cantidad }} pz.</span
                          >
                        </td>
                      </tr>
                    </template>
                    <tr v-else>
                      <td colspan="3" class="text-center italic text-muted py-3">
                        {{ historico.medicamentos }}
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- OBSERVACIONES Y USUARIOS -->
            <div class="row mt-4 pt-3 border-top border-primary-subtle">
              <div class="col-md-7 printable-col">
                <p class="mb-1 small fw-bold text-primary text-uppercase">
                  Observaciones Generales:
                </p>
                <div
                  class="p-3 bg-primary-subtle rounded border border-primary-subtle italic text-primary-emphasis"
                >
                  {{ historico.observaciones || "Sin observaciones registradas." }}
                </div>
              </div>
              <div class="col-md-5 text-md-end pt-3 pt-md-0 printable-col">
                <div class="mb-2">
                  <small class="text-muted d-block text-uppercase small-font"
                    >Usuario que Registró:</small
                  >
                  <span class="fw-bold text-primary">{{
                    historico.usuarioQueRegistro
                  }}</span>
                </div>
                <div>
                  <small class="text-muted d-block text-uppercase small-font"
                    >Autorizado por:</small
                  >
                  <span class="fw-bold text-info-emphasis">{{ historico.autorizo }}</span>
                </div>
              </div>
            </div>

            <!-- Sello de impresión -->
            <div class="only-print mt-5 pt-5">
              <div class="row text-center mt-5">
                <div class="col-6 offset-3 border-top border-dark pt-2">
                  <small class="text-uppercase fw-bold"
                    >Sello y Firma de Autorización</small
                  >
                </div>
              </div>
            </div>
          </div>

          <!-- FOOTER BOTONES -->
          <div class="card-footer bg-light py-3 d-flex justify-content-between no-print">
            <button
              type="button"
              @click.prevent="previousState()"
              class="btn btn-outline-secondary px-4 shadow-sm"
            >
              <font-awesome-icon icon="arrow-left" class="me-1"></font-awesome-icon>
              Volver
            </button>
            <div class="btn-group">
              <button
                type="button"
                @click="imprimirReceta"
                class="btn btn-info px-4 shadow-sm fw-bold text-white me-2"
              >
                <font-awesome-icon icon="print" class="me-1"></font-awesome-icon> Imprimir
                Receta
              </button>
              <router-link
                v-if="historico.id"
                :to="{ name: 'HistoricoEdit', params: { historicoId: historico.id } }"
                custom
                v-slot="{ navigate }"
              >
                <button @click="navigate" class="btn btn-primary px-4 shadow-sm fw-bold">
                  <font-awesome-icon icon="pencil-alt" class="me-1"></font-awesome-icon>
                  Editar
                </button>
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historico-details.component.ts"></script>

<style scoped>
.card-header {
  border-bottom: 4px solid #0056b3;
}
.table-responsive {
  border-radius: 8px;
}
.table thead th {
  font-size: 0.85rem;
  letter-spacing: 0.5px;
}
.italic {
  font-style: italic;
}
.small-font {
  font-size: 0.75rem;
}

/* --- ESTILOS DE IMPRESIÓN --- */
.only-print {
  display: none;
}

@media print {
  .no-print,
  .btn,
  .card-footer,
  .card-header,
  .navbar {
    display: none !important;
  }

  .only-print {
    display: block !important;
  }

  .container,
  .card,
  .card-body {
    width: 100% !important;
    margin: 0 !important;
    padding: 0 !important;
    border: none !important;
    box-shadow: none !important;
  }

  .printable-col {
    width: 50% !important;
    float: left !important;
  }

  .card-body {
    padding: 1cm !important;
  }

  .table-primary {
    background-color: #f8f9fa !important;
    -webkit-print-color-adjust: exact;
  }

  .print-badge {
    background: white !important;
    color: black !important;
    border: 1px solid #ccc !important;
  }
}
</style>
