<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="medico">
        <h2 class="jh-entity-heading" data-cy="medicoDetailsHeading">
          <span v-text="t$('gatewayApp.medicoMedico.detail.title')"></span> {{ medico.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.medicoMedico.nombre')"></span>
          </dt>
          <dd>
            <span>{{ medico.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.medicoMedico.especialidad')"></span>
          </dt>
          <dd>
            <span>{{ medico.especialidad }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.medicoMedico.telefono')"></span>
          </dt>
          <dd>
            <span>{{ medico.telefono }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.medicoMedico.turno')"></span>
          </dt>
          <dd>
            <span>{{ medico.turno }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.medicoMedico.activo')"></span>
          </dt>
          <dd>
            <span>{{ medico.activo }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="medico.id" :to="{ name: 'MedicoEdit', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./medico-details.component.ts"></script>
