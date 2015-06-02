import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Point;

class Surface extends JPanel 
{
    int highScore = 0;
    int playerPieces = 2;
    int opponentPieces = 2;
    int[][] board = new int[8][8];        
    int whoseTurn = BLACK;
    int endFlipX = 0;
    int endFlipY = 0;
    Point opponentPoint;

    public static final int TOP_MARGIN = 140;
    public static final int LEFT_MARGIN = 100;

    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    Opponent opponent = new Opponent(this); 
    
    /**
     * get surface ready for game
     */
    public Surface() 
    {
        initSurface();
    } 

    /**
     * sets the positions of the initial pieces
     */
    private void initSurface() 
    {
        this.addMouseListener(new HitTestAdapter());       

        //set initial board start
        board[3][3] = BLACK; 
        board[4][4] = BLACK; 
        board[3][4] = WHITE; 
        board[4][3] = WHITE;      
    } 

    /**
     * draws the grid
     * @param g used to create the graphics
     */
    public void drawGrid(Graphics g) {
        // draw the rows
        int rowHt = 60;
        for (int i = 0; i <= 8; i++)
        {
            g.drawLine(LEFT_MARGIN, TOP_MARGIN + (i * rowHt), LEFT_MARGIN+480, TOP_MARGIN + (i * rowHt));
        }
        // draw the columns
        int rowWid = 60;
        for (int j = 0; j <= 8; j++)
        {
            g.drawLine(LEFT_MARGIN + (j * rowWid), TOP_MARGIN, LEFT_MARGIN + (j * rowWid), TOP_MARGIN+480);
        }
    }

    /**
     * sets screen and draws playing pieces
     * @param g used to create the graphics
     */
    private void doDrawing(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        // draw grid
        drawGrid(g2d);

        setBackground(new Color(20, 168, 23));

        for( int row = 0; row < board.length; row++)
        {
            for( int col = 0; col < board[0].length; col++)
            {
                if( board[row][col] == BLACK)
                {   
                    Ellipse2D.Double blackCircle = new Ellipse2D.Double( (double)(LEFT_MARGIN+60*row)+7, (double)(TOP_MARGIN+60*col)+7, 50, 50);
                    g2d.setColor(Color.BLACK);
                    g2d.fill(blackCircle);
                }
                else if( board[row][col] == WHITE)
                {
                    Ellipse2D.Double whiteCircle = new Ellipse2D.Double( (double)(LEFT_MARGIN+60*row)+7, (double)(TOP_MARGIN+60*col)+7, 50, 50);
                    g2d.setColor(Color.WHITE);
                    g2d.fill(whiteCircle);
                }
            }
        }     

        g2d.setColor(Color.BLACK);
        g2d.setFont( new Font("Helvetica", Font.PLAIN, 60));
        g2d.drawString( "OTHELLO", 200, 90);
        g2d.setColor(Color.WHITE);
        g2d.setFont( new Font("Helvetica", Font.BOLD, 16));
        g2d.drawString( "Black: " + playerPieces, 100, 70);
        g2d.drawString( "White: " + opponentPieces, 520, 70);
        
        if( whoseTurn == BLACK)
        {
            g2d.setColor(Color.BLACK);
            g2d.setFont( new Font("Helvetica", Font.BOLD, 30));
            g2d.drawString( "YOUR TURN", 100, 130);
        }
        else
        {
            g2d.setColor(Color.WHITE);
            g2d.setFont( new Font("Helvetica", Font.BOLD, 30));
            g2d.drawString( "COMPUTER'S TURN", 290, 130);
        }
        
        if( !isPlayingGame())
        {
            g2d.setColor(Color.WHITE);
            g2d.setFont( new Font("Helvetica", Font.BOLD, 70));
            g2d.fillRect(120, 230, 460, 180);
            g2d.setColor(Color.BLUE);
            g2d.drawString( "GAME OVER", 135, 300);
            if( opponentPieces < playerPieces)
            {
            	g2d.drawString( "YOU WIN", 135, 380);
            	
            }
            else if( opponentPieces > playerPieces)
            {
            	g2d.drawString( "YOU LOSE", 135, 380);            	
            }
            else
            {
            	g2d.drawString( "TIE", 270, 380);   
            }
        }
    } // sets graphics for game

