// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import eu.telecomsudparis.csc4102.gcc.GCC;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;

class TestAjouterPresidente {
    private GCC gcc;
    private String identifiant;
    private String nom;
    private String prenom;
    private String institution;

    @BeforeEach
    void setUp() {
        Datutil.resetDateDuTest();
        LocalDate aujourdhui = Datutil.aujourdhui();
        gcc = new GCC("ICT4S", aujourdhui, Datutil.ajouterJoursADate(aujourdhui, 1),
                Datutil.ajouterJoursADate(aujourdhui, 2), Datutil.ajouterJoursADate(aujourdhui, 3),
                Datutil.ajouterJoursADate(aujourdhui, 4));
        identifiant = "Presidente1";
        nom = "nom1";
        prenom = "prenom1";
        institution = "identifiant";
    }

    @AfterEach
    void tearDown() {
        gcc = null;
        identifiant = null;
        nom = null;
        prenom = null;
        institution = null;
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("id null ou vide")
    void ajouterPresidenteTest1(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(input, nom, prenom, institution));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("nom null ou vide")
    void ajouterPresidenteTest2(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, input, prenom, institution));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("prénom null ou vide")
    void ajouterPresidenteTest3(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, nom, input, institution));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("institution null ou vide")
    void ajouterPresidenteTest4(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, nom, prenom, input));
    }

    @Disabled
    @Test
    @DisplayName("utilisateur avec id existe")
    void ajouterPresidenteTest5() {
        Assertions.assertEquals(null, gcc.getPresidente());
//      ajouter un auteur avec le même identifiant tel que « id existe », mais pas en tant que présidente 
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, nom, prenom, institution));
    }

    @Test
    @DisplayName("création présidente ok puis doublon ko")
    void ajouterPresidenteTest7Puis6() {
        Assertions.assertEquals(null, gcc.getPresidente());
        Assertions.assertDoesNotThrow(() -> gcc.ajouterPresidente(identifiant, nom, prenom, institution));
        Assertions.assertEquals(identifiant, gcc.getPresidente());
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, nom, prenom, institution));
    }
}
