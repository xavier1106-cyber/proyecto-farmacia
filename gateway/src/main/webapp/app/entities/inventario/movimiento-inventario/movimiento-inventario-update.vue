<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="gatewayApp.inventarioMovimientoInventario.home.createOrEditLabel"
          data-cy="MovimientoInventarioCreateUpdateHeading"
          v-text="t$('gatewayApp.inventarioMovimientoInventario.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="movimientoInventario.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="movimientoInventario.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.inventarioMovimientoInventario.tipoMovimiento')"
              for="movimiento-inventario-tipoMovimiento"
            ></label>
            <select
              class="form-control"
              name="tipoMovimiento"
              :class="{ valid: !v$.tipoMovimiento.$invalid, invalid: v$.tipoMovimiento.$invalid }"
              v-model="v$.tipoMovimiento.$model"
              id="movimiento-inventario-tipoMovimiento"
              data-cy="tipoMovimiento"
              required
            >
              <option
                v-for="tipoMovimiento in tipoMovimientoValues"
                :key="tipoMovimiento"
                :value="tipoMovimiento"
                :label="t$('gatewayApp.TipoMovimiento.' + tipoMovimiento)"
              >
                {{ tipoMovimiento }}
              </option>
            </select>
            <div v-if="v$.tipoMovimiento.$anyDirty && v$.tipoMovimiento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.tipoMovimiento.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.inventarioMovimientoInventario.cantidad')"
              for="movimiento-inventario-cantidad"
            ></label>
            <input
              type="number"
              class="form-control"
              name="cantidad"
              id="movimiento-inventario-cantidad"
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
              v-text="t$('gatewayApp.inventarioMovimientoInventario.fecha')"
              for="movimiento-inventario-fecha"
            ></label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="movimiento-inventario-fecha"
                  v-model="v$.fecha.$model"
                  name="fecha"
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
                id="movimiento-inventario-fecha"
                data-cy="fecha"
                type="text"
                class="form-control"
                name="fecha"
                :class="{ valid: !v$.fecha.$invalid, invalid: v$.fecha.$invalid }"
                v-model="v$.fecha.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fecha.$anyDirty && v$.fecha.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fecha.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.inventarioMovimientoInventario.observacion')"
              for="movimiento-inventario-observacion"
            ></label>
            <input
              type="text"
              class="form-control"
              name="observacion"
              id="movimiento-inventario-observacion"
              data-cy="observacion"
              :class="{ valid: !v$.observacion.$invalid, invalid: v$.observacion.$invalid }"
              v-model="v$.observacion.$model"
            />
            <div v-if="v$.observacion.$anyDirty && v$.observacion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.observacion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('gatewayApp.inventarioMovimientoInventario.inventario')"
              for="movimiento-inventario-inventario"
            ></label>
            <select
              class="form-control"
              id="movimiento-inventario-inventario"
              data-cy="inventario"
              name="inventario"
              v-model="movimientoInventario.inventario"
            >
              <option :value="null"></option>
              <option
                :value="
                  movimientoInventario.inventario && inventarioOption.id === movimientoInventario.inventario.id
                    ? movimientoInventario.inventario
                    : inventarioOption
                "
                v-for="inventarioOption in inventarios"
                :key="inventarioOption.id"
              >
                {{ inventarioOption.nombre }}
              </option>
            </select>
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
<script lang="ts" src="./movimiento-inventario-update.component.ts"></script>
