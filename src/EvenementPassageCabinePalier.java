public class EvenementPassageCabinePalier extends Evenement {
    /* PCP: Passage Cabine Palier
       L'instant précis où la cabine passe juste en face d'un étage précis.
       Vous pouvez modifier cette classe comme vous voulez (ajouter/modifier des méthodes etc.).
    */
    
    private Etage etage;
    
    public EvenementPassageCabinePalier(long d, Etage e) {
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
				boolean jeDoisOuvrir = false;
				for (Passager pass : etage.getPassagers()) {
					if (pass.sens() == c.intention()) {
						jeDoisOuvrir = true;
						break;
					}
				}
				if (jeDoisOuvrir) {
					echeancier.ajouter(new EvenementOuverturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
				} else {
					echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage, immeuble.etage(c.intention() == '^' ? c.etage.numero()+1 : c.etage.numero()-1)));
				}
			} else {
				echeancier.ajouter(new EvenementOuverturePorteCabine(date + tempsPourOuvrirOuFermerLesPortes));
			}
		} else {
			echeancier.ajouter(new EvenementPassageCabinePalier(date + tempsPourBougerLaCabineDUnEtage, immeuble.etage(c.intention() == '^' ? c.etage.numero()+1 : c.etage.numero()-1)));
		}
	}
}
