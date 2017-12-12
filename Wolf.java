/*Wolf Class
 * By:William Wang
 * Nov.27,2017
 */
class Wolf extends Life implements Comparable<Wolf>{
  
  //constructors
  Wolf(int health){
    super(health); 
  }
  
  //methods 
  public void eat(int food){
    addHealth(food);
  }
  public void hunger(){
    reduceHealth(1);
  } 
  public int compareTo(Wolf w){
    if(this.getHealth()<((Wolf)w).getHealth()){
      return 0;
    }else if(this.getHealth()>((Wolf)w).getHealth()){
      return 1;
    }else{
      return 2; 
    }  
  }
}