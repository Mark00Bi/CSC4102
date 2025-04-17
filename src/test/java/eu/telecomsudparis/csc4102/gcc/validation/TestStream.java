// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestStream {

    private GCC gcc;
    private Auteur auteur;
    private Evaluateur eval;
    private Communication comm;

    @BeforeEach
    void setUp() throws OperationImpossible {
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();

        gcc = new GCC("ConfStream2025",
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

        Presidente presidente = (Presidente) gcc.utilisateurs().get("p1");
        presidente.ajouterEvaluateur(eval);
        gcc.ajouterEvaluatriceACommunication("c1", "e1");

        gcc.ajouterEvaluation("c1", "e1", Avis.ACCEPTATION_FAIBLE, "Bon travail", Datutil.aujourdhui());
    }

    @Test
    @DisplayName("Lister les auteurs d'une communication")
    void testListerAuteurs() throws OperationImpossible {
        List<String> auteurs = gcc.listerAuteurs("c1");
        assertEquals(1, auteurs.size());
        assertTrue(auteurs.contains("a1"));
    }

    @Test
    @DisplayName("Lister les évaluatrices d'une communication")
    void testGetEvaluatricesPourCommunication() throws OperationImpossible {
        Communication comm = gcc.getCommunications().get("c1");
        List<String> evaluatrices = gcc.getEvaluatricesPourCommunication(comm);
        assertEquals(1, evaluatrices.size());
        assertTrue(evaluatrices.contains("e1"));
    }

    @Test
    @DisplayName("Lister les évaluations d'une communication")
    void testListerEvaluations() throws OperationImpossible {
        List<String> evaluations = gcc.listerEvaluations("c1");
        assertEquals(1, evaluations.size());
        assertTrue(evaluations.get(0).contains("e1"));
    }
}
