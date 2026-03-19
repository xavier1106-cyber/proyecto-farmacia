import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import MedicoService from './medico.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IMedico, Medico } from '@/shared/model/medico/medico.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MedicoUpdate',
  setup() {
    const medicoService = inject('medicoService', () => new MedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const medico: Ref<IMedico> = ref(new Medico());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'es'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveMedico = async medicoId => {
      try {
        const res = await medicoService().find(medicoId);
        medico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.medicoId) {
      retrieveMedico(route.params.medicoId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      nombre: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 2 }).toString(), 2),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      especialidad: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      telefono: {
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 10 }).toString(), 10),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 15 }).toString(), 15),
      },
      turno: {
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 50 }).toString(), 50),
      },
      activo: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, medico as any);
    v$.value.$validate();

    return {
      medicoService,
      alertService,
      medico,
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
      if (this.medico.id) {
        this.medicoService()
          .update(this.medico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('medicoApp.medicoMedico.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.medicoService()
          .create(this.medico)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('medicoApp.medicoMedico.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
