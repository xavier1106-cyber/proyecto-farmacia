import { type Ref, defineComponent, inject, onMounted, ref, watch, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';

import InventarioService from './inventario.service';
import { type IInventario } from '@/shared/model/inventario/inventario.model';
import { useAlertService } from '@/shared/alert/alert.service';

import MovimientoInventarioService from '@/entities/inventario/movimiento-inventario/movimiento-inventario.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Inventario',
  setup() {
    const { t: t$ } = useI18n();
    const router = useRouter();

    // Servicios
    const inventarioService = inject('inventarioService', () => new InventarioService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const movimientoService = inject('movimientoInventarioService', () => new MovimientoInventarioService());

    // Variables de paginación y orden
    const itemsPerPage = ref(20);
    const queryCount: Ref<number> = ref(null);
    const page: Ref<number> = ref(1);
    const propOrder = ref('id');
    const reverse = ref(false);
    const totalItems = ref(0);

    // Inventarios y estado
    const inventarios: Ref<IInventario[]> = ref([]);
    const isFetching = ref(false);

    // Variables para entradas
    const showEntradaModal = ref(false);
    const entrada = ref({
      nombre: '',
      cantidad: 0,
      observacion: '',
    });


    const filtroTexto = ref('');
    const inventariosFiltrados = computed(() => {
      if (!filtroTexto.value) return inventarios.value;
      const texto = filtroTexto.value.toLowerCase();
      return inventarios.value.filter(
        (inv) =>
          inv.nombre?.toLowerCase().includes(texto) ||
          inv.claveMedicamento?.toLowerCase().includes(texto) ||
          inv.lote?.toLowerCase().includes(texto) ||
          inv.presentacion?.toLowerCase().includes(texto) ||
          inv.ubicacion?.toLowerCase().includes(texto)
      );
    });


    const irMovimientos = () => {
      router.push({ name: 'MovimientoInventario' });
    };

    const clear = () => { page.value = 1; };

    const sort = (): Array<any> => {
      const result = [`${propOrder.value},${reverse.value ? 'desc' : 'asc'}`];
      if (propOrder.value !== 'id') result.push('id');
      return result;
    };

    const retrieveInventarios = async () => {
      isFetching.value = true;
      try {
        const paginationQuery = { page: page.value - 1, size: itemsPerPage.value, sort: sort() };
        const res = await inventarioService().retrieve(paginationQuery);
        totalItems.value = Number(res.headers['x-total-count']);
        queryCount.value = totalItems.value;
        inventarios.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => { retrieveInventarios(); };

    onMounted(async () => { await retrieveInventarios(); });

  
    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);

    const prepareRemove = (instance: IInventario) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };

    const closeDialog = () => { removeEntity.value.hide(); };

    const removeInventario = async () => {
      try {
        await inventarioService().delete(removeId.value);
        const message = t$('inventarioApp.inventarioInventario.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveInventarios();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

   
    const changeOrder = (newOrder: string) => {
      if (propOrder.value === newOrder) reverse.value = !reverse.value;
      else reverse.value = false;
      propOrder.value = newOrder;
    };

    watch([propOrder, reverse], async () => {
      if (page.value === 1) await retrieveInventarios();
      else clear();
    });

    watch(page, async () => { await retrieveInventarios(); });

  
    const guardarEntrada = async () => {
      try {
        //Validaciones básicas
        if (!entrada.value.nombre || entrada.value.cantidad <= 0) {
          alertService.showInfo('Por favor, selecciona un medicamento y una cantidad válida', { variant: 'warning' });
          return;
        }

        //Buscar el objeto inventario real por el nombre seleccionado en el datalist
        const inventarioExistente = inventarios.value.find(
          i => i.nombre?.toLowerCase().trim() === entrada.value.nombre.toLowerCase().trim()
        );

        if (!inventarioExistente) {
          alertService.showInfo(`No se encontró el medicamento "${entrada.value.nombre}" en el inventario actual.`, { variant: 'danger' });
          return;
        }

        //preparar el objeto del Movimiento para JHipster
        const nuevoMovimiento = {
          tipoMovimiento: 'ENTRADA',
          cantidad: entrada.value.cantidad,
          fecha: new Date().toISOString(), // Formato ISO string para mayor compatibilidad
          observacion: entrada.value.observacion,
          inventario: {
            id: inventarioExistente.id,
            nombre: inventarioExistente.nombre // Ayuda a la persistencia visual inmediata
          }
        };

        //Registrar movimiento en el backend
        await movimientoService().create(nuevoMovimiento);

        //Actualiza el stock físicamente (en la base de datos de inventario)
        inventarioExistente.cantidad += entrada.value.cantidad;
        await inventarioService().update(inventarioExistente);

        alertService.showInfo('Entrada registrada y stock actualizado con éxito', { variant: 'success' });

        // 6. Limpieza y refresco de UI
        showEntradaModal.value = false;
        entrada.value = { nombre: '', cantidad: 0, observacion: '' };
        
        // Volver a cargar la lista para ver el nuevo stock reflejado en la tabla
        await retrieveInventarios();

      } catch (error) {
        console.error('Error al guardar entrada:', error);
        alertService.showHttpError(error.response);
      }
    };

    return {
      inventarios,
      inventariosFiltrados,
      filtroTexto,
      irMovimientos,
      handleSyncList,
      isFetching,
      retrieveInventarios,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeInventario,
      itemsPerPage,
      queryCount,
      page,
      propOrder,
      reverse,
      totalItems,
      changeOrder,
      t$,
      showEntradaModal,
      entrada,
      guardarEntrada,
    };
  },
});