//IMPORTAZIONE DELLA LIBRERIE
import processing.core.*;//Importazione della libreria di Processing
import java.util.*;//Importazione della libreria dei arrayList
import controlP5.*;//Importazione della libreria dei arrayList
import ddf.minim.*;//Importazione delle librerie dei suoni

public class Memory_Card extends PApplet{
    //Variabili globali
    Card[] myCard;
    ArrayList<Boolean>bool;//array delle schermate
    int []x;//array delle X
    int []y;//array delle Y
    int []fv;//array per memorizzare l'indice di ogni carta
    int []cardUp;//array per comparare due carte girate
    boolean[]clicked;//array per capire se le carte sono state girate
    int flipped;//serve a capire quante carte sono girate
    int win;//variabile della VITTORIA
    ControlP5 cp5;
    //BOTTONI
    Button bottoneRules;
    Button bottoneQuit;
    Button bottoneGame;
    Button bottonePlayagain;
    Button bottoneMainmenu;
    ArrayList<PImage>wallpaper;//array degli sfondi
    boolean check;//variabile per i bottoni
    PFont font;//font delle scritte
    boolean checkingPairs;//controlla se le carte sono uguali oppure no
    long startTime;//tempo per le carte girate
    Minim m;//variabile minim
    AudioPlayer soundTrack;//audio sottofondo
    AudioPlayer button;//audio bottoni
    AudioPlayer right;//audio giusto
    AudioPlayer wrong;//audio sbagliato
    AudioPlayer turnCard;//audio giraCarta
    AudioPlayer victory;//audio per la vittora


    public void settings(){
        fullScreen();
    }

    public void setup() {
        //inizializzazione delle variabili globali/variabili
        font=createFont("CurlzMT.ttf",30);
        myCard= new Card[16];
        bool= new ArrayList<Boolean>();
        x=new int [16];
        y=new int [16];
        fv=new int [16];
        cardUp=new int [2];
        clicked =new boolean[16];
        flipped=0;
        win=0;
        cp5=new ControlP5(this);
        wallpaper= new ArrayList<PImage>();
        check=false;
        checkingPairs=false;

        //variabili audio
        m = new Minim(this);
        soundTrack = m.loadFile("soundTrack.wav");

        //audio LOOP;
        soundTrack.loop();

        //corretto
        m=new Minim(this);
        right= m.loadFile("right.wav");


        //sbagliato
        m=new Minim(this);
        wrong = m.loadFile("wrong.wav");

        //giraCarta
        m=new Minim(this);
        turnCard = m.loadFile("turnCard.wav");

        //bottoni
        m=new Minim(this);
        button = m.loadFile("right.wav");

        //vittora
        m=new Minim(this);
        victory=m.loadFile("victory.wav");

        //inizializzazione dell'array del cambio schermata (bool)
        for(int i=0;i<3;i++){
            if(i==0){
                bool.add(true);
            }
            else {
                bool.add(false);
            }
        }
        //inizializzazione delle'array delle schermate
        wallpaper.add(0,(loadImage("MainMenu.png")));
        wallpaper.get(0).resize(width,height);
        wallpaper.add(1,(loadImage("sfondoRules.jpg")));
        wallpaper.get(1).resize(width,height);
        wallpaper.add(2,(loadImage("sfondoGame.jpeg")));
        wallpaper.get(2).resize(width,height);
        wallpaper.add(3,(loadImage("sfondoWin.png")));
        wallpaper.get(3).resize(width,height);


        //inizializzazione dell'array dei bottoni
        bottoneRules=cp5.addButton("bottoneRules").setValue(100).setSize(200,100).setPosition(width/2-400,height/2+200).setColorBackground(color(158,192,255)).setColorLabel((0)).setFont(font).setLabel("RULES").hide();
        bottoneGame=cp5.addButton("bottoneGame").setValue(100).setSize(200,100).setPosition(width/2-100,height/2+200).setColorBackground(color(158,192,255)).setColorLabel((0)).setFont(font).setLabel("PLAY").hide();
        bottonePlayagain=cp5.addButton("bottonePlayagain").setValue(100).setSize(200,100).setPosition(width/2,height/2+200).setColorBackground(color(101,241,203)).setColorLabel((0)).setFont(font).setLabel("PLAY AGAIN").hide();
        bottoneMainmenu=cp5.addButton("bottoneMainmenu").setValue(100).setSize(200,100).setPosition(width/2-80,height/2+200).setColorBackground(color(158,192,255)).setColorLabel((0)).setFont(font).setLabel("MAIN MENU").hide();
        bottoneQuit=cp5.addButton("bottoneQuit").setValue(100).setSize(200,100).setPosition(width/2+200,height/2+200).setColorBackground(color(101,241,203)).setColorLabel(0).setFont(font).setLabel("QUIT").hide();

        //impostazioni finestra
        background(0,255,255);

        //variabili
        int myX= width/4;
        int myY= 15;
        int count=1;

        //inizializzazione degli array
        for(int i=0; i<16;i++){
            clicked[i]=false;//se una carta è stata cliccata non puoi cliccarla di nuovo
            y[i]=myY;
            x[i]=myX;
            fv[i]=count;//assegni il contatore delle carte ad un array così poi da poter mostrare tutte le carte tramite un ciclo FOR
            count++;
            if(count==9){//controllo per evitare l'indexOutofBound
                count=1;
            }
            if(myX<width-width/3){
                myX+=215;
            }
            else if(myX>width-width/3){
                myX=width/4;
                myY+=200;
            }
        }

        shuffle();//mescola le carte

        //inizializzazione di tutte le carte
        for(int i=0;i<16;i++){
            myCard[i]= new Card(x[i],y[i],fv[i],this);
        }
    }

