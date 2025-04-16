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

    @Test
    @DisplayName("TDUC1 - Test 1 : identifiant auteur mal formé")
    void test1_identifiantAuteurMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication(null, "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 2 : auteur inexistant")
    void test2_auteurInexistant() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("inconnu", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }
    
    @Test
    @DisplayName("TDUC1 - Test 3 : utilisateur n’est pas un auteur")
    void test3_utilisateurPasAuteur() {
        Evaluateur eval = new Evaluateur("e1", "Nom", "Prénom", "INS");
        gcc.utilisateurs().put("e1", eval);

        Assertions.assertThrows(OperationImpossible.class, () ->
            gcc.soumettreCommunication("e1", "c3", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 4 : utilisateur est dans le comité")
    void test4_utilisateurDansComite() throws OperationImpossible {
        Evaluateur eval = new Evaluateur("e1", "Nom", "Prénom", "INS");
        gcc.utilisateurs().put("e1", eval);

        Presidente presidente = new Presidente("p1", "Nom", "Prénom", "INS");
        presidente.ajouterEvaluateur(eval); // ajout au comité
        //  Simulation manuelle car pas d'accès direct à gcc.Presidente

        Assertions.assertThrows(OperationImpossible.class, () ->
            gcc.soumettreCommunication("e1", "c4", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 5 : date de soumission après la limite")
    void test5_dateSoumissionAprèsLimite() {
        LocalDate dateAprèsLimite = Datutil.ajouterJoursADate(Datutil.aujourdhui(), 5); // au-delà de daSoumission

        Assertions.assertThrows(OperationImpossible.class, () ->
            gcc.soumettreCommunication("a1", "c5", "Titre", "Résumé", "Contenu", dateAprèsLimite));
    }

    @Test
    @DisplayName("TDUC1 - Test 6 : contenu mal formé")
    void test6_contenuMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "", Datutil.aujourdhui()));
    }
    
    @Test
    @DisplayName("TDUC1 - Test 7 : résumé vide")
    void test7_resumeMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
            gcc.soumettreCommunication("a1", "c7", "Titre", "", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 8 : titre vide")
    void test8_titreMalForme() {
        Assertions.assertThrows(OperationImpossible.class, () ->
            gcc.soumettreCommunication("a1", "c8", "", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 9 : communication avec ce titre existe déjà")
    void test9_communicationExistant() throws OperationImpossible {
        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui());

        Assertions.assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 10 : soumission valide")
    void test10_soumissionValide() throws OperationImpossible {
        Assertions.assertDoesNotThrow(() ->
                gcc.soumettreCommunication("a1", "c2", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));

        Communication c = gcc.getCommunications().get("c2");

        Assertions.assertNotNull(c);
        Assertions.assertEquals("c2", c.getIdentificateur());
        Assertions.assertEquals(ÉtatCommunication.Soumise, c.getEtat());
        Assertions.assertTrue(c.getAuteurs().contains(auteur));
    }
}
