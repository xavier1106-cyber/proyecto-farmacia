<template>
  <div class="container mt-4">
    <!-- BIENVENIDA -->
    <div class="row mb-4">
      <div class="col">
        <h1 class="display-5">Sistema de Farmacia</h1>

        <div class="alert alert-success" v-if="authenticated">
          Bienvenido <b>{{ username }}</b>
        </div>

        <div class="alert alert-warning" v-if="!authenticated">
          Debes iniciar sesión para acceder al sistema
          <a class="alert-link" @click="openLogin()">Iniciar sesión</a>
        </div>
      </div>
    </div>

    <!-- TARJETAS DE ACCESO -->
    <div class="row text-center" v-if="authenticated">
      <div class="col-md-3 mb-3">
        <div class="card shadow">
          <div class="card-body">
            <h5>Pacientes</h5>
            <p>Gestión de pacientes</p>
            <router-link class="btn btn-primary" to="/paciente">Entrar</router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card shadow">
          <div class="card-body">
            <h5>Médicos</h5>
            <p>Administrar médicos</p>
            <router-link class="btn btn-primary" to="/medico">Entrar</router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card shadow">
          <div class="card-body">
            <h5>Inventario</h5>
            <p>Medicamentos disponibles</p>
            <router-link class="btn btn-primary" to="/inventario">Entrar</router-link>
          </div>
        </div>
      </div>

      <div class="col-md-3 mb-3">
        <div class="card shadow">
          <div class="card-body">
            <h5>Histórico</h5>
            <p>Registro de actividad</p>
            <router-link class="btn btn-primary" to="/historico">Entrar</router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- ALERTAS -->
    <div class="row mt-4" v-if="authenticated">
      <div class="col-md-6">
        <div class="card border-warning">
          <div class="card-header bg-warning text-dark">Alertas del sistema</div>
          <div class="card-body">
            <ul>
              <li>Medicamentos por caducar</li>
              <li>Inventario bajo</li>
              <li>Nuevas recetas registradas</li>
            </ul>
          </div>
        </div>
      </div>

      <!-- ACTIVIDAD -->
      <div class="col-md-6">
        <div class="card border-info">
          <div class="card-header bg-info text-white">Actividad reciente</div>
          <div class="card-body">
            <ul>
              <li>Paciente registrado</li>
              <li>Medicamento agregado</li>
              <li>Receta generada</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./home.component.ts"></script>
