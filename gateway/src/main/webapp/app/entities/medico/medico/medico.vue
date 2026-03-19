<template>
  <div>
    <h2 id="page-heading" data-cy="MedicoHeading">
      <span v-text="t$('gatewayApp.medicoMedico.home.title')" id="medico-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('gatewayApp.medicoMedico.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'MedicoCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-medico"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('gatewayApp.medicoMedico.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && medicos && medicos.length === 0">
      <span v-text="t$('gatewayApp.medicoMedico.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="medicos && medicos.length > 0">
      <table class="table table-striped" aria-describedby="medicos">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('nombre')">
              <span v-text="t$('gatewayApp.medicoMedico.nombre')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'nombre'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('especialidad')">
              <span v-text="t$('gatewayApp.medicoMedico.especialidad')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'especialidad'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('telefono')">
              <span v-text="t$('gatewayApp.medicoMedico.telefono')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'telefono'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('turno')">
              <span v-text="t$('gatewayApp.medicoMedico.turno')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'turno'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('activo')">
              <span v-text="t$('gatewayApp.medicoMedico.activo')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'activo'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="medico in medicos" :key="medico.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'MedicoView', params: { medicoId: medico.id } }">{{ medico.id }}</router-link>
            </td>
            <td>{{ medico.nombre }}</td>
            <td>{{ medico.especialidad }}</td>
            <td>{{ medico.telefono }}</td>
            <td>{{ medico.turno }}</td>
            <td>{{ medico.activo }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'MedicoView', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'MedicoEdit', params: { medicoId: medico.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(medico)"
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
        <span id="gatewayApp.medicoMedico.delete.question" data-cy="medicoDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-medico-heading" v-text="t$('gatewayApp.medicoMedico.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-medico"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeMedico()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="medicos && medicos.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./medico.component.ts"></script>
