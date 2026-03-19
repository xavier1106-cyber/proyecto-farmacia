<template>
  <div>
    <b-dropdown-item to="/inventario">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.inventarioInventario')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/paciente">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.pacientePaciente')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/medico">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.medicoMedico')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/historico">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.historicoHistorico')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/movimiento-inventario">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.inventarioMovimientoInventario')"></span>
    </b-dropdown-item>

    <b-dropdown-item to="/buscador">
      <font-awesome-icon icon="asterisk" />
      <span>buscador</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
