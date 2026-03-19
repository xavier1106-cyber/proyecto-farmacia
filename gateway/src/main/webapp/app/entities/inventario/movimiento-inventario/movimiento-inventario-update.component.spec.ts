/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MovimientoInventarioUpdate from './movimiento-inventario-update.vue';
import MovimientoInventarioService from './movimiento-inventario.service';
import AlertService from '@/shared/alert/alert.service';

import InventarioService from '@/entities/inventario/inventario/inventario.service';

type MovimientoInventarioUpdateComponentType = InstanceType<typeof MovimientoInventarioUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const movimientoInventarioSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MovimientoInventarioUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MovimientoInventario Management Update Component', () => {
    let comp: MovimientoInventarioUpdateComponentType;
    let movimientoInventarioServiceStub: SinonStubbedInstance<MovimientoInventarioService>;

    beforeEach(() => {
      route = {};
      movimientoInventarioServiceStub = sinon.createStubInstance<MovimientoInventarioService>(MovimientoInventarioService);
      movimientoInventarioServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          movimientoInventarioService: () => movimientoInventarioServiceStub,
          inventarioService: () =>
            sinon.createStubInstance<InventarioService>(InventarioService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MovimientoInventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.movimientoInventario = movimientoInventarioSample;
        movimientoInventarioServiceStub.update.resolves(movimientoInventarioSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(movimientoInventarioServiceStub.update.calledWith(movimientoInventarioSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        movimientoInventarioServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MovimientoInventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.movimientoInventario = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(movimientoInventarioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        movimientoInventarioServiceStub.find.resolves(movimientoInventarioSample);
        movimientoInventarioServiceStub.retrieve.resolves([movimientoInventarioSample]);

        // WHEN
        route = {
          params: {
            movimientoInventarioId: `${movimientoInventarioSample.id}`,
          },
        };
        const wrapper = shallowMount(MovimientoInventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.movimientoInventario).toMatchObject(movimientoInventarioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        movimientoInventarioServiceStub.find.resolves(movimientoInventarioSample);
        const wrapper = shallowMount(MovimientoInventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
