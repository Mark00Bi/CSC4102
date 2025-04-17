// CHECKSTYLE:OFF
package eu.telecomsudparis.csc4102.gcc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import eu.telecomsudparis.csc4102.util.OperationImpossible;

/**
 * Cette classe réalise le concept de présidente du comité de programme.
 * 
 * @author Denis Conan
 */
public class Presidente extends Evaluateur {
    /**
     * construit une présidente.
     * 
     * @param identifiant       l'identifiant.
     * @param nom               le nom.
     * @param prenom            le prénom.
     * @param institution       l'institution de la présidente.
     */
	private Set<Evaluateur> evaluateurs = new HashSet<>();
	private Map<Communication, Set<Evaluateur>> affectations = new HashMap<>();
    public Presidente(final String identifiant, final String nom, final String prenom, final String institution) {
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
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Presidente other = (Presidente) obj;
        return Objects.equals(getIdentificateur(), other.getIdentificateur());
    }
    
    public void ajouterEvaluateur(Evaluateur e) {
        evaluateurs.add(e);
    }

    public boolean contientEvaluateur(Evaluateur e) {
        return evaluateurs.contains(e);
    }
    
    public boolean estAssigneA(Evaluateur e, Communication c) {
        return affectations.containsKey(c) && affectations.get(c).contains(e);
    }

    public void affecterEvaluateur(Evaluateur e, Communication c) throws OperationImpossible {
    	if (e == null || c == null) {
            throw new OperationImpossible("L’évaluateur ou la communication ne peut pas être null.");
        }
        affectations.computeIfAbsent(c, k -> new HashSet<>()).add(e);
    }
    
    
    public Map<Communication, Set<Evaluateur>> getAffectations() {
        return affectations;
    } 

    @Override
    public String toString() {
        return "Présidente du comité de programme [identifiant=" + getIdentificateur() + ", nom=" + getNom()
                + ", prenom=" + getPrenom() + ", institution=" + getInstitution() + "]";
    }
}
