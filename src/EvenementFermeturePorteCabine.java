public class EvenementFermeturePorteCabine extends Evenement {
    /* FPC : Fermeture Porte Cabine
       L'instant précis ou la porte termine de se fermer.
       Tant que la porte n'est pas complètement fermée, il est possible pour un passager
       de provoquer la réouverture de la porte. Dans ce cas le FPC est décalé dans le temps
       selon la méthode decaler FPC qui est dans l'échéancier.
    */

    public EvenementFermeturePorteCabine(long d) {
    	super(d);
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
    	buffer.append("FPC");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		Cabine cabine = immeuble.cabine;
		assert cabine.porteOuverte : "precondition";
	
		cabine.porteOuverte = false;
		//notYetImplemented();

		assert (! cabine.porteOuverte) : "postcondition";

        echeancier.ajouter(EvenementPassageCabinePalier.setEvent(date + tempsPourBougerLaCabineDUnEtage, immeuble.etage(cabine.intention() == '^' ? cabine.etage.numero()+1 : cabine.etage.numero()-1)));
    }


    public void setDate(long d){
    	this.date = d;
    }

}
