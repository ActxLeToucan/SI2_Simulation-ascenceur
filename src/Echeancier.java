import java.util.LinkedList;
/* Vous pouvez modifier cette classe comme vous voulez.
 */

public class Echeancier extends Global {

    private LinkedList<Evenement> listeEvenements;
    /* Comme toute les collections, il ne faut pas l'exporter.
     */

    public Echeancier() {
    	listeEvenements = new LinkedList<Evenement>();
    }

    public boolean estVide() {
    	return listeEvenements.isEmpty();
    }

    public void ajouter(Evenement e) {
		int pos = 0;
		while (pos < listeEvenements.size()) {
		    if (listeEvenements.get(pos).date >= e.date) {
		    	listeEvenements.add(pos, e);
		    	return;
		    } else {
		    	pos++;
		    }
		}
		listeEvenements.add(pos, e);
    }

    public Evenement retourneEtEnlevePremier() {
		Evenement e = listeEvenements.getFirst();
		listeEvenements.removeFirst();
		return e;
    }

    public void affiche(StringBuilder buffer, Immeuble ascenseur) {
		buffer.setLength(0);
		buffer.append("Echéancier = ");
		int index = 0;
		while (index < listeEvenements.size()) {
		    listeEvenements.get(index).affiche(buffer,ascenseur);
		    index++;
		    if (buffer.length() > 180) {
		    	index = listeEvenements.size();
		    	buffer.append(", ... (");
		    	buffer.append(index);
		    	buffer.append(")");
		    } else if (index < listeEvenements.size()) {
		    	buffer.append(',');
		    }
		}
		System.out.println(buffer);
    }

    public void decalerFPC(){
		int index = 0;
		while ( true ) {
		    Evenement e = listeEvenements.get(index);
		    if(e instanceof EvenementFermeturePorteCabine){
				listeEvenements.remove(index);
				ajouter(EvenementFermeturePorteCabine.setEvent(e.date + tempsPourOuvrirOuFermerLesPortes));
				return;
		    }
		    index++;
		}	
    }

    public void supprimeAPPs(){
		int index = 0;
		while ( index < listeEvenements.size() ) {
		    Evenement e = listeEvenements.get(index);
		    if(e instanceof EvenementArriveePassagerPalier){
		    	listeEvenements.remove(index);
		    } else {
		    	index++;
		    }
		}	
    }


	public void supprimerPAP(Passager passager, Cabine cabine){
		int index = 0;
		while ( index < listeEvenements.size() ) {
			Evenement e = listeEvenements.get(index);
			if(e instanceof EvenementPietonArrivePalier && cabine.transporte(passager) && passager.numeroDeCreation == ((EvenementPietonArrivePalier)e).getPassager().numeroDeCreation){
				listeEvenements.remove(e);
				return;
			}
			index++;
		}
	}

	public Evenement getNextEvent() {
		return listeEvenements.getFirst();
	}
}
