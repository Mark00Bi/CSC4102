package eu.telecomsudparis.csc4102.gcc;

import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import eu.telecomsudparis.csc4102.util.OperationImpossible;

import java.time.LocalDate;

/**
 * Cette classe réalise le concept de communication.
 * 
 * @author MBARKI Marwen
 */
public class Communication {

	/**
	 * L'identifiant unique de la communication.
	 */
	private final String identificateur;

	/**
	 * Le titre de la communication.
	 */
	private String titre;

	/**
	 * Le résumé de la communication, destiné à donner un aperçu rapide du contenu.
	 */
	private String resume;

	/**
	 * Le contenu complet de la communication.
	 */
	private String contenu;

	/**
	 * La date à laquelle la communication a été soumise.
	 */
	private LocalDate dateSoumission;

	/**
	 * L’ensemble des auteurs ayant contribué à la communication.
	 */
	private Set<Utilisateur> auteurs;

	/**
	 * L’ÉtatCommunication courant de la communication (ex. : Brouillon, Soumise, En_Evaluation, Acceptée...).
	 */
	private ÉtatCommunication etatCommunication;

	/**
	 * L’ensemble des évaluations associées à la communication.
	 */
	private Set<Evaluation> evaluations = new HashSet<>();


	/**
	 * Construit une communication avec ses données principales.
	 *
	 * @param identifiant           l'identifiant unique de la communication
	 * @param titre                 le titre de la communication
	 * @param resume                le résumé de la communication
	 * @param contenu               le contenu complet de la communication
	 * @param dateSoumission        la date à laquelle la communication a été soumise
	 * @throws OperationImpossible  si une des valeurs est invalide (null ou vide)
	 */
	public Communication(final String identifiant, final String titre, final String resume, 
	                     final String contenu, final LocalDate dateSoumission) throws OperationImpossible {

        // Préconditions
        if (identifiant == null || identifiant.isBlank()) {
            throw new OperationImpossible("identifiant ne peut pas être null ou vide");
        }
        if (titre == null || titre.isBlank()) {
            throw new OperationImpossible("titre ne peut pas être null ou vide");
        }
        if (resume == null || resume.isBlank()) {
            throw new OperationImpossible("résumé ne peut pas être null ou vide");
        }
        if (contenu == null || contenu.isBlank()) {
            throw new OperationImpossible("contenu ne peut pas être null ou vide");
        }
        if (dateSoumission == null) {
            throw new OperationImpossible("date de soumission ne peut pas être null");
        }

        this.identificateur = identifiant;
        this.titre = titre;
        this.resume = resume;
        this.contenu = contenu;
        this.dateSoumission = dateSoumission;
        this.auteurs = new HashSet<>();
        this.etatCommunication = ÉtatCommunication.Brouillon;

        assert invariant();
    }

	/**
	 * Ajoute un auteur à la communication.
	 *
	 * @param auteur l'utilisateur à ajouter comme auteur
	 * @throws OperationImpossible si l’auteur est invalide ou si l'ÉtatCommunication de la communication ne le permet pas
	 */
    public void ajouterAuteurs(final Utilisateur auteur) throws OperationImpossible {
        if (auteur == null) {
            throw new OperationImpossible("Veuillez donner en argument l'auteur à ajouter.");
        }
        if (etatCommunication != ÉtatCommunication.Brouillon) {
            throw new OperationImpossible("Impossible d’ajouter un auteur en dehors de l’ÉtatCommunication Brouillon.");
        }
        if (!(etatCommunication == ÉtatCommunication.Brouillon 
        	|| etatCommunication == ÉtatCommunication.Soumise 
        	|| etatCommunication == ÉtatCommunication.En_Evaluation)) {
        	    throw new OperationImpossible("Impossible d’ajouter un auteur en dehors des ÉtatCommunications Brouillon, Soumise ou En_Evaluation.");
        	}
        auteurs.add(auteur);
        assert invariant();
    }

    /**
     * Modifie l'ÉtatCommunication de la communication 
     * Méthode publique uniquement pour les besoins de tests.
     *
     * @param nouvelEtat le nouvel ÉtatCommunication à appliquer à la communication
     * @throws OperationImpossible si l'ÉtatCommunication fourni est nul
     */
    
    public void changerEtat(final ÉtatCommunication nouvelEtat) throws OperationImpossible {
    	if (nouvelEtat == null) {
    		throw new OperationImpossible("L'ÉtatCommunication ne peut pas être null");
    	}
    	this.etatCommunication = nouvelEtat;
    	assert invariant();
    }
    
    /**
     * Soumet la communication pour évaluation.
     * <p>
     * Change l'ÉtatCommunication de la communication de {@code Brouillon} à {@code Soumise}.
     * </p>
     *@param auteur l'auteur de la communication  
     * @throws OperationImpossible si la communication n'est pas dans l'ÉtatCommunication {@code Brouillon}
     */
    
    public void soumettre(Utilisateur auteur) throws OperationImpossible {
    	if (etatCommunication != ÉtatCommunication.Brouillon) {
    		throw new OperationImpossible("La communication doit être en ÉtatCommunication brouillon pour être soumise");
    	}
        this.ajouterAuteurs(auteur);
    	this.etatCommunication = ÉtatCommunication.Soumise;
    	assert invariant();
    }
    
    /**
     * Active l'évaluation de la communication.
     * <p>
     * Change l'ÉtatCommunication de la communication de {@code Soumise} à {@code En_Evaluation}.
     * </p>
     *
     * @throws OperationImpossible si la communication n'est pas dans l'ÉtatCommunication {@code Soumise}
     */
    
