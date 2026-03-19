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

describe('Historico e2e test', () => {
  const historicoPageUrl = '/historico';
  const historicoPageUrlPattern = new RegExp('/historico(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const historicoSample = {
    fechaEmision: '2026-03-02T19:16:52.914Z',
    folio: 'hence plop',
    pacienteId: 17692,
    pacienteNombre: 'gadzooks',
    medicoId: 31858,
    usuarioQueRegistro: 'skyscraper great inborn',
    medicamentos: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
  };

  let historico;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/historico/api/historicos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/historico/api/historicos').as('postEntityRequest');
    cy.intercept('DELETE', '/services/historico/api/historicos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (historico) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/historico/api/historicos/${historico.id}`,
      }).then(() => {
        historico = undefined;
      });
    }
  });

  it('Historicos menu should load Historicos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('historico');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Historico').should('exist');
    cy.url().should('match', historicoPageUrlPattern);
  });

  describe('Historico page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(historicoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Historico page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/historico/new$'));
        cy.getEntityCreateUpdateHeading('Historico');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/historico/api/historicos',
          body: historicoSample,
        }).then(({ body }) => {
          historico = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/historico/api/historicos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/historico/api/historicos?page=0&size=20>; rel="last",<http://localhost/services/historico/api/historicos?page=0&size=20>; rel="first"',
              },
              body: [historico],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(historicoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Historico page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('historico');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoPageUrlPattern);
      });

      it('edit button click should load edit Historico page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Historico');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoPageUrlPattern);
      });

      it('edit button click should load edit Historico page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Historico');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoPageUrlPattern);
      });

      it('last delete button click should delete instance of Historico', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('historico').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', historicoPageUrlPattern);

        historico = undefined;
      });
    });
  });

  describe('new Historico page', () => {
    beforeEach(() => {
      cy.visit(`${historicoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Historico');
    });

    it('should create an instance of Historico', () => {
      cy.get(`[data-cy="fechaEmision"]`).type('2026-03-03T00:02');
      cy.get(`[data-cy="fechaEmision"]`).blur();
      cy.get(`[data-cy="fechaEmision"]`).should('have.value', '2026-03-03T00:02');

      cy.get(`[data-cy="folio"]`).type('woefully given');
      cy.get(`[data-cy="folio"]`).should('have.value', 'woefully given');

      cy.get(`[data-cy="pacienteId"]`).type('674');
      cy.get(`[data-cy="pacienteId"]`).should('have.value', '674');

      cy.get(`[data-cy="pacienteNombre"]`).type('portray modulo');
      cy.get(`[data-cy="pacienteNombre"]`).should('have.value', 'portray modulo');

      cy.get(`[data-cy="pacienteCurp"]`).type('adventurously');
      cy.get(`[data-cy="pacienteCurp"]`).should('have.value', 'adventurously');

      cy.get(`[data-cy="medicoId"]`).type('6610');
      cy.get(`[data-cy="medicoId"]`).should('have.value', '6610');

      cy.get(`[data-cy="medicoNombre"]`).type('terrible');
      cy.get(`[data-cy="medicoNombre"]`).should('have.value', 'terrible');

      cy.get(`[data-cy="medicoEspecialidad"]`).type('beside bruised theorize');
      cy.get(`[data-cy="medicoEspecialidad"]`).should('have.value', 'beside bruised theorize');

      cy.get(`[data-cy="usuarioQueRegistro"]`).type('aha softly');
      cy.get(`[data-cy="usuarioQueRegistro"]`).should('have.value', 'aha softly');

      cy.get(`[data-cy="medicamentos"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="medicamentos"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="autorizo"]`).type('animated');
      cy.get(`[data-cy="autorizo"]`).should('have.value', 'animated');

      cy.get(`[data-cy="observaciones"]`).type('questioningly');
      cy.get(`[data-cy="observaciones"]`).should('have.value', 'questioningly');

      cy.get(`[data-cy="cantidad"]`).type('12877');
      cy.get(`[data-cy="cantidad"]`).should('have.value', '12877');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        historico = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', historicoPageUrlPattern);
    });
  });
});
