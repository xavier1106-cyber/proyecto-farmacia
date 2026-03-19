<template>
  <div>
    <h2 id="page-heading" data-cy="MovimientoInventarioHeading">
      <span
        v-text="t$('gatewayApp.inventarioMovimientoInventario.home.title')"
        id="movimiento-inventario-heading"
      ></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span
            v-text="t$('gatewayApp.inventarioMovimientoInventario.home.refreshListLabel')"
          ></span>
        </button>
        <router-link
          :to="{ name: 'MovimientoInventarioCreate' }"
          custom
          v-slot="{ navigate }"
        >
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-movimiento-inventario"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span
              v-text="t$('gatewayApp.inventarioMovimientoInventario.home.createLabel')"
            ></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div
      class="alert alert-warning"
      v-if="!isFetching && movimientoInventarios && movimientoInventarios.length === 0"
    >
      <span v-text="t$('gatewayApp.inventarioMovimientoInventario.home.notFound')"></span>
    </div>
    <div
      class="table-responsive"
      v-if="movimientoInventarios && movimientoInventarios.length > 0"
    >
      <table class="table table-striped" aria-describedby="movimientoInventarios">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'id'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('tipoMovimiento')">
              <span
                v-text="t$('gatewayApp.inventarioMovimientoInventario.tipoMovimiento')"
              ></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'tipoMovimiento'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('cantidad')">
              <span
                v-text="t$('gatewayApp.inventarioMovimientoInventario.cantidad')"
              ></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'cantidad'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fecha')">
              <span v-text="t$('gatewayApp.inventarioMovimientoInventario.fecha')"></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'fecha'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('observacion')">
              <span
                v-text="t$('gatewayApp.inventarioMovimientoInventario.observacion')"
              ></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'observacion'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('inventario.nombre')">
              <span
                v-text="t$('gatewayApp.inventarioMovimientoInventario.inventario')"
              ></span>
              <jhi-sort-indicator
                :current-order="propOrder"
                :reverse="reverse"
                :field-name="'inventario.nombre'"
              ></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="movimientoInventario in movimientoInventarios"
            :key="movimientoInventario.id"
            data-cy="entityTable"
          >
            <td>
              <router-link
                :to="{
                  name: 'MovimientoInventarioView',
                  params: { movimientoInventarioId: movimientoInventario.id },
                }"
                >{{ movimientoInventario.id }}</router-link
              >
            </td>
            <td
              v-text="
                t$('gatewayApp.TipoMovimiento.' + movimientoInventario.tipoMovimiento)
              "
            ></td>
            <td>{{ movimientoInventario.cantidad }}</td>
            <td>{{ movimientoInventario.fecha }}</td>
            <td>{{ movimientoInventario.observacion }}</td>
            <td>
             <td>
          <div v-if="movimientoInventario.inventario && movimientoInventario.inventario.id">
            <router-link :to="{ name: 'InventarioView', params: { inventarioId: movimientoInventario.inventario.id } }">
              {{ movimientoInventario.inventario.nombre }}
            </router-link>
          </div>
  <span v-else class="text-muted">Sin inventario</span>
</td>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{
                    name: 'MovimientoInventarioView',
                    params: { movimientoInventarioId: movimientoInventario.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button
                    @click="navigate"
                    class="btn btn-info btn-sm details"
                    data-cy="entityDetailsButton"
                  >
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span
                      class="d-none d-md-inline"
                      v-text="t$('entity.action.view')"
                    ></span>
                  </button>
                </router-link>
                <router-link
                  :to="{
                    name: 'MovimientoInventarioEdit',
                    params: { movimientoInventarioId: movimientoInventario.id },
                  }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button
                    @click="navigate"
                    class="btn btn-primary btn-sm edit"
                    data-cy="entityEditButton"
                  >
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span
                      class="d-none d-md-inline"
                      v-text="t$('entity.action.edit')"
                    ></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(movimientoInventario)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span
                    class="d-none d-md-inline"
                    v-text="t$('entity.action.delete')"
                  ></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="gatewayApp.inventarioMovimientoInventario.delete.question"
          data-cy="movimientoInventarioDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-movimientoInventario-heading"
          v-text="
            t$('gatewayApp.inventarioMovimientoInventario.delete.question', {
              id: removeId,
            })
          "
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button
            type="button"
            class="btn btn-secondary"
            v-text="t$('entity.action.cancel')"
            @click="closeDialog()"
          ></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-movimientoInventario"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeMovimientoInventario()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="movimientoInventarios && movimientoInventarios.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count
          :page="page"
          :total="queryCount"
          :itemsPerPage="itemsPerPage"
        ></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination
          size="md"
          :total-rows="totalItems"
          v-model="page"
          :per-page="itemsPerPage"
        ></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./movimiento-inventario.component.ts"></script>
