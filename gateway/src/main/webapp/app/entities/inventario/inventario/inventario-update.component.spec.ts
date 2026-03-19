/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import InventarioUpdate from './inventario-update.vue';
import InventarioService from './inventario.service';
import AlertService from '@/shared/alert/alert.service';

type InventarioUpdateComponentType = InstanceType<typeof InventarioUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const inventarioSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<InventarioUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Inventario Management Update Component', () => {
    let comp: InventarioUpdateComponentType;
    let inventarioServiceStub: SinonStubbedInstance<InventarioService>;

    beforeEach(() => {
      route = {};
      inventarioServiceStub = sinon.createStubInstance<InventarioService>(InventarioService);
      inventarioServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          inventarioService: () => inventarioServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(InventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.inventario = inventarioSample;
        inventarioServiceStub.update.resolves(inventarioSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(inventarioServiceStub.update.calledWith(inventarioSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        inventarioServiceStub.create.resolves(entity);
        const wrapper = shallowMount(InventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.inventario = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(inventarioServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        inventarioServiceStub.find.resolves(inventarioSample);
        inventarioServiceStub.retrieve.resolves([inventarioSample]);

        // WHEN
        route = {
          params: {
            inventarioId: `${inventarioSample.id}`,
          },
        };
        const wrapper = shallowMount(InventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.inventario).toMatchObject(inventarioSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        inventarioServiceStub.find.resolves(inventarioSample);
        const wrapper = shallowMount(InventarioUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
