/* tslint:disable max-line-length */
import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import HistoricoService from './historico.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { Historico } from '@/shared/model/historico/historico.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('Historico Service', () => {
    let service: HistoricoService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new HistoricoService();
      currentDate = new Date();
      elemDefault = new Historico(
        123,
        currentDate,
        'AAAAAAA',
        0,
        'AAAAAAA',
        'AAAAAAA',
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { fechaEmision: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a Historico', async () => {
        const returnedFromService = { id: 123, fechaEmision: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { fechaEmision: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a Historico', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a Historico', async () => {
        const returnedFromService = {
          fechaEmision: dayjs(currentDate).format(DATE_TIME_FORMAT),
          folio: 'BBBBBB',
          pacienteId: 1,
          pacienteNombre: 'BBBBBB',
          pacienteCurp: 'BBBBBB',
          medicoId: 1,
          medicoNombre: 'BBBBBB',
          medicoEspecialidad: 'BBBBBB',
          usuarioQueRegistro: 'BBBBBB',
          medicamentos: 'BBBBBB',
          autorizo: 'BBBBBB',
          observaciones: 'BBBBBB',
          cantidad: 1,
          ...elemDefault,
        };

        const expected = { fechaEmision: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a Historico', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a Historico', async () => {
        const patchObject = {
          pacienteId: 1,
          pacienteNombre: 'BBBBBB',
          medicoNombre: 'BBBBBB',
          usuarioQueRegistro: 'BBBBBB',
          medicamentos: 'BBBBBB',
          observaciones: 'BBBBBB',
          cantidad: 1,
          ...new Historico(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { fechaEmision: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a Historico', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of Historico', async () => {
        const returnedFromService = {
          fechaEmision: dayjs(currentDate).format(DATE_TIME_FORMAT),
          folio: 'BBBBBB',
          pacienteId: 1,
          pacienteNombre: 'BBBBBB',
          pacienteCurp: 'BBBBBB',
          medicoId: 1,
          medicoNombre: 'BBBBBB',
          medicoEspecialidad: 'BBBBBB',
          usuarioQueRegistro: 'BBBBBB',
          medicamentos: 'BBBBBB',
          autorizo: 'BBBBBB',
          observaciones: 'BBBBBB',
          cantidad: 1,
          ...elemDefault,
        };
        const expected = { fechaEmision: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of Historico', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a Historico', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a Historico', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
