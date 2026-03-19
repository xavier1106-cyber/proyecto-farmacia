<template>
  <div class="container mt-4 mb-5">
    <!-- Encabezado -->
    <h2 class="mb-4 text-center border-bottom pb-3 text-institucional fw-bold">
      <font-awesome-icon icon="history" class="me-2"></font-awesome-icon>
      Historial de Recetas Emitidas
    </h2>

    <!-- Barra de acciones y filtro -->
    <div class="d-flex justify-content-between align-items-center mb-4 no-print">
      <div class="btn-group shadow-sm">
        <button
          class="btn btn-outline-institucional"
          @click="handleSyncList"
          :disabled="isFetching"
        >
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          Actualizar Lista
        </button>
        <router-link to="/buscador" class="btn btn-institucional">
          <font-awesome-icon icon="search" /> Volver al Buscador
        </router-link>
      </div>

      <div class="flex-grow-1 mx-4">
        <div class="input-group shadow-sm">
          <span class="input-group-text bg-white border-end-0">
            <font-awesome-icon icon="search" class="text-muted" />
          </span>
          <input
            type="text"
            class="form-control border-start-0"
            placeholder="Filtrar por folio, paciente o médico..."
            v-model="filtroTexto"
          />
        </div>
      </div>

      <router-link :to="{ name: 'HistoricoCreate' }" custom v-slot="{ navigate }">
        <button @click="navigate" class="btn btn-success-custom shadow-sm">
          <font-awesome-icon icon="plus"></font-awesome-icon> Nueva Entrada Manual
        </button>
      </router-link>
    </div>

    <!-- Mensaje si no hay datos -->
    <div
      class="alert alert-info shadow-sm border-0"
      v-if="!isFetching && (!historicosFiltrados || historicosFiltrados.length === 0)"
    >
      <span v-text="t$('gatewayApp.historicoHistorico.home.notFound')"></span>
    </div>

    <!-- Tabla de históricos -->
    <div
      class="card shadow-sm border-0"
      v-if="historicosFiltrados && historicosFiltrados.length > 0"
    >
      <div class="table-responsive table-scroll-container">
        <table class="table table-hover align-middle mb-0">
          <thead class="bg-institucional text-white sticky-top">
            <tr class="text-center">
              <th style="width: 140px" class="text-white">Folio / Fecha</th>
              <th class="text-white">Paciente</th>
              <th class="text-white">Médico</th>
              <th style="min-width: 300px" class="text-white text-start">
                Medicamentos y osbservaciones
              </th>
              <th class="text-white">Responsable</th>
              <th class="text-white">Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="historico in historicosFiltrados" :key="historico.id">
              <td class="text-center">
                <span class="fw-bold text-institucional">{{ historico.folio }}</span
                ><br />
                <small class="text-muted">{{
                  formatDateShort(historico.fechaEmision)
                }}</small>
              </td>
              <td>
                <div class="fw-bold text-dark">{{ historico.pacienteNombre }}</div>
                <small class="text-muted">ID: {{ historico.pacienteId }}</small>
              </td>
              <td>
                <div class="fw-bold text-dark">{{ historico.medicoNombre }}</div>
                <span class="badge bg-institucional-dark text-white small">
                  <font-awesome-icon
                    icon="user-md"
                    class="me-1 small"
                  ></font-awesome-icon>
                  {{
                    historico.medicoEspecialidad ||
                    historico.especialidad ||
                    "Médico General"
                  }}
                </span>
              </td>
              <td>
                <ul
                  class="list-unstyled mb-0 small"
                  v-if="esJsonValido(historico.medicamentos)"
                >
                  <li
                    v-for="m in parsearMeds(historico.medicamentos)"
                    :key="m.id"
                    class="border-bottom py-1"
                  >
                    <div class="d-flex justify-content-between">
                      <span>
                        <span class="badge bg-light text-institucional border me-1">{{
                          m.cantidad
                        }}</span>
                        <b class="text-dark">{{ m.nombre }}</b>
                        <small class="text-muted ms-2">Lote: {{ m.lote || "-" }}</small>
                        <small class="text-muted ms-2"
                          >Caducidad: {{ m.fechaCaducidad || "-" }}</small
                        >-->
                      </span>
                    </div>
                    <div
                      v-if="m.observaciones"
                      class="text-muted italic mt-1"
                      style="font-size: 0.8rem"
                    >
                      <font-awesome-icon
                        icon="comment-dots"
                        class="me-1 small"
                      ></font-awesome-icon>
                      {{ m.observaciones }}
                    </div>
                  </li>
                </ul>
                <span v-else class="text-muted italic">{{ historico.medicamentos }}</span>
              </td>
              <td class="text-center">
                <div class="small text-muted">
                  Registro: <b>{{ historico.usuarioQueRegistro }}</b>
                </div>
                <div class="small">
                  <span
                    class="badge bg-success-subtle text-success border border-success"
                  >
                    Autorizó: {{ historico.autorizo }}
                  </span>
                </div>
              </td>
              <td class="text-center">
                <div class="btn-group shadow-sm">
                  <router-link
                    :to="{ name: 'HistoricoView', params: { historicoId: historico.id } }"
                    custom
                    v-slot="{ navigate }"
                  >
                    <button
                      @click="navigate"
                      class="btn btn-outline-info btn-sm"
                      title="Ver Detalle"
                    >
                      <font-awesome-icon icon="eye"></font-awesome-icon>
                    </button>
                  </router-link>
                  <router-link
                    :to="{ name: 'HistoricoEdit', params: { historicoId: historico.id } }"
                    custom
                    v-slot="{ navigate }"
                  >
                    <button
                      @click="navigate"
                      class="btn btn-outline-institucional btn-sm"
                      title="Editar"
                    >
                      <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    </button>
                  </router-link>
                  <b-button
                    @click="prepareRemove(historico)"
                    variant="outline-danger"
                    class="btn-sm"
                    v-b-modal.removeEntity
                    title="Eliminar"
                  >
                    <font-awesome-icon icon="times"></font-awesome-icon>
                  </b-button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Paginación -->
    <div v-show="historicosFiltrados && historicosFiltrados.length > 0" class="mt-4">
      <div class="d-flex justify-content-center">
        <jhi-item-count
          :page="page"
          :total="queryCount"
          :itemsPerPage="itemsPerPage"
          class="text-muted small"
        ></jhi-item-count>
      </div>
      <div class="d-flex justify-content-center mt-2 custom-pagination">
        <b-pagination
          size="md"
          :total-rows="totalItems"
          v-model="page"
          :per-page="itemsPerPage"
          class="shadow-sm"
        ></b-pagination>
      </div>
    </div>

    <!-- Modal eliminar -->
    <b-modal
      ref="removeEntity"
      id="removeEntity"
      title="Confirmar Eliminación"
      header-bg-variant="danger"
      header-text-variant="white"
    >
      <p class="my-2 text-center">
        ¿Está seguro de que desea eliminar el registro con folio:<br />
        <b class="text-danger fs-5">{{ removeId }}</b
        >?
      </p>
      <template #modal-footer>
        <button type="button" class="btn btn-secondary" @click="closeDialog()">
          Cancelar
        </button>
        <button type="button" class="btn btn-danger" @click="removeHistorico()">
          Eliminar permanentemente
        </button>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./historico.component.ts"></script>

