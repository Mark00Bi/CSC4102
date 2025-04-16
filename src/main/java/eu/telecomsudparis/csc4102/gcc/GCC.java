package eu.telecomsudparis.csc4102.gcc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import eu.telecomsudparis.csc4102.util.Datutil;
import eu.telecomsudparis.csc4102.util.OperationImpossible;
import eu.telecomsudparis.csc4102.gcc.ÉtatCommunication;


/**
 * Cette classe est la façade du logiciel.
 * 
 * @author Denis Conan
 */
public class GCC {
    /**
     * le nom du système est le nom de la conférence.
     */
    private final String nomConference;
    /**
     * la date limite de soummission.
     */
    private LocalDate dateLimiteSoumission;
    /**
     * la date d'annonce des décisions.
     */
    private LocalDate dateAnnonceDecisions;
    /**
     * la date limite de préparation de la version finale.
     */
    private LocalDate dateVersionFinale;
    /**
     * la date de début de la conférence.
     */
    private LocalDate dateDebutConference;
    /**
     * la date de fin de la conférence.
     */
    private LocalDate dateFinConference;
    /**
     * la présidente du comité de programme.
     */
    private Presidente presidente;
    /**
     * les utilisateurs, y compris la présidente.
     */
    private final Map<String, Utilisateur> utilisateurs;
   
    /** 
     * La communication.
     */
    private Communication communication;
   
    /**
     * les communications.
     */
    private final Map<String, Communication> communications;

    /**
     * construit une instance du système.
     * 
     * @param nomConference        le nom de la conférence.
     * @param dateLimiteSoumission date limite de soummission.
     * @param dateAnnonceDecisions date d'annonce des décisions.
     * @param dateVersionFinale    date limite de préparation de la version finale.
     * @param dateDebutConference  date de début de la conférence.
     * @param dateFinConference    date de fin de la conférence.
     */
    public GCC(final String nomConference, final LocalDate dateLimiteSoumission, final LocalDate dateAnnonceDecisions,
            final LocalDate dateVersionFinale, final LocalDate dateDebutConference, final LocalDate dateFinConference) {
        if (nomConference == null || nomConference.isBlank()) {
            throw new IllegalArgumentException("nomConference ne peut pas être null ou vide");
        }
        Objects.requireNonNull(dateLimiteSoumission, "dateLimiteSoumission ne peut pas être null");
        Objects.requireNonNull(dateAnnonceDecisions, "dateAnnonceDecisions ne peut pas être null");
        Objects.requireNonNull(dateVersionFinale, "dateVersionFinale ne peut pas être null");
        Objects.requireNonNull(dateDebutConference, "dateDebutConference ne peut pas être null");
        Objects.requireNonNull(dateFinConference, "dateFinConference ne peut pas être null");
        if (!(dateLimiteSoumission.isBefore(dateAnnonceDecisions) && dateAnnonceDecisions.isBefore(dateVersionFinale)
                && dateVersionFinale.isBefore(dateDebutConference) && (dateDebutConference.isBefore(dateFinConference)
                        || dateDebutConference.isEqual(dateFinConference)))) {
            throw new IllegalArgumentException("erreur dans l'agenda");
        }
        this.nomConference = nomConference;
        this.dateLimiteSoumission = dateLimiteSoumission;
        this.dateAnnonceDecisions = dateAnnonceDecisions;
        this.dateVersionFinale = dateVersionFinale;
        this.dateDebutConference = dateDebutConference;
        this.dateFinConference = dateFinConference;
        this.utilisateurs = new HashMap<>();
        this.communications = new HashMap<>();
        assert invariant();
    }

    /**
     * l'invariant de la façade.
     * 
     * @return {@code true} si l'invariant est respecté.
     */
    public boolean invariant() {
        return nomConference != null && !nomConference.isBlank() && dateLimiteSoumission != null
                && dateAnnonceDecisions != null && dateVersionFinale != null && dateDebutConference != null
                && dateFinConference != null && dateLimiteSoumission.isBefore(dateAnnonceDecisions)
                && dateAnnonceDecisions.isBefore(dateVersionFinale) && dateVersionFinale.isBefore(dateDebutConference)
                && (dateDebutConference.isBefore(dateFinConference) || dateDebutConference.isEqual(dateFinConference))
                && utilisateurs != null;
    }

