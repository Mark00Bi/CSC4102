package eu.telecomsudparis.csc4102.gcc;

import java.util.Objects;

/**
 * Cette classe réalise le concept d'utilisateur du système.
 * 
 * @author Denis Conan
 */
public abstract class Utilisateur {
    /**
     * l'identificateur de l'utilisateur.
     */
    private final String identificateur;
    /**
     * le nom de l'utilisateur.
     */
    private String nom;
    /**
     * le prénom de l'utilisateur.
     */
    private String prenom;
    /**
     * l'institution de l'utilisateur chercheur.
     */
    private String institution;

    /**
     * construit un utilisateur.
     * 
     * @param identifiant       l'identifiant.
     * @param nom               le nom.
     * @param prenom            le prénom.
     * @param institution       l'institution du chercheur.
     */
    protected Utilisateur(final String identifiant, final String nom, final String prenom, final String institution) {
        if (identifiant == null || identifiant.isBlank()) {
            throw new IllegalArgumentException("identifiant ne peut pas être null ou vide");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("nom ne peut pas être null ou vide");
        }
        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("prenom ne peut pas être null ou vide");
        }
        if (institution == null || institution.isBlank()) {
            throw new IllegalArgumentException("institution ne peut pas être null ou vide");
        }
        this.identificateur = identifiant;
        this.nom = nom;
        this.prenom = prenom;
        this.institution = institution;
        assert invariant();
    }

    /**
     * vérifie l'invariant de la classe.
     * 
     * @return {@code true} si l'invariant est respecté.
     */
    public boolean invariant() {
        return identificateur != null && !identificateur.isBlank() && nom != null && !nom.isBlank() && prenom != null
                && !prenom.isBlank() && institution != null && !institution.isBlank();
    }

    /**
     * obtient l'identificateur.
     * 
     * @return l'identificateur.
     */
    public String getIdentificateur() {
        return identificateur;
    }

    /**
     * obtient le nom.
     * 
     * @return le nom.
     */
    protected String getNom() {
        return nom;
    }

    /**
     * obtient le prénom.
     * 
     * @return le prénom.
     */
    protected String getPrenom() {
        return prenom;
    }

    /**
     * obtient l'institution.
     * 
     * @return l'institution.
     */
    protected String getInstitution() {
        return institution;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificateur);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Utilisateur other = (Utilisateur) obj;
        return Objects.equals(identificateur, other.identificateur);
    }

    @Override
    public String toString() {
        return "Utilisateur [identifiant=" + identificateur + ", nom=" + nom + ", prenom=" + prenom + ", institution="
                + institution + "]";
    }
}
