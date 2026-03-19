import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PacienteService from './paciente.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IPaciente, Paciente } from '@/shared/model/paciente/paciente.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteUpdate',
  setup() {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paciente: Ref<IPaciente> = ref(new Paciente());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaciente = async pacienteId => {
      try {
        const res = await pacienteService().find(pacienteId);
        paciente.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteId) {
      retrievePaciente(route.params.pacienteId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      curp: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 18 }).toString(), 18),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 18 }).toString(), 18),
      },
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 2 }).toString(), 2),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      fechaNacimiento: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      sexo: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 1 }).toString(), 1),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 20 }).toString(), 20),
      },
      numeroSeguroSocial: {
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 8 }).toString(), 8),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 20 }).toString(), 20),
      },
    };
    const v$ = useVuelidate(validationRules, paciente as any);
    v$.value.$validate();

    return {
      pacienteService,
      alertService,
      paciente,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.paciente.id) {
        this.pacienteService()
          .update(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('pacienteApp.pacientePaciente.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.pacienteService()
          .create(this.paciente)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('pacienteApp.pacientePaciente.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
