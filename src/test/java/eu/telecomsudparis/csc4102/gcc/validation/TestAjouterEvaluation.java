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
    private Presidente presidente;

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

        // Ajout des utilisateurs
        gcc.ajouterPresidente("p1", "Prez", "One", "IMT");
        auteur = new Auteur("a1", "Alice", "Durand", "ENS");
        eval = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");

        gcc.utilisateurs().put("a1", auteur);
        gcc.utilisateurs().put("e1", eval);

        // Soumettre une communication
        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", today);

        // Ajouter évaluateur au comité et à la communication
        presidente = (Presidente) gcc.utilisateurs().get("p1");
        presidente.ajouterEvaluateur(eval);
        gcc.ajouterEvaluatriceACommunication("c1", "e1");
    }

    @AfterEach
    void tearDown() {
        gcc = null;
        auteur = null;
        eval = null;
        presidente = null;
    }

    // Test 1 - Vérifie que l'identifiant évaluateur n'est pas vide
    @Test
    @DisplayName("TDUC3 - Test 1 : identifiant évaluateur vide")
    void test1_idEvaluateurVide() {
        LocalDate dateEvaluation = Datutil.aujourdhui();
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "", Avis.ACCEPTATION_FORTE, "Rapport", dateEvaluation));
    }

    // Test 2 - Vérifie que l'évaluateur existe dans le système
    @Test
    @DisplayName("TDUC3 - Test 2 : évaluateur inexistant")
    void test2_evalInexistant() {
        LocalDate dateEvaluation = Datutil.aujourdhui();
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "ghost", Avis.ACCEPTATION_FORTE, "Rapport", dateEvaluation));
    }

    // Test 3 - Vérifie que l'identifiant communication n'est pas vide
    @Test
    @DisplayName("TDUC3 - Test 3 : identifiant communication vide")
    void test3_commInvalide() {
        LocalDate dateEvaluation = Datutil.aujourdhui();
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("", "e1", Avis.ACCEPTATION_FORTE, "Rapport", dateEvaluation));
    }

    // Test 4 - Vérifie que la communication existe dans le système
    @Test
    @DisplayName("TDUC3 - Test 4 : communication introuvable")
    void test4_commPasDansSysteme() {
        LocalDate dateEvaluation = Datutil.aujourdhui();
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("inconnue", "e1", Avis.ACCEPTATION_FORTE, "Rapport", dateEvaluation));
    }

    // Test 5 - Vérifie que l'évaluateur est affecté à la communication
    @Test
    @DisplayName("TDUC3 - Test 5 : évaluateur non affecté à cette communication")
    void test5_nonAssigne() throws OperationImpossible {
        Evaluateur eval2 = new Evaluateur("e2", "Test", "NonAssigne", "INSA");
        gcc.utilisateurs().put("e2", eval2);

        LocalDate dateEvaluation = Datutil.aujourdhui();
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e2", Avis.ACCEPTATION_FORTE, "Rapport", dateEvaluation));
    }

    // Test 6 - Vérifie que la date d'évaluation n'est pas dans le passé
    @Test
    @DisplayName("TDUC3 - Test 6 : date d'évaluation dans le passé")
    void test6_dateSoumissionDansLePasse() {
        LocalDate dateEvaluation = Datutil.ajouterJoursADate(Datutil.aujourdhui(), -1);
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Rapport", dateEvaluation));
    }

    // Test 7 - Vérifie que la date d'évaluation est après la soumission
    @Test
    @DisplayName("TDUC3 - Test 7 : date d'évaluation avant date limite de soumission")
    void test7_dateAvantSoumission() {
        LocalDate dateEvaluation = Datutil.ajouterJoursADate(Datutil.aujourdhui(), -1);
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Trop tôt", dateEvaluation));
    }

    // Test 8 - Vérifie que la date d'évaluation est avant la date d'annonce
    @Test
    @DisplayName("TDUC3 - Test 8 : date d'évaluation après date d'annonce")
    void test8_dateApresAnnonce() {
        LocalDate dateEvaluation = Datutil.ajouterJoursADate(Datutil.aujourdhui(), 5);
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Trop tard", dateEvaluation));
    }

    // Test 9 - Vérifie que tous les champs obligatoires sont remplis
    @Test
    @DisplayName("TDUC3 - Test 9 : rapport vide")
    void test9_rapportVide() {
        LocalDate dateEvaluation = Datutil.aujourdhui();
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "", dateEvaluation));
    }

    // Test 10 - Cas nominal : tout est valide → succès de l'évaluation
    @Test
    @DisplayName("TDUC3 - Test 10 : tout est valide")
    void test10_valide() {
        LocalDate dateEvaluation = Datutil.aujourdhui();
        assertDoesNotThrow(() -> 
            gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Rapport complet", dateEvaluation));
    }

    /*
    // Test 11 - Vérifie que la présidente est notifiée lors d'une évaluation
    @Test
    @DisplayName("TDUC3 - Test 11 : notification à la présidente")
    void test11_notificationPresidente() throws Exception {
        // Création d'un consommateur pour capturer les notifications
        StringBuilder notifications = new StringBuilder();
        MonConsommateur consommateur = new MonConsommateur() {
            @Override
            public void onNext(String message) {
                notifications.append(message);
            }
        };
        
        presidente.subscribe(consommateur);
        
        // Ajout d'une évaluation
        gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "Rapport de test", Datutil.aujourdhui());
        
        // Vérification de la notification
        assertTrue(notifications.toString().contains("Nouvelle évaluation"),
            "La présidente devrait être notifiée des nouvelles évaluations");
    }
    */
}
