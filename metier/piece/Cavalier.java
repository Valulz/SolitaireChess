/**@author DELAUNAY Brice - DESPORTES Valentin*/

package metier.piece;

public class Cavalier extends Piece
{
	public boolean deplacer(int x1, int y1, int x2, int y2)
	{
		int deltaX = Math.abs(x1-x2);
		int deltaY = Math.abs(y1-y2);
		
		return ( (deltaX==2 && deltaY == 1) || (deltaX==1 && deltaY == 2) ); 
	}
	
	public String getType() { return "cavalier"; }
}