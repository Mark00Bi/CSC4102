// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;

import org.junit.jupiter.api.*;

import java.time.LocalDate;

class TestSoumettreCommunication {

    private GCC gcc;
    private Auteur auteur;

    @BeforeEach
    void setUp() {
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();

        gcc = new GCC("ICT4S",
                today,
                Datutil.ajouterJoursADate(today, 1),
                Datutil.ajouterJoursADate(today, 2),
                Datutil.ajouterJoursADate(today, 3),
                Datutil.ajouterJoursADate(today, 4));

        auteur = new Auteur("a1", "Alice", "Durand", "IMT");
        gcc.utilisateurs().put("a1", auteur); // simulate registration
    }

    @AfterEach
    void tearDown() {
        gcc = null;
        auteur = null;
    }

    // Vérifie que l'identifiant auteur n'est pas null ou vide
    @Test
    @DisplayName("TDUC1 - Test 1 : identifiant auteur null ou vide")
    void test1_identifiantAuteurMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication(null, "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("  ", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    // Vérifie que l'auteur est bien enregistré dans le système
    @Test
    @DisplayName("TDUC1 - Test 2 : auteur inexistant")
    void test2_auteurInexistant() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("inconnu", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    // Vérifie que l'utilisateur est bien de type Auteur
    @Test
    @DisplayName("TDUC1 - Test 3 : utilisateur n’est pas un auteur")
    void test3_utilisateurPasAuteur() {
        Evaluateur eval = new Evaluateur("e1", "Nom", "Prénom", "INS");
        gcc.utilisateurs().put("e1", eval);

        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("e1", "c3", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    // Vérifie que l'identifiant de la communication n'est pas null ou vide
    @Test
    @DisplayName("TDUC1 - Test 4 : identifiant communication null ou vide")
    void test4_identifiantCommunicationMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", null, "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", " ", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    // Vérifie qu'une communication avec le même identifiant ne peut être soumise deux fois
    @Test
    @DisplayName("TDUC1 - Test 5 : communication avec ce titre existe déjà")
    void test5_communicationDejaExistante() throws OperationImpossible {
        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui());

        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    // Vérifie que le titre n'est pas null ou vide
    @Test
    @DisplayName("TDUC1 - Test 6 : titre null ou vide")
    void test6_titreMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c6", null, "Résumé", "Contenu", Datutil.aujourdhui()));
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c6", "", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    // Vérifie que le résumé n'est pas null ou vide
    @Test
    @DisplayName("TDUC1 - Test 7 : résumé null ou vide")
    void test7_resumeMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c7", "Titre", null, "Contenu", Datutil.aujourdhui()));
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c7", "Titre", "", "Contenu", Datutil.aujourdhui()));
    }

    // Vérifie que le contenu n'est pas null ou vide
    @Test
    @DisplayName("TDUC1 - Test 8 : contenu null ou vide")
    void test8_contenuMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c8", "Titre", "Résumé", null, Datutil.aujourdhui()));
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c8", "Titre", "Résumé", "", Datutil.aujourdhui()));
    }

    // Vérifie que la date de soumission n'est pas null
    @Test
    @DisplayName("TDUC1 - Test 9 : date de soumission null")
    void test9_dateSoumissionNull() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c9", "Titre", "Résumé", "Contenu", null));
    }

    // Vérifie que la date de soumission n'est pas dans le passé
    @Test
    @DisplayName("TDUC1 - Test 10 : date de soumission dans le passé")
    void test10_dateSoumissionDansLePasse() {
        LocalDate hier = Datutil.ajouterJoursADate(Datutil.aujourdhui(), -1);

        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c10", "Titre", "Résumé", "Contenu", hier));
    }

    // Vérifie que la date de soumission ne dépasse pas la date limite fixée
    @Test
    @DisplayName("TDUC1 - Test 11 : date de soumission après la limite")
    void test11_dateSoumissionApresLimite() {
        LocalDate dateAprèsLimite = Datutil.ajouterJoursADate(Datutil.aujourdhui(), 5); // > dateLimiteSoumission

        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c11", "Titre", "Résumé", "Contenu", dateAprèsLimite));
    }

    // Vérifie que la soumission fonctionne avec des données valides
    @Test
    @DisplayName("TDUC1 - Test 12 : soumission valide")
    void test12_soumissionValide() throws OperationImpossible {
        Assertions.assertDoesNotThrow(() ->
                gcc.soumettreCommunication("a1", "c12", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));

        Communication c = gcc.getCommunications().get("c12");

        Assertions.assertNotNull(c);
        Assertions.assertEquals("c12", c.getIdentificateur());
        Assertions.assertEquals(ÉtatCommunication.Soumise, c.getEtat());
        Assertions.assertTrue(c.getAuteurs().contains(auteur));
    }
}
