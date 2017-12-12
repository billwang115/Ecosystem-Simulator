/*Life class
 * By:William Wang
 * Nov 27,2017
 */

abstract class Life{
  //declare variables
  private int health;
  
//constructors
  Life(){
    this.health=0; 
  }
  Life(int health){
    this.health=health;
  }
  
  //methods  
  public int getHealth(){
    return health; 
  }
  public void setHealth(int health){
    this.health=health;  
  } 
  public void addHealth(int healthChange){
    this.health=health+healthChange; 
  }                     
  public void reduceHealth(int healthChange){
    this.health=health-healthChange; 
  }
  public void eat(int food){    
  }
  abstract void hunger();
  
  public int compareTo(Life Wolf){   //this is being overriden
    return 5;
  }
  
  
}