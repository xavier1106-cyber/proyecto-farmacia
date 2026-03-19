  import { Authority } from '@/shared/security/authority';
  /* tslint:disable */
  // prettier-ignore
  const Entities = () => import('@/entities/entities.vue');

  const Inventario = () => import('@/entities/inventario/inventario/inventario.vue');
  const InventarioUpdate = () => import('@/entities/inventario/inventario/inventario-update.vue');
  const InventarioDetails = () => import('@/entities/inventario/inventario/inventario-details.vue');

  const Paciente = () => import('@/entities/paciente/paciente/paciente.vue');
  const PacienteUpdate = () => import('@/entities/paciente/paciente/paciente-update.vue');
  const PacienteDetails = () => import('@/entities/paciente/paciente/paciente-details.vue');

  const Medico = () => import('@/entities/medico/medico/medico.vue');
  const MedicoUpdate = () => import('@/entities/medico/medico/medico-update.vue');
  const MedicoDetails = () => import('@/entities/medico/medico/medico-details.vue');

  const Historico = () => import('@/entities/historico/historico/historico.vue');
  const HistoricoUpdate = () => import('@/entities/historico/historico/historico-update.vue');
  const HistoricoDetails = () => import('@/entities/historico/historico/historico-details.vue');

  const buscador =() => import('@/entities/buscador/buscador.vue');

  const MovimientoInventario = () => import('@/entities/inventario/movimiento-inventario/movimiento-inventario.vue');
  const MovimientoInventarioUpdate = () => import('@/entities/inventario/movimiento-inventario/movimiento-inventario-update.vue');
  const MovimientoInventarioDetails = () => import('@/entities/inventario/movimiento-inventario/movimiento-inventario-details.vue');

  // jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

  export default {
    path: '/',
    component: Entities,
    children: [
      {
        path: 'inventario',
        name: 'Inventario',
        component: Inventario,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'inventario/new',
        name: 'InventarioCreate',
        component: InventarioUpdate,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'inventario/:inventarioId/edit',
        name: 'InventarioEdit',
        component: InventarioUpdate,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'inventario/:inventarioId/view',
        name: 'InventarioView',
        component: InventarioDetails,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'paciente',
        name: 'Paciente',
        component: Paciente,
        meta: { authorities: [Authority.USER]},
      },
      {
        path: 'paciente/new',
        name: 'PacienteCreate',
        component: PacienteUpdate,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'paciente/:pacienteId/edit',
        name: 'PacienteEdit',
        component: PacienteUpdate,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'paciente/:pacienteId/view',
        name: 'PacienteView',
        component: PacienteDetails,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'medico',
        name: 'Medico',
        component: Medico,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'medico/new',
        name: 'MedicoCreate',
        component: MedicoUpdate,
        meta: { authorities: [Authority.USER,] },
      },
      {
        path: 'medico/:medicoId/edit',
        name: 'MedicoEdit',
        component: MedicoUpdate,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'medico/:medicoId/view',
        name: 'MedicoView',
        component: MedicoDetails,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'historico',
        name: 'Historico',
        component: Historico,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'historico/new',
        name: 'HistoricoCreate',
        component: HistoricoUpdate,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'historico/:historicoId/edit',
        name: 'HistoricoEdit',
        component: HistoricoUpdate,
        meta: { authorities: [Authority.USER] },
      },
      {
        path: 'historico/:historicoId/view',
        name: 'HistoricoView',
        component: HistoricoDetails,
        meta: { authorities: [Authority.USER] },
      },
      {
      path: 'buscador',
      name: 'Buscador',
      component: buscador,
      meta: { authorities: ['ROLE_USER'] },
      },


      {
    path: 'movimiento-inventario',
    name: 'MovimientoInventario',
    component: MovimientoInventario,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: 'movimiento-inventario/new',
    name: 'MovimientoInventarioCreate',
    component: MovimientoInventarioUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: 'movimiento-inventario/:movimientoInventarioId/edit',
    name: 'MovimientoInventarioEdit',
    component: MovimientoInventarioUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: 'movimiento-inventario/:movimientoInventarioId/view',
    name: 'MovimientoInventarioView',
    component: MovimientoInventarioDetails,
    meta: { authorities: [Authority.USER] },
  },
      // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
    ],
  };
