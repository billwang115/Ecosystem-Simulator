/*Plant class
 * By:William Wang
 * Nov.27, 2017
 */
class Plant extends Life{
  
  //constructors
  Plant(int health){
    super(health);  
  }
  
  //methods
  public void hunger(){
   reduceHealth(1); 
  }
  
}