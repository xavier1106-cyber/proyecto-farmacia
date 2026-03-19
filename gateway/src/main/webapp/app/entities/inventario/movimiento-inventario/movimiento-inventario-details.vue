<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="movimientoInventario">
        <h2 class="jh-entity-heading" data-cy="movimientoInventarioDetailsHeading">
          <span
            v-text="t$('gatewayApp.inventarioMovimientoInventario.detail.title')"
          ></span>
          {{ movimientoInventario.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span
              v-text="t$('gatewayApp.inventarioMovimientoInventario.tipoMovimiento')"
            ></span>
          </dt>
          <dd>
            <span
              v-text="
                t$('gatewayApp.TipoMovimiento.' + movimientoInventario.tipoMovimiento)
              "
            ></span>
          </dd>
          <dt>
            <span
              v-text="t$('gatewayApp.inventarioMovimientoInventario.cantidad')"
            ></span>
          </dt>
          <dd>
            <span>{{ movimientoInventario.cantidad }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioMovimientoInventario.fecha')"></span>
          </dt>
          <dd>
            <span>{{ movimientoInventario.fecha }}</span>
          </dd>
          <dt>
            <span
              v-text="t$('gatewayApp.inventarioMovimientoInventario.observacion')"
            ></span>
          </dt>
          <dd>
            <span>{{ movimientoInventario.observacion }}</span>
          </dd>
          <dt>
            <span
              v-text="t$('gatewayApp.inventarioMovimientoInventario.inventario')"
            ></span>
          </dt>
          <dd>
            <div
              v-if="movimientoInventario.inventario && movimientoInventario.inventario.id"
            >
              <router-link
                :to="{
                  name: 'InventarioView',
                  params: { inventarioId: movimientoInventario.inventario.id },
                }"
              >
                {{
                  movimientoInventario.inventario.nombre ||
                  movimientoInventario.inventario.id
                }}
              </router-link>
            </div>
            <span v-else>N/A</span>
          </dd>
        </dl>

        <button
          type="submit"
          @click.prevent="previousState()"
          class="btn btn-info"
          data-cy="entityDetailsBackButton"
        >
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span
            v-text="t$('entity.action.back')"
          ></span>
        </button>

        <router-link
          v-if="movimientoInventario.id"
          :to="{
            name: 'MovimientoInventarioEdit',
            params: { movimientoInventarioId: movimientoInventario.id },
          }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span
              v-text="t$('entity.action.edit')"
            ></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./movimiento-inventario-details.component.ts"></script>
