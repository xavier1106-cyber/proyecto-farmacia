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

describe('MovimientoInventario e2e test', () => {
  const movimientoInventarioPageUrl = '/movimiento-inventario';
  const movimientoInventarioPageUrlPattern = new RegExp('/movimiento-inventario(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const movimientoInventarioSample = { tipoMovimiento: 'SALIDA', cantidad: 14670, fecha: '2026-03-15' };

  let movimientoInventario;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/inventario/api/movimiento-inventarios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/inventario/api/movimiento-inventarios').as('postEntityRequest');
    cy.intercept('DELETE', '/services/inventario/api/movimiento-inventarios/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (movimientoInventario) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/inventario/api/movimiento-inventarios/${movimientoInventario.id}`,
      }).then(() => {
        movimientoInventario = undefined;
      });
    }
  });

  it('MovimientoInventarios menu should load MovimientoInventarios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('movimiento-inventario');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MovimientoInventario').should('exist');
    cy.url().should('match', movimientoInventarioPageUrlPattern);
  });

  describe('MovimientoInventario page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(movimientoInventarioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MovimientoInventario page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/movimiento-inventario/new$'));
        cy.getEntityCreateUpdateHeading('MovimientoInventario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', movimientoInventarioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/inventario/api/movimiento-inventarios',
          body: movimientoInventarioSample,
        }).then(({ body }) => {
          movimientoInventario = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/inventario/api/movimiento-inventarios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/inventario/api/movimiento-inventarios?page=0&size=20>; rel="last",<http://localhost/services/inventario/api/movimiento-inventarios?page=0&size=20>; rel="first"',
              },
              body: [movimientoInventario],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(movimientoInventarioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MovimientoInventario page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('movimientoInventario');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', movimientoInventarioPageUrlPattern);
      });

      it('edit button click should load edit MovimientoInventario page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MovimientoInventario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', movimientoInventarioPageUrlPattern);
      });

      it('edit button click should load edit MovimientoInventario page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MovimientoInventario');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', movimientoInventarioPageUrlPattern);
      });

      it('last delete button click should delete instance of MovimientoInventario', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('movimientoInventario').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', movimientoInventarioPageUrlPattern);

        movimientoInventario = undefined;
      });
    });
  });

  describe('new MovimientoInventario page', () => {
    beforeEach(() => {
      cy.visit(`${movimientoInventarioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MovimientoInventario');
    });

    it('should create an instance of MovimientoInventario', () => {
      cy.get(`[data-cy="tipoMovimiento"]`).select('CADUCADO');

      cy.get(`[data-cy="cantidad"]`).type('2067');
      cy.get(`[data-cy="cantidad"]`).should('have.value', '2067');

      cy.get(`[data-cy="fecha"]`).type('2026-03-16');
      cy.get(`[data-cy="fecha"]`).blur();
      cy.get(`[data-cy="fecha"]`).should('have.value', '2026-03-16');

      cy.get(`[data-cy="observacion"]`).type('bah');
      cy.get(`[data-cy="observacion"]`).should('have.value', 'bah');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        movimientoInventario = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', movimientoInventarioPageUrlPattern);
    });
  });
});
