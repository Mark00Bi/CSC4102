// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;

import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TestPrendreDecision {

    private GCC gcc;
    private Auteur auteur;

    @BeforeEach
    void setUp() throws OperationImpossible {
        Datutil.resetDateDuTest();
        LocalDate aujourdHui = Datutil.aujourdhui();

        gcc = new GCC("Conf2025",
            aujourdHui.plusDays(1),        // dateLimiteSoumission
            aujourdHui.plusDays(2),        // dateAnnonceDecisions
            aujourdHui.plusDays(3),        // dateVersionFinale
            aujourdHui.plusDays(4),        // dateDebutConference
            aujourdHui.plusDays(5));       // dateFinConference

        gcc.ajouterPresidente("p1", "Présidente", "Nom", "IMT");

        auteur = new Auteur("a1", "Alice", "Durand", "INSA");


        gcc.utilisateurs().put("a1", auteur);

        // Communication valide soumise
        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", aujourdHui); 
    }

    @AfterEach
    void tearDown() {
        gcc = null;
        auteur = null;
    }

    @Test
    @DisplayName("TDUC4 - Test 1 : identifiant présidente vide")
    void test1_presidenteVide() {
        assertThrows(OperationImpossible.class,
            () -> gcc.prendreDecision("", "c1", Décision.Refusée));
    }

    @Test
    @DisplayName("TDUC4 - Test 2 : identifiant incorrect (pas la présidente)")
    void test2_nonPresidente() {
        gcc.utilisateurs().put("intrus", new Auteur("intrus", "X", "Y", "INRIA"));
        assertThrows(OperationImpossible.class,
            () -> gcc.prendreDecision("intrus", "c1", Décision.Acceptée));
    }

    @Test
    @DisplayName("TDUC4 - Test 3 : communication introuvable")
    void test3_commInconnue() {
        assertThrows(OperationImpossible.class,
            () -> gcc.prendreDecision("p1", "inconnue", Décision.Acceptée));
    }

    @Test
    @DisplayName("TDUC4 - Test 4 : état de la communication invalide (ex. Brouillon)")
    void test4_etatNonValide() throws OperationImpossible {
        Communication comm = gcc.getCommunications().get("c1");
        comm.changerEtat(ÉtatCommunication.Brouillon);
        assertThrows(OperationImpossible.class,
            () -> gcc.prendreDecision("p1", "c1", Décision.Refusée));
    }

    @Test
    @DisplayName("TDUC4 - Test 5 : communication déjà décidée")
    void test5_dejaDecidee() throws OperationImpossible {
        gcc.prendreDecision("p1", "c1", Décision.Refusée);
        assertThrows(OperationImpossible.class,
            () -> gcc.prendreDecision("p1", "c1", Décision.Acceptée));
    }

    @Test
    @DisplayName("TDUC4 - Test 6 : décision acceptée avec succès")
    void test6_decisionAcceptée() {
        assertDoesNotThrow(() -> gcc.prendreDecision("p1", "c1", Décision.Acceptée));
        assertEquals(ÉtatCommunication.Acceptée, gcc.getCommunications().get("c1").getEtat());
    }

    @Test
    @DisplayName("TDUC4 - Test 7 : décision refusée avec succès")
    void test7_decisionRefusée() {
        assertDoesNotThrow(() -> gcc.prendreDecision("p1", "c1", Décision.Refusée));
        assertEquals(ÉtatCommunication.Refusée, gcc.getCommunications().get("c1").getEtat());
    }

    @Test
    @DisplayName("TDUC4 - Test 8 : notification envoyée à l’auteur après décision")
    void test8_notificationEnvoyee() throws Exception {
        StringBuilder result = new StringBuilder();
        
        //consommateur personnalisé
        MonConsommateur customConsumer = new MonConsommateur() {
        	@Override
        	public void onNext(String item) {
        		result.append(item).append("\n");
        		super.onNext(item);
        	}
        };
        Auteur a =new Auteur("a2","Bob","Martin","TSP");
        a.subscribe(customConsumer);
        gcc.utilisateurs().put("a2", a);
        gcc.soumettreCommunication("a2", "c2","titre2", "Résumé 2", "Contenu 2",Datutil.aujourdhui());
        gcc.prendreDecision("p1", "c2", Décision.Refusée);
    	
        // Laisser un délai pour les notifications asynchrones
        Thread.sleep(100);
        assertTrue(result.toString().contains("Refusée"), "Le message de notification doit indiquer l'état 'Refusée'");
    }
}