<style scoped>
/* PALETA PANTONE INSTITUCIONAL */
.text-institucional {
  color: #9b2247 !important; /* PANTONE 7420 C */
}

.bg-institucional {
  background-color: #9b2247 !important;
}

.bg-institucional-dark {
  background-color: #611232 !important; /* PANTONE 7421 C */
}

.btn-institucional {
  background-color: #9b2247 !important;
  border-color: #9b2247 !important;
  color: white !important;
}

.btn-outline-institucional {
  border-color: #9b2247 !important;
  color: #9b2247 !important;
}

.btn-outline-institucional:hover {
  background-color: #9b2247 !important;
  color: white !important;
}

.btn-success-custom {
  background-color: #28a745 !important;
  border-color: #28a745 !important;
  color: white !important;
}

/* TABLA Y DESPLAZAMIENTO */
.table-scroll-container {
  max-height: 65vh;
  overflow-y: auto;
  scrollbar-width: thin;
}

.sticky-top {
  position: sticky;
  top: 0;
  z-index: 10;
}

.table thead th {
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border: none;
}

.table tbody td {
  font-size: 0.88rem;
  border-bottom: 1px solid #f0f0f0;
}

.table-hover tbody tr:hover {
  background-color: rgba(155, 34, 71, 0.04); /* Sombra suave guinda */
  transition: background-color 0.2s ease;
}

/* PAGINACIÓN */
::v-deep .page-item.active .page-link {
  background-color: #9b2247 !important;
  border-color: #9b2247 !important;
}

::v-deep .page-link {
  color: #9b2247;
}

.badge {
  font-weight: 500;
  padding: 0.4em 0.6em;
}

.italic {
  font-style: italic;
}
</style>
