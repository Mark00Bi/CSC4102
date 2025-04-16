package eu.telecomsudparis.csc4102.gcc;

import java.util.Objects;

/**
 * Cette classe réalise le concept d'Évaluateur
 * 
 * @auteur MBARKI Marwen
 */
public class Evaluateur extends Utilisateur {
    /**
     * construit un évaluateur.
     * 
     * @param identifiant       l'identifiant.
     * @param nom               le nom.
     * @param prenom            le prénom.
     * @param institution       l'institution de l'évaluateur.
     */
    public Evaluateur(final String identifiant, final String nom, final String prenom, final String institution) {
        super(identifiant, nom, prenom, institution);
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
        if (!(obj instanceof Evaluateur)) {
            return false;
        }
        Evaluateur other = (Evaluateur) obj;
        return Objects.equals(getIdentificateur(), other.getIdentificateur());
    }

    @Override
    public String toString() {
        return "Évaluateur de programme [identifiant=" + getIdentificateur() + ", nom=" + getNom()
                + ", prenom=" + getPrenom() + ", institution=" + getInstitution() + "]";
    }
}
