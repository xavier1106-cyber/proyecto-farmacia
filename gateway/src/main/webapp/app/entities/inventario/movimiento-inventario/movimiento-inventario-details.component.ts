import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MovimientoInventarioService from './movimiento-inventario.service';
import { type IMovimientoInventario } from '@/shared/model/inventario/movimiento-inventario.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MovimientoInventarioDetails',
  setup() {
    const movimientoInventarioService = inject('movimientoInventarioService', () => new MovimientoInventarioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const movimientoInventario: Ref<IMovimientoInventario> = ref({});

    const retrieveMovimientoInventario = async movimientoInventarioId => {
      try {
        const res = await movimientoInventarioService().find(movimientoInventarioId);
        movimientoInventario.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.movimientoInventarioId) {
      retrieveMovimientoInventario(route.params.movimientoInventarioId);
    }

    return {
      alertService,
      movimientoInventario,

      previousState,
      t$: useI18n().t,
    };
  },
});