    /**
     * obtenir la date limite de soumission.
     * 
     * @return la chaîne de caractères.
     */
    public String getDateLimiteSoumission() {
        return dateLimiteSoumission.toString();
    }

    /**
     * obtenir la date d'annonce des décisions.
     * 
     * @return la chaîne de caractères.
     */
    public String getDateAnnonceDecisions() {
        return dateAnnonceDecisions.toString();
    }

    /**
     * obtenir la date limite de modification pour la version finale.
     * 
     * @return la chaîne de caractères.
     */
   public String getDateVersionFinale() {
        return dateVersionFinale.toString();
    }

   /**
    * obtenir la date de début de la conférence.
    * 
    * @return la chaîne de caractères.
    */
    public String getDateDebutConference() {
        return dateDebutConference.toString();
    }

    /**
     * obtenir la date de fin de la conférence.
     * 
     * @return la chaîne de caractères.
     */
    public String getDateFinConference() {
        return dateFinConference.toString();
    }
    
    /**
     * Définit une nouvelle date limite pour la soumission des communications.
     *
     * @param date la nouvelle date limite de soumission
     */
    
    public void setdateLimiteSoumission(final LocalDate date) {
    	this.dateLimiteSoumission = date;
    }
    
    /**
     * Définit une nouvelle date pour l'annonce des décisions.
     *
     * @param date la nouvelle date d'annonce des décisions
     */
    
    public void setdateAnnonceDecisions(final LocalDate date) {
    	this.dateAnnonceDecisions = date;
    }

    /**
     * ajoute une présidente. Le système n'est pas ouvert pour la présidente, mais
     * l'administrateur n'a pas de représentation dans le système : il n'y a donc
     * pas de contrôle ici car on suppose que le contrôle d'accès est fait avant la
     * façade.
     * 
     * @param identificateur    l'identificateur de la présidente.
     * @param nom               le nom de la présidente.
     * @param prenom            le prénom de la présidente.
     * @param institution       l'institution de la présidente.
     * @throws OperationImpossible en cas de problème sur les pré-conditions.
     */
    public void ajouterPresidente(final String identificateur, final String nom, final String prenom,
            final String institution) throws OperationImpossible {
        if (identificateur == null || identificateur.isBlank()) {
            throw new OperationImpossible("identificateur ne peut pas être null ou vide");
        }
        if (nom == null || nom.isBlank()) {
            throw new OperationImpossible("nom ne peut pas être null ou vide");
        }
        if (prenom == null || prenom.isBlank()) {
            throw new OperationImpossible("prenom ne peut pas être null ou vide");
        }
        if (institution == null || institution.isBlank()) {
            throw new OperationImpossible("institution ne peut pas être null ou vide");
        }
        if (presidente != null) {
            throw new OperationImpossible("il y a déjà une présidente");
        }
        if (utilisateurs.get(identificateur) != null) {
            throw new OperationImpossible(
                    identificateur + " déjà dans les utilisateurs (donc avec un rôle déjà défini)");
        }
        presidente = new Presidente(identificateur, nom, prenom, institution);
        utilisateurs.put(identificateur, presidente);
        assert invariant();
    }

    /**
     * obtenir la présidente du comité de programme.
     * 
     * @return l'identificateur de la présidente.
     */
    public String getPresidente() {
        return (presidente == null) ? null : presidente.getIdentificateur();
    }
    
    /**
     * Fournit l'ensemble des utilisateurs enregistrés dans le système.
     * @return une map contenant les utilisateurs identifiés par leur identifiant
     */

    public Map<String, Utilisateur> utilisateurs() {
        return utilisateurs;
    }
    
