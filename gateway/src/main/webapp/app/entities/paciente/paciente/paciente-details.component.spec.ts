/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PacienteDetails from './paciente-details.vue';
import PacienteService from './paciente.service';
import AlertService from '@/shared/alert/alert.service';

type PacienteDetailsComponentType = InstanceType<typeof PacienteDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const pacienteSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Paciente Management Detail Component', () => {
    let pacienteServiceStub: SinonStubbedInstance<PacienteService>;
    let mountOptions: MountingOptions<PacienteDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      pacienteServiceStub = sinon.createStubInstance<PacienteService>(PacienteService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          pacienteService: () => pacienteServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        pacienteServiceStub.find.resolves(pacienteSample);
        route = {
          params: {
            pacienteId: `${123}`,
          },
        };
        const wrapper = shallowMount(PacienteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.paciente).toMatchObject(pacienteSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        pacienteServiceStub.find.resolves(pacienteSample);
        const wrapper = shallowMount(PacienteDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
