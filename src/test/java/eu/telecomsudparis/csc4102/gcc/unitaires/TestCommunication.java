// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.unitaires;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.OperationImpossible;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TestCommunication {

    private Auteur auteur;
    private Evaluateur evaluatrice;
    private Presidente presidente;
    private Communication comm;

    @BeforeEach
    void setUp() {
        auteur = new Auteur("a1", "Nom", "Prenom", "Institution");
        evaluatrice = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");
        presidente = new Presidente("p1", "Mme", "Prez", "IMT");
    }

    @AfterEach
    void tearDown() {
        auteur = null;
        evaluatrice = null;
        presidente = null;
        comm = null;
    }

    // === ALGO1C1 : Constructeur Communication ===

    @Test
    @DisplayName("Constructeur - Titre null → exception")
    void testConstructeurTitreNull() {
        assertThrows(OperationImpossible.class, () ->
            new Communication("c1", null, "Résumé", "Contenu", LocalDate.now()));
    }

    @Test
    @DisplayName("Constructeur - Résumé vide → exception")
    void testConstructeurResumeVide() {
        assertThrows(OperationImpossible.class, () ->
            new Communication("c1", "Titre", "", "Contenu", LocalDate.now()));
    }

    @Test
    @DisplayName("Constructeur - Contenu vide → exception")
    void testConstructeurContenuVide() {
        assertThrows(OperationImpossible.class, () ->
            new Communication("c1", "Titre", "Résumé", "", LocalDate.now()));
    }

    @Test
    @DisplayName("Constructeur - Tout valide → état = BROUILLON")
    void testConstructeurValide() throws OperationImpossible {
        Communication c = new Communication("c1", "Titre", "Résumé", "Contenu", LocalDate.now());
        c.ajouterAuteurs(auteur);
        assertEquals(ÉtatCommunication.Brouillon, c.getEtat());
        assertTrue(c.getAuteurs().contains(auteur));
    }

    // === ALGO2C1 : Ajouter Evaluatrice ===

    @Test
    @DisplayName("ajouterEvaluatrice - Evaluatrice null → exception")
    void testAjouterEvaluatriceNull() {
        assertThrows(OperationImpossible.class, () -> {
            Communication c = new Communication("c1", "Titre", "Résumé", "Contenu", LocalDate.now());
            presidente.affecterEvaluateur(null, c);
        });
    }

    @Test
    @DisplayName("ajouterEvaluatrice - Evaluatrice non membre du comité → exception")
    void testAjouterEvaluatriceNonDansComite() throws OperationImpossible {
        Communication c = new Communication("c1", "Titre", "Résumé", "Contenu", LocalDate.now());
        c.soumettre();
        // Evaluatrice non ajoutée au comité
        assertFalse(presidente.contientEvaluateur(evaluatrice));
        assertThrows(OperationImpossible.class, () -> {
            if (!presidente.contientEvaluateur(evaluatrice)) {
                throw new OperationImpossible("Évaluatrice non membre");
            }
        });
    }

    @Test
    @DisplayName("ajouterEvaluatrice - Évaluatrice = autrice → exception")
    void testAjouterEvaluatriceEstAutrice() throws OperationImpossible {
        Communication c = new Communication("c1", "Titre", "Résumé", "Contenu", LocalDate.now());
        c.ajouterAuteurs(evaluatrice);
        c.soumettre();
        presidente.ajouterEvaluateur(evaluatrice);
        assertThrows(OperationImpossible.class, () -> {
            if (c.getAuteurs().contains(evaluatrice)) {
                throw new OperationImpossible("Évaluatrice est aussi autrice !");
            }
        });
    }

    @Test
    @DisplayName("ajouterEvaluatrice - État invalide → exception")
    void testAjouterEvaluatriceEtatInvalide() throws OperationImpossible {
        Communication c = new Communication("c1", "Titre", "Résumé", "Contenu", LocalDate.now());
        // État = BROUILLON → invalide
        presidente.ajouterEvaluateur(evaluatrice);
        assertThrows(OperationImpossible.class, () -> {
            if (!c.getEtat().equals(ÉtatCommunication.Soumise) && !c.getEtat().equals(ÉtatCommunication.En_Evaluation)) {
                throw new OperationImpossible("État non autorisé");
            }
        });
    }

    @Test
    @DisplayName("ajouterEvaluatrice - Tout est valide → succès")
    void testAjouterEvaluatriceValide() throws OperationImpossible {
        Communication c = new Communication("c1", "Titre", "Résumé", "Contenu", LocalDate.now());
        c.ajouterAuteurs(auteur);
        c.soumettre();
        presidente.ajouterEvaluateur(evaluatrice);
        assertFalse(presidente.estAssigneA(evaluatrice, c));
        presidente.affecterEvaluateur(evaluatrice, c);
        assertTrue(presidente.estAssigneA(evaluatrice, c));
    }
}
