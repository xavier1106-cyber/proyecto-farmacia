import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import HistoricoService from './historico.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat } from '@/shared/composables';
import { type IHistorico } from '@/shared/model/historico/historico.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistoricoDetails',
  setup() {
    const dateFormat = useDateFormat();
    const historicoService = inject('historicoService', () => new HistoricoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();
    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const historico: Ref<IHistorico> = ref({});

    const retrieveHistorico = async (historicoId: string | number) => {
      try {
        const res = await historicoService().find(historicoId);
        historico.value = res;
      } catch (error: any) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.historicoId) {
      retrieveHistorico(route.params.historicoId as string);
    }

    // --- NUEVAS FUNCIONES PARA EL TEMPLATE ---

    const esJsonValido = (datos: string | any): boolean => {
      if (!datos || typeof datos !== 'string') return false;
      try {
        const res = JSON.parse(datos);
        return Array.isArray(res); // Verifica que sea una lista de medicamentos
      } catch (e) {
        return false;
      }
    };

    const parsearMeds = (datos: string | any) => {
      if (typeof datos !== 'string') return datos;
      try {
        return JSON.parse(datos);
      } catch (e) {
        return [];
      }
    };

    const imprimirReceta = () => {
      window.print();
    };

    // ------------------------------------------

    return {
      ...dateFormat,
      alertService,
      historico,
      ...dataUtils,
      previousState,
      esJsonValido,
      parsearMeds,
      imprimirReceta,
      t$: useI18n().t,
    };
  },
});