import { defineComponent, inject, onMounted, ref, computed } from 'vue';
import { Bar, Pie } from 'vue-chartjs'; 
import { Modal } from 'bootstrap';

import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  ArcElement,
  PointElement,
  LineElement,
} from 'chart.js';

import MovimientoInventarioService from '@/entities/inventario/movimiento-inventario/movimiento-inventario.service';
import InventarioService from './inventario.service';

ChartJS.register(
  Title, Tooltip, Legend, BarElement, CategoryScale, 
  LinearScale, ArcElement, PointElement, LineElement
);

export default defineComponent({
  name: 'GraficasInventario',
  components: { BarChart: Bar, PieChart: Pie },
  setup() {
    const movimientoService = inject('movimientoInventarioService', () => new MovimientoInventarioService());
    const inventarioService = inject('inventarioService', () => new InventarioService());
    
    const isFetching = ref(false);
    const isSaving = ref(false);
    const inventarioCompleto = ref<any[]>([]); 

    // Colores Institucionales INFOTEC
    const COLORES = {
      VINO: '#611232',
      GUINDA: '#9B2247',
      OCRE: '#A57F2C',
      GRIS: '#98989A',
      EXITO: '#2E7D32',
      BORDE_VINO: 'rgba(97, 18, 50, 0.8)'
    };

    // --- LÓGICA DE ALERTAS ---
    const alertasStockBajo = computed(() => {
      return inventarioCompleto.value.filter(item => (item.cantidad || 0) <= 50);
    });

    const alertasCaducidad = computed(() => {
      const hoy = new Date();
      const limiteCercano = new Date();
      limiteCercano.setDate(hoy.getDate() + 30);
      return inventarioCompleto.value
        .filter(item => item.fechaCaducidad && new Date(item.fechaCaducidad) <= limiteCercano)
        .sort((a, b) => new Date(a.fechaCaducidad).getTime() - new Date(b.fechaCaducidad).getTime());
    });

    const getBadgeCaducidad = (fecha: string) => {
      const hoy = new Date();
      hoy.setHours(0, 0, 0, 0);
      const vencimiento = new Date(fecha);
      return vencimiento < hoy ? 'bg-vino text-white' : 'bg-ocre text-white';
    };

    // --- CARGA DE DATOS ---
    const topEntregadosLabels = ref<string[]>([]);
    const topEntregadosValues = ref<number[]>([]);
    const datosMovimientos = ref({ entradas: 0, salidas: 0 });
    const etiquetasStock = ref<string[]>([]);
    const stockActualValues = ref<number[]>([]);
    const stockMinimoValues = ref<number[]>([]);

    const cargarDatos = async () => {
      isFetching.value = true;
      try {
        const resMov = await movimientoService().retrieve({ size: 500, sort: ['fecha,desc'] });
        const movimientos = resMov.data || [];
        
        datosMovimientos.value.entradas = movimientos.filter(m => m.tipoMovimiento === 'ENTRADA').reduce((acc, m) => acc + (m.cantidad || 0), 0);
        datosMovimientos.value.salidas = movimientos.filter(m => m.tipoMovimiento === 'SALIDA').reduce((acc, m) => acc + (m.cantidad || 0), 0);

        const mapaEntregas: Record<string, number> = {};
        movimientos.filter(m => m.tipoMovimiento === 'SALIDA').forEach(m => {
          const nombre = m.inventario?.nombre || 'Desconocido';
          mapaEntregas[nombre] = (mapaEntregas[nombre] || 0) + (m.cantidad || 0);
        });

        const ordenados = Object.entries(mapaEntregas).sort((a, b) => b[1] - a[1]).slice(0, 7);
        topEntregadosLabels.value = ordenados.map(e => e[0]);
        topEntregadosValues.value = ordenados.map(e => e[1]);

        const resInv = await inventarioService().retrieve({ size: 100, sort: ['cantidad,asc'] });
        inventarioCompleto.value = resInv.data || [];
        
        const top10 = inventarioCompleto.value.slice(0, 10);
        etiquetasStock.value = top10.map(i => i.nombre || 'Sin nombre');
        stockActualValues.value = top10.map(i => i.cantidad || 0);
        stockMinimoValues.value = top10.map(i => i.cantidadMinima || 50);
      } finally {
        isFetching.value = false;
      }
    };

    // --- COMPUTED DATASETS ---
    const entregadosData = computed(() => ({
      labels: topEntregadosLabels.value,
      datasets: [{ 
        label: 'Salidas Totales', 
        backgroundColor: COLORES.GUINDA, 
        borderRadius: 5,
        data: topEntregadosValues.value 
      }]
    }));

    const pieData = computed(() => ({
      labels: ['Entradas', 'Salidas'],
      datasets: [{ 
        backgroundColor: [COLORES.EXITO, COLORES.VINO], 
        hoverOffset: 10,
        data: [datosMovimientos.value.entradas, datosMovimientos.value.salidas] 
      }]
    }));

    const stockData = computed(() => ({
      labels: etiquetasStock.value,
      datasets: [
        { 
          label: 'Stock Actual', 
          backgroundColor: COLORES.VINO, 
          borderRadius: 4,
          data: stockActualValues.value 
        },
        { 
          label: 'Mínimo Requerido', 
          backgroundColor: COLORES.OCRE, 
          borderRadius: 4,
          data: stockMinimoValues.value 
        }
      ]
    }));

    // --- ACCIONES DE MODAL ---
    const itemSeleccionado = ref<any>(null);
    const cantidadASumar = ref(0);
    const resurtidoModalRef = ref<HTMLElement | null>(null);
    let modalInstance: Modal | null = null;

    onMounted(async () => {
      await cargarDatos();
      if (resurtidoModalRef.value) {
        modalInstance = new Modal(resurtidoModalRef.value);
      }
    });

    const abrirModalResurtido = (item: any) => {
      itemSeleccionado.value = { ...item };
      cantidadASumar.value = 0;
      modalInstance?.show();
    };

    // Función unificada para cerrar el modal y limpiar el DOM
    const cerrarModal = () => {
      modalInstance?.hide();
      
      // Limpieza manual de residuos (clases y backdrops) tras la animación
      setTimeout(() => {
        document.body.classList.remove('modal-open');
        document.body.style.overflow = '';
        document.body.style.paddingRight = '';
        const backdrops = document.getElementsByClassName('modal-backdrop');
        while(backdrops.length > 0) {
          backdrops[0].parentNode?.removeChild(backdrops[0]);
        }
      }, 150); 
    };

    const confirmarResurtido = async () => {
      if (!itemSeleccionado.value || cantidadASumar.value <= 0) return;
      isSaving.value = true;
      try {
        const nuevoMovimiento = {
          fecha: new Date().toISOString(),
          tipoMovimiento: 'ENTRADA',
          cantidad: cantidadASumar.value,
          motivo: 'Resurtido desde panel de gráficas',
          inventario: { id: itemSeleccionado.value.id }
        };
        await movimientoService().create(nuevoMovimiento);
        const nuevaCantidad = Number(itemSeleccionado.value.cantidad) + Number(cantidadASumar.value);
        await inventarioService().update({ ...itemSeleccionado.value, cantidad: nuevaCantidad });
        
        cerrarModal();
        await cargarDatos(); 
      } catch (error) {
        console.error('Error:', error);
      } finally {
        isSaving.value = false;
      }
    };

    return { 
      pieData, stockData, entregadosData, isLoaded: computed(() => !isFetching.value), 
      cargarDatos, alertasStockBajo, alertasCaducidad, getBadgeCaducidad,
      resurtidoModalRef, itemSeleccionado, cantidadASumar, isSaving, 
      abrirModalResurtido, confirmarResurtido, cerrarModal,
      chartOptions: { 
        responsive: true, 
        maintainAspectRatio: false, 
        plugins: { 
          legend: { position: 'bottom' as const, labels: { color: '#444', font: { weight: 'bold' } } } 
        } 
      },
      horizontalOptions: { 
        responsive: true, 
        maintainAspectRatio: false, 
        indexAxis: 'y' as const, 
        plugins: { legend: { display: false } } 
      }
    };
  },
});