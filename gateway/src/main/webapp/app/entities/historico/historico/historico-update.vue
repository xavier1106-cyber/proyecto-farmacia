<template>
  <div class="container mt-4 pb-5">
    <div class="row justify-content-center">
      <div class="col-lg-10">
        <form
          name="editForm"
          novalidate
          @submit.prevent="save()"
          class="card shadow border-0"
        >
          <div class="card-header bg-primary text-white py-3">
            <h4 class="mb-0 text-center">
              <font-awesome-icon icon="file-medical" class="me-2"></font-awesome-icon>
              {{
                historico.id ? "Modificar Registro Histórico" : "Registrar Nueva Entrada"
              }}
            </h4>
          </div>

          <div class="card-body bg-light">
            <div class="card mb-4 shadow-sm border-0">
              <div class="card-header bg-white py-2">
                <h6 class="mb-0 text-primary fw-bold text-uppercase small">
                  Control de Registro
                </h6>
              </div>
              <div class="card-body row py-3">
                <div class="form-group col-md-4" v-if="historico.id">
                  <label class="form-label small fw-bold text-muted">ID Sistema</label>
                  <input
                    type="text"
                    class="form-control form-control-sm bg-light"
                    v-model="historico.id"
                    readonly
                  />
                </div>
                <div class="form-group col-md-4">
                  <label class="form-label small fw-bold">Folio</label>
                  <input
                    type="text"
                    class="form-control form-control-sm"
                    v-model="v$.folio.$model"
                    :class="{ 'is-invalid': v$.folio.$invalid && v$.folio.$dirty }"
                  />
                </div>
                <div class="form-group col-md-4">
                  <label class="form-label small fw-bold">Fecha Emisión</label>
                  <input
                    type="datetime-local"
                    class="form-control form-control-sm"
                    :value="convertDateTimeFromServer(v$.fechaEmision.$model)"
                    @change="updateInstantField('fechaEmision', $event)"
                  />
                </div>
              </div>
            </div>

            <div class="row mb-4">
              <div class="col-md-6">
                <div
                  class="card border-0 border-start border-primary border-4 shadow-sm h-100"
                >
                  <div class="card-body">
                    <h6 class="text-primary fw-bold text-uppercase small mb-3">
                      Información del Paciente
                    </h6>
                    <div class="mb-2">
                      <label class="form-label small fw-bold">Nombre</label>
                      <input
                        type="text"
                        class="form-control form-control-sm"
                        v-model="v$.pacienteNombre.$model"
                      />
                    </div>
                    <div class="row small">
                      <div class="col-6">
                        <label class="fw-bold">ID</label>
                        <input
                          type="number"
                          class="form-control form-control-sm"
                          v-model.number="v$.pacienteId.$model"
                        />
                      </div>
                      <div class="col-6">
                        <label class="fw-bold">CURP</label>
                        <input
                          type="text"
                          class="form-control form-control-sm"
                          v-model="v$.pacienteCurp.$model"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div class="col-md-6">
                <div
                  class="card border-0 border-start border-info border-4 shadow-sm h-100"
                >
                  <div class="card-body">
                    <h6 class="text-info-emphasis fw-bold text-uppercase small mb-3">
                      Información del Médico
                    </h6>
                    <div class="mb-2">
                      <label class="form-label small fw-bold">Nombre</label>
                      <input
                        type="text"
                        class="form-control form-control-sm"
                        v-model="v$.medicoNombre.$model"
                      />
                    </div>
                    <div class="row small">
                      <div class="col-7">
                        <label class="fw-bold">Especialidad</label>
                        <input
                          type="text"
                          class="form-control form-control-sm"
                          v-model="v$.medicoEspecialidad.$model"
                        />
                      </div>
                      <div class="col-5">
                        <label class="fw-bold">ID Médico</label>
                        <input
                          type="number"
                          class="form-control form-control-sm"
                          v-model.number="v$.medicoId.$model"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div class="card shadow-sm border-0 mb-4">
              <div
                class="card-header bg-white py-2 d-flex justify-content-between align-items-center"
              >
                <h6 class="mb-0 text-primary fw-bold text-uppercase small">
                  Medicamentos Entregados
                </h6>
                <button
                  type="button"
                  class="btn btn-outline-primary btn-sm"
                  @click="agregarMedicamentoRow()"
                >
                  <font-awesome-icon icon="plus"></font-awesome-icon> Agregar Fármaco
                </button>
              </div>
              <div class="card-body py-3">
                <div class="table-responsive" v-if="listaMedsEdicion">
                  <table class="table table-sm table-hover align-middle">
                    <thead class="table-primary text-primary-emphasis small text-center">
                      <tr>
                        <th style="width: 10%">ID</th>
                        <th style="width: 25%">Medicamento</th>
                        <th style="width: 12%">Cantidad</th>
                        <th style="width: 43%">Indicaciones / Observaciones</th>
                        <th style="width: 10%">Acción</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="(med, index) in listaMedsEdicion" :key="index">
                        <td>
                          <input
                            type="text"
                            class="form-control form-control-sm font-monospace bg-light"
                            v-model="med.id"
                            placeholder="ID"
                            readonly
                          />
                        </td>
                        <td>
                          <input
                            type="text"
                            class="form-control form-control-sm fw-bold"
                            v-model="med.nombre"
                            list="lista-inventario-meds"
                            @change="onMedicamentoChange(index)"
                            placeholder="Buscar en inventario..."
                          />
                        </td>
                        <td>
                          <div class="input-group input-group-sm">
                            <input
                              type="number"
                              class="form-control text-center"
                              v-model.number="med.cantidad"
                              min="1"
                            />
                            <span class="input-group-text p-1">pz</span>
                          </div>
                        </td>
                        <td>
                          <input
                            type="text"
                            class="form-control form-control-sm"
                            v-model="med.observaciones"
                            placeholder="Indicaciones..."
                          />
                        </td>
                        <td class="text-center">
                          <button
                            type="button"
                            class="btn btn-outline-danger btn-sm border-0"
                            @click="removerMedicamentoRow(index)"
                          >
                            <font-awesome-icon icon="trash"></font-awesome-icon>
                          </button>
                        </td>
                      </tr>
                      <tr v-if="listaMedsEdicion.length === 0">
                        <td colspan="5" class="text-center py-3 text-muted italic small">
                          No hay medicamentos en la lista. Presione "Agregar Fármaco".
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            <datalist id="lista-inventario-meds" v-if="listaInventario">
              <option v-for="item in listaInventario" :key="item.id" :value="item.nombre">
                Lote: {{ item.lote }} | Stock: {{ item.cantidad }}
              </option>
            </datalist>

            <div class="card shadow-sm border-0">
              <div class="card-body row py-3">
                <div class="form-group col-md-4">
                  <label class="form-label small fw-bold">Registró</label>
                  <input
                    type="text"
                    class="form-control form-control-sm bg-light"
                    v-model="v$.usuarioQueRegistro.$model"
                    readonly
                  />
                </div>
                <div class="form-group col-md-4">
                  <label class="form-label small fw-bold">Autorizó</label>
                  <input
                    type="text"
                    class="form-control form-control-sm"
                    v-model="v$.autorizo.$model"
                  />
                </div>
                <div class="form-group col-md-4">
                  <label class="form-label small fw-bold text-muted"
                    >Observaciones Generales</label
                  >
                  <input
                    type="text"
                    class="form-control form-control-sm"
                    v-model="v$.observaciones.$model"
                    placeholder="Notas del folio..."
                  />
                </div>
              </div>
            </div>
          </div>

          <div class="card-footer bg-white py-3 d-flex justify-content-between">
            <button
              type="button"
              class="btn btn-outline-secondary px-4 shadow-sm"
              @click="previousState()"
            >
              <font-awesome-icon icon="ban" class="me-1"></font-awesome-icon> Cancelar
            </button>
            <button
              type="submit"
              :disabled="v$.$invalid || isSaving"
              class="btn btn-primary px-5 shadow-sm fw-bold"
            >
              <font-awesome-icon icon="save" class="me-1"></font-awesome-icon> Guardar
              Registro
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./historico-update.component.ts"></script>

<style scoped>
.form-label {
  margin-bottom: 0.2rem;
}
.card {
  border-radius: 12px;
}
.table-primary {
  --bs-table-bg: #e7f1ff;
}
.font-monospace {
  font-family: monospace;
  font-size: 0.8rem;
}
.italic {
  font-style: italic;
}
.input-group-text {
  font-size: 0.7rem;
  background-color: #f8f9fa;
}
</style>
