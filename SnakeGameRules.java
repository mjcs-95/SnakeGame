import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SnakeGameRules {
	private final int MAPSIZE = 50;
	private final Random RAND = new Random();
	private int[][] board;
	private int[][] snake;
	private int[] food;
	private int snakesize;
	private AtomicInteger direction;//0 up, 1 right, 2 down , 3 left
	
	public SnakeGameRules(){
		
		this.snake = new int[MAPSIZE*MAPSIZE][2];
		this.snake[0][0] = MAPSIZE/2;
		this.snake[0][1] = MAPSIZE/2;
		this.snakesize = 1;
		
		this.board = new int[MAPSIZE][MAPSIZE];
		this.board[ snake[0][0] ][ snake[0][1] ]=1;
		this.food = new int[2];
		this.setFood();

		this.direction = new AtomicInteger(0);
	}
	
	/**
	 * @return board of the game
	 */
	public int[][] getBoard() {
		return board;
	}

	/**
	 * @return snakeBody
	 */
	public int[][] getSnake() {
		return snake;
	}

	/**
	 * @return food's location
	 * 
	 */
	public int[] getFood() {
		return food;
	}

	public void setFood(){
		food[0] = RAND.nextInt(MAPSIZE);
		food[1] = RAND.nextInt(MAPSIZE);		
		while( board[ food[0] ][ food[1] ] != 0) {
			food[0] = RAND.nextInt(MAPSIZE);
			food[1] = RAND.nextInt(MAPSIZE);
		}
		board[ food[0] ][ food[1] ] = 2;
	}

	/**
	 * @param dir New direction of the snake
	 */
	public void setDirection(int dir){
		if(0<=dir && dir < 4){ 
			if( (this.direction.intValue()-dir+4)%4 != 2){ //Snake can't turn 180 degrees
				this.direction.set(dir); 				
			}
		}
	}
	
	public void snakemoves(){
		this.board[this.snake[0][0]][this.snake[0][1]]=0;
		for(int k=0; k<this.snakesize-1; ++k) {
			this.snake[k][0]=this.snake[k+1][0];
			this.snake[k][1]=this.snake[k+1][1];
		}
		if(direction.intValue()==0) {
			if(	this.snake[this.snakesize-1][0]-1== food[0] 
			&&	this.snake[this.snakesize-1][1]  == food[1]){
				this.snakeeats();
			}else {
				this.snake[this.snakesize-1][0]--;
			}
		}else if(direction.intValue()==1) {
			if(	this.snake[this.snakesize-1][0]  == food[0] 
			&&	this.snake[this.snakesize-1][1]+1== food[1]){
				this.snakeeats();
			}else {
				this.snake[this.snakesize-1][1]++;
			}			
		}else if(direction.intValue()==2) {
			if(	this.snake[this.snakesize-1][0]+1== food[0] 
			&&	this.snake[this.snakesize-1][1]  == food[1]){
				this.snakeeats();
			}else {
				this.snake[this.snakesize-1][0]++;
			}
		}else if(direction.intValue()==3) {
			if(	this.snake[this.snakesize-1][0]  == food[0] 
			&&	this.snake[this.snakesize-1][1]-1== food[1]){
				this.snakeeats();
			}else {
				this.snake[this.snakesize-1][1]--;
			}
		}
		int i= this.snake[this.snakesize-1][0];
		if(i>=0 && i<MAPSIZE) {
			int j= this.snake[this.snakesize-1][1];
			if(j>=0 && j<MAPSIZE) {
				this.board[i][j] = 1;							
			}
		}
	}
	
	public void snakeeats() {
		this.snake[this.snakesize][0]=food[0];
		this.snake[this.snakesize][1]=food[1];
		this.snakesize++;
		this.board[ food[0] ][ food[1] ] = 1;
		this.setFood();
	}
	
	public boolean defeat(){
		if(snake[0][0]<0 || snake[0][0]==MAPSIZE) { return true; }
		if(snake[0][1]<0 || snake[0][1]==MAPSIZE) { return true; }
		int head = this.snakesize-1;
		for(int i=0; i<this.snakesize-1; ++i) {
			if(snake[i][0]==snake[head][0] && snake[i][1]==snake[head][1]){
				return true;
			}
		}
		return false;
	}
}