    /**
     * paints on game field
     * @param g used for Graphics
     */
    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        doDrawing(g);
    }

    public int getOppositeColor( int whoseTurn)
    {
        if( whoseTurn == BLACK)
        {
            return WHITE;
        }
        else // if( whoseTurn == WHITE)
        {
            return BLACK;
        }
    }

    /**
     * checks sandwich conditions
     * @param x x position
     * @param y y position
     */
    public boolean makesSandwich( int x, int y, int whoseTurn)
    {      
        /*
         **FORMAT**
        if() // next to opposite color
            for() // starts checking pieces in that direction
                if() // if a same color piece is found
                    // return true;
         */

        if( y-1 > 0 && board[x][y-1] == getOppositeColor(whoseTurn)) // n
        {
            for( int i = y; i >= 0; i--) // n  
            {
                if( board[x][i] == whoseTurn)
                {
                    return true;
                }
            }     
        }        
        if( y+1 < 7 && board[x][y+1] == getOppositeColor(whoseTurn)) // s
        {
            for( int i = y; i <= 7; i++) // s
            {
                if( board[x][i] == whoseTurn)
                {
                    return true;
                }
            }     
        }
        if( x+1 < 7  && board[x+1][y] == getOppositeColor(whoseTurn)) // e
        {
            for( int i = x; i <= 7; i++) // e  
            {
                if( board[i][y] == whoseTurn)
                {
                    return true;
                }
            }    
        }
        if( x-1 > 0 && board[x-1][y] == getOppositeColor(whoseTurn)) // w
        {
            for( int i = x; i >= 0; i--) // w  
            {
                if( board[i][y] == whoseTurn)
                {
                    return true;
                }
            }   
        }
        if( (x-1 > 0 && y-1 > 0) && (board[x-1][y-1] == getOppositeColor(whoseTurn))) // nw
        {
            for( int i = 1; x-i >= 0 && y-i >= 0; i++) // nw  
            {
                if( board[x-i][y-i] == whoseTurn)
                {
                    return true;
                }
            }     
        }
        if( (x+1 < 7 && y-1 > 0) && (board[x+1][y-1] == getOppositeColor(whoseTurn))) // ne
        {
            for( int i = 1; x+i <= 7 && y-i >= 0; i++) // ne  
            {
                if( board[x+i][y-i] == whoseTurn)
                {
                    return true;
                }
            }     
        }
        if( (x-1 > 0 && y+1 < 7) && (board[x-1][y+1] == getOppositeColor(whoseTurn))) // sw
        {
            for( int i = 1; x-i >= 0 && y+i <= 7; i++) // sw 
            {
                if( board[x-i][y+i] == whoseTurn)
                {
                    return true;
                }
            }    
        }
        if( (x+1 < 7 && y+1 < 7) && (board[x+1][y+1] == getOppositeColor(whoseTurn))) // se
        {
            for( int i = 1; x+i <= 7 && y+i <= 7; i++) // se  
            {
                if( board[x+i][y+i] == whoseTurn)
                {
                    return true;
                }
            }     
        }
        return false;
    }

    /**
     * flips
     * @param x x position
     * @param y y position
     */
    public int flip( int x, int y, boolean countOnly)
    {      
    	int count = 0;
        /*
         **FORMAT**
        if() // next to opposite color
            for() // starts checking pieces in that direction
                if() // if a same color piece is found
                    // flips the pieces
                    // return true;
         */

        if( y-1 > 0 && board[x][y-1] == getOppositeColor(whoseTurn)) // n
        {
            for( int i = y; i >= 0; i--) // n  
            {
                if( board[x][i] == whoseTurn)
                {
                    endFlipX = x;
                    endFlipY = i;
                    for( int j = y-1; j >= endFlipY; j--)
                    {
                        if( !countOnly)
                        {
                        	board[x][j] = whoseTurn;
                        }
                        count++;
                    }
                    break;
                }
            }     
        }        
        if( y+1 < 7 && board[x][y+1] == getOppositeColor(whoseTurn)) // s
        {
            for( int i = y; i <= 7; i++) // s
            {
                if( board[x][i] == whoseTurn)
                {
                    endFlipX = x;
                    endFlipY = i;
                    for( int j = y+1; j <= endFlipY; j++)
                    {
                        if( !countOnly)
                        {
                        	board[x][j] = whoseTurn;
                        }
                        count++;
                    }
                    break;
                }
            }     
        }
        if( x+1 < 7  && board[x+1][y] == getOppositeColor(whoseTurn)) // e
        {
            for( int i = x; i <= 7; i++) // e  
            {
                if( board[i][y] == whoseTurn)
                {
                    endFlipX = i;
                    endFlipY = y;
                    for( int j = x+1; j <= endFlipX; j++)
                    {
                    	if( !countOnly)
                        {
                        	board[j][y] = whoseTurn;
                        }
                        count++;                       
                    }
                    break;
                }
            }    
        }
        if( x-1 > 0 && board[x-1][y] == getOppositeColor(whoseTurn)) // w
        {
            for( int i = x; i >= 0; i--) // w  
            {
                if( board[i][y] == whoseTurn)
                {
                    endFlipX = i;
                    endFlipY = y;
                    for( int j = x-1; j >= endFlipX; j--)
                    {
                    	if( !countOnly)
                        {
                        	board[j][y] = whoseTurn;
                        }
                        count++;                   	
                    }
                    break;
                }
            }   
        }
        if( (x-1 > 0 && y-1 > 0) && (board[x-1][y-1] == getOppositeColor(whoseTurn))) // nw
        {
            for( int i = 1; x-i >= 0 && y-i >= 0; i++) // nw  
            {
                if( board[x-i][y-i] == whoseTurn)
                {
                    endFlipX = x-i;
                    endFlipY = y-i;
                    for( int j = 1; x-j >= endFlipX && y-j >= endFlipY; j++)
                    {
                    	if( !countOnly)
                        {
                        	board[x-j][y-j] = whoseTurn;
                        }
                        count++;                        
                    }
                    break;
                }
            }     
        }
        if( (x+1 < 7 && y-1 > 0) && (board[x+1][y-1] == getOppositeColor(whoseTurn))) // ne
        {
            for( int i = 1; x+i <= 7 && y-i >= 0; i++) // ne  
            {
                if( board[x+i][y-i] == whoseTurn)
                {
                    endFlipX = x+i;
                    endFlipY = y-i;
                    for( int j = 1; x+j <= endFlipX && y-j >= endFlipY; j++)
                    {
                    	if( !countOnly)
                        {
                        	board[x+j][y-j] = whoseTurn;
                        }
                        count++;                       
                    }
                    break;
                }
            }     
        }
        if( (x-1 > 0 && y+1 < 7) && (board[x-1][y+1] == getOppositeColor(whoseTurn))) // sw
        {
            for( int i = 1; x-i >= 0 && y+i <= 7; i++) // sw 
            {
                if( board[x-i][y+i] == whoseTurn)
                {
                    endFlipX = x-i;
                    endFlipY = y+i;
                    for( int j = 1; x-j >= endFlipX && y+j <= endFlipY; j++)
                    {
                    	if( !countOnly)
                        {
                        	board[x-j][y+j] = whoseTurn;
                        }
                        count++;                        
                    }
                    break;
                }
            }    
        }
        if( (x+1 < 7 && y+1 < 7) && (board[x+1][y+1] == getOppositeColor(whoseTurn))) // se
        {
            for( int i = 1; x+i <= 7 && y+i <= 7; i++) // se  
            {
                if( board[x+i][y+i] == whoseTurn)
                {
                    endFlipX = x+i;
                    endFlipY = y+i;
                    for( int j = 1; x+j <= endFlipX && y+j <= endFlipY; j++)
                    {
                    	if( !countOnly)
                        {
                        	board[x+j][y+j] = whoseTurn;
                        }
                        count++;
                    }
                    break;
                }
            }     
        }
        return count;
    }

    public void calculatePoints()
    {
        playerPieces = 0;
        opponentPieces = 0;
        for( int row = 0; row < 8; row++)
        {
            for( int col = 0; col < 8; col++)
            {
                if( board[row][col] == BLACK)
                {
                    playerPieces++;
                }
                else if( board[row][col] == WHITE)
                {
                    opponentPieces++;
                }
            }
        }
    }

    public boolean blackCanMove()
    {
        for( int x = 0; x < 8; x++)
        {
            for( int y = 0; y < 8; y++)
            {
                if( board[x][y] == EMPTY && makesSandwich(x, y, BLACK))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isPlayingGame()
    {
    	if( playerPieces + opponentPieces == 64 
    			|| (!blackCanMove() && opponentPoint == null))
    	{
    		return false;
    	}
    	return true;
    }
    
    /**
     * used to detect mouse clicks
     */
    class HitTestAdapter extends MouseAdapter
    implements Runnable 
    {
    	private Thread pause;   	
        /**
         * when mouse is pressed do these actions
         * @param e used to find out where the mouse clicked
         */
        @Override
        public void mousePressed(MouseEvent e) 
        {  
            if( isPlayingGame())
            {
                int x = e.getX();
                int y = e.getY(); 
                
                if( whoseTurn == BLACK )
                {
                	if (      
	                 // check to make sure click is on the board
	                  x > LEFT_MARGIN && x < LEFT_MARGIN+480
	                 && y > TOP_MARGIN && y < TOP_MARGIN+480
	                    // check if spot is empty
	                 && board[(x-LEFT_MARGIN)/60][(y-TOP_MARGIN)/60] == EMPTY
	                    // check if it makes sandwich
	                 && makesSandwich((x-LEFT_MARGIN)/60, (y-TOP_MARGIN)/60, BLACK)
	                )
	                {
	                    flip((x-LEFT_MARGIN)/60, (y-TOP_MARGIN)/60, false); 
	                    board[(x-LEFT_MARGIN)/60][(y-TOP_MARGIN)/60] = whoseTurn;   
	                    
	                    // tallies points and alternates between turns
	                    calculatePoints();
	                    repaint();
	                    
	                    whoseTurn = WHITE;
	                    pause = new Thread(this);
	                	pause.start();
	                }   
                }
            }     
        } 

        @Override
        public void run() 
        {
        	isPlayingGame();
            repaint(); 
            do
            {
            	opponentPoint = opponent.bestPosition();
            	
            	// if white can't go, skip
                if( opponentPoint == null)
                {
                    whoseTurn = BLACK;
                    return;
                }
           	
                try
                {
                    Thread.sleep(1000);
                }
                catch( InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                
            	flip(opponentPoint.x, opponentPoint.y, false); 
            	board[opponentPoint.x][opponentPoint.y] = WHITE;   

                repaint();  
                
                // tallies points and alternates between turns
                calculatePoints();
                
            } while (!blackCanMove() && isPlayingGame());           
            
            whoseTurn = BLACK;
            
        }
    }
}