    /**
     * Permet à un auteur de soumettre une communication au système.
     * 
     * <p>Cette méthode vérifie la validité des informations de la communication
     * ainsi que les préconditions liées à l'auteur, aux champs obligatoires, 
     * à la date de soumission et à l'unicité de l'identifiant.</p>
     *
     * @param idAuteur          l'identifiant de l'auteur de la communication.
     * @param idComm            l'identifiant unique de la communication.
     * @param titre             le titre de la communication.
     * @param resume            le résumé de la communication.
     * @param contenu           le contenu de la communication.
     * @param dateSoumission    la date à laquelle la communication est soumise.
     * @throws OperationImpossible si une des préconditions n'est pas respectée :
     *                              - identifiants null ou invalides,
     *                              - utilisateur non-auteur,
     *                              - titre, résumé ou contenu vide,
     *                              - date de soumission dépassant la date limite,
     *                              - identifiant de communication déjà existant.
     */
    public void soumettreCommunication(final String idAuteur, final String idComm,
    		final String titre, final String resume, final String contenu, final LocalDate dateSoumission) throws OperationImpossible {

    		if (idAuteur == null || idAuteur.isBlank()) {
    			throw new OperationImpossible("L'identifiant de l'auteur est invalide.");
    		}

    		Utilisateur auteur = utilisateurs.get(idAuteur);
    		if (auteur == null || !(auteur instanceof Auteur)) {
    			throw new OperationImpossible("L'utilisateur spécifié n'est pas un auteur.");
    		}

    		if (idComm == null || idComm.isBlank() || communications.containsKey(idComm)) {
    			throw new OperationImpossible("Identifiant de communication invalide ou déjà utilisé.");
    		}

    		if (titre == null || titre.isBlank() 
    				|| resume == null || resume.isBlank() 
    				|| contenu == null || contenu.isBlank()) {
    			throw new OperationImpossible("Titre, résumé ou contenu non valides.");
    		}

    		if (dateSoumission.isAfter(dateLimiteSoumission)) {
    			throw new OperationImpossible("La date de soumission dépasse la date limite.");
    		}

    		Communication comm = new Communication(idComm, titre, resume, contenu, dateSoumission);
    		comm.ajouterAuteurs(auteur);
    		comm.soumettre();

    		communications.put(idComm, comm);
    		assert invariant();
    }


    /**
     * obtenir la présidente du comité de programme.
     * 
     * @return l'identificateur de la communication.
     */
    public String getCommunication() {
        return (communication == null) ? null : communication.getIdentificateur();
    }
    
    /**
     * Retourne l'ensemble des communications enregistrées dans le système.
     *
     * @return une map contenant les communications, associées à leur identifiant.
     */
    public Map<String, Communication> getCommunications() {
        return communications;
    }
    
    /**
     * Affecte une évaluatrice à une communication.
     *
     * @param idCommunication l'identifiant de la communication.
     * @param idEvaluatrice l'identifiant de l'évaluatrice.
     * @throws OperationImpossible en cas de violation des préconditions.
     */
    public void ajouterEvaluatriceACommunication(final String idCommunication, final String idEvaluatrice) throws OperationImpossible {
        // Vérifier l'existence de la présidente
        if (presidente == null) {
            throw new OperationImpossible("Aucune présidente n’est définie.");
        }

        // Vérifier l'existence de la communication
        Communication comm = communications.get(idCommunication);
        if (comm == null) {
            throw new OperationImpossible("Communication introuvable.");
        }

        // Vérifier l'ÉtatCommunication de la communication
        if (!(comm.getEtat().equals(ÉtatCommunication.Soumise) || comm.getEtat().equals(ÉtatCommunication.En_Evaluation))) {
            throw new OperationImpossible("La communication n’est pas dans un ÉtatCommunication permettant l’évaluation.");
        }

        // Vérifier l'existence et le rôle de l’évaluatrice
        Utilisateur u = utilisateurs.get(idEvaluatrice);
        if (u == null || !(u instanceof Evaluateur)) {
            throw new OperationImpossible("L’utilisateur n’est pas une évaluatrice valide.");
        }
        Evaluateur evaluatrice = (Evaluateur) u;

        // Vérifier que l’évaluatrice fait partie du comité
        if (!presidente.contientEvaluateur(evaluatrice)) {
            throw new OperationImpossible("L’évaluatrice n’est pas membre du comité.");
        }

        // Vérifier que l’évaluatrice n’est pas un(e) auteur(e) de la communication
        if (comm.getAuteurs().stream().anyMatch(a -> a.getIdentificateur().equals(evaluatrice.getIdentificateur()))) {
            throw new OperationImpossible("L’évaluatrice est aussi autrice de cette communication.");
        }


        // Vérifier qu'elle n’est pas déjà affectée
        if (presidente.estAssigneA(evaluatrice, comm)) {
            throw new OperationImpossible("L’évaluatrice est déjà affectée à cette communication.");
        }

        // Affectation
        presidente.affecterEvaluateur(evaluatrice, comm);

        // Si l'ÉtatCommunication est encore Soumise → on passe à En_Evaluation
        if (comm.getEtat().equals(ÉtatCommunication.Soumise)) {
            comm.activerEvaluation();
        }

        assert invariant();
    }
    
