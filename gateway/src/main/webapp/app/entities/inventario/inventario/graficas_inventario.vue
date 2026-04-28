<template>
  <div class="container-fluid mt-3 bg-transparent">
    <div
      class="row mb-4 animate__animated animate__fadeIn"
      v-if="alertasStockBajo.length > 0 || alertasCaducidad.length > 0"
    >
      <div
        :class="alertasCaducidad.length > 0 ? 'col-lg-6' : 'col-12'"
        class="mb-3 mb-lg-0"
      >
        <div class="card border-vino shadow-sm h-100 bg-white-opacity">
          <div
            class="card-header bg-vino text-white d-flex justify-content-between align-items-center py-2"
          >
            <h6 class="mb-0 fw-bold">
              <font-awesome-icon icon="exclamation-triangle" class="me-2" /> Stock Crítico
            </h6>
            <span class="badge bg-white text-vino rounded-pill">{{
              alertasStockBajo.length
            }}</span>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive" style="max-height: 250px">
              <table class="table table-hover align-middle mb-0 table-sm">
                <thead class="bg-light-guinda text-guinda small text-uppercase">
                  <tr>
                    <th class="ps-3">Medicamento</th>
                    <th class="text-center">Stock</th>
                    <th class="text-center">Acción</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in alertasStockBajo" :key="'stock-' + item.id">
                    <td class="ps-3">
                      <div class="fw-bold text-dark">{{ item.nombre }}</div>
                      <small class="text-muted">Lote: {{ item.lote || "N/A" }}</small>
                    </td>
                    <td class="text-center">
                      <span
                        class="badge rounded-pill px-3"
                        :class="item.cantidad <= 10 ? 'bg-vino' : 'bg-guinda'"
                        >{{ item.cantidad }}</span
                      >
                    </td>
                    <td class="text-center">
                      <button
                        class="btn btn-xs btn-outline-guinda shadow-sm px-2 rounded-pill"
                        @click="abrirModalResurtido(item)"
                      >
                        <font-awesome-icon icon="plus-circle" class="me-1" /> Resurtir
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>

      <div :class="alertasStockBajo.length > 0 ? 'col-lg-6' : 'col-12'">
        <div class="card border-ocre shadow-sm h-100 bg-white-opacity">
          <div
            class="card-header bg-ocre text-white d-flex justify-content-between align-items-center py-2"
          >
            <h6 class="mb-0 fw-bold">
              <font-awesome-icon icon="calendar-times" class="me-2" /> Próximos a Caducar
            </h6>
            <span class="badge bg-white text-ocre rounded-pill">{{
              alertasCaducidad.length
            }}</span>
          </div>
          <div class="card-body p-0">
            <div class="table-responsive" style="max-height: 250px">
              <table class="table table-hover align-middle mb-0 table-sm">
                <thead class="bg-light-ocre text-ocre small text-uppercase">
                  <tr>
                    <th class="ps-3">Medicamento / Lote</th>
                    <th class="text-center">Vencimiento</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="item in alertasCaducidad" :key="'cad-' + item.id">
                    <td class="ps-3">
                      <div class="fw-bold text-dark">{{ item.nombre }}</div>
                      <code class="small text-ocre">Lote: {{ item.lote || "N/A" }}</code>
                    </td>
                    <td class="text-center">
                      <span
                        :class="[
                          'badge shadow-sm rounded-pill px-3',
                          getBadgeCaducidad(item.fechaCaducidad),
                        ]"
                      >
                        <font-awesome-icon icon="clock" class="me-1" />
                        {{ item.fechaCaducidad }}
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="card shadow-lg border-0 mb-4 rounded-xl overflow-hidden bg-white-opacity">
      <div
        class="card-header d-flex justify-content-between align-items-center bg-white border-bottom py-3"
      >
        <h5 class="mb-0 text-guinda fw-bold">
          <font-awesome-icon icon="chart-line" class="me-2" /> Inteligencia de Inventario
        </h5>
        <button
          class="btn btn-sm btn-outline-guinda px-3 shadow-sm rounded-pill"
          @click="cargarDatos"
          :disabled="!isLoaded"
        >
          <font-awesome-icon icon="sync" :spin="!isLoaded" class="me-1" />
          {{ isLoaded ? "Sincronizar" : "Analizando..." }}
        </button>
      </div>

      <div class="card-body bg-light-soft p-4">
        <div v-if="!isLoaded" class="text-center py-5">
          <div class="spinner-border text-guinda" role="status"></div>
          <p class="mt-2 text-guinda fw-bold">Procesando métricas institucionales...</p>
        </div>

        <div v-else class="animate__animated animate__fadeInUp">
          <div class="row mb-4">
            <div class="col-12">
              <div class="card p-4 border-0 shadow-sm rounded-4 chart-container">
                <h6 class="text-dark fw-bold mb-3 border-bottom pb-2">
                  <font-awesome-icon icon="balance-scale" class="text-guinda me-2" />
                  Comparativa de Niveles de Stock
                </h6>
                <div style="height: 350px">
                  <BarChart :data="stockData" :options="chartOptions" />
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-lg-8 mb-3 mb-lg-0">
              <div class="card p-4 border-0 shadow-sm rounded-4 chart-container h-100">
                <h6 class="text-dark fw-bold mb-3 border-bottom pb-2">
                  <font-awesome-icon icon="shipping-fast" class="text-ocre me-2" />
                  Medicamentos con Mayor Demanda
                </h6>
                <div style="height: 380px">
                  <BarChart :data="entregadosData" :options="horizontalOptions" />
                </div>
              </div>
            </div>

            <div class="col-lg-4">
              <div
                class="card p-4 border-0 shadow-sm rounded-4 chart-container h-100 d-flex flex-column justify-content-between"
              >
                <div>
                  <h6 class="text-center text-dark fw-bold mb-4">Balance de Flujo</h6>
                  <div style="height: 250px">
                    <PieChart :data="pieData" :options="chartOptions" />
                  </div>
                </div>
                <div
                  class="alert bg-light-guinda border-0 shadow-sm py-3 small mb-0 mt-3 text-guinda"
                >
                  <div class="d-flex align-items-center">
                    <font-awesome-icon icon="lightbulb" class="me-3 fs-4" />
                    <div>
                      Análisis basado en los últimos <strong>500 movimientos</strong>.
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <teleport to="body">
      <div
        class="modal fade"
        id="modalResurtido"
        tabindex="-1"
        aria-hidden="true"
        ref="resurtidoModalRef"
      >
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content border-0 shadow-lg rounded-xl">
            <div class="modal-header bg-vino text-white py-3">
              <h5 class="modal-title">
                <font-awesome-icon icon="pills" class="me-2" /> Registrar Entrada
              </h5>
              <button
                type="button"
                class="btn-close btn-close-white"
                @click="cerrarModal"
              ></button>
            </div>
            <div class="modal-body p-4" v-if="itemSeleccionado">
              <div class="mb-4">
                <label class="form-label small fw-bold text-muted text-uppercase"
                  >Medicamento</label
                >
                <div class="h5 mb-1 text-vino fw-bold">{{ itemSeleccionado.nombre }}</div>
                <span class="badge bg-light text-dark border"
                  >Lote: {{ itemSeleccionado.lote || "N/A" }}</span
                >
              </div>
              <div class="row g-3">
                <div class="col-6">
                  <label class="form-label small fw-bold">Existencia</label>
                  <div
                    class="form-control bg-light text-center border-0 rounded-pill fw-bold"
                  >
                    {{ itemSeleccionado.cantidad }}
                  </div>
                </div>
                <div class="col-6">
                  <label class="form-label small fw-bold text-guinda"
                    >Ingreso de Piezas</label
                  >
                  <input
                    type="number"
                    class="form-control border-guinda text-center fw-bold rounded-pill focus-guinda"
                    v-model.number="cantidadASumar"
                    min="1"
                    @keyup.enter="confirmarResurtido"
                  />
                </div>
              </div>
            </div>
            <div
              class="modal-footer bg-light border-0 py-3 d-flex justify-content-between"
            >
              <button
                type="button"
                class="btn btn-outline-secondary rounded-pill px-4"
                @click="cerrarModal"
              >
                Cancelar
              </button>
              <button
                type="button"
                class="btn btn-guinda px-4 shadow-sm rounded-pill"
                @click="confirmarResurtido"
                :disabled="cantidadASumar <= 0 || isSaving"
              >
                <font-awesome-icon icon="check-circle" class="me-1" />
                {{ isSaving ? "Guardando..." : "Confirmar Entrada" }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </teleport>
  </div>
</template>

<script lang="ts" src="./graficas_inventario.component.ts"></script>

<style scoped>
/* Estilos Institucionales */
.text-guinda {
  color: #9b2247 !important;
}
.text-vino {
  color: #611232 !important;
}
.text-ocre {
  color: #a57f2c !important;
}
.bg-vino {
  background-color: #611232 !important;
}
.bg-guinda {
  background-color: #9b2247 !important;
}
.bg-ocre {
  background-color: #a57f2c !important;
}
.bg-light-guinda {
  background-color: #fdf2f5;
}
.bg-light-soft {
  background-color: #f8f9fa;
}
.border-vino {
  border: 1.5px solid #611232 !important;
}
.border-guinda {
  border: 1.5px solid #9b2247 !important;
}
.btn-guinda {
  background-color: #9b2247;
  color: white;
  border: none;
}
.btn-guinda:hover {
  background-color: #611232;
  transform: translateY(-2px);
}
.btn-outline-guinda {
  color: #9b2247;
  border: 1.5px solid #9b2247;
}
.btn-outline-guinda:hover {
  background-color: #9b2247;
  color: white;
}
.rounded-xl {
  border-radius: 20px !important;
}
.rounded-4 {
  border-radius: 1.2rem !important;
}
.bg-white-opacity {
  background-color: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(4px);
}
.chart-container {
  transition: all 0.3s ease;
  background: white;
}
.chart-container:hover {
  box-shadow: 0 10px 20px rgba(97, 18, 50, 0.1) !important;
}
.focus-guinda:focus {
  border-color: #9b2247;
  box-shadow: 0 0 0 0.25rem rgba(155, 34, 71, 0.15);
}
.btn-xs {
  padding: 0.25rem 0.6rem;
  font-size: 0.7rem;
  font-weight: bold;
}
</style>
