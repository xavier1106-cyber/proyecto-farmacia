import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import InventarioService from './inventario.service';
import { type IInventario } from '@/shared/model/inventario/inventario.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'InventarioDetails',
  setup() {
    const inventarioService = inject('inventarioService', () => new InventarioService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const inventario: Ref<IInventario> = ref({});

    const retrieveInventario = async inventarioId => {
      try {
        const res = await inventarioService().find(inventarioId);
        inventario.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.inventarioId) {
      retrieveInventario(route.params.inventarioId);
    }

    return {
      alertService,
      inventario,

      previousState,
      t$: useI18n().t,
    };
  },
});
