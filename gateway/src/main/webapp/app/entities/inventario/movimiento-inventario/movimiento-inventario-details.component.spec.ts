/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MovimientoInventarioDetails from './movimiento-inventario-details.vue';
import MovimientoInventarioService from './movimiento-inventario.service';
import AlertService from '@/shared/alert/alert.service';

type MovimientoInventarioDetailsComponentType = InstanceType<typeof MovimientoInventarioDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const movimientoInventarioSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('MovimientoInventario Management Detail Component', () => {
    let movimientoInventarioServiceStub: SinonStubbedInstance<MovimientoInventarioService>;
    let mountOptions: MountingOptions<MovimientoInventarioDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      movimientoInventarioServiceStub = sinon.createStubInstance<MovimientoInventarioService>(MovimientoInventarioService);

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
          movimientoInventarioService: () => movimientoInventarioServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        movimientoInventarioServiceStub.find.resolves(movimientoInventarioSample);
        route = {
          params: {
            movimientoInventarioId: `${123}`,
          },
        };
        const wrapper = shallowMount(MovimientoInventarioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.movimientoInventario).toMatchObject(movimientoInventarioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        movimientoInventarioServiceStub.find.resolves(movimientoInventarioSample);
        const wrapper = shallowMount(MovimientoInventarioDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
