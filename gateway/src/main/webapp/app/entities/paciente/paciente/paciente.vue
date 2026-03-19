<template>
  <div>
    <h2 id="page-heading" data-cy="PacienteHeading">
      <span v-text="t$('gatewayApp.pacientePaciente.home.title')" id="paciente-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.pacientePaciente.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PacienteCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-paciente"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.pacientePaciente.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && pacientes && pacientes.length === 0">
      <span v-text="t$('gatewayApp.pacientePaciente.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="pacientes && pacientes.length > 0">
      <table class="table table-striped" aria-describedby="pacientes">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('curp')">
              <span v-text="t$('gatewayApp.pacientePaciente.curp')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'curp'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span v-text="t$('gatewayApp.pacientePaciente.nombre')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('fechaNacimiento')">
              <span v-text="t$('gatewayApp.pacientePaciente.fechaNacimiento')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'fechaNacimiento'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('sexo')">
              <span v-text="t$('gatewayApp.pacientePaciente.sexo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sexo'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('numeroSeguroSocial')">
              <span v-text="t$('gatewayApp.pacientePaciente.numeroSeguroSocial')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'numeroSeguroSocial'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paciente in pacientes" :key="paciente.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PacienteView', params: { pacienteId: paciente.id } }">{{ paciente.id }}</router-link>
            </td>
            <td>{{ paciente.curp }}</td>
            <td>{{ paciente.nombre }}</td>
            <td>{{ paciente.fechaNacimiento }}</td>
            <td>{{ paciente.sexo }}</td>
            <td>{{ paciente.numeroSeguroSocial }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PacienteView', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PacienteEdit', params: { pacienteId: paciente.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(paciente)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
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
          id="gatewayApp.pacientePaciente.delete.question"
          data-cy="pacienteDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-paciente-heading" v-text="t$('gatewayApp.pacientePaciente.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-paciente"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePaciente()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="pacientes && pacientes.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./paciente.component.ts"></script>
