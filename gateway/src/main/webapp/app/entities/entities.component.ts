import { defineComponent, provide } from 'vue';

import InventarioService from './inventario/inventario/inventario.service';
import PacienteService from './paciente/paciente/paciente.service';
import MedicoService from './medico/medico/medico.service';
import HistoricoService from './historico/historico/historico.service';
import MovimientoInventarioService from './inventario/movimiento-inventario/movimiento-inventario.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('inventarioService', () => new InventarioService());
    provide('pacienteService', () => new PacienteService());
    provide('medicoService', () => new MedicoService());
    provide('historicoService', () => new HistoricoService());
    provide('movimientoInventarioService', () => new MovimientoInventarioService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
