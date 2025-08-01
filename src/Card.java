//importazione della libre
import processing.core.*;//importazine della libreria di processing
public class Card{
    //Attributi
    PApplet a;
    PImage cardImage;
    int show=0;
    int cardX=0;
    int cardY=0;
    int faceValue=0;
    String[] cardName={"memoryCard.png","elephantCard.png","giraffeCard.png","lionCard.png","pandaCard.png","crocodileCard.png","rhinocerosCard.png","roosterCard.png","snakeCard.png"};

    //Costruttore
    Card(int x,int y, int fv, PApplet a){
        this.a=a;
        cardX=x;
        cardY=y;
        faceValue=fv;
    }
    void display(){
        cardImage=a.loadImage(cardName[show]);
        a.image(cardImage, cardX,cardY);//mostra le carte in base al faceValue
    }
    void displayFront(){
        show=faceValue;//mostra le carte frontali
    }
    void faceDown(){
        show=0;
    }
    void matched(){
        cardX=-150;
    }
}
