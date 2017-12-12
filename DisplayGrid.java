/*draws grid and displays it graphically
 * By:William Wang
 * Dec 04,2017
 */
import javax.swing.*;
import java.awt.*;

class DisplayGrid{
  //declare variables
  private JFrame frame;
  private int maxX,maxY, GridToScreenRatio;
  private Life[][] world;     
  
  //methods
  DisplayGrid(Life[][] w) { 
    this.world = w;
    
    maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
    maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
    GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map
    
    this.frame = new JFrame("Ecosystem Map");
    
    GridAreaPanel worldPanel = new GridAreaPanel();
    
    frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setVisible(true);
  } 
  public void refresh() { ///refresh the display
    frame.repaint();
  }
  public void hide(){      //hide the display panel
    frame.setVisible(false);
  }
  
  class GridAreaPanel extends JPanel {
    public void paintComponent(Graphics g) {        
      //super.repaint();
      
      //images
      Image sheep=Toolkit.getDefaultToolkit().getImage("Sheep.png");
      Image wolf=Toolkit.getDefaultToolkit().getImage("Wolf.png");
      Image plant=Toolkit.getDefaultToolkit().getImage("Sunflower.png");
      Image background=Toolkit.getDefaultToolkit().getImage("Background.png");
      
      g.setColor(Color.WHITE);
      g.drawImage(background,0*GridToScreenRatio,0*GridToScreenRatio,world.length*GridToScreenRatio,world[0].length*GridToScreenRatio,this);
      
      //loop through array
      for(int i = 0; i<world.length;i=i+1)
      { 
        for(int j = 0; j<world[0].length;j=j+1) 
        { 
          //draw organisms 
          if (world[i][j] instanceof Sheep) {   
            g.drawImage(sheep,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }else if (world[i][j] instanceof Plant){ 
            g.drawImage(plant,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }else if (world[i][j] instanceof Wolf){
            g.drawImage(wolf,j*GridToScreenRatio,i*GridToScreenRatio,GridToScreenRatio,GridToScreenRatio,this);
          }
        }   
      }
    }
  }//end of GridAreaPanel
  
} //end of DisplayGrid




