//CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;

import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TestSoumettreCommunication {

    private GCC gcc;
    private Auteur auteur;
    private Presidente presidente;

    @BeforeEach
    void setUp() throws OperationImpossible {
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();

        gcc = new GCC("ICT4S",
                today,
                Datutil.ajouterJoursADate(today, 1),
                Datutil.ajouterJoursADate(today, 2),
                Datutil.ajouterJoursADate(today, 3),
                Datutil.ajouterJoursADate(today, 4));

        // Ajout d'un auteur et d'une présidente
        auteur = new Auteur("a1", "Alice", "Durand", "IMT");
        gcc.ajouterPresidente("p1", "Présidente", "Test", "IMT");
        
        gcc.utilisateurs().put("a1", auteur);
        presidente = (Presidente) gcc.utilisateurs().get("p1");
    }

    @AfterEach
    void tearDown() {
        gcc = null;
        auteur = null;
        presidente = null;
    }

    @Test
    @DisplayName("TDUC1 - Test 1 : identifiant auteur null ou vide")
    void test1_identifiantAuteurMalForme() {
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication(null, "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("  ", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 2 : auteur inexistant")
    void test2_auteurInexistant() {
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("inconnu", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 3 : utilisateur n'est pas un auteur")
    void test3_utilisateurPasAuteur() {
        Evaluateur eval = new Evaluateur("e1", "Nom", "Prénom", "INS");
        gcc.utilisateurs().put("e1", eval);

        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("e1", "c3", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 4 : identifiant communication null ou vide")
    void test4_identifiantCommunicationMalForme() {
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", null, "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", " ", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 5 : communication avec ce titre existe déjà")
    void test5_communicationDejaExistante() throws OperationImpossible {
        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui());

        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 6 : titre null ou vide")
    void test6_titreMalForme() {
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c6", null, "Résumé", "Contenu", Datutil.aujourdhui()));
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c6", "", "Résumé", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 7 : résumé null ou vide")
    void test7_resumeMalForme() {
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c7", "Titre", null, "Contenu", Datutil.aujourdhui()));
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c7", "Titre", "", "Contenu", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 8 : contenu null ou vide")
    void test8_contenuMalForme() {
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c8", "Titre", "Résumé", null, Datutil.aujourdhui()));
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c8", "Titre", "Résumé", "", Datutil.aujourdhui()));
    }

    @Test
    @DisplayName("TDUC1 - Test 9 : date de soumission null")
    void test9_dateSoumissionNull() {
        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c9", "Titre", "Résumé", "Contenu", null));
    }

    @Test
    @DisplayName("TDUC1 - Test 10 : date de soumission dans le passé")
    void test10_dateSoumissionDansLePasse() {
        LocalDate hier = Datutil.ajouterJoursADate(Datutil.aujourdhui(), -1);

        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c10", "Titre", "Résumé", "Contenu", hier));
    }

    @Test
    @DisplayName("TDUC1 - Test 11 : date de soumission après la limite")
    void test11_dateSoumissionApresLimite() {
        LocalDate dateAprèsLimite = Datutil.ajouterJoursADate(Datutil.aujourdhui(), 5);

        assertThrows(OperationImpossible.class, () ->
                gcc.soumettreCommunication("a1", "c11", "Titre", "Résumé", "Contenu", dateAprèsLimite));
    }

    @Test
    @DisplayName("TDUC1 - Test 12 : soumission valide")
    void test12_soumissionValide() throws OperationImpossible {
        assertDoesNotThrow(() ->
                gcc.soumettreCommunication("a1", "c12", "Titre", "Résumé", "Contenu", Datutil.aujourdhui()));

        Communication c = gcc.getCommunications().get("c12");

        assertNotNull(c);
        assertEquals("c12", c.getIdentificateur());
        assertEquals(ÉtatCommunication.Soumise, c.getEtat());
        assertTrue(c.getAuteurs().contains(auteur));
    }

    /*
    @Test
    @DisplayName("TDUC1 - Test 13 : Notification envoyée à la présidente lors d'une soumission")
    void test13_NotificationSoumission() throws Exception {
        // Création d'un consommateur de test
        StringBuilder notificationsCapturees = new StringBuilder();
        MonConsommateur testConsumer = new MonConsommateur() {
            @Override
            public void onNext(String message) {
                notificationsCapturees.append(message);
                super.onNext(message);
            }
        };
        
        // Abonnement de la présidente
        presidente.subscribe(testConsumer);

        // Soumission d'une communication
        String titreTest = "Titre de test notification";
        gcc.soumettreCommunication("a1", "c13", titreTest, "Résumé", "Contenu", Datutil.aujourdhui());

        // Vérifications
        String notifications = notificationsCapturees.toString();
        assertFalse(notifications.isEmpty(), "Aucune notification reçue");
        assertTrue(notifications.contains(titreTest), "Le titre doit figurer dans la notification");
        assertTrue(notifications.contains("communication") || notifications.contains("soumis"), 
            "La notification doit concerner une soumission");
    }
    */
}