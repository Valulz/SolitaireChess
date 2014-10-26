/**@author DELAUNAY Brice - DESPORTES Valentin*/

package metier.piece;
public class Fou extends Piece
{
	public boolean deplacer(int x1, int y1, int x2, int y2) { return( Math.abs(x1-x2) == Math.abs(y1-y2) );}
	public String getType()                                 { return "fou";                                }
}