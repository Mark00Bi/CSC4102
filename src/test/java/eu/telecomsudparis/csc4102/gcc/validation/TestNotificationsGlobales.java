
// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;

import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestNotificationsGlobales {

    private GCC gcc;

    @BeforeEach
    void setUp() throws OperationImpossible {
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();
        gcc = new GCC("Conf2025",
                today,
                Datutil.ajouterJoursADate(today, 1),
                Datutil.ajouterJoursADate(today, 2),
                Datutil.ajouterJoursADate(today, 3),
                Datutil.ajouterJoursADate(today, 4));
    }

    @Test
    @DisplayName("Validation des notifications pour chaque rôle")
    void testNotificationsDifférenciées() throws Exception {
        StringBuilder notifAuteur = new StringBuilder();
        StringBuilder notifEvaluateur = new StringBuilder();
        StringBuilder notifPresidente = new StringBuilder();

        MonConsommateur consAuteur = new MonConsommateur() {
            @Override public void onNext(String item) {
                notifAuteur.append(item).append("\n");
                super.onNext(item);
            }
        };

        MonConsommateur consEvaluateur = new MonConsommateur() {
            @Override public void onNext(String item) {
                notifEvaluateur.append(item).append("\n");
                super.onNext(item);
            }
        };

        MonConsommateur consPresidente = new MonConsommateur() {
            @Override public void onNext(String item) {
                notifPresidente.append(item).append("\n");
                super.onNext(item);
            }
        };

        gcc.ajouterPresidente("p1", "Julie", "Durand", "TSP");
        Presidente pres = (Presidente) gcc.utilisateurs().get("p1");
        pres.subscribe(consPresidente);

        Auteur a = new Auteur("a1", "Alice", "Dupont", "INSA");
        a.subscribe(consAuteur);
        gcc.utilisateurs().put("a1", a);

        Evaluateur e = new Evaluateur("e1", "Eva", "Luateur", "IMT");
        e.subscribe(consEvaluateur);
        gcc.utilisateurs().put("e1", e);
        pres.ajouterEvaluateur(e);

        // Soumission
        gcc.soumettreCommunication("a1", "c1", "Titre", "Résumé", "Contenu", LocalDate.now());

        // Affectation
        gcc.ajouterEvaluatriceACommunication("c1", "e1");

        // Evaluation
        gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FORTE, "rapport", LocalDate.now());

        // Décision
        gcc.prendreDecision("p1", "c1", Décision.Refusée);

        Thread.sleep(100);

        assertTrue(notifAuteur.toString().contains("Décision"), "L’auteur doit recevoir la notification de décision.");
        assertTrue(notifEvaluateur.toString().contains("affectée"), "L’évaluateur doit recevoir la notification d’affectation.");
        assertTrue(notifPresidente.toString().contains("Nouvelle communication"), "La présidente doit recevoir une notification de soumission.");
        assertTrue(notifPresidente.toString().contains("Nouvelle évaluation"), "La présidente doit recevoir une notification d’évaluation.");

        assertFalse(notifEvaluateur.toString().contains("Décision"));
        assertFalse(notifAuteur.toString().contains("affectée"));
        assertFalse(notifAuteur.toString().contains("Nouvelle évaluation"));
    }
}