    public void draw(){
        //Controlla se le carte girate non sono uguali allora le rigira dopo 1 secondo
        if (checkingPairs) {
            if (millis() - startTime >= 1000) { // Controlla se è passato 1 secondo
                for (int k = 0; k < 2; k++) {
                    //audio carte diverse
                    if(wrong.position()==wrong.length()){
                        wrong.rewind();
                        wrong.play();
                    }else{
                        wrong.play();

                    }

                    myCard[cardUp[k]].faceDown();
                    clicked[cardUp[k]] = false;
                }
                flipped = 0;
                checkingPairs = false;
            }
        }

        if(bool.get(0)){
            //Main Menu
            for(int i=0;i<bool.size();i++){
                if(i!=0){
                    bool.set(i,false);
                }
            }
            bottoneRules.show();//bottone rules
            bottoneGame.show();//bottone play
            bottoneQuit.setPosition(width/2+200,height/2+200).show();//bottone quitGame

            bottonePlayagain.hide();//bottone play again
            bottoneMainmenu.hide();//bottone main menu
            background(wallpaper.get(0));//sfondo menù
            fill(0);
            textAlign(CENTER,BOTTOM);
            textFont(font);
            textSize(80);
            text("Ciao! Benevunto nel Memory Card!",width/2,height/2);
        }
        else if(bool.get(1)){
            //Rules
            for(int i=0;i<bool.size();i++){
                if(i!=1){
                    bool.set(i,false);
                }
            }
            background(wallpaper.get(1));//sfondo delle rules
            imageMode(CENTER);
            image(loadImage("rules.png"),width/2,height/2+100);//regole
            imageMode(CORNER);
            bottoneMainmenu.setPosition(width/2+300,height/2);
            bottoneMainmenu.show();
            bottoneRules.hide();
            bottoneGame.hide();
            bottoneQuit.hide();
        }
        else if(bool.get(2)) {
            //Game
            for(int i=0;i<bool.size();i++){
                if(i!=2){
                    bool.set(i,false);
                }
            }
            background(wallpaper.get(2));//sfondo game
            bottonePlayagain.hide();
            bottoneMainmenu.hide();
            bottoneRules.hide();
            bottoneGame.hide();
            bottoneQuit.hide();
            for (int i = 0; i < 16; i++) {
                myCard[i].display();//mostra le carte coperte
            }
            if (win == 8) {//ha raccolto tutte le carte
                if(victory.position()==victory.length()){
                    victory.rewind();
                    victory.play();
                }else{
                    victory.play();

                }

                for (int i =0; i<bool.size();i++)
                    bool.set(i,false);
            }
        }
        else{
            background(wallpaper.get(3));//sfondo win
            bottonePlayagain.setPosition(width/3-250,height/2+80);
            bottoneQuit.setPosition(width-width/3+80,height/2+80);
            bottoneMainmenu.setPosition(width/2-100,height/2+200);
            bottonePlayagain.show();
            bottoneQuit.show();
            bottoneMainmenu.show();


            textSize(100);
            textAlign(CENTER);
            fill(0);
            text("You win!!", width / 2, height / 2);
        }
    }

