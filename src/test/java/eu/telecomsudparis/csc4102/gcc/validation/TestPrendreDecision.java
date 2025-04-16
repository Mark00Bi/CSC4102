// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc.validation;

import eu.telecomsudparis.csc4102.gcc.*;
import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;

import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestPrendreDecision {

 private GCC gcc;
 private Auteur auteur;

 @BeforeEach
 void setUp() throws OperationImpossible {
     Datutil.resetDateDuTest();
     LocalDate today = Datutil.aujourdhui();

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
 @DisplayName("TDUC4 - Test 4 : ÉtatCommunication de la communication invalide (ex. Brouillon)")
 void test4_etatNonValide() throws OperationImpossible {
     Communication comm = gcc.getCommunications().get("c1");
     comm.changerEtat(ÉtatCommunication.Brouillon); // ÉtatCommunication non autorisé
     assertThrows(OperationImpossible.class,
             () -> gcc.prendreDecision("p1", "c1", Décision.Refusée));
 }



 @Test
 @DisplayName("TDUC4 - Test 5 : communication déjà décidée")
 void test6_dejaDecidee() throws OperationImpossible {
     gcc.prendreDecision("p1", "c1", Décision.Refusée);
     assertThrows(OperationImpossible.class,
             () -> gcc.prendreDecision("p1", "c1", Décision.Acceptée));
 }

 @Test
 @DisplayName("TDUC4 - Test 6 : décision acceptée avec succès")
 void test7_decisionAcceptée() {
     assertDoesNotThrow(() -> gcc.prendreDecision("p1", "c1", Décision.Acceptée));
     assertEquals(ÉtatCommunication.Acceptée, gcc.getCommunications().get("c1").getEtat());
 }

 @Test
 @DisplayName("TDUC4 - Test 7 : décision refusée avec succès")
 void test8_decisionRefusée() {
     assertDoesNotThrow(() -> gcc.prendreDecision("p1", "c1", Décision.Refusée));
     assertEquals(ÉtatCommunication.Refusée, gcc.getCommunications().get("c1").getEtat());
 }
}