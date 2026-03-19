<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.medicoMedico.home.createOrEditLabel"
          data-cy="MedicoCreateUpdateHeading"
          v-text="t$('gatewayApp.medicoMedico.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="medico.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="medico.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.medicoMedico.nombre')" for="medico-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="medico-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.medicoMedico.especialidad')" for="medico-especialidad"></label>
            <input
              type="text"
              class="form-control"
              name="especialidad"
              id="medico-especialidad"
              data-cy="especialidad"
              :class="{ valid: !v$.especialidad.$invalid, invalid: v$.especialidad.$invalid }"
              v-model="v$.especialidad.$model"
              required
            />
            <div v-if="v$.especialidad.$anyDirty && v$.especialidad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.especialidad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.medicoMedico.telefono')" for="medico-telefono"></label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="medico-telefono"
              data-cy="telefono"
              :class="{ valid: !v$.telefono.$invalid, invalid: v$.telefono.$invalid }"
              v-model="v$.telefono.$model"
            />
            <div v-if="v$.telefono.$anyDirty && v$.telefono.$invalid">
              <small class="form-text text-danger" v-for="error of v$.telefono.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.medicoMedico.turno')" for="medico-turno"></label>
            <input
              type="text"
              class="form-control"
              name="turno"
              id="medico-turno"
              data-cy="turno"
              :class="{ valid: !v$.turno.$invalid, invalid: v$.turno.$invalid }"
              v-model="v$.turno.$model"
            />
            <div v-if="v$.turno.$anyDirty && v$.turno.$invalid">
              <small class="form-text text-danger" v-for="error of v$.turno.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.medicoMedico.activo')" for="medico-activo"></label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="medico-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
              required
            />
            <div v-if="v$.activo.$anyDirty && v$.activo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.activo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./medico-update.component.ts"></script>
