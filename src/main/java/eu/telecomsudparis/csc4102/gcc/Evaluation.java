package eu.telecomsudparis.csc4102.gcc;

import java.time.LocalDate;
import java.util.Objects;

import eu.telecomsudparis.csc4102.util.OperationImpossible;

public class Evaluation {

    private final Evaluateur evaluateur;
    private final Avis avis;
    private final String rapport;
    private final LocalDate dateEvaluation;

    public Evaluation(Evaluateur evaluateur, Avis avis, String rapport, LocalDate dateEvaluation) throws OperationImpossible {
        if (evaluateur == null) {
            throw new OperationImpossible("L’évaluateur ne peut pas être null.");
        }
        
        if (rapport == null || rapport.isBlank()) {
            throw new OperationImpossible("Le rapport ne peut pas être vide.");
        }
        if (dateEvaluation == null) {
            throw new OperationImpossible("La date de l’évaluation est requise.");
        }

        this.evaluateur = evaluateur;
        this.avis = avis;
        this.rapport = rapport;
        this.dateEvaluation = dateEvaluation;

        assert invariant();
    }

    public Evaluateur getEvaluateur() {
        return evaluateur;
    }

    public Avis getAvis() {
        return avis;
    }

    public String getRapport() {
        return rapport;
    }

    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }

    public boolean invariant() {
        return evaluateur != null && avis != null && rapport != null && !rapport.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Evaluation)) return false;
        Evaluation that = (Evaluation) o;
        return evaluateur.equals(that.evaluateur); // Un évaluateur peut faire une seule évaluation par comm
    }

    @Override
    public int hashCode() {
        return Objects.hash(evaluateur);
    }

    @Override
    public String toString() {
        return "Évaluation{" +
                "evaluateur=" + evaluateur.getIdentificateur() +
                ", avis='" + avis + '\'' +
                ", rapport='" + rapport + '\'' +
                ", date=" + dateEvaluation +
                '}';
    }
}
