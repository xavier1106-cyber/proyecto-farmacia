import { type Ref, computed, defineComponent, inject, onMounted, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import HistoricoService from './historico.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Historico, type IHistorico } from '@/shared/model/historico/historico.model';
import InventarioService from '@/entities/inventario/inventario/inventario.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistoricoUpdate',
  setup() {
    const historicoService = inject('historicoService', () => new HistoricoService());
    const alertService = inject('alertService', () => useAlertService(), true);
    const inventarioService = inject('inventarioService', () => new InventarioService());

    const historico: Ref<IHistorico> = ref(new Historico());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    // --- CORRECCIÓN: Inicialización garantizada como Array vacío ---
    const listaMedsEdicion = ref<any[]>([]); 
    const listaInventario = ref<any[]>([]); 

    const agregarMedicamentoRow = () => {
      listaMedsEdicion.value.push({
        id: '',
        nombre: '',
        cantidad: 1,
        observaciones: ''
      });
    };

    const removerMedicamentoRow = (index: number) => {
      listaMedsEdicion.value.splice(index, 1);
    };

    const cargarInventario = async () => {
      try {
        const res = await inventarioService().retrieve();
        listaInventario.value = res.data || [];
      } catch (err) {
        console.error("Error cargando inventario", err);
      }
    };

    const onMedicamentoChange = (index: number) => {
      const fila = listaMedsEdicion.value[index];
      if (!fila) return;
      const match = listaInventario.value.find(i => i.nombre === fila.nombre);
      if (match) {
        fila.id = match.id;
      }
    };

    const retrieveHistorico = async (historicoId: number) => {
      try {
        const res = await historicoService().find(historicoId);
        res.fechaEmision = res.fechaEmision ? new Date(res.fechaEmision) : new Date();
        historico.value = res;

        if (res.medicamentos) {
          try {
            const parsed = JSON.parse(res.medicamentos);
            listaMedsEdicion.value = Array.isArray(parsed) ? parsed : [];
          } catch (e) {
            console.error("Error al parsear medicamentos", e);
            listaMedsEdicion.value = [];
          }
        }
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.historicoId) {
      retrieveHistorico(Number(route.params.historicoId));
    }

    onMounted(() => {
      cargarInventario();
    });

    const dataUtils = useDataUtils();
    const { t: t$ } = useI18n();
    const validations = useValidation();

    const validationRules = {
      fechaEmision: { required: validations.required(t$('entity.validation.required').toString()) },
      folio: { required: validations.required(t$('entity.validation.required').toString()) },
      pacienteId: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      pacienteNombre: { required: validations.required(t$('entity.validation.required').toString()) },
      pacienteCurp: {},
      medicoId: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      medicoNombre: {},
      medicoEspecialidad: {},
      usuarioQueRegistro: { required: validations.required(t$('entity.validation.required').toString()) },
      medicamentos: {},
      autorizo: {},
      observaciones: {},
      cantidad: {},
    };

    const v$ = useVuelidate(validationRules, historico as any);

    return {
      historicoService,
      alertService,
      historico,
      previousState,
      isSaving,
      currentLanguage,
      ...dataUtils,
      v$,
      ...useDateFormat({ entityRef: historico }),
      t$,
      listaMedsEdicion, // Verificado: se exporta correctamente
      listaInventario,
      agregarMedicamentoRow,
      removerMedicamentoRow,
      onMedicamentoChange
    };
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.listaMedsEdicion && this.listaMedsEdicion.length > 0) {
        this.historico.medicamentos = JSON.stringify(this.listaMedsEdicion);
        this.historico.cantidad = this.listaMedsEdicion.reduce(
          (acc, cur) => acc + (Number(cur.cantidad) || 0),
          0
        );
      } else {
        this.historico.medicamentos = '[]';
        this.historico.cantidad = 0;
      }

      if (this.historico.id) {
        this.historicoService()
          .update(this.historico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('historicoApp.historicoHistorico.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.historicoService()
          .create(this.historico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('historicoApp.historicoHistorico.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});