public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */
    
    public EvenementOuverturePorteCabine(long d) {
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
		cabine.faireDescendrePassagers(immeuble, date);

		cabine.changerIntention('-');

		for (int i = 0; i<etage.getPassagers().size(); i++) {
			cabine.faireMonterPassager(etage.getPassagers().get(i));
		}
		etage.getPassagers().clear();

		// TODO fermer les portes :)
		//cabine.changerIntention(cabine.calculerIntention());
	
    	assert cabine.porteOuverte;
    }

}
