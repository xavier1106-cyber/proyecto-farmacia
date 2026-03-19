import axios from 'axios';

import buildPaginationQueryOpts from '@/shared/sort/sorts';

import { type IMovimientoInventario } from '@/shared/model/inventario/movimiento-inventario.model';

const baseApiUrl = 'services/inventario/api/movimiento-inventarios';

export default class MovimientoInventarioService {
  public find(id: number): Promise<IMovimientoInventario> {
    return new Promise<IMovimientoInventario>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public retrieve(paginationQuery?: any): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .get(`${baseApiUrl}?${buildPaginationQueryOpts(paginationQuery)}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public delete(id: number): Promise<any> {
    return new Promise<any>((resolve, reject) => {
      axios
        .delete(`${baseApiUrl}/${id}`)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public create(entity: IMovimientoInventario): Promise<IMovimientoInventario> {
    return new Promise<IMovimientoInventario>((resolve, reject) => {
      axios
        .post(`${baseApiUrl}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public update(entity: IMovimientoInventario): Promise<IMovimientoInventario> {
    return new Promise<IMovimientoInventario>((resolve, reject) => {
      axios
        .put(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }

  public partialUpdate(entity: IMovimientoInventario): Promise<IMovimientoInventario> {
    return new Promise<IMovimientoInventario>((resolve, reject) => {
      axios
        .patch(`${baseApiUrl}/${entity.id}`, entity)
        .then(res => {
          resolve(res.data);
        })
        .catch(err => {
          reject(err);
        });
    });
  }
}
