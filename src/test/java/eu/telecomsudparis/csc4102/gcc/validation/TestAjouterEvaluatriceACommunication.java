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
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();

        gcc = new GCC("Conf2025",
                today,
                Datutil.ajouterJoursADate(today, 2),
                Datutil.ajouterJoursADate(today, 3),
                Datutil.ajouterJoursADate(today, 4),
                Datutil.ajouterJoursADate(today, 5));

        gcc.ajouterPresidente("p1", "Nom", "Prenom", "IMT");
        presidente = (Presidente) gcc.utilisateurs().get("p1");

        auteur = new Auteur("a1", "Alice", "Dupont", "ENS");
        eval = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");

        gcc.utilisateurs().put("a1", auteur);
        gcc.utilisateurs().put("e1", eval);

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

    @Test
    @DisplayName("TDUC2 - Test 1 : identifiant évaluatrice vide")
    void test1_idEvaluatriceVide() {
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", ""));
    }

    @Test
    @DisplayName("TDUC2 - Test 2 : évaluatrice inexistante")
    void test2_evaluatriceInexistante() {
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", "x999"));
    }

    @Test
    @DisplayName("TDUC2 - Test 3 : évaluatrice déjà dans le comité")
    void test3_dejaDansComite() throws OperationImpossible {
        presidente.ajouterEvaluateur(eval); // ajouter au comité
        gcc.ajouterEvaluatriceACommunication("c1", "e1"); // première fois
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", "e1")); // deuxième fois → exception
    }

    @Test
    @DisplayName("TDUC2 - Test 4 : évaluatrice est aussi autrice → exception")
    void test4_evalEstAutrice() throws OperationImpossible {
        // Créer une utilisatrice qui est à la fois Evaluateur et Auteur
        Utilisateur evalAutrice = new Auteur("e1", "Eva", "Luatrice", "INRIA"); // On la fait passer pour une autrice
        gcc.utilisateurs().put("e1", evalAutrice);

        // Soumettre une communication avec cette utilisatrice comme autrice
        gcc.soumettreCommunication("e1", "cEval", "Titre", "Résumé", "Contenu", Datutil.aujourdhui());
        Communication c = gcc.getCommunications().get("cEval");

        // Forcer aussi son rôle d’évaluatrice (elle est dans le comité)
        Evaluateur evaluateur = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");
        gcc.utilisateurs().put("e1", evaluateur); // Remplace temporairement dans la map

        Presidente presidente = (Presidente) gcc.utilisateurs().get("p1");
        presidente.ajouterEvaluateur(evaluateur);

        // Tenter de l’ajouter comme évaluatrice à sa propre communication → devrait échouer
        Assertions.assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("cEval", "e1"));
    }


    @Test
    @DisplayName("TDUC2 - Test 5 : communication pas en ÉtatCommunication valide")
    void test5_etatPasSoumiseNiEval() throws OperationImpossible {
        comm.changerEtat(ÉtatCommunication.Brouillon); // forcer un ÉtatCommunication invalide
        presidente.ajouterEvaluateur(eval);
        assertThrows(OperationImpossible.class, () ->
            gcc.ajouterEvaluatriceACommunication("c1", "e1"));
    }

    @Test
    @DisplayName("TDUC2 - Test 6 : ajout évaluatrice réussi")
    void test6_ajoutOK() throws OperationImpossible {
        presidente.ajouterEvaluateur(eval);
        assertDoesNotThrow(() -> gcc.ajouterEvaluatriceACommunication("c1", "e1"));
    }
}
