public class EvenementPassageCabinePalier extends Evenement {
    /* PCP: Passage Cabine Palier
       L'instant précis où la cabine passe juste en face d'un étage précis.
       Vous pouvez modifier cette classe comme vous voulez (ajouter/modifier des méthodes etc.).
    */

	/**
	 * singleton
	 */
	private static EvenementPassageCabinePalier event;

	public static EvenementPassageCabinePalier setEvent(long d, Etage e) {
		if (event == null) {
			event = new EvenementPassageCabinePalier(d, e);
		} else {
			event.recycle(d, e);
		}
		return event;
	}

	private void recycle(long d, Etage e) {
		this.date = d;
		this.etage = e;
	}


    
    private Etage etage;
    
    private EvenementPassageCabinePalier(long d, Etage e) {
    	super(d);
    	etage = e;
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
    	buffer.append("PCP ");
    	buffer.append(etage.numero());
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		Cabine c = immeuble.cabine;
		assert !c.porteOuverte;
		assert etage.numero() != c.etage.numero();

		c.etage = immeuble.etage(etage.numero());
		if (c.passagersVeulentDescendre()) {
			echeancier.ajouter(new EvenementOuverturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
		} else if (etage.aDesPassagers()) {
			if (c.contientDesPassagers() && modeParfait) {
				modeParfait_ouvertureOuMouvementCabine(immeuble, echeancier, c);
			} else {
				if (modeParfait && (immeuble.passagerEnDessous(etage) || immeuble.passagerAuDessus(etage))) {
					modeParfait_ouvertureOuMouvementCabine(immeuble, echeancier, c);
				} else {
					echeancier.ajouter(new EvenementOuverturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
				}
			}
		} else {
			echeancier.ajouter(EvenementPassageCabinePalier.setEvent(date + tempsPourBougerLaCabineDUnEtage, immeuble.etage(c.intention() == '^' ? c.etage.numero()+1 : c.etage.numero()-1)));
		}
	}

	private void modeParfait_ouvertureOuMouvementCabine(Immeuble immeuble, Echeancier echeancier, Cabine c) {
		boolean jeDoisOuvrir = false;
		for (Passager pass : etage.getPassagers()) {
			if (pass.sens() == c.intention()) {
				jeDoisOuvrir = true;
				break;
			}
		}
		if (!jeDoisOuvrir && etage != immeuble.etageLePlusBas() && etage != immeuble.etageLePlusHaut()) {
			echeancier.ajouter(EvenementPassageCabinePalier.setEvent(date + tempsPourBougerLaCabineDUnEtage, immeuble.etage(c.intention() == '^' ? c.etage.numero()+1 : c.etage.numero()-1)));
		} else {
			echeancier.ajouter(new EvenementOuverturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
		}
	}
}
