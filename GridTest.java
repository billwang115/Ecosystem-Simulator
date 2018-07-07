/* test grid program
 * By:William Wang
 * Dec 04 2017
 */
import java.util.Scanner;

class GridTest{
  public static void main(String[] args){
    //declare variables
    Scanner input=new Scanner(System.in);
    int decision;
    int mapX;
    int mapY;
    int numSheep=1; //set to 1 cause the program ends when the number is 0
    int numPlant=1;
    int numWolf=1;
    int plantLimit=0;
    int sheepLimit=0;
    int wolfLimit=0;
    int plantRate=0;
    int sheepRate=0;
    int wolfRate=0;
    int plantHealth;
    int sheepHealth;
    int wolfHealth;
    int days=0;
    
    //start program
    //user input
    System.out.println("Welcome to the ecosystem simulator. There are wolves, sheep and plants in this ecosystem.");
    System.out.println();
    System.out.println("Enter the amount of rows that you want on the grid");
    mapX=input.nextInt();
    System.out.println("Enter the amount of columns that you want on the grid");
    mapY=input.nextInt();
    do{
      System.out.println("Do you want to control spawn rate(chance) of each organism or the amount of each organism to be created? Enter 1 for spawn rate or 2 for amount of each organism.");
      System.out.println("Note: You cannot control both");
      decision=input.nextInt();
      if(decision==1){                                       ///ask user for spawn rate////
        do{
          System.out.println();
          System.out.println("Enter the spawning rate of the organisms.");
          System.out.println("Enter the plant spawning rate. (percentage value)");
          plantRate=input.nextInt();
          System.out.println("Enter the sheep spawning rate.(percentage value)");
          sheepRate=input.nextInt();
          System.out.println("Enter the wolf spawning rate.(percentage value)");
          wolfRate=input.nextInt();
          if((plantRate+sheepRate+wolfRate)>100){
            System.out.println("Please enter values that add to less than 100%");
          }
        }while((plantRate+sheepRate+wolfRate)>100);
      }else if(decision==2){                  ///ask user for amount of organisms///
        do{
          System.out.println();
          System.out.println("Enter the amount of plants you want to be created");
          plantLimit=input.nextInt();
          System.out.println("Enter the amount of sheep you want to be created");
          sheepLimit=input.nextInt();
          System.out.println("Enter the amount of wolves you want to be created");
          wolfLimit=input.nextInt();
          if((plantLimit+sheepLimit+wolfLimit)>(mapX*mapY)){  //if the total number of organisms entered is higher than the grid size//
            System.out.println("Please change your amounts to be in total, less than "+(mapX*mapY));  
          }
        }while((plantLimit+sheepLimit+wolfLimit)>(mapX*mapY));
      }else{
        System.out.println("please enter a valid choice.");
      }
    }while(decision!=1&&decision!=2);
    System.out.println();
    System.out.println("Enter the health of the organisms.");         //get health of organisms//
    System.out.println("Enter the health/nutritional value of the plants");
    plantHealth=input.nextInt();
    System.out.println("Enter the health of the sheep");
    sheepHealth=input.nextInt();
    System.out.println("Enter the health of the wolves");
    wolfHealth=input.nextInt();
    
    
    //create map
    Life[][] map=new Life[mapX][mapY];
    
    //Intialize the map and display grid
    generateLife(map,plantRate,sheepRate,wolfRate,plantHealth,sheepHealth,wolfHealth,plantLimit,sheepLimit,wolfLimit,decision);
    DisplayGrid grid=new DisplayGrid (map);
    grid.refresh();
    
    //start the simulation
    while(numSheep!=0&&numPlant!=0&&numWolf!=0){
      //delay the life spawning
      try{
        Thread.sleep(1000);
      }catch(Exception e){};
      days=days+1;
      movement(map);
      numSheep=countSheep(map);
      numPlant=countPlant(map);
      numWolf=countWolf(map);
      grid.refresh();
    }
    //output results
    grid.hide();
    System.out.println();
    System.out.println("The simulation has finished because a species has died!");
    System.out.println("Here are the statistics:");
    if(numSheep==0){
      System.out.println("The Sheep species has become extinct!");
    }else if(numPlant==0){
      System.out.println("The Plant species has become extinct!");
    }else{
      System.out.println("The Wolf species has become extinct!"); 
    }
    System.out.println(days+" days has passed.");
  }
  
//methods
  
