import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PacienteService from './paciente.service';
import { type IPaciente } from '@/shared/model/paciente/paciente.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PacienteDetails',
  setup() {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paciente: Ref<IPaciente> = ref({});

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

    return {
      alertService,
      paciente,

      previousState,
      t$: useI18n().t,
    };
  },
});
