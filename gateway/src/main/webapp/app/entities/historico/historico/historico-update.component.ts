import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import HistoricoService from './historico.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { Historico, type IHistorico } from '@/shared/model/historico/historico.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'HistoricoUpdate',
  setup() {
    const historicoService = inject('historicoService', () => new HistoricoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const historico: Ref<IHistorico> = ref(new Historico());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveHistorico = async historicoId => {
      try {
        const res = await historicoService().find(historicoId);
        res.fechaEmision = new Date(res.fechaEmision);
        historico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.historicoId) {
      retrieveHistorico(route.params.historicoId);
    }

    const dataUtils = useDataUtils();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      fechaEmision: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      folio: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      pacienteId: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      pacienteNombre: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      pacienteCurp: {},
      medicoId: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      medicoNombre: {},
      medicoEspecialidad: {},
      usuarioQueRegistro: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      medicamentos: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      autorizo: {},
      observaciones: {},
      cantidad: {},
    };
    const v$ = useVuelidate(validationRules, historico as any);
    v$.value.$validate();

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
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
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
