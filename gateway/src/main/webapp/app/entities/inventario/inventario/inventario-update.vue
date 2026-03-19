<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.inventarioInventario.home.createOrEditLabel"
          data-cy="InventarioCreateUpdateHeading"
          v-text="t$('gatewayApp.inventarioInventario.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="inventario.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="inventario.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.inventarioInventario.claveMedicamento')"
              for="inventario-claveMedicamento"
            ></label>
            <input
              type="text"
              class="form-control"
              name="claveMedicamento"
              id="inventario-claveMedicamento"
              data-cy="claveMedicamento"
              :class="{ valid: !v$.claveMedicamento.$invalid, invalid: v$.claveMedicamento.$invalid }"
              v-model="v$.claveMedicamento.$model"
              required
            />
            <div v-if="v$.claveMedicamento.$anyDirty && v$.claveMedicamento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.claveMedicamento.$errors" :key="error.$uid">{{
                error.$message
              }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.inventarioInventario.nombre')" for="inventario-nombre"></label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="inventario-nombre"
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
              v-text="t$('gatewayApp.inventarioInventario.presentacion')"
              for="inventario-presentacion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="presentacion"
              id="inventario-presentacion"
              data-cy="presentacion"
              :class="{ valid: !v$.presentacion.$invalid, invalid: v$.presentacion.$invalid }"
              v-model="v$.presentacion.$model"
              required
            />
            <div v-if="v$.presentacion.$anyDirty && v$.presentacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.presentacion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.inventarioInventario.lote')" for="inventario-lote"></label>
            <input
              type="text"
              class="form-control"
              name="lote"
              id="inventario-lote"
              data-cy="lote"
              :class="{ valid: !v$.lote.$invalid, invalid: v$.lote.$invalid }"
              v-model="v$.lote.$model"
              required
            />
            <div v-if="v$.lote.$anyDirty && v$.lote.$invalid">
              <small class="form-text text-danger" v-for="error of v$.lote.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.inventarioInventario.cantidad')" for="inventario-cantidad"></label>
            <input
              type="number"
              class="form-control"
              name="cantidad"
              id="inventario-cantidad"
              data-cy="cantidad"
              :class="{ valid: !v$.cantidad.$invalid, invalid: v$.cantidad.$invalid }"
              v-model.number="v$.cantidad.$model"
              required
            />
            <div v-if="v$.cantidad.$anyDirty && v$.cantidad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.cantidad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.inventarioInventario.cantidadMinima')"
              for="inventario-cantidadMinima"
            ></label>
            <input
              type="number"
              class="form-control"
              name="cantidadMinima"
              id="inventario-cantidadMinima"
              data-cy="cantidadMinima"
              :class="{ valid: !v$.cantidadMinima.$invalid, invalid: v$.cantidadMinima.$invalid }"
              v-model.number="v$.cantidadMinima.$model"
              required
            />
            <div v-if="v$.cantidadMinima.$anyDirty && v$.cantidadMinima.$invalid">
              <small class="form-text text-danger" v-for="error of v$.cantidadMinima.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.inventarioInventario.fechaCaducidad')"
              for="inventario-fechaCaducidad"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="inventario-fechaCaducidad"
                  v-model="v$.fechaCaducidad.$model"
                  name="fechaCaducidad"
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
                id="inventario-fechaCaducidad"
                data-cy="fechaCaducidad"
                type="text"
                class="form-control"
                name="fechaCaducidad"
                :class="{ valid: !v$.fechaCaducidad.$invalid, invalid: v$.fechaCaducidad.$invalid }"
                v-model="v$.fechaCaducidad.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaCaducidad.$anyDirty && v$.fechaCaducidad.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaCaducidad.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.inventarioInventario.ubicacion')" for="inventario-ubicacion"></label>
            <input
              type="text"
              class="form-control"
              name="ubicacion"
              id="inventario-ubicacion"
              data-cy="ubicacion"
              :class="{ valid: !v$.ubicacion.$invalid, invalid: v$.ubicacion.$invalid }"
              v-model="v$.ubicacion.$model"
              required
            />
            <div v-if="v$.ubicacion.$anyDirty && v$.ubicacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.ubicacion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('gatewayApp.inventarioInventario.controlado')" for="inventario-controlado"></label>
            <input
              type="checkbox"
              class="form-check"
              name="controlado"
              id="inventario-controlado"
              data-cy="controlado"
              :class="{ valid: !v$.controlado.$invalid, invalid: v$.controlado.$invalid }"
              v-model="v$.controlado.$model"
              required
            />
            <div v-if="v$.controlado.$anyDirty && v$.controlado.$invalid">
              <small class="form-text text-danger" v-for="error of v$.controlado.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./inventario-update.component.ts"></script>