  //count the number of sheep
  public static int countSheep(Life[][] map){
    int sheepCount=0;
    for(int i=0;i<map.length;i=i+1){
      for(int j=0;j<map[0].length;j=j+1){ 
        if(map[i][j] instanceof Sheep){
          sheepCount=sheepCount+1;
        }
      }
    }
    return sheepCount;
  }
  //count the number of plants
  public static int countPlant(Life[][] map){
    int plantCount=0;
    for(int i=0;i<map.length;i=i+1){
      for(int j=0;j<map[0].length;j=j+1){ 
        if(map[i][j] instanceof Plant){
          plantCount=plantCount+1;
        }
      }
    }
    return plantCount;
  }
  
  //count the number of Wolves
  public static int countWolf(Life[][] map){
    int wolfCount=0;
    for(int i=0;i<map.length;i=i+1){
      for(int j=0;j<map[0].length;j=j+1){ 
        if(map[i][j] instanceof Wolf){
          wolfCount=wolfCount+1;
        }
      }
    }
    return wolfCount; 
  }
  
  //initialize the map method(spawn the organisms)
  public static void generateLife(Life[][] map,int plantRate,int sheepRate,int wolfRate,int plantHealth,int sheepHealth,int wolfHealth,int plantLimit,int sheepLimit,int wolfLimit,int decision){
    if(decision==1){      //if user decides to only modify spawn rate//
      for(int i=0;i<map.length;i=i+1){
        for(int j=0;j<map[0].length;j=j+1){
          int position=(int)(Math.random()* 100+1);
          if(position<=plantRate){       //create organisms based on a percentage chance decided by the user//
            map[i][j]=new Plant(plantHealth); 
          }else if(position>plantRate&&position<=sheepRate+plantRate){
            map[i][j]=new Sheep(sheepHealth);      
          }else if(position>plantRate+sheepRate&&position<=wolfRate+plantRate+sheepRate){
            map[i][j]=new Wolf(wolfHealth);
          }else{
            map[i][j]=null;
          }
        }        
      }             
    }else if(decision==2){     //if user decides to only modify amount of organisms//
      int numSpaces=0;
      int spaceAllowed=(map.length*map[0].length)-(plantLimit+sheepLimit+wolfLimit);
      int plantCount=0;
      int sheepCount=0;
      int wolfCount=0;
      boolean withinLimit;
      for(int i=0;i<map.length;i=i+1){
        for(int j=0;j<map[0].length;j=j+1){
          int position;
          do{                                    //check if the max amount of an organism has been created//
            position=(int)(Math.random()* 7+1);   /////the position represents which object is created. Each position represents a certain 
            withinLimit=true;
            if((numSpaces==spaceAllowed)&&(position==7)){
              withinLimit=false;  
            }
            if((plantCount==plantLimit)&&(position==1||position==2)){
              withinLimit=false;
            }
            if ((sheepCount==sheepLimit)&&(position==3||position==4||position==5)){
              withinLimit=false;
            }
            if ((wolfCount==wolfLimit)&&(position==6)){
              withinLimit=false; 
            }
          }while(withinLimit==false);
          
          if(position==1||position==2){         //create organisms based off a random value//
            map[i][j]=new Plant(plantHealth); 
            plantCount=plantCount+1;
          }else if(position==3||position==4||position==5){
            map[i][j]=new Sheep(sheepHealth);   
            sheepCount=sheepCount+1;
          }else if(position==6){
            map[i][j]=new Wolf(wolfHealth);
            wolfCount=wolfCount+1;
          }else if(position==7||position==8){
            map[i][j]=null;
            numSpaces=numSpaces+1;
          }
        }
      }
    }
  }
  
