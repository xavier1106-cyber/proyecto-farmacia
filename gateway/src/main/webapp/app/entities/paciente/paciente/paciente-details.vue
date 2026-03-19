<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paciente">
        <h2 class="jh-entity-heading" data-cy="pacienteDetailsHeading">
          <span v-text="t$('gatewayApp.pacientePaciente.detail.title')"></span> {{ paciente.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('gatewayApp.pacientePaciente.curp')"></span>
          </dt>
          <dd>
            <span>{{ paciente.curp }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientePaciente.nombre')"></span>
          </dt>
          <dd>
            <span>{{ paciente.nombre }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientePaciente.fechaNacimiento')"></span>
          </dt>
          <dd>
            <span>{{ paciente.fechaNacimiento }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientePaciente.sexo')"></span>
          </dt>
          <dd>
            <span>{{ paciente.sexo }}</span>
          </dd>
          <dt>
            <span v-text="t$('gatewayApp.pacientePaciente.numeroSeguroSocial')"></span>
          </dt>
          <dd>
            <span>{{ paciente.numeroSeguroSocial }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="paciente.id" :to="{ name: 'PacienteEdit', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paciente-details.component.ts"></script>