    /**
     * Permet à une évaluatrice affectée de soumettre une évaluation sur une communication.
     *
     * <p>Cette méthode vérifie que :</p>
     * <ul>
     *   <li>La communication existe dans le système.</li>
     *   <li>L'identifiant correspond à une évaluatrice valide.</li>
     *   <li>L'évaluatrice est bien affectée à la communication par la présidente.</li>
     *   <li>La communication est en ÉtatCommunication {@code En_Evaluation}.</li>
     *   <li>La date d'évaluation est comprise entre la date limite de soumission et la date d'annonce des décisions.</li>
     * </ul>
     *
     * @param idComm       identifiant de la communication à évaluer.
     * @param idEvaluateur identifiant de l’évaluatrice.
     * @param avis         l’avis donné par l’évaluatrice (ex. ACCEPTATION_FORTE).
     * @param rapport      le contenu du rapport d’évaluation.
     * @throws OperationImpossible si une des vérifications échoue.
     */
    public void ajouterEvaluation(final String idComm, final String idEvaluateur, final Avis avis, final String rapport) throws OperationImpossible {
        
    	// On vérifie que la communication existe
    	if (idComm == null || idComm.isBlank() || !communications.containsKey(idComm)) {
            throw new OperationImpossible("Communication introuvable.");
        }
        Communication comm = communications.get(idComm);

        // On vérifie que l'évaluateur est bien un évaluateur
        Utilisateur u = utilisateurs.get(idEvaluateur);
        if (u == null || !(u instanceof Evaluateur)) {
            throw new OperationImpossible("Évaluateur invalide.");
        }
        Evaluateur evaluateur = (Evaluateur) u;

        // On vérifie qu'il y'a un président dans le système
        if (!presidente.estAssigneA(evaluateur, comm)) {
            throw new OperationImpossible("L’évaluateur n’est pas affecté à cette communication.");
        }

        // On vérifie que l'évluation est dans un ÉtatCommunication permettant de l'évaluer
        if (!comm.getEtat().equals(ÉtatCommunication.En_Evaluation)) {
            throw new OperationImpossible("La communication n'est pas en cours d'évaluation.");
        }
        
        // On vérifie si on est dans les délais pour effectuer une évaluation
        LocalDate dateEvaluation = Datutil.aujourdhui();
        if (dateEvaluation.isBefore(dateLimiteSoumission) || !dateEvaluation.isBefore(dateAnnonceDecisions)) {
            throw new OperationImpossible("La date d’évaluation doit être entre la soumission et la date de décision.");
        }
        
        // On crée l'évaluation
        Evaluation evaluation = new Evaluation(evaluateur, avis, rapport, dateEvaluation);

        comm.ajouterEvaluation(evaluation);
        assert invariant();
    }
    
