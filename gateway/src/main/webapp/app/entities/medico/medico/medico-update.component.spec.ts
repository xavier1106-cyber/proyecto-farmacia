/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MedicoUpdate from './medico-update.vue';
import MedicoService from './medico.service';
import AlertService from '@/shared/alert/alert.service';

type MedicoUpdateComponentType = InstanceType<typeof MedicoUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const medicoSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MedicoUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Medico Management Update Component', () => {
    let comp: MedicoUpdateComponentType;
    let medicoServiceStub: SinonStubbedInstance<MedicoService>;

    beforeEach(() => {
      route = {};
      medicoServiceStub = sinon.createStubInstance<MedicoService>(MedicoService);
      medicoServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          medicoService: () => medicoServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.medico = medicoSample;
        medicoServiceStub.update.resolves(medicoSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medicoServiceStub.update.calledWith(medicoSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        medicoServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.medico = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(medicoServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        medicoServiceStub.find.resolves(medicoSample);
        medicoServiceStub.retrieve.resolves([medicoSample]);

        // WHEN
        route = {
          params: {
            medicoId: `${medicoSample.id}`,
          },
        };
        const wrapper = shallowMount(MedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.medico).toMatchObject(medicoSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        medicoServiceStub.find.resolves(medicoSample);
        const wrapper = shallowMount(MedicoUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
