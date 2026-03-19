<template>
  <div>
    <div class="d-flex justify-content-between align-items-center flex-wrap mb-3">
      <h2 id="page-heading" data-cy="InventarioHeading" class="mb-0">
        <span
          v-text="t$('gatewayApp.inventarioInventario.home.title')"
          id="inventario-heading"
        ></span>
      </h2>

      <div class="d-flex align-items-center">
        <div class="mr-2" style="width: 350px">
          <div class="input-group shadow-sm">
            <span class="input-group-text bg-white border-end-0">
              <font-awesome-icon icon="search" class="text-muted" />
            </span>
            <input
              type="text"
              class="form-control border-start-0"
              placeholder="Buscar medicamento..."
              v-model="filtroTexto"
            />
          </div>
        </div>

        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
        </button>

        <router-link :to="{ name: 'InventarioCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon> Resurtir
          </button>
        </router-link>
      </div>
    </div>

    <div class="mb-3 d-flex gap-2">
      <button class="btn btn-success" @click="showEntradaModal = true">
        <font-awesome-icon icon="plus-circle"></font-awesome-icon> Entrada
      </button>
      <!--
      <button class="btn btn-warning">
        <font-awesome-icon icon="minus-circle"></font-awesome-icon> Salida
      </button>
      -->
      <button class="btn btn-secondary" @click="irMovimientos">
        <font-awesome-icon icon="history"></font-awesome-icon> Movimientos
      </button>
      <button class="btn btn-dark">
        <font-awesome-icon icon="chart-line"></font-awesome-icon> Estadísticas
      </button>
    </div>

    <div
      class="table-responsive"
      v-if="inventariosFiltrados && inventariosFiltrados.length > 0"
    >
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Clave</th>
            <th>Nombre</th>
            <th>Presentación</th>
            <th>Lote</th>
            <th>Cantidad</th>
            <th>Mínimo</th>
            <th>Caducidad</th>
            <th>Ubicación</th>
            <th>Controlado</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inventario in inventariosFiltrados" :key="inventario.id">
            <td>{{ inventario.id }}</td>
            <td>{{ inventario.claveMedicamento }}</td>
            <td>{{ inventario.nombre }}</td>
            <td>{{ inventario.presentacion }}</td>
            <td>{{ inventario.lote }}</td>
            <td>{{ inventario.cantidad }}</td>
            <td>{{ inventario.cantidadMinima }}</td>
            <td>{{ inventario.fechaCaducidad }}</td>
            <td>{{ inventario.ubicacion }}</td>
            <td>{{ inventario.controlado }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{
                    name: 'InventarioView',
                    params: { inventarioId: inventario.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                  </button>
                </router-link>
                <router-link
                  :to="{
                    name: 'InventarioEdit',
                    params: { inventarioId: inventario.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(inventario)"
                  variant="danger"
                  class="btn btn-sm"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <b-modal v-model="showEntradaModal" title="Registrar Entrada" hide-footer size="md">
      <div class="mb-3">
        <label class="form-label">Medicamento</label>
        <input
          list="medicamentos-data"
          class="form-control"
          v-model="entrada.nombre"
          placeholder="Escribe para buscar medicamento..."
        />
        <datalist id="medicamentos-data">
          <option v-for="inv in inventarios" :key="inv.id" :value="inv.nombre">
            Lote: {{ inv.lote }} | Stock: {{ inv.cantidad }}
          </option>
        </datalist>
      </div>

      <div class="mb-3">
        <label class="form-label">Cantidad</label>
        <input
          type="number"
          class="form-control"
          v-model.number="entrada.cantidad"
          placeholder="Cantidad a ingresar"
          min="1"
        />
      </div>

      <div class="mb-3">
        <label class="form-label">Observación</label>
        <input
          type="text"
          class="form-control"
          v-model="entrada.observacion"
          placeholder="Ej: Compra directa"
        />
      </div>

      <div class="d-flex justify-content-end gap-2">
        <b-button variant="secondary" @click="showEntradaModal = false"
          >Cancelar</b-button
        >
        <b-button
          variant="success"
          @click="guardarEntrada"
          :disabled="!entrada.nombre || entrada.cantidad <= 0"
          >Registrar</b-button
        >
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./inventario.component.ts"></script>