    /**
     * Permet à la présidente de prendre une décision (acceptée ou refusée) sur une communication.
     *
     * <p>Cette méthode vérifie que :</p>
     * <ul>
     *   <li>L'identifiant donné correspond bien à la présidente enregistrée.</li>
     *   <li>La communication existe dans le système.</li>
     *   <li>Son ÉtatCommunication est {@code Soumise} ou {@code Evaluée}.</li>
     *   <li>Aucune décision n'a encore été prise pour cette communication.</li>
     *   <li>La décision donnée est bien {@code Acceptée} ou {@code Refusée}.</li>
     * </ul>
     *
     * @param idPres    identifiant de la présidente prenant la décision.
     * @param idComm    identifiant de la communication concernée.
     * @param decision  décision prise ({@code Acceptée} ou {@code Refusée}).
     * @throws OperationImpossible si les préconditions ne sont pas respectées.
     */
    public void prendreDecision(final String idPres, final String idComm, final Décision decision) throws OperationImpossible {

    	 if (idPres == null || idPres.isBlank() || !idPres.equals(presidente.getIdentificateur())) {
    	 throw new OperationImpossible("Seule la présidente peut prendre la décision");
    	 }

    	 if (idComm == null || idComm.isBlank() || !communications.containsKey(idComm)) {
    	 throw new OperationImpossible("Communication introuvable.");
    	 }

    	 ÉtatCommunication etat = communications.get(idComm).getEtat();

    	 if (etat != ÉtatCommunication.Evaluée && etat != ÉtatCommunication.Soumise) {
    	 throw new OperationImpossible("Communication doit être évaluée ou soumise pour prendre la décision");
    	 }

    	 ÉtatCommunication etatActuel = communications.get(idComm).getEtat();

    	 if (etatActuel == ÉtatCommunication.Acceptée || etatActuel == ÉtatCommunication.Refusée) {
    	 throw new OperationImpossible("Décision déjà prise pour cette communication.");
    	 }
    	 
    	 if (decision != Décision.Acceptée && decision != Décision.Refusée) {
    	 throw new OperationImpossible("La décision doit être soit refusée soit acceptée.");
    	 }
    	 
    	 communications.get(idComm).decider(decision);
    	 
    }
    
    /**
     * Retourne la liste des identifiants des auteurs associés à une communication.
     *
     * @param idComm l'identifiant de la communication
     * @return une liste des identifiants des auteurs
     * @throws OperationImpossible si la communication est introuvable
     */
    
    public List<String> listerAuteurs(String idComm) throws OperationImpossible {
        Communication comm = communications.get(idComm);
        if (comm == null) {
            throw new OperationImpossible("Communication introuvable.");
        }
        return comm.getAuteurs()
                   .stream()
                   .map(Utilisateur::getIdentificateur)
                   .collect(Collectors.toList());
    }
    
    
    /**
     * Liste les évaluations associées à une communication (sous forme de texte).
     *
     * @param idComm l'identifiant de la communication
     * @return une liste de représentations textuelles des évaluations
     * @throws OperationImpossible si la communication est introuvable
     */
    public List<String> listerEvaluations(String idComm) throws OperationImpossible {
        Communication comm = communications.get(idComm);
        if (comm == null) {
            throw new OperationImpossible("Communication introuvable.");
        }

        return comm.getEvaluations().stream()
                   .map(Evaluation::toString) // ou toute autre représentation
                   .collect(Collectors.toList());
    }
    
    /**
     * Retourne la liste des identifiants des évaluatrices affectées à une communication donnée.
     *
     * @param comm la communication ciblée
     * @return la liste des identifiants des évaluatrices affectées à cette communication
     */
    public List<String> getEvaluatricesPourCommunication(Communication comm) {
        Set<Evaluateur> evaluateurs = presidente.getAffectations().getOrDefault(comm, Set.of());
        return evaluateurs.stream()
                          .map(Evaluateur::getIdentificateur)
                          .collect(Collectors.toList());
    }
    
    /**
     * Recherche une communication dans le système à partir de son titre.
     *
     * @param titre le titre de la communication à rechercher
     * @return un {@link Optional} contenant la communication si elle est trouvée, vide sinon
     */
    private Optional<Communication> chercherCommunicationParTitre(String titre) {
        return communications.values()
                             .stream()
                             .filter(c -> c.getTitre().equalsIgnoreCase(titre))
                             .findFirst();
    }
    
    /**
     * Récupère une communication à partir de son titre.
     *
     * @param titre le titre de la communication à rechercher
     * @return la communication trouvée
     * @throws OperationImpossible si aucune communication avec ce titre n'existe
     */
    public Communication getCommunicationParTitre(String titre) throws OperationImpossible {
        return chercherCommunicationParTitre(titre)
               .orElseThrow(() -> new OperationImpossible("Aucune communication avec ce titre."));
    }



}
