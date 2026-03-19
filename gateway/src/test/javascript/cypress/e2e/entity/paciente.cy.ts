import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Paciente e2e test', () => {
  const pacientePageUrl = '/paciente';
  const pacientePageUrlPattern = new RegExp('/paciente(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const pacienteSample = {
    curp: 'viceXXXXXXXXXXXXXX',
    nombre: 'ugh conventional',
    fechaNacimiento: '2026-02-25',
    sexo: 'compost sojourn eman',
  };

  let paciente;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/paciente/api/pacientes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/paciente/api/pacientes').as('postEntityRequest');
    cy.intercept('DELETE', '/services/paciente/api/pacientes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paciente) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/paciente/api/pacientes/${paciente.id}`,
      }).then(() => {
        paciente = undefined;
      });
    }
  });

  it('Pacientes menu should load Pacientes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('paciente');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Paciente').should('exist');
    cy.url().should('match', pacientePageUrlPattern);
  });

  describe('Paciente page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pacientePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Paciente page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/paciente/new$'));
        cy.getEntityCreateUpdateHeading('Paciente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pacientePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/paciente/api/pacientes',
          body: pacienteSample,
        }).then(({ body }) => {
          paciente = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/paciente/api/pacientes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/paciente/api/pacientes?page=0&size=20>; rel="last",<http://localhost/services/paciente/api/pacientes?page=0&size=20>; rel="first"',
              },
              body: [paciente],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(pacientePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Paciente page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paciente');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pacientePageUrlPattern);
      });

      it('edit button click should load edit Paciente page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Paciente');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pacientePageUrlPattern);
      });

      it('edit button click should load edit Paciente page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Paciente');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pacientePageUrlPattern);
      });

      it('last delete button click should delete instance of Paciente', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paciente').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', pacientePageUrlPattern);

        paciente = undefined;
      });
    });
  });

  describe('new Paciente page', () => {
    beforeEach(() => {
      cy.visit(`${pacientePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Paciente');
    });

    it('should create an instance of Paciente', () => {
      cy.get(`[data-cy="curp"]`).type('smoothly bestXXXXX');
      cy.get(`[data-cy="curp"]`).should('have.value', 'smoothly bestXXXXX');

      cy.get(`[data-cy="nombre"]`).type('divulge since');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'divulge since');

      cy.get(`[data-cy="fechaNacimiento"]`).type('2026-02-26');
      cy.get(`[data-cy="fechaNacimiento"]`).blur();
      cy.get(`[data-cy="fechaNacimiento"]`).should('have.value', '2026-02-26');

      cy.get(`[data-cy="sexo"]`).type('if');
      cy.get(`[data-cy="sexo"]`).should('have.value', 'if');

      cy.get(`[data-cy="numeroSeguroSocial"]`).type('whoa powerless');
      cy.get(`[data-cy="numeroSeguroSocial"]`).should('have.value', 'whoa powerless');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        paciente = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', pacientePageUrlPattern);
    });
  });
});