  //simulate movement method
  public static void movement(Life[][] map){
    
    for(int i=0;i<map.length;i=i+1){
      for(int j=0;j<map[0].length;j=j+1){
        if(map[i][j] instanceof Sheep){    /////IF THE CURRENT ANIMAL IS A SHEEP////////////
          map[i][j].hunger();
          if(map[i][j].getHealth()<=0){
            map[i][j]=null;
          }else{
            int direction;
            boolean moveable;
            do{                                 //check if random direction is moveable to prevent array out of bounds////
              direction=(int)(Math.random()* 4+1);
              moveable=true;
              if(i==0&&direction==1){  
                moveable=false;
              }
              if(j==map[0].length-1&&direction==2){
                moveable=false; 
              }
              if(i==map.length-1&&direction==3){
                moveable=false;
              }
              if(j==0&&direction==4){
                moveable=false;
              }
            }while(moveable==false);
            
            //check for objects in a random direction and moves accordingly//
            if(direction==1){            ////moving up////
              if(map[i-1][j]==null){
                map[i-1][j]=map[i][j];
                map[i][j]=null;
              }else if(map[i-1][j] instanceof Plant){//check plant
                map[i][j].eat(map[i-1][j].getHealth());
                map[i-1][j]=map[i][j];
                map[i][j]=null;
              }else if(map[i-1][j] instanceof Wolf){//check wolf
                map[i-1][j].eat((map[i][j].getHealth())/5);
                map[i][j]=map[i-1][j];
                map[i-1][j]=null;
              }else if(map[i-1][j] instanceof Sheep){//check sheep
                if((map[i][j].getHealth()>20)&&(map[i-1][j].getHealth()>20)){
                  map[i][j].reduceHealth(10);
                  map[i-1][j].reduceHealth(10);
                  boolean taken1=false,taken2=false,taken3=false,taken4=false;
                  do{                       /////Check for open spaces beside the sheep to spawn new sheep/////
                    int spawnLocation=(int)(Math.random()* 4+1);
                    if(spawnLocation==1){  //check left
                      taken1=true;
                      if(i!=map.length-1){
                        if (map[i+1][j]==null){
                          map[i+1][j]=new Sheep(20); 
                        }
                      }
                    }else if (spawnLocation==2){//check right
                      taken2=true;
                      if(j!=map[0].length-1){
                        if (map[i][j+1]==null){
                          map[i][j+1]=new Sheep(20); 
                        }
                      }
                    }else if(spawnLocation==3){ //check down
                      taken3=true;
                      if(i!=0){
                        if (map[i-1][j]==null){
                          map[i-1][j]=new Sheep(20); 
                        }   
                      }
                    }else if(spawnLocation==4){ //check left
                      taken4=true;
                      if(j!=0){
                        if (map[i][j-1]==null){
                          map[i][j-1]=new Sheep(20); 
                        }
                      }
                    }
                  }while(taken1!=true||taken2!=true||taken3!=true||taken4!=true);
                }
              }
            }else if(direction==2){         ///////moving right/////
              if(map[i][j+1]==null){
                map[i][j+1]=map[i][j];
                map[i][j]=null;
              }else if(map[i][j+1] instanceof Plant){//check plant
                map[i][j].eat(map[i][j+1].getHealth());
                map[i][j+1]=map[i][j];
                map[i][j]=null;
              }else if(map[i][j+1] instanceof Wolf){//check wolf
                map[i][j+1].eat((map[i][j].getHealth())/5);
                map[i][j]=map[i][j+1];
                map[i][j+1]=null;
              }else if(map[i][j+1] instanceof Sheep){//check sheep
                if((map[i][j].getHealth()>20)&&(map[i][j+1].getHealth()>20)){
                  map[i][j].reduceHealth(10);
                  map[i][j+1].reduceHealth(10);
                  boolean taken1=false,taken2=false,taken3=false,taken4=false;
                  do{                       ///Check for open spaces beside the sheep to spawn new sheep///////
                    int spawnLocation=(int)(Math.random()* 4+1);
                    if(spawnLocation==1){  //check left
                      taken1=true;
                      if(i!=map.length-1){
                        if (map[i+1][j]==null){
                          map[i+1][j]=new Sheep(20); 
                        }
                      }
                    }else if (spawnLocation==2){//check right
                      taken2=true;
                      if(j!=map[0].length-1){
                        if (map[i][j+1]==null){
                          map[i][j+1]=new Sheep(20); 
                        }
                      }
                    }else if(spawnLocation==3){ //check down
                      taken3=true;
                      if(i!=0){
                        if (map[i-1][j]==null){
                          map[i-1][j]=new Sheep(20); 
                        }   
                      }
                    }else if(spawnLocation==4){ //check left
                      taken4=true;
                      if(j!=0){
                        if (map[i][j-1]==null){
                          map[i][j-1]=new Sheep(20); 
                        }
                      }
                    }
                  }while(taken1!=true||taken2!=true||taken3!=true||taken4!=true);
                }
              }
            }else if(direction==3){         //////moving down//////
              if(map[i+1][j]==null){
                map[i+1][j]=map[i][j];
                map[i][j]=null;
              }else if(map[i+1][j] instanceof Plant){//check plant
                map[i][j].eat(map[i+1][j].getHealth());
                map[i+1][j]=map[i][j];
                map[i][j]=null;
              }else if(map[i+1][j] instanceof Wolf){//check wolf
                map[i+1][j].eat((map[i][j].getHealth())/5);
                map[i][j]=map[i+1][j];
                map[i+1][j]=null;
              }else if(map[i+1][j] instanceof Sheep){//check sheep
                if((map[i][j].getHealth()>20)&&(map[i+1][j].getHealth()>20)){
                  map[i][j].reduceHealth(10);
                  map[i+1][j].reduceHealth(10);
                  boolean taken1=false,taken2=false,taken3=false,taken4=false;
                  do{                       ///Check for open spaces beside the sheep to spawn new sheep///////
                    int spawnLocation=(int)(Math.random()* 4+1);
                    if(spawnLocation==1){  //check left
                      taken1=true;
                      if(i!=map.length-1){
                        if (map[i+1][j]==null){
                          map[i+1][j]=new Sheep(20); 
                        }
                      }
                    }else if (spawnLocation==2){//check right
                      taken2=true;
                      if(j!=map[0].length-1){
                        if (map[i][j+1]==null){
                          map[i][j+1]=new Sheep(20); 
                        }
                      }
                    }else if(spawnLocation==3){ //check down
                      taken3=true;
                      if(i!=0){
                        if (map[i-1][j]==null){
                          map[i-1][j]=new Sheep(20); 
                        }   
                      }
                    }else if(spawnLocation==4){ //check left
                      taken4=true;
                      if(j!=0){
                        if (map[i][j-1]==null){
                          map[i][j-1]=new Sheep(20); 
                        }
                      }
                    }
                  }while(taken1!=true||taken2!=true||taken3!=true||taken4!=true);
                }
              }
            }else if(direction==4){                     //////moving left//////
              if(map[i][j-1]==null){       
                map[i][j-1]=map[i][j];
                map[i][j]=null;
              }else if(map[i][j-1] instanceof Plant){//check plant
                map[i][j].eat(map[i][j-1].getHealth());
                map[i][j-1]=map[i][j];
                map[i][j]=null;
              }else if(map[i][j-1] instanceof Wolf){//check wolf
                map[i][j-1].eat((map[i][j].getHealth())/5);
                map[i][j]=map[i][j-1];
                map[i][j-1]=null;
              }else if(map[i][j-1] instanceof Sheep){//check sheep
                if((map[i][j].getHealth()>20)&&(map[i][j-1].getHealth()>20)){
                  map[i][j].reduceHealth(10);
                  map[i][j-1].reduceHealth(10);
                  boolean taken1=false,taken2=false,taken3=false,taken4=false;
                  do{                       ///Check for open spaces beside the sheep to spawn new sheep///////
                    int spawnLocation=(int)(Math.random()* 4+1);
                    if(spawnLocation==1){  //check left
                      taken1=true;
                      if(i!=map.length-1){
                        if (map[i+1][j]==null){
                          map[i+1][j]=new Sheep(20); 
                        }
                      }
                    }else if (spawnLocation==2){//check right
                      taken2=true;
                      if(j!=map[0].length-1){
                        if (map[i][j+1]==null){
                          map[i][j+1]=new Sheep(20); 
                        }
                      }
                    }else if(spawnLocation==3){ //check down
                      taken3=true;
                      if(i!=0){
                        if (map[i-1][j]==null){
                          map[i-1][j]=new Sheep(20); 
                        }   
                      }
                    }else if(spawnLocation==4){ //check left
                      taken4=true;
                      if(j!=0){
                        if (map[i][j-1]==null){
                          map[i][j-1]=new Sheep(20); 
                        }
                      }
                    }
                  }while(taken1!=true||taken2!=true||taken3!=true||taken4!=true);
                }
              }
            }   
          }
        }else if(map[i][j] instanceof Wolf){          ///////IF THE CURRENT ANIMAL IS A WOLF////////
          map[i][j].hunger();
          if(map[i][j].getHealth()<=0){
            map[i][j]=null;
          }else{
            int direction;
            boolean moveable;
            do{                                  //check if random direction is moveable to prevent array out of bounds///
              direction=(int)(Math.random()* 4+1);
              moveable=true;
              if(i==0&&direction==1){  
                moveable=false;
              }
              if(j==map[0].length-1&&direction==2){
                moveable=false; 
              }
              if(i==map.length-1&&direction==3){
                moveable=false;
              }
              if(j==0&&direction==4){
                moveable=false;
              }
            }while(moveable==false);
            //check for objects in random directions and moves accordingly
            if(direction==1){                ////////moving up///////
              if(map[i-1][j]==null){
                map[i-1][j]=map[i][j];
                map[i][j]=null;
              }else if(map[i-1][j] instanceof Wolf){     //check for wolves
                if(map[i][j].compareTo(map[i-1][j])==0){
                  map[i][j].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i-1][j])==1){
                  map[i-1][j].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i-1][j])==2){
                  map[i][j].reduceHealth(10);
                  map[i-1][j].reduceHealth(10);
                }
              }else if(map[i-1][j] instanceof Sheep){   //check for sheep
                map[i][j].eat((map[i-1][j].getHealth())/5);
                map[i-1][j]=map[i][j];
                map[i][j]=null;
              }
              
            }else if(direction==2){          ///////moving right///////
              if(map[i][j+1]==null){
                map[i][j+1]=map[i][j];
                map[i][j]=null;
              }else if(map[i][j+1] instanceof Wolf){   //check for wolves
                if(map[i][j].compareTo(map[i][j+1])==0){
                  map[i][j].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i][j+1])==1){
                  map[i][j+1].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i][j+1])==2){
                  map[i][j].reduceHealth(10);
                  map[i][j+1].reduceHealth(10);
                }
              }else if(map[i][j+1] instanceof Sheep){   //check for sheep
                map[i][j].eat((map[i][j+1].getHealth())/5);
                map[i][j+1]=map[i][j];
                map[i][j]=null;
              }
            }else if(direction==3){          //////moving down///////
              if(map[i+1][j]==null){
                map[i+1][j]=map[i][j];
                map[i][j]=null;
              }else if(map[i+1][j] instanceof Wolf){   //check for wolves
                if(map[i][j].compareTo(map[i+1][j])==0){
                  map[i][j].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i+1][j])==1){
                  map[i+1][j].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i+1][j])==2){
                  map[i][j].reduceHealth(10);
                  map[i+1][j].reduceHealth(10);
                }
              }else if(map[i+1][j] instanceof Sheep){    //check for sheep
                map[i][j].eat(map[i+1][j].getHealth());
                map[i+1][j]=map[i][j];
                map[i][j]=null;
              }
            }else if(direction==4){
              if(map[i][j-1]==null){            ////////moving left///////
                map[i][j-1]=map[i][j];
                map[i][j]=null;
              }else if(map[i][j-1] instanceof Wolf){   //check for wolves
                if(map[i][j].compareTo(map[i][j-1])==0){
                  map[i][j].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i][j-1])==1){
                  map[i][j-1].reduceHealth(10);
                }else if(map[i][j].compareTo(map[i][j-1])==2){
                  map[i][j].reduceHealth(10);
                  map[i][j-1].reduceHealth(10);
                }
              }else if(map[i][j-1] instanceof Sheep){ //check for sheep
                map[i][j].eat(map[i][j-1].getHealth());
                map[i][j-1]=map[i][j];
                map[i][j]=null;
              }
            }
          }
        }else if(map[i][j] instanceof Plant){    ///////IF THE CURRENT ANIMAL IS A PLANT//////
          map[i][j].hunger();
          if(map[i][j].getHealth()<=0){
            map[i][j]=null;
          }
        }
      }
    }    
  }
}
