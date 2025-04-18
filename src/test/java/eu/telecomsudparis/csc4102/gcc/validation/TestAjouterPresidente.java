// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import eu.telecomsudparis.csc4102.gcc.GCC;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;
import eu.telecomsudparis.csc4102.gcc.Auteur;

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
        gcc = new GCC("ICT4S", aujourdhui, 
            Datutil.ajouterJoursADate(aujourdhui, 1),
            Datutil.ajouterJoursADate(aujourdhui, 2),
            Datutil.ajouterJoursADate(aujourdhui, 3),
            Datutil.ajouterJoursADate(aujourdhui, 4));
        
        identifiant = "Presidente1";
        nom = "nom1";
        prenom = "prenom1";
        institution = "institution1";
    }

    @AfterEach
    void tearDown() {
        gcc = null;
        identifiant = null;
        nom = null;
        prenom = null;
        institution = null;
    }

    // TDUC - Test 1 : identifiant null ou vide → doit lever une exception
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("TDUC - Test 1 : identifiant null ou vide")
    void ajouterPresidenteTest1(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(input, nom, prenom, institution));
    }

    // TDUC - Test 2 : nom null ou vide → doit lever une exception
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("TDUC - Test 2 : nom null ou vide")
    void ajouterPresidenteTest2(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, input, prenom, institution));
    }

    // TDUC - Test 3 : prénom null ou vide → doit lever une exception
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("TDUC - Test 3 : prénom null ou vide")
    void ajouterPresidenteTest3(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, nom, input, institution));
    }

    // TDUC - Test 4 : institution null ou vide → doit lever une exception
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("TDUC - Test 4 : institution null ou vide")
    void ajouterPresidenteTest4(String input) {
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, nom, prenom, input));
    }

    // TDUC - Test 5 : un utilisateur avec le même identifiant existe déjà (ex : un auteur) → doit lever une exception
    @Test
    @DisplayName("TDUC - Test 5 : identifiant déjà utilisé par un autre utilisateur")
    void ajouterPresidenteTest5() throws OperationImpossible {
        Assertions.assertNull(gcc.getPresidente());

        // Ajout d’un auteur avec le même identifiant
        Auteur auteur = new Auteur(identifiant, "Dupont", "Jean", "Université X");
        gcc.utilisateurs().put(identifiant, auteur);

        // Tentative d’ajouter une présidente avec le même identifiant → doit échouer
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente(identifiant, nom, prenom, institution));
    }

    // TDUC - Test 6 : une présidente existe déjà dans le système → ajout d’une deuxième interdite
    @Test
    @DisplayName("TDUC - Test 6 : présidente déjà existante")
    void ajouterPresidenteTest6() throws OperationImpossible {
        gcc.ajouterPresidente(identifiant, nom, prenom, institution);
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente("Presidente2", "Nom2", "Prenom2", "Institution2"));
    }

    // TDUC - Test 7 : ajout d’une présidente avec des données valides → succès attendu
    @Test
    @DisplayName("TDUC - Test 7 : ajout valide de présidente")
    void ajouterPresidenteTest7() {
        Assertions.assertNull(gcc.getPresidente());
        Assertions.assertDoesNotThrow(() ->
                gcc.ajouterPresidente(identifiant, nom, prenom, institution));
        Assertions.assertEquals(identifiant, gcc.getPresidente());
    }

    // TDUC - Test 8 : ajout valide puis tentative de doublon (reprend test 7 + 6)
    @Test
    @DisplayName("TDUC - Test 8 : ajout valide puis doublon")
    void ajouterPresidenteTest8() {
        Assertions.assertNull(gcc.getPresidente());
        Assertions.assertDoesNotThrow(() -> gcc.ajouterPresidente(identifiant, nom, prenom, institution));
        Assertions.assertEquals(identifiant, gcc.getPresidente());

        // Deuxième ajout interdit car présidente déjà existante
        Assertions.assertThrows(OperationImpossible.class,
                () -> gcc.ajouterPresidente("Presidente2", "Nom2", "Prenom2", "Institution2"));
    }
}