    public void activerEvaluation() throws OperationImpossible {
    	if (etatCommunication != ÉtatCommunication.Soumise) {
    		throw new OperationImpossible("La communication doit être en ÉtatCommunication soumise pour être évaluer");
    	}
    	this.etatCommunication = ÉtatCommunication.En_Evaluation;
    	assert invariant();
    }
    
    /**
     * Prend une décision finale sur la communication (acceptée ou refusée).
     * <p>
     * Change l'ÉtatCommunication de la communication vers {@code Acceptée} ou {@code Refusée},
     * selon la décision fournie.
     * </p>
     *
     * @param d la décision prise par la présidente ({@code Acceptée} ou {@code Refusée})
     * @throws OperationImpossible si la communication n'est pas dans un ÉtatCommunication permettant la décision
     *                             ou si une décision a déjà été prise
     */
    
    public void decider(final Décision d) throws OperationImpossible {
    	if (etatCommunication != ÉtatCommunication.Evaluée && etatCommunication != ÉtatCommunication.Soumise) {
    		throw new OperationImpossible("La communication n'est pas dans un ÉtatCommunication permettant la prise de décision ( Evaluée ou Soumise )");
    	}
    	if (etatCommunication == ÉtatCommunication.Acceptée || etatCommunication == ÉtatCommunication.Refusée) {
    		throw new OperationImpossible("Décision déjà pris");
    	}
    	this.etatCommunication = ÉtatCommunication.valueOf(d.name());
    	assert invariant();
    }
    
    /**
<<<<<<< HEAD
    * Crée et ajoute une évaluation à la communication, puis met à jour son {@link ÉtatCommunication} à {@code ÉtatCommunication.Evaluée}.
    *
    * <p>
    * Cette méthode crée une nouvelle instance de {@link Evaluation} à partir des paramètres fournis
    * (évaluateur, avis, rapport et date d’évaluation), puis l’ajoute à la communication.
    * Une fois l’évaluation ajoutée, l’état de la communication est mis à jour pour refléter qu’elle a été évaluée.
    * </p>
    *
    * @param evaluateur l’évaluateur qui a réalisé l’évaluation.
    * @param avis l’avis rendu (par exemple {@code ACCEPTATION_FORTE}, {@code REFUS}...).
    * @param rapport le contenu du rapport d’évaluation.
    * @param dateEvaluation la date à laquelle l’évaluation a été effectuée.
    * @throws OperationImpossible si l’un des paramètres est {@code null
    */
    public void ajouterEvaluation(final Evaluateur evaluateur, final Avis avis, final String rapport, final LocalDate dateEvaluation) throws OperationImpossible {
        if (evaluateur == null) {
            throw new OperationImpossible("le champs évaluateur ne peut pas être null");
        }
        if (avis == null) {
            throw new OperationImpossible("le champs avis ne peut pas être null");
        }
    	if (rapport == null || rapport.isBlank()) {
            throw new OperationImpossible("le champs rapport ne peut pas être null ou vide");
        }
        if (dateEvaluation == null) {
            throw new OperationImpossible("le champs date d'évaluation ne peut pas être null");
        }
    	Evaluation e = new Evaluation(evaluateur, avis, rapport, dateEvaluation);
        evaluations.add(e);
        this.etatCommunication = ÉtatCommunication.Evaluée;
        assert invariant();
    }


    /**
     * Vérifie si l'invariant de la classe {@code Communication} est respecté.
     * @return {@code true} si l'invariant est respecté, {@code false} sinon.
     */
    public boolean invariant() {
        return identificateur != null && !identificateur.isBlank() 
        		&& titre != null && !titre.isBlank() 
        		&& resume != null && !resume.isBlank() 
        		&& contenu != null && !contenu.isBlank() 
        		&& dateSoumission != null 
        		&& etatCommunication != null 
        		&& auteurs != null;
    }

    // Getters
    
    /**
     * Retourne l'identificateur unique de la communication.
     *
     * @return l'identifiant de cette communication.
     */
    public String getIdentificateur() {
        return identificateur;
    }
    
    /**
     * Retourne le titre de la communication.
     *
     * @return le titre de cette communication.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Retourne le resume unique de la communication.
     *
     * @return le resume de cette communication.
     */
    public String getResume() {
        return resume;
    }

    /**
     * Retourne le contenu de la communication.
     *
     * @return le contenu de cette communication.
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Retourne la date de soumission de la communication.
     *
     * @return la date de soumission de cette communication.
     */
    public LocalDate getDateSoumission() {
        return dateSoumission;
    }
    
    /**
     * Retourne l'ensemble des auteurs associés à cette communication.
     *
     * @return un {@code Set} contenant les utilisateurs auteurs de la communication.
     */

    public Set<Utilisateur> getAuteurs() {
        return auteurs;
    }
    
    /**
     * Retourne l'ÉtatCommunication actuel de la communication.
     *
     * @return l'ÉtatCommunication {@code ÉtatCommunication} de la communication.
     */

    public ÉtatCommunication getEtat() {
        return etatCommunication;
    }
    
    /**
     * Retourne la liste des évaluations associées à cette communication.
     *
     * @return un ensemble d'évaluations
     */
    public Set<Evaluation> getEvaluations() {
        return evaluations;
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
        if (!(obj instanceof Communication)) { 
        	return false; 
        	}
        Communication other = (Communication) obj;
        return Objects.equals(identificateur, other.identificateur);
    }

    @Override
    public String toString() {
        return "Communication [identifiant=" + identificateur 
        		+ ", titre=" + titre 
        		+ ", résumé=" + resume 
        		+ ", contenu=" + contenu 
        		+ ", ÉtatCommunication=" + etatCommunication + "]";
    }
}
