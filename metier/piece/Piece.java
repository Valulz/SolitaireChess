/**@author DELAUNAY Brice - DESPORTES Valentin*/

package metier.piece;
public abstract class Piece
{
	/**Cette methode permet de verifier si les coordonnees passees en parametres correspondent bien au deplacement de la piece*/
	public abstract boolean deplacer(int x1, int y1, int x2, int y2);
	
	/**Cette methode renvoie le type de la piece*/
	public abstract String getType();
}