public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */

	/**
	 * singleton
	 */
	private static EvenementOuverturePorteCabine event;

	public static EvenementOuverturePorteCabine setEvent(long d) {
		if (event == null) {
			event = new EvenementOuverturePorteCabine(d);
		} else {
			event.recycle(d);
		}
		return event;
	}

	private void recycle(long d) {
		this.date = d;
	}




	private EvenementOuverturePorteCabine(long d) {
    	super(d);
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
    	buffer.append("OPC");
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
    	Cabine cabine = immeuble.cabine;
    	Etage etage = cabine.etage;
    	assert ! cabine.porteOuverte;
	
    	cabine.porteOuverte = true;
		int passagersDescendus = cabine.faireDescendrePassagers(immeuble, date);

		cabine.changerIntention(cabine.calculerIntention(immeuble));

		int i = 0;
		int passagersQuiSontMontes = 0;
		while (!etage.getPassagers().isEmpty() && i<etage.getPassagers().size()) {
			Passager p = etage.getPassagers().get(i);
			if (cabine.faireMonterPassager(p) == 'O') {
				passagersQuiSontMontes++;
				echeancier.supprimerPAP(p, cabine);
			} else {
				i++;
			}
		}

		if (cabine.contientDesPassagers() || etage.getImmeuble().passagerEnDessous(etage) || etage.getImmeuble().passagerAuDessus(etage)) {
			echeancier.ajouter(EvenementFermeturePorteCabine.setEvent(date+tempsPourOuvrirOuFermerLesPortes + (passagersDescendus + passagersQuiSontMontes)*tempsPourEntrerOuSortirDeLaCabine));
		}
	
    	assert cabine.porteOuverte;
    }

}
