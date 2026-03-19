<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.pacientePaciente.home.createOrEditLabel"
          data-cy="PacienteCreateUpdateHeading"
          v-text="t$('gatewayApp.pacientePaciente.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="paciente.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="paciente.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientePaciente.curp')" for="paciente-curp"></label>
            <input
              type="text"
              class="form-control"
              name="curp"
              id="paciente-curp"
              data-cy="curp"
              :class="{ valid: !v$.curp.$invalid, invalid: v$.curp.$invalid }"
              v-model="v$.curp.$model"
              required
            />
            <div v-if="v$.curp.$anyDirty && v$.curp.$invalid">
              <small class="form-text text-danger" v-for="error of v$.curp.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientePaciente.nombre')" for="paciente-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="paciente-nombre"
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
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientePaciente.fechaNacimiento')"
              for="paciente-fechaNacimiento"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="paciente-fechaNacimiento"
                  v-model="v$.fechaNacimiento.$model"
                  name="fechaNacimiento"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="paciente-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !v$.fechaNacimiento.$invalid, invalid: v$.fechaNacimiento.$invalid }"
                v-model="v$.fechaNacimiento.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaNacimiento.$anyDirty && v$.fechaNacimiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaNacimiento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.pacientePaciente.sexo')" for="paciente-sexo"></label>
            <input
              type="text"
              class="form-control"
              name="sexo"
              id="paciente-sexo"
              data-cy="sexo"
              :class="{ valid: !v$.sexo.$invalid, invalid: v$.sexo.$invalid }"
              v-model="v$.sexo.$model"
              required
            />
            <div v-if="v$.sexo.$anyDirty && v$.sexo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.sexo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.pacientePaciente.numeroSeguroSocial')"
              for="paciente-numeroSeguroSocial"
            ></label>
            <input
              type="text"
              class="form-control"
              name="numeroSeguroSocial"
              id="paciente-numeroSeguroSocial"
              data-cy="numeroSeguroSocial"
              :class="{ valid: !v$.numeroSeguroSocial.$invalid, invalid: v$.numeroSeguroSocial.$invalid }"
              v-model="v$.numeroSeguroSocial.$model"
            />
            <div v-if="v$.numeroSeguroSocial.$anyDirty && v$.numeroSeguroSocial.$invalid">
              <small class="form-text text-danger" v-for="error of v$.numeroSeguroSocial.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
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
<script lang="ts" src="./paciente-update.component.ts"></script>
