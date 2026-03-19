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

describe('Inventario e2e test', () => {
  const inventarioPageUrl = '/inventario';
  const inventarioPageUrlPattern = new RegExp('/inventario(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const inventarioSample = {
    claveMedicamento: 'about',
    nombre: 'boo after wildly',
    presentacion: 'swoon',
    lote: 'near',
    cantidad: 29152,
    cantidadMinima: 14206,
    fechaCaducidad: '2026-02-26',
    ubicacion: 'politely yippee',
    controlado: false,
  };

  let inventario;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/inventario/api/inventarios+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/inventario/api/inventarios').as('postEntityRequest');
    cy.intercept('DELETE', '/services/inventario/api/inventarios/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inventario) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/inventario/api/inventarios/${inventario.id}`,
      }).then(() => {
        inventario = undefined;
      });
    }
  });

  it('Inventarios menu should load Inventarios page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventario');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Inventario').should('exist');
    cy.url().should('match', inventarioPageUrlPattern);
  });

  describe('Inventario page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventarioPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Inventario page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inventario/new$'));
        cy.getEntityCreateUpdateHeading('Inventario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventarioPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/inventario/api/inventarios',
          body: inventarioSample,
        }).then(({ body }) => {
          inventario = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/inventario/api/inventarios+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/inventario/api/inventarios?page=0&size=20>; rel="last",<http://localhost/services/inventario/api/inventarios?page=0&size=20>; rel="first"',
              },
              body: [inventario],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventarioPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Inventario page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventario');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventarioPageUrlPattern);
      });

      it('edit button click should load edit Inventario page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inventario');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventarioPageUrlPattern);
      });

      it('edit button click should load edit Inventario page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inventario');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventarioPageUrlPattern);
      });

      it('last delete button click should delete instance of Inventario', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventario').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', inventarioPageUrlPattern);

        inventario = undefined;
      });
    });
  });

  describe('new Inventario page', () => {
    beforeEach(() => {
      cy.visit(`${inventarioPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Inventario');
    });

    it('should create an instance of Inventario', () => {
      cy.get(`[data-cy="claveMedicamento"]`).type('trifling dictate scavenge');
      cy.get(`[data-cy="claveMedicamento"]`).should('have.value', 'trifling dictate scavenge');

      cy.get(`[data-cy="nombre"]`).type('cure what');
      cy.get(`[data-cy="nombre"]`).should('have.value', 'cure what');

      cy.get(`[data-cy="presentacion"]`).type('woozy');
      cy.get(`[data-cy="presentacion"]`).should('have.value', 'woozy');

      cy.get(`[data-cy="lote"]`).type('carburise platypus since');
      cy.get(`[data-cy="lote"]`).should('have.value', 'carburise platypus since');

      cy.get(`[data-cy="cantidad"]`).type('31122');
      cy.get(`[data-cy="cantidad"]`).should('have.value', '31122');

      cy.get(`[data-cy="cantidadMinima"]`).type('4518');
      cy.get(`[data-cy="cantidadMinima"]`).should('have.value', '4518');

      cy.get(`[data-cy="fechaCaducidad"]`).type('2026-02-26');
      cy.get(`[data-cy="fechaCaducidad"]`).blur();
      cy.get(`[data-cy="fechaCaducidad"]`).should('have.value', '2026-02-26');

      cy.get(`[data-cy="ubicacion"]`).type('lonely ugh eek');
      cy.get(`[data-cy="ubicacion"]`).should('have.value', 'lonely ugh eek');

      cy.get(`[data-cy="controlado"]`).should('not.be.checked');
      cy.get(`[data-cy="controlado"]`).click();
      cy.get(`[data-cy="controlado"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventario = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', inventarioPageUrlPattern);
    });
  });
});
