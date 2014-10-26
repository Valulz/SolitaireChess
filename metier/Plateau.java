/**@author DELAUNAY Brice - DESPORTES Valentin*/

package metier;

import metier.piece.*;
import java.io.*;
import java.util.Scanner;

public class Plateau
{
	Piece grille[][];
	
	int   coordJoue[][];
	Piece piecePrise[];
	int   indice;
	
	String difficulte;
	int    numNiv, nbDpc;
	int nbPiece;
	
	/**Construit un plateau a partir du fichier texte passe en parametre*/
	public Plateau(String fichier )
	{			       
		this.grille = new Piece[4][4];
		this.initGrille(fichier);
		this.initNbPiece();
		
		this.indice = 0;
		this.coordJoue = new int[50][2];
		this.piecePrise = new Piece[50];
		
		
		this.nbDpc = 0;
	}
	
	/**Cette methode initialise le nombre de pieces posees sur le plateau*/
	private void initNbPiece()
	{
		this.nbPiece = 0;
		for(int i = 0; i<grille.length; i++)
			for(int j=0; j<grille[i].length; j++)
				if(this.grille[i][j] != null )
					this.nbPiece ++;
	}
	
	/**Cette methode permet a partir d'un fichier texte d'initialiser la grille de pieces*/
	private void initGrille(String f)
	{
		try
		{
			Scanner sc = new Scanner (new File(f) );
			Scanner scChamp;
			String ligne = "";
			
			ligne = sc.nextLine();
			
			scChamp = new Scanner(ligne);
			scChamp.useDelimiter(" ");
			
			while(scChamp.hasNext())
			{
				scChamp.next();
				this.numNiv = Integer.parseInt(scChamp.next());
				scChamp.next();
				this.difficulte = scChamp.next();
			}
			
			ligne = sc.nextLine();

			for(int i = 0; sc.hasNextLine(); i+=2)
			{
				ligne = sc.nextLine();
				for(int j=2; j<ligne.length(); j+=4)
				{
					if(ligne.charAt(j) == 'P')	this.grille[i/2][j/4] = new Pion();
					else if(ligne.charAt(j) == 'T')	this.grille[i/2][j/4] = new Tour();
					else if(ligne.charAt(j) == 'C')	this.grille[i/2][j/4] = new Cavalier();
					else if(ligne.charAt(j) == 'F')	this.grille[i/2][j/4] = new Fou();
					else if(ligne.charAt(j) == 'K')	this.grille[i/2][j/4] = new Roi();
					else if(ligne.charAt(j) == 'Q')	this.grille[i/2][j/4] = new Reine();
					else if(ligne.charAt(j) == ' ')	this.grille[i/2][j/4] = null;
				}
				
				ligne = sc.nextLine();
			}
			
			sc.close();
			scChamp.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**Cette methode permet de deplacer le pion de la case (x1, y1) vers la case (x2, y2)*/
	public boolean deplacer(int x1, int y1, int x2, int y2)
	{
		/*On verifie que les coordonnees sont bien dans la grille */
		if( (x1<0 && x1>=grille.length) && (y1<0 && y1>=grille[0].length) &&
		    (x2<0 && x2>=grille.length) && (y2<0 && y2>=grille[0].length) )
			return false;
		
		/*On verifie que les coordonnees de depart ne soient pas les memes que les coordonnees d'arrivees */
		if ( x1 == x2 && y1 == y2 ) return false;

		
		/*On verifie que les coordonnees de depart et d'arrivee ne soient pas vide */
		/*Et qu'elles ne soient pas hors du plateau*/
		try
		{		
			if( grille[x1][y1] == null || grille[x2][y2] == null)
				return false;
		}
		catch (ArrayIndexOutOfBoundsException _) { return false; }
		
		/*On verifie que le deplacement demandé est bien conforme au deplacement de la piece en question */
		if( grille[x1][y1].deplacer(x1,y1, x2,y2) == false  )
			return false;
		
		/*On verifie que la piece sur la case d'arrivee ne soit pas une piece Roi*/
		if( grille[x2][y2] instanceof Roi)	return false;
		
		/*On verifie qu'il n'y ait pas de pieces entre les coordonees de depart et d'arrivee, pour les pieces Tour, Fou et Reine*/
		if( grille[x1][y1] instanceof Tour)
		{			
			if( ! this.dpcTour(x1,y1,x2,y2) ) 	return false;
		}
		
		if( grille[x1][y1] instanceof Fou)
		{			
			if( ! this.dpcFou(x1,y1,x2,y2) ) 	return false;
		}
		
		if( grille[x1][y1] instanceof Reine)
		{	
			if(x1 == x2 || y1 == y2){
				if( ! this.dpcTour(x1,y1,x2,y2) ) 	return false;
			}
			else	if( ! this.dpcFou(x1,y1,x2,y2)  ) 	return false;
		}
		
		/*On enregistre les coups joues afin de pouvoir utiliser le CTRL Z */
		coupsJouer(x1,y1);
		coupsJouer(x2,y2);
		
		/*On effectue le deplacement*/
		grille[x2][y2] = grille[x1][y1];
		grille[x1][y1] = null;
		
		this.nbPiece --;
		this.nbDpc ++;
		
		return true;
	}
	
	/**Cette methode teste si la grille est finie ou non en fonction du nombre de pieces restantes*/
	public boolean finDePartie(){return this.nbPiece == 1;}
	
	/**Cette methode teste que, pour le deplacement d'une Tour, il n'y ait pas de pieces ente la case de depart et d'arrivee*/
	private boolean dpcTour(int x1, int y1, int x2, int y2)
	{
		int deltaX = Math.abs(x2-x1),	deltaY = Math.abs(y2-y1);
		
		if(deltaX == 0 && deltaY > 1 )
			for(int j = ( (y2-y1) > 0 ? y1 : y2)+1 ; j<((y2-y1) >0 ? y2 : y1); j++)
				if( grille[x1][j] != null)	return false;
						
		if(deltaY == 0 && deltaX > 1 )
			for(int i = ( (x2-x1) >0 ? x1 : x2)+1 ; i < ((x2-x1) >0 ? x2 : x1); i++)
				if( grille[i][y1] != null)	return false;
		
		return true;
	}
	
	/**Cette methode teste que pour le deplacement d'un Fou, il n'y ait pas de pieces ente la case de depart et d'arrivee*/
	private boolean dpcFou(int x1, int y1, int x2, int y2)
	{
		if(x1 == y1 && x2 == y2)
		{
			
			for(int i= ( (x2-x1) >0 ? x1 : x2) +1 , j = ( (y2-y1) > 0 ? y1 : y2)+1; 
				i < ((x2-x1) >0 ? x2 : x1)    && j<((y2-y1) >0 ? y2 : y1); i++, j++)
			{
				if(grille[i][j] != null)	return false;
			}
		}
		
		else
		{
			int iDeb = x1, jDeb = y1; 
			int iFin = x2, jFin = y2; 
			
			iDeb = (iDeb<iFin) ? iDeb+1 : iDeb-1;
			jDeb = (jDeb<jFin) ? jDeb+1 : jDeb-1;
			
			while( !(iDeb == iFin && jDeb == jFin))
			{
				if(grille[iDeb][jDeb] != null)	return false;
				
				iDeb = (iDeb<iFin) ? iDeb+1 : iDeb-1;
				jDeb = (jDeb<jFin) ? jDeb+1 : jDeb-1;
			} 

		}
		return true;
	}
	
	/**Cette methode enregistre les coordonne (x,y) de la piece deplacer ainsi que la dite piece*/
	private void coupsJouer( int x, int y)
	{
		if(indice < coordJoue.length)
		{
			coordJoue[indice][0] = x;
			coordJoue[indice][1] = y;
			piecePrise[indice]   = grille[x][y];
			
			indice ++;
		}
	}
	
	/**Cette methode permet d'annuler les coups joues precedements*/
	public boolean annuler()
	{
		if(indice <= 0)	return false;
		
		indice --;
		grille[ coordJoue[indice][0] ][ coordJoue[indice][1] ] = piecePrise[indice];
		
		indice --;
		grille[ coordJoue[indice][0] ][ coordJoue[indice][1] ] = piecePrise[indice];
		
		this.nbDpc --;
		this.nbPiece++;
		
		return true;
	}
	
	/**Cette methode renvoie le symbole de la case[x][y]*/
	public String getSymbole(int x, int y)
	{
		if(grille[x][y] == null)	return "vide52";
		else				return grille[x][y].getType();
	}
	
	/*----------------Accesseurs----------------------*/
	/**Cette methode renvoie le nombre de ligne*/
	public int getNbLigne()       {  return this.grille.length;     }
	
	/**Cette methode renvoie le nombre de colonne*/
	public int getNbColonne()     {  return this.grille[0].length;  }
	
	/**Cette methode renvoie le numero du niveau en cours*/
	public int getNumNiv()        {  return this.numNiv;            }
	
	/**Cette methode renvoie la difficulte du niveau en cours*/
	public String getDifficulte() {  return this.difficulte;        }
	
	/**Cette methode renvoie le nombre de deplacement effectue*/
	public int getNbDpc()         {  return this.nbDpc;             }
	
	/**Cette methode renvoie le nombre de pieces restantes sur le jeu*/
	public int getNbPiece()      {  return this.nbPiece;            }
}