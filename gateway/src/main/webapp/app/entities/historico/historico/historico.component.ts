import { type Ref, defineComponent, inject, onMounted, ref, watch, computed } from 'vue';
import { useI18n } from 'vue-i18n';

import HistoricoService from './historico.service';
import { type IHistorico } from '@/shared/model/historico/historico.model';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Historico',
  setup() {
    const { t: t$ } = useI18n();
    const dateFormat = useDateFormat();
    const dataUtils = useDataUtils();
    const historicoService = inject('historicoService', () => new HistoricoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    // ---- Paginación y orden ----
    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(0);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    // ---- Datos ----
    const historicos: Ref<IHistorico[]> = ref([]);
    const isFetching = ref(false);

    // ---- NUEVO: Filtro de buscador ----
    const filtroTexto = ref('');

    // ---- Funciones ----
    const clear = () => {
      page.value = 1;
      filtroTexto.value = '';
      retrieveHistoricos();
    };

       const esJsonValido = (texto: string): boolean => {
        if (!texto) return false;
        const t = texto.trim();
        return t.startsWith('[') && t.endsWith(']');
      };

      const parsearMeds = (texto: string): any[] => {
        try {
          return JSON.parse(texto);
        } catch (e) {
          return [];
        }
      };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') {
        result.push('id');
      }
      return result;
    };

    const retrieveHistoricos = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = {
          page: page.value - 1,
          size: itemsPerPage.value,
          sort: sort(),
        };
        const res = await historicoService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']) || 0;
        queryCount.value = totalItems.value;
        historicos.value = res.data || [];
      } catch (err: any) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveHistoricos();
    };

    onMounted(async () => {
      await retrieveHistoricos();
    });

    // ---- Búsqueda local filtrada ----
    const historicosFiltrados = computed(() => {
      const lista = historicos.value || [];
      if (!filtroTexto.value) return lista;

      const f = filtroTexto.value.toLowerCase().trim();
      return lista.filter(h => {
        return (
          (h.folio && h.folio.toLowerCase().includes(f)) ||
          (h.pacienteNombre && h.pacienteNombre.toLowerCase().includes(f)) ||
          (h.medicoNombre && h.medicoNombre.toLowerCase().includes(f))
        );
      });
    });

    watch(filtroTexto, () => {
      page.value = 1; // reset de página al filtrar
    });

    // ---- Eliminación ----
    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);

    const prepareRemove = (instance: IHistorico) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };

    const closeDialog = () => {
      removeEntity.value.hide();
    };

    const removeHistorico = async () => {
      try {
        await historicoService().delete(removeId.value);
        const message = t$('historicoApp.historicoHistorico.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveHistoricos();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    // ---- Ordenamiento ----
    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) {
        reverse.value = !reverse.value;
      } else {
        reverse.value = false;
      }
      propOrder.value = newOrder;
    };

    // ---- Watchers ----
    watch([propOrder, reverse], async () => {
      if (page.value === 1) {
        await retrieveHistoricos();
      } else {
        clear();
      }
    });

    watch(page, async () => {
      await retrieveHistoricos();
    });

    // ---- Retorno ----
    return {
      historicos,
      historicosFiltrados, 
      filtroTexto,          
      esJsonValido,   
      parsearMeds,    
      handleSyncList,
      isFetching,
      retrieveHistoricos,
      clear,
      ...dateFormat,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeHistorico,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      t$,
      ...dataUtils,
    };
  },
});