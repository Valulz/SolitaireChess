package metier.piece;
public class Pion extends Piece
{
	public boolean deplacer(int x1, int y1, int x2, int y2) { return( (x2-x1) == -1 && ( Math.abs(y2-y1) == 1 )); }
	public String getType()                                 { return "pion";                                      }
}