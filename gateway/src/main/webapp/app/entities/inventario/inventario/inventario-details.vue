<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="inventario">
        <h2 class="jh-entity-heading" data-cy="inventarioDetailsHeading">
          <span v-text="t$('gatewayApp.inventarioInventario.detail.title')"></span> {{ inventario.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.claveMedicamento')"></span>
          </dt>
          <dd>
            <span>{{ inventario.claveMedicamento }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.nombre')"></span>
          </dt>
          <dd>
            <span>{{ inventario.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.presentacion')"></span>
          </dt>
          <dd>
            <span>{{ inventario.presentacion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.lote')"></span>
          </dt>
          <dd>
            <span>{{ inventario.lote }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.cantidad')"></span>
          </dt>
          <dd>
            <span>{{ inventario.cantidad }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.cantidadMinima')"></span>
          </dt>
          <dd>
            <span>{{ inventario.cantidadMinima }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.fechaCaducidad')"></span>
          </dt>
          <dd>
            <span>{{ inventario.fechaCaducidad }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.ubicacion')"></span>
          </dt>
          <dd>
            <span>{{ inventario.ubicacion }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.inventarioInventario.controlado')"></span>
          </dt>
          <dd>
            <span>{{ inventario.controlado }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="inventario.id"
          :to="{ name: 'InventarioEdit', params: { inventarioId: inventario.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./inventario-details.component.ts"></script>
