    import java.awt.Canvas;
    import java.awt.Color;
    import java.awt.Dimension;
    import java.awt.Graphics;
    import java.awt.event.KeyEvent;
    import java.awt.image.BufferStrategy;
    import java.awt.image.BufferedImage;
    import java.util.ArrayList;
    import java.util.List;
    import javax.swing.JFrame;
    import java.awt.event.KeyListener;

    
    import Entity.Entity;
    import Entity.Player;
    import Graficos.Spritesheet;
    
    
    public class Game extends Canvas implements Runnable,KeyListener{
        
                    private static final long serialVersionUID = 1L;
                    public static JFrame frame;
                    private Thread thread;
                    private boolean isRunning = true;
                    private final int WIDTH = 240;
                    private final int HEIGHT = 160;
                    private final int SCALE = 4;
                
                    private BufferedImage image;
                    public List<Entity> entities;
                    public Spritesheet spritesheet;
                    private Player player;


    public Game() {
                    addKeyListener(this);
                    setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
                    initFrame();
                    //inicializando Objetos
                    image = new BufferedImage( WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
                    entities = new ArrayList<Entity>();
                    spritesheet = new Spritesheet("/res/spritesheet.png");
                    player = new Player(5,5,50,37,spritesheet.getSprite(15, 13, 35, 22)); 
                    entities.add(player);
            
            }
        
        public void initFrame(){
            frame = new JFrame("Jogo");
            frame.add(this);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        
        public synchronized void start() {
            thread = new Thread(this);
            isRunning = true;
            thread.start();
            
        }
        
        public synchronized void stop() {
    
            isRunning = false;
            try{
                thread.join();
            }
            catch(InterruptedException e){
                e.printStackTrace();
            }   
        }
            
        public static void main(String[] args ){
            Game game = new Game();
            game.start();
            
        }
        
        public void tick() {

            for(int i = 0; i<entities.size();i++){
                Entity e = entities.get(i);

               /*  if(e instanceof Player){
                    //estou dando tick no player
                }*/
                e.tick();
            }
            
        }
        
        public void render() {
            BufferStrategy bs = this.getBufferStrategy();
            
            if (bs == null) {
                this.createBufferStrategy(3);
                return;
            }

                    Graphics g = image.getGraphics();
                    g.setColor(new Color(0,0,0));
                    g.fillRect(0, 0,WIDTH,HEIGHT);
    
    
            /*Renderiza????o do jogo 
            Graphics2D g2 = (Graphics2D) g; //cast*/
            for(int i = 0; i<entities.size();i++){
                Entity e = entities.get(i);
                e.render(g);
            }
            
            g.dispose();/*limpar dados da imagem */
            g = bs.getDrawGraphics();
            g.drawImage(image, 0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
            bs.show();
            
            
        }
    
        public void run() {
            long LastTime = System.nanoTime();
            double amountOfTicks = 60.0;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;
            int frames = 0;
            double timer = System.currentTimeMillis();
            
            while(isRunning) {
                long now = System.nanoTime();
                delta += (now - LastTime)/ns;
                LastTime = now;
                
                if (delta >= 1) {
                    tick();
                    render();
                    frames++;
                    delta--;
                    
                }
                
                if(System.currentTimeMillis() - timer >=1000) {
                    System.out.println("FPS:" +frames);
                    frames = 0;
                    timer = System.currentTimeMillis();
                }
     
            }
            stop();   
        }

        public void keyPressed(KeyEvent e){

            if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
                player.right = true;
                //Execute tal a????o!
            }else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
                player.left = true;
            }

            if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                player.up = true;
            }else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S);
            player.down = true;
        }

        public void keyReleased(KeyEvent e){

            if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
                player.right = false;
                //Execute tal a????o!
            }else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
                player.left = false;
            }

            if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W){
                player.up = false;
            }else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S);
            player.down = false;

        }

        public void keyTyped(KeyEvent e){

        }


    } 

