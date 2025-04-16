// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.unitaires;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import eu.telecomsudparis.csc4102.gcc.Presidente;

class TestPresidente {

    @BeforeEach
    void setUp() {
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
                () -> new Presidente(input, "nom", "prénom", "institution"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("nom null ou vide")
    void constructeurPresidenteTest2(String input) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Presidente("identificateur", input, "prenom", "institution"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("prénom null ou vide")
    void constructeurPresidenteTest3(String input) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Presidente("identificateur", "nom", input, "institution"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("institution null ou vide")
    void constructeurPresidenteTest4(String input) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Presidente("identificateur", "nom", "prenom", input));
    }

    @Test
    @DisplayName("création présidente ok")
    void constructeurPresidenteTest6Puis5() {
        Presidente Presidente = new Presidente("identificateur", "nom", "prénom", "institution");
        Assertions.assertNotNull(Presidente);
        Assertions.assertEquals("identificateur", Presidente.getIdentificateur());
    }
}
