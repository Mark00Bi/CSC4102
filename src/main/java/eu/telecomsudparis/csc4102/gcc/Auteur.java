// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc;

import java.util.Objects;
import java.util.concurrent.SubmissionPublisher;


/**
 * Cette classe réalise le concept d'auteur
 * 
 * @auteur MBARKI Marwen
 */
public class Auteur extends Utilisateur {
	
	protected SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
	/**
     * construit un auteur.
     * 
     * @param identifiant       l'identifiant.
     * @param nom               le nom.
     * @param prenom            le prénom.
     * @param institution       l'institution de l'évaluateur.
     */
    public Auteur(final String identifiant, final String nom, final String prenom, final String institution) {
        super(identifiant, nom, prenom, institution);
        this.publisher = new SubmissionPublisher<>();
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(getIdentificateur());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Auteur)) {
            return false;
        }
        Auteur other = (Auteur) obj;
        return Objects.equals(getIdentificateur(), other.getIdentificateur());
    }

    @Override
    public String toString() {
        return "Auteur [identifiant=" + getIdentificateur() + ", nom=" + getNom()
                + ", prenom=" + getPrenom() + ", institution=" + getInstitution() + "]";
    }

}
