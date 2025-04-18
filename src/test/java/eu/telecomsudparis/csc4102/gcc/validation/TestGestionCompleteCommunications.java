// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests complets de validation pour le système GCC
 */
class TestGestionCompleteCommunications {

    private GCC gcc;
    private Auteur auteur;
    private Evaluateur eval;

    @BeforeEach
    void setUp() throws OperationImpossible {
        Datutil.resetDateDuTest();
        LocalDate today = Datutil.aujourdhui();

        gcc = new GCC("ConfTestIntégration",
                today,
                Datutil.ajouterJoursADate(today, 2),
                Datutil.ajouterJoursADate(today, 3),
                Datutil.ajouterJoursADate(today, 4),
                Datutil.ajouterJoursADate(today, 5));

        gcc.ajouterPresidente("p1", "Nom", "Prenom", "IMT");
        auteur = new Auteur("a1", "Alice", "Durand", "ENS");
        eval = new Evaluateur("e1", "Eva", "Luatrice", "INRIA");
        gcc.utilisateurs().put("a1", auteur);
        gcc.utilisateurs().put("e1", eval);

        gcc.soumettreCommunication("a1", "c1", "MaComm", "Résumé", "Contenu", today);
        gcc.soumettreCommunication("a1", "c2", "Titre", "Résumé", "Contenu", today);

        Presidente presidente = (Presidente) gcc.utilisateurs().get("p1");
        presidente.ajouterEvaluateur(eval);
        gcc.ajouterEvaluatriceACommunication("c2", "e1");
        gcc.ajouterEvaluation("c2", "e1", Avis.ACCEPTATION_FAIBLE, "Bon travail", Datutil.aujourdhui());
    }

    // Tests sur la création et récupération basique des communications
    @Test
    @DisplayName("TDUC - Test1: Recherche d'une communication par titre")
    void testRechercheCommunicationParTitre() {
        Optional<Communication> result = gcc.getCommunications()
                                        .values()
                                        .stream()
                                        .filter(c -> c.getTitre().equalsIgnoreCase("MaComm"))
                                        .findFirst();
        assertTrue(result.isPresent(), "La communication devrait être trouvée");
    }

    @Test
    @DisplayName("TDUC - Test2: Récupération directe d'une communication par titre")
    void testGetCommunicationParTitre() throws OperationImpossible {
        Communication comm = gcc.getCommunicationParTitre("MaComm");
        assertNotNull(comm, "La communication ne devrait pas être null");
        assertEquals("MaComm", comm.getTitre(), "Le titre devrait correspondre");
    }

    @Test
    @DisplayName("TDUC - Test3: Tentative de récupération avec titre inexistant")
    void testGetCommunicationTitreInexistant() {
        assertThrows(OperationImpossible.class, 
            () -> gcc.getCommunicationParTitre("Inconnu"),
            "Devrait lever une exception pour un titre inconnu");
    }

    // Tests sur la gestion des auteurs
    @Test
    @DisplayName("TDUC - Test4: Liste des auteurs d'une communication")
    void testListeAuteursCommunication() throws OperationImpossible {
        List<String> auteurs = gcc.listerAuteurs("c1");
        assertEquals(1, auteurs.size(), "Devrait avoir exactement 1 auteur");
        assertTrue(auteurs.contains("a1"), "L'auteur 'a1' devrait être présent");
    }

    // Tests sur le processus d'évaluation
    @Test
    @DisplayName("TDUC - Test5: Liste des évaluateurs assignés à une communication")
    void testListeEvaluateursCommunication() throws OperationImpossible {
        Communication comm = gcc.getCommunications().get("c2");
        List<String> evaluatrices = gcc.getEvaluatricesPourCommunication(comm);
        assertEquals(1, evaluatrices.size(), "Devrait avoir exactement 1 évaluateur");
        assertTrue(evaluatrices.contains("e1"), "L'évaluateur 'e1' devrait être présent");
    }

    @Test
    @DisplayName("TDUC - Test6: Consultation des évaluations d'une communication")
    void testConsultationEvaluations() throws OperationImpossible {
        List<String> evaluations = gcc.listerEvaluations("c2");
        assertEquals(1, evaluations.size(), "Devrait avoir exactement 1 évaluation");
        assertTrue(evaluations.get(0).contains("e1"), "L'évaluation devrait mentionner l'évaluateur 'e1'");
    }
}