    public void mouseClicked() {
        for (int i = 0; i < 16; i++) {
            if (mouseX > x[i] && mouseX < (x[i] + 105) && mouseY > y[i] && mouseY < (y[i] + 140) && (clicked[i] == false)) {
                //audio carta girata
                if(turnCard.position()==turnCard.length()){
                    turnCard.rewind();
                    turnCard.play();
                }else{
                    turnCard.play();

                }

                myCard[i].displayFront();
                clicked[i] = true;
                cardUp[flipped] = i;

                flipped++;
                if (flipped == 2) {

                    //controlla se le carte girate sono uguali
                    if (fv[cardUp[0]] == fv[cardUp[1]]) {
                        //audio carte uguali
                        if(right.position()==right.length()){
                            right.rewind();
                            right.play();
                        }else{
                            right.play();

                        }
                        //le carte scompaiono dello schermo
                        myCard[cardUp[0]].matched();
                        myCard[cardUp[1]].matched();
                        win += 1;
                        checkingPairs=false;
                    }else{
                        checkingPairs = true;
                        startTime = millis(); // Salva il tempo iniziale
                    }
                    flipped=0;
                }
            }
        }
    }

    //CALL BACK DEI BOTTONI
    public void bottoneRules(ControlEvent event){
        if(event.isFrom(bottoneRules)){
            if(button.position()==button.length()){
                button.rewind();
                button.play();
            }else{
                button.play();

            }
            bool.set(0,false);//sfondo menù
            bool.set(1,true);//sfondo rules
        }
    }
    public void bottoneGame(ControlEvent event){
        if(event.isFrom(bottoneGame)){
            if(check){
                reset();
            }
            if(button.position()==button.length()){
                button.rewind();
                button.play();
            }else{
                button.play();

            }
            bool.set(2,true);//sfondo game
            bool.set(0,false);//sfondo menù
            check=true;
        }
    }
    public void bottonePlayagain(ControlEvent event){
        if(event.isFrom(bottonePlayagain)){
            if(button.position()==button.length()){
                button.rewind();
                button.play();
            }else{
                button.play();

            }

            reset();
            bool.set(2,true);//sfondo game
            bool.set(0,false);//sfondo menù
            bool.set(1,false);//sfondo rules
        }
    }
    public void bottoneMainmenu(ControlEvent event){
        if(event.isFrom(bottoneMainmenu)){
            if(button.position()==button.length()){
                button.rewind();
                button.play();
            }else{
                button.play();

            }
            bool.set(1,false);//sfondo rules
            bool.set(0,true);//sfondo menù
        }
    }

    public void bottoneQuit(ControlEvent event){
        if (event.isFrom(bottoneQuit)) {
            button.play();
            exit();
        }
    }

    //FUNZIONI
    void shuffle(){
        int temp=0;
        int rand=0;
        for(int i=0;i<16;i++){
            rand= (int)random(0,16);
            temp=fv[i];
            fv[i]=fv[rand];
            fv[rand]=temp;
        }
    }

    void reset(){
        //inizializzazione dell'array del cambio schermata (bool)
        for(int i=0;i<3;i++){
            if(i==0){
                bool.add(true);
            }
            else {
                bool.add(false);
            }
        }

        //variabili
        int myX= width/4;
        int myY= 15;
        int count=1;

        //inizializzazione degli array
        for(int i=0; i<16;i++){
            clicked[i]=false;//se una carta è stata cliccata non puoi cliccarla di nuovo
            y[i]=myY;
            x[i]=myX;
            fv[i]=count;//assegni il contatore delle carte ad un array così poi da poter mostrare tutte le carte tramite un ciclo FOR
            count++;//count aumenta di 1
            if(count==9){//controllo per evitare l'indexOutofBound
                count=1;
            }
            if(myX<width-width/3){
                myX+=215;
            }
            else if(myX>width-width/3){
                myX=width/4;
                myY+=200;
            }
        }

        shuffle();//mescola le carte

        //inizializzazione di tutte le carte
        for(int i=0;i<16;i++){
            myCard[i]= new Card(x[i],y[i],fv[i],this);
        }

        //nascondi i bottoni
        bottoneRules.hide();
        bottoneGame.hide();
        bottoneMainmenu.hide();
        bottonePlayagain.hide();
        bottoneQuit.hide();
        //reset delle variabili
        flipped=0;
        win=0;
    }



    

    //Main
    public static void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Memory_Card" };
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
