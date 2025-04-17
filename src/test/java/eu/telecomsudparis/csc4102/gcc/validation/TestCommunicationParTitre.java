// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.OperationImpossible;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestCommunicationParTitre {

    private GCC gcc;

    @BeforeEach
    void setUp() throws OperationImpossible {
        LocalDate today = LocalDate.now();
        gcc = new GCC("TestConf", today, today.plusDays(2), today.plusDays(3), today.plusDays(4), today.plusDays(5));
        gcc.ajouterPresidente("p1", "Nom", "Prenom", "IMT");

        Auteur auteur = new Auteur("a1", "Alice", "Durand", "INSA");
        gcc.utilisateurs().put("a1", auteur);

        // Ajouter une communication
        gcc.soumettreCommunication("a1", "c1", "MaComm", "Résumé", "Contenu", today);
    }

    @Test
    @DisplayName("Chercher communication existante par titre")
    void testChercherCommunicationExistante() {
        Optional<Communication> result = gcc.getCommunications()
                                            .values()
                                            .stream()
                                            .filter(c -> c.getTitre().equalsIgnoreCase("MaComm"))
                                            .findFirst();
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Récupérer communication existante par titre")
    void testGetCommunicationExistante() throws OperationImpossible {
        Communication comm = gcc.getCommunicationParTitre("MaComm");
        assertNotNull(comm);
        assertEquals("MaComm", comm.getTitre());
    }

    @Test
    @DisplayName("Récupérer communication inexistante par titre doit lancer une exception")
    void testGetCommunicationInexistante() {
        assertThrows(OperationImpossible.class, () -> gcc.getCommunicationParTitre("Inconnu"));
    }
}
