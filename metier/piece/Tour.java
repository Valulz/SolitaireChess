/**@author DELAUNAY Brice - DESPORTES Valentin*/

package metier.piece;
public class Tour extends Piece
{
	public boolean deplacer(int x1, int y1, int x2, int y2){ return( (x1==x2) ^ (y1 == y2) ); } //Le ^ correspond au OU Exclusif
	public String getType()                                { return "tour";                   }
}