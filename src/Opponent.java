import java.awt.Point;

public class Opponent // WHITE
{
    Surface surface;
    
    // creates a pointBonus array to show best places to go
    int[][] pointBonus = new int[][]{ 
        { 99,  -8,  8,  6,  6,  8,  -8, 99 },
        { -8, -24, -4, -3, -3, -4, -24, -8 },
        {  8,  -4,  7,  4,  4,  7,  -4,  8 },
        {  6,  -3,  4,  0,  0,  4,  -3,  6 },
        {  6,  -3,  4,  0,  0,  4,  -3,  6 },
        {  8,  -4,  7,  4,  4,  7,  -4,  8 },
        { -8, -24, -4, -3, -3, -4, -24, -8 },
        { 99,  -8,  8,  6,  6,  8,  -8, 99 }
    }; 
    
    public Opponent(Surface s)
    {
        surface = s;
    }
    
    /**
     * returns the pointBonus at the point on the 2D array
     * @param x x position
     * @param y y position
     */
    public int getPointBonus( int x, int y)
    {
        return pointBonus[x][y];
    }       

    public Point bestPosition()
    {
    	Point ptBest = null;;
        for( int x = 0; x < 8; x++)
        {
            for( int y = 0; y < 8; y++)
            {
                if( surface.board[x][y] == Surface.EMPTY 
                 && surface.makesSandwich(x, y, Surface.WHITE))
                {
                    if(ptBest == null || 
                    		(getPointBonus(x, y)+surface.flip(x, y, true) >= getPointBonus(ptBest.x, ptBest.y)))
                    {
                    	if( ptBest == null)
                    	{
                    		ptBest = new Point();		
                    	}
                    	//System.out.println( "new bonus: " + getPointBonus(ptBest.x, ptBest.y));
                    	ptBest.x = x; 
                    	ptBest.y = y;
                    }                   
                }
            }
        }
        return ptBest;	
    }  
}
