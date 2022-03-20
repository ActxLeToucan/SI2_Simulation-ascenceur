public class EvenementArriveePassagerPalier extends Evenement {
    /* APP : Arrivée Passager Palier
       L'instant précis ou un nouveau passager arrive sur un palier donné, dans le but
       de monter dans la cabine.
    */
    
    private Etage etage;

    public EvenementArriveePassagerPalier(long d, Etage edd) {
    	super(d);
		etage = edd;
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
		buffer.append("APP ");
		buffer.append(etage.numero());
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		assert etage != null;
		assert immeuble.etage(etage.numero()) == etage;
		Passager p = new Passager(date, etage, immeuble);
		Cabine c = immeuble.cabine;

		if (c.porteOuverte && c.etage == etage) {
		    if (c.intention() == '-') {
				c.changerIntention(p.sens());
				echeancier.ajouter(EvenementFermeturePorteCabine.setEvent(date + tempsPourOuvrirOuFermerLesPortes));
				char fmp = c.faireMonterPassager(p);
				// Il faudrait aussi ajouter le premier PCP...
				if (fmp == 'O') {
					assert true;
				} else {
					assert false : "else impossible";
				}
		    } else {
				if (c.faireMonterPassager(p) != 'O') {
					echeancier.ajouter(new EvenementPietonArrivePalier(date + delaiDePatienceAvantSportif, p.etageDepart(), p));
					immeuble.etage(etage.numero()).ajouter(p);
				} else {
					echeancier.decalerFPC();
				}
		    }
		} else {
			if (c.porteOuverte && !c.contientDesPassagers() && !(echeancier.getNextEvent() instanceof EvenementFermeturePorteCabine)) {
				echeancier.ajouter(EvenementFermeturePorteCabine.setEvent(date + tempsPourOuvrirOuFermerLesPortes));
			}
			echeancier.ajouter(new EvenementPietonArrivePalier(date + delaiDePatienceAvantSportif, p.etageDepart(), p));
			immeuble.etage(etage.numero()).ajouter(p);
			if (!c.contientDesPassagers()) c.changerIntention(c.calculerIntention(immeuble));
		}

		date += etage.arriveeSuivante();
		echeancier.ajouter(this);
	
		assert c.intention() != '-' : "intention impossible";
    }
}
