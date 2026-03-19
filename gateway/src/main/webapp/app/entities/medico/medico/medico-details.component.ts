import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import MedicoService from './medico.service';
import { type IMedico } from '@/shared/model/medico/medico.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'MedicoDetails',
  setup() {
    const medicoService = inject('medicoService', () => new MedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const medico: Ref<IMedico> = ref({});

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

    return {
      alertService,
      medico,

      previousState,
      t$: useI18n().t,
    };
  },
});
