// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.unitaires;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import eu.telecomsudparis.csc4102.gcc.Communication;
import eu.telecomsudparis.csc4102.gcc.Evaluateur;
import eu.telecomsudparis.csc4102.gcc.Presidente;

class TestPresidente {

	private Map<Communication, Set<Evaluateur>> affectations;
    @BeforeEach
    void setUp() {
    	affectations = new HashMap<>();
        // nop
    }

    @AfterEach
    void tearDown() {
        // nop
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("id null ou vide")
    void constructeurPresidenteTest1(String input) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Presidente(input, "nom", "prénom", "institution", affectations));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("nom null ou vide")
    void constructeurPresidenteTest2(String input) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Presidente("identificateur", input, "prenom", "institution", affectations));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("prénom null ou vide")
    void constructeurPresidenteTest3(String input) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Presidente("identificateur", "nom", input, "institution", affectations));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("institution null ou vide")
    void constructeurPresidenteTest4(String input) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Presidente("identificateur", "nom", "prenom", input , affectations));
    }

    @Test
    @DisplayName("création présidente ok")
    void constructeurPresidenteTest6Puis5() {
        Presidente Presidente = new Presidente("identificateur", "nom", "prénom", "institution", affectations);
        Assertions.assertNotNull(Presidente);
        Assertions.assertEquals("identificateur", Presidente.getIdentificateur());
    }
}
