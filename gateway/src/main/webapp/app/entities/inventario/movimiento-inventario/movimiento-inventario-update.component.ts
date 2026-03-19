import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MovimientoInventarioService from './movimiento-inventario.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import InventarioService from '@/entities/inventario/inventario/inventario.service';
import { type IInventario } from '@/shared/model/inventario/inventario.model';
import { type IMovimientoInventario, MovimientoInventario } from '@/shared/model/inventario/movimiento-inventario.model';
import { TipoMovimiento } from '@/shared/model/enumerations/tipo-movimiento.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MovimientoInventarioUpdate',
  setup() {
    const movimientoInventarioService = inject('movimientoInventarioService', () => new MovimientoInventarioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const movimientoInventario: Ref<IMovimientoInventario> = ref(new MovimientoInventario());

    const inventarioService = inject('inventarioService', () => new InventarioService());

    const inventarios: Ref<IInventario[]> = ref([]);
    const tipoMovimientoValues: Ref<string[]> = ref(Object.keys(TipoMovimiento));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMovimientoInventario = async movimientoInventarioId => {
      try {
        const res = await movimientoInventarioService().find(movimientoInventarioId);
        movimientoInventario.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    const movimientoInventarioId = route.params.movimientoInventarioId;

if (movimientoInventarioId) {
  retrieveMovimientoInventario(Number(movimientoInventarioId));
}

    const initRelationships = () => {
      inventarioService()
        .retrieve()
        .then(res => {
          inventarios.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      tipoMovimiento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      cantidad: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 1 }).toString(), 1),
      },
      fecha: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      observacion: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 250 }).toString(), 250),
      },
      inventario: {},
    };
    const v$ = useVuelidate(validationRules, movimientoInventario as any);
    v$.value.$validate();

    return {
      movimientoInventarioService,
      alertService,
      movimientoInventario,
      previousState,
      tipoMovimientoValues,
      isSaving,
      currentLanguage,
      inventarios,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.movimientoInventario.id) {
        this.movimientoInventarioService()
          .update(this.movimientoInventario)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('inventarioApp.inventarioMovimientoInventario.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.movimientoInventarioService()
          .create(this.movimientoInventario)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('inventarioApp.inventarioMovimientoInventario.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
