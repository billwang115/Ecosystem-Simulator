/*Sheep Class
 * By:William Wang
 * Nov.27,2017
 */
class Sheep extends Life{
  
  //constructors
  Sheep(int health){
    super(health); 
  }
  
  //methods 
  public void eat(int food){
    addHealth(food);
  }
  public void hunger(){
   reduceHealth(1); 
  }
  
}