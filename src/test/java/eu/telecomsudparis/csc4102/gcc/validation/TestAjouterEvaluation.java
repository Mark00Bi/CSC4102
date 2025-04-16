// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TestAjouterEvaluation {

    private GCC gcc;
    private Auteur auteur;
    private Evaluateur eval;

    @BeforeEach
    void setUp() throws OperationImpossible {
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();

        gcc = new GCC("Conf2025",
                today,
                Datutil.ajouterJoursADate(today, 2),
                Datutil.ajouterJoursADate(today, 3),
                Datutil.ajouterJoursADate(today, 4),
                Datutil.ajouterJoursADate(today, 5));

        gcc.ajouterPresidente("p1", "Prez", "One", "IMT");

        auteur = new Auteur("a1", "Alice", "Durand", "ENS");
        eval = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");

        gcc.utilisateurs().put("a1", auteur);
        gcc.utilisateurs().put("e1", eval);

        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", today);

        //Ajouter eval au comité
        Presidente presidente = (Presidente) gcc.utilisateurs().get("p1");
        presidente.ajouterEvaluateur(eval);
        gcc.ajouterEvaluatriceACommunication("c1", "e1");
    }


    @AfterEach
    void tearDown() {
        gcc = null;
        auteur = null;
        eval = null;
    }

    @Test
    @DisplayName("TDUC3 - Test 1 : identifiant évaluateur vide")
    void test1_idEvaluateurVide() {
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "", Avis.ACCEPTATION_FORTE, "Rapport"));
    }

    @Test
    @DisplayName("TDUC3 - Test 2 : évaluateur inexistant")
    void test2_evalInexistant() {
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "ghost", Avis.ACCEPTATION_FORTE, "Rapport"));
    }

    @Test
    @DisplayName("TDUC3 - Test 3 : titre communication mal formé")
    void test3_commInvalide() {
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("", "e1", Avis.ACCEPTATION_FORTE, "Rapport"));
    }

    @Test
    @DisplayName("TDUC3 - Test 4 : communication introuvable")
    void test4_commPasDansSysteme() {
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("inconnue", "e1", Avis.ACCEPTATION_FORTE, "Rapport"));
    }

    @Test
    @DisplayName("TDUC3 - Test 5 : évaluateur non affecté à cette communication")
    void test5_nonAssigne() throws OperationImpossible {
        Evaluateur eval2 = new Evaluateur("e2", "Test", "NonAssigné", "INSA");
        gcc.utilisateurs().put("e2", eval2);
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e2", Avis.ACCEPTATION_FORTE, "Rapport"));
    }
    
    @Test
    @DisplayName("TDUC3 - Test 6 : date d’évaluation avant date de soumission")
    void test6_dateAvantSoumission() throws OperationImpossible {
    	gcc.setdateLimiteSoumission(Datutil.ajouterJoursADate(Datutil.aujourdhui(), 1));
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Trop court"));
    }

    @Test
    @DisplayName("TDUC3 - Test 7 : date d’évaluation après date de décision")
    void test7_dateApresDA() throws OperationImpossible {
    	gcc.setdateAnnonceDecisions(Datutil.ajouterJoursADate(Datutil.aujourdhui(), -3));
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Trop tard"));
    }


    @Test
    @DisplayName("TDUC3 - Test 8 : tout est valide")
    void test8_valide() {
        assertDoesNotThrow(() -> gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Rapport"));
    }
}
