// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TestAjouterEvaluatriceACommunication {

    private GCC gcc;
    private Presidente presidente;
    private Communication comm;
    private Auteur auteur;
    private Evaluateur eval;

    @BeforeEach
    void setUp() throws OperationImpossible {
        // Réinitialisation de la date du test et création d'une instance de la conférence.
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();

        gcc = new GCC("Conf2025",
                today,
                Datutil.ajouterJoursADate(today, 2),
                Datutil.ajouterJoursADate(today, 3),
                Datutil.ajouterJoursADate(today, 4),
                Datutil.ajouterJoursADate(today, 5));

        // Ajouter une présidente
        gcc.ajouterPresidente("p1", "Nom", "Prenom", "IMT");
        presidente = (Presidente) gcc.utilisateurs().get("p1");

        // Ajouter un auteur et un évaluateur
        auteur = new Auteur("a1", "Alice", "Dupont", "ENS");
        eval = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");

        gcc.utilisateurs().put("a1", auteur);
        gcc.utilisateurs().put("e1", eval);

        // Soumettre une communication
        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui());
        comm = gcc.getCommunications().get("c1");
    }

    @AfterEach
    void tearDown() {
        gcc = null;
        comm = null;
        auteur = null;
        eval = null;
    }

    // Pré-condition : Présidente existe dans le système
    @Test
    @DisplayName("TDUC2 - Test 1 : Vérifier si la présidente existe dans le système")
    void test1_presidenteInexistante() {
        // Suppression de la présidente pour vérifier l'exception
        gcc.utilisateurs().remove("p1");
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", "e1"));
    }

    // Pré-condition : Communication existe dans le système
    @Test
    @DisplayName("TDUC2 - Test 2 : Vérifier si la communication existe dans le système")
    void test2_commInexistante() {
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("inconnue", "e1"));
    }

    // Pré-condition : Communication dans un état valide (Soumise ou En Évaluation)
    @Test
    @DisplayName("TDUC2 - Test 3 : Vérifier si la communication est dans un état valide")
    void test3_commEtatInvalide() throws OperationImpossible {
        // Changer l'état de la communication pour un état invalide
        comm.changerEtat(ÉtatCommunication.Brouillon);
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", "e1"));
    }

    // Pré-condition : Identifiant évaluatrice bien formé
    @Test
    @DisplayName("TDUC2 - Test 4 : Vérifier si l'identifiant de l'évaluatrice est valide")
    void test4_idEvaluatriceVide() {
        // Identifiant vide
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", ""));
    }

    @Test
    @DisplayName("TDUC2 - Test 5 : Vérifier si l'identifiant de l'évaluatrice existe")
    void test5_evaluatriceInexistante() {
        // Identifiant d'évaluatrice inexistant
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", "x999"));
    }

    // Pré-condition : Évaluatrice n'est pas un auteur ou co-auteur
    @Test
    @DisplayName("TDUC2 - Test 6 : Vérifier si l'évaluatrice n'est pas une autrice de la communication")
    void test6_evalEstAutrice() throws OperationImpossible {
        // Créer une utilisatrice qui est à la fois Evaluateur et Auteur
        Utilisateur evalAutrice = new Auteur("e1", "Eva", "Luatrice", "INRIA"); // On la fait passer pour une autrice
        gcc.utilisateurs().put("e1", evalAutrice);

        // Soumettre une communication avec cette utilisatrice comme autrice
        gcc.soumettreCommunication("e1", "cEval", "Titre", "Résumé", "Contenu", Datutil.aujourdhui());

        // Forcer aussi son rôle d’évaluatrice (elle est dans le comité)
        Evaluateur evaluateur = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");
        gcc.utilisateurs().put("e1", evaluateur); // Remplace temporairement dans la map

        // Ajouter la présidente et l'évaluatrice au comité
        presidente.ajouterEvaluateur(evaluateur);

        // Tenter de l’ajouter comme évaluatrice à sa propre communication → devrait échouer
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("cEval", "e1"));
    }

    // Pré-condition : Évaluatrice n'est pas déjà membre du comité des évaluateurs
    @Test
    @DisplayName("TDUC2 - Test 7 : Vérifier si l'évaluatrice est déjà dans le comité")
    void test7_dejaDansComite() throws OperationImpossible {
        // Ajouter l'évaluatrice au comité
        presidente.ajouterEvaluateur(eval);
        gcc.ajouterEvaluatriceACommunication("c1", "e1"); // Première fois

        // Essayer de l'ajouter une deuxième fois → devrait échouer
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", "e1"));
    }

    // Cas où l'ajout de l'évaluatrice est valide
    @Test
    @DisplayName("TDUC2 - Test 8 : Ajout d'une évaluatrice valide")
    void test8_ajoutOK() throws OperationImpossible {
        // Ajouter l'évaluatrice au comité
        presidente.ajouterEvaluateur(eval);
        assertDoesNotThrow(() -> gcc.ajouterEvaluatriceACommunication("c1", "e1"));
    }
    
    // Cas où la communication est déjà en évaluation et l'évaluatrice peut être affectée
    @Test
    @DisplayName("TDUC2 - Test 9 : Communication déjà en évaluation et ajout de l'évaluatrice")
    void test9_commEnEvaluation() throws OperationImpossible {
        // Passer la communication à l'état 'En_Evaluation'
        comm.changerEtat(ÉtatCommunication.En_Evaluation);
        
        // Ajouter l'évaluatrice
        presidente.ajouterEvaluateur(eval);
        assertDoesNotThrow(() -> gcc.ajouterEvaluatriceACommunication("c1", "e1"));
    }

    @Test
@DisplayName("Test10 :Notification envoyée à l'évaluatrice lors de l'affectation")
void test10_NotificationAffectationEvaluatrice() throws Exception {
    StringBuilder result = new StringBuilder();

    MonConsommateur customConsumer = new MonConsommateur() {
        @Override
        public void onNext(String item) {
            result.append(item).append("\n");
            super.onNext(item);
        }
    };

    eval.subscribe(customConsumer);

    // Ajout de l’évaluatrice au comité
    presidente.ajouterEvaluateur(eval);

    // Affecter l’évaluatrice
    gcc.ajouterEvaluatriceACommunication("c1", "e1");

    // Attendre l’appel asynchrone
    Thread.sleep(100);

    assertTrue(result.toString().contains("Titre"),
        "La notification devrait mentionner le titre de la communication.");
}

}
