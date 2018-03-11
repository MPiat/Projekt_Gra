import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

//Klasa gry
class Gameplay implements Serializable{
  //Liczba skoków
  public int jumps = 0;
  //Historia ruchów - dwie równoległe listy zapamiętujące położenia j oraz i
  //Do sprawdzania na bieżąco
  ArrayList<Integer> histi = new ArrayList<Integer>();
  ArrayList<Integer> histj = new ArrayList<Integer>();
  //Zapis wszystkich liczących się ruchów
  ArrayList<Integer> movedbi = new ArrayList<Integer>();
  ArrayList<Integer> movedbj = new ArrayList<Integer>();
  //zapis cofniętych ruchow
  ArrayList<Integer> revmovedbi = new ArrayList<Integer>();
  ArrayList<Integer> revmovedbj = new ArrayList<Integer>();
  //boolean editmode = false;
}

class Model{
    char  tab[][] = new char[4][4];

}

class Plansza extends JFrame {
//=======================Tworzenie elementów====================================
  //Plansza
  Model model = new Model();
  JButton tab[][] = new JButton[4][4] ;
  JPanel plansza = new JPanel();
  JPanel sterowanie = new JPanel();
  JPanel directions = new JPanel();
  JPanel save_load = new JPanel();
  //Przyciski sterowania
  JTextField t = new JTextField(10);
  JButton back = new JButton("Wstecz");
  JButton forward = new JButton("Dalej");
  JButton help = new JButton("Pomoc");
  JTextField sfilename = new JTextField(10);
  JButton save = new JButton("Zapisz");
  JButton load = new JButton("Wczytaj");
  JButton edit = new JButton("Edycja");
  //Ikony przycisków
  static ImageIcon left = new ImageIcon("arrow_left.png");
  static ImageIcon right = new ImageIcon("arrow_right.png");
  static ImageIcon up = new ImageIcon("arrow_up.png");
  static ImageIcon down = new ImageIcon("arrow_down.png");

//=======================Dodawanie obiektów na planszę==========================
  public Plansza() {
    int i,j;
    Container cp = getContentPane();
    cp.setLayout(new GridLayout(1,2));
    cp.add(plansza);
    cp.add(sterowanie);
    sterowanie.setLayout(new GridLayout(6,4));
    sterowanie.add(t);
    directions.setLayout(new GridLayout(1,2));
    directions.add(back);
    directions.add(forward);
    sterowanie.add(directions);
    sterowanie.add(help);
    sterowanie.add(sfilename);
    sfilename.setText("Nazwa pliku");
    save_load.setLayout(new GridLayout(1,2));
    save_load.add(save);
    save_load.add(load);
    sterowanie.add(save_load);
    //sterowanie.add(edit);
    t.setFont(t.getFont().deriveFont(30.0f));
    plansza.setLayout(new GridLayout(4,4));

    for (i=0;i<4;i++)
       for (j=0;j<4;j++){
      	       tab[i][j]=new JButton(""); //Tworzenie pól
               plansza.add(tab[i][j]); //Dodawanie pól
               if  (i == 3 && j == 3) {
                 (tab[i][j]).addActionListener(new Fin(i,j));
               }else if (i == 0 && j == 0) {
                 (tab[i][j]).addActionListener(new Start(i,j));
               }
               else{
                 (tab[i][j]).addActionListener(new B(i,j));
               }
    }
    back.addActionListener(new Back());
    forward.addActionListener(new Forward());
    help.addActionListener(new Help());
    save.addActionListener(new Save());
    load.addActionListener(new Load());
    edit.addActionListener(new Edit());
//Oznaczenia pól na planszy - zastępstwo strzałek


      tab[0][0].setText("S | d");
      tab[0][1].setIcon(down);
      tab[0][2].setIcon(down);
      tab[0][3].setIcon(left);
      tab[1][0].setIcon(right);
      tab[1][1].setIcon(down);
      tab[1][2].setIcon(right);
      tab[1][3].setIcon(left);
      tab[2][0].setIcon(right);
      tab[2][1].setIcon(right);
      tab[2][2].setIcon(up);
      tab[2][3].setIcon(up);
      tab[3][0].setIcon(up);
      tab[3][1].setIcon(right);
      tab[3][2].setIcon(up);
      tab[3][3].setText("*");

    model.tab[3][3]='M';
    model.tab[0][0]='S';
    edit.setEnabled(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }
  //Tworzenie obiektu gry
  Gameplay game = new Gameplay();


//======================Listenery===============================================
  class B implements ActionListener {

    int i,j;
    B(int i,int j){this.i=i;this.j=j;}
      public void actionPerformed(ActionEvent e) {
        boolean move_check = false;
        System.out.println("I:" + Integer.toString(i) + "J:" + Integer.toString(j));
        if (game.jumps != 0) {
          int previ = game.histi.get(game.histi.size()-1);
          int prevj = game.histj.get(game.histj.size()-1);

          // j > prevj && i == previ - prawo
          //i > previ && j == prevj  - dół
          //j < prevj && i == previ  - lewo
          //i < previ && j == prevj  - góra

          System.out.println("PI:" + Integer.toString(previ) + "PJ:" + Integer.toString(prevj));
          if (previ == 0) { //Pierwszy wiersz
            if (prevj == 0) {
              if (i > previ && prevj == j) { //dół
                move_check = true;
              }
            }else if (prevj == 1) {
              if (i > previ && prevj == j) { //dół
                move_check = true;
              }
            }else if (prevj == 2) {
              if (i > previ && prevj == j) { //dół
                move_check = true;
              }
            }else if (prevj == 3) {
              if (i == previ && prevj > j) { //lewo
                move_check = true;
              }
            }
          }else if (previ == 1) { //Drugi wiersz
            if (prevj == 0) {
              if (i == previ && prevj < j) { //prawo
                move_check = true;

              }
            }else if (prevj == 1) {
              if (i > previ && prevj == j) { //dół
                move_check = true;
              }
            }else if (prevj == 2) {
              if (i == previ && prevj < j) { //prawo
                move_check = true;
              }
            }else if (prevj == 3) {
              if (i == previ && prevj > j) { //lewo
                move_check = true;
              }
            }
          }else if (previ == 2) {
            if (prevj == 0) {
              if (i == previ && prevj < j) { //prawo
                move_check = true;

              }
            }else if (prevj == 1) {
              if (i == previ && prevj < j) { //prawo
                move_check = true;
              }
            }else if (prevj == 2) {
              if (i < previ && prevj == j) { //góra
                move_check = true;
              }
            }else if (prevj == 3) {
              if (i < previ && prevj == j) { //góra
                move_check = true;
              }
            }
          }else if (previ == 3) {
            if (prevj == 0) {
              if (i < previ && prevj == j) { //góra
                move_check = true;
              }
            }else if (prevj == 1) {
              if (i == previ && prevj < j) { //prawo
                move_check = true;
              }
            }else if (prevj == 2) {
              if (i < previ && prevj == j) { //góra
                move_check = true;
              }
            }
          }
          if (move_check == false) {
            System.out.println("False");
          }
      }else{
        if(i > 0 && j == 0){
          System.out.println("Ruch ok.");
          move_check = true;
        }

      }

      //Tutaj if, który sprawdza czy flaga poprawności ruchu jest na true
      if (move_check == true) {
        game.histi.add(i);
        game.movedbi.add(i);
        game.histj.add(j);
        game.movedbj.add(j);
        game.jumps++;
        back.setEnabled(true);

        t.setText("I:"+i+",J: "+j);
        tab[i][j].setIcon(null);
        tab[i][j].setText(Integer.toString(game.jumps)); //Wypisuje ilość skoków
        //wyłączenie pola
        tab[i][j].setEnabled(false);
      }else{


          t.setText("I:"+i+",J: "+j+"-Error");

      }
}


}

//Przycisk umożliwiający edycję ostatniego cofniętego pola
class Edit implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      System.out.println("Histlist");
      for(int i = 0; i < game.histi.size(); i++) {
          System.out.println("IH: " + game.histi.get(i) + " JH: " + game.histj.get(i));
      }
      System.out.println("\n");
      try{
          System.out.println("histi size -1: "+ (game.histi.size()-1));
          game.histi.remove(game.histi.size()-1);
          game.histj.remove(game.histj.size()-1);
          System.out.println("pHistlist");
          for(int k = 0; k < game.histi.size(); k++) {

              System.out.println("IH: " + game.histi.get(k) + " JH: " + game.histj.get(k));

          }
          System.out.println("\n");
          //game.editmode = true;
          edit.setEnabled(false);
        }
      catch(ArrayIndexOutOfBoundsException ed){t.setText("Błąd edycji.");}
  }
}


  //Przycisk kończący rozgrywkę - wygrana gdy wszystkie pola są zaznaczone
  class Fin implements ActionListener {
    int i,j;
    Fin(int i,int j){this.i=i;this.j=j;}
      public void actionPerformed(ActionEvent e) {
        if (game.jumps == 14) {
          t.setText("Wygrana.");
        } else{
          t.setText("Przegrana.");
        }
    }
  }
  //Przycisk Start
  class Start implements ActionListener {
    int i,j;
    Start(int i,int j){this.i=i;this.j=j;}
      public void actionPerformed(ActionEvent e) {
        t.setText("Start");
    }
  }

  //Listener do wracania
  class Back implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        if(game.jumps > 0){ //dla ruchów dla których można cofnąć(nie pierwszy)
          try{
          int mprevi = game.movedbi.remove(game.movedbi.size()-1);
          int mprevj = game.movedbj.remove(game.movedbj.size()-1);

          game.revmovedbi.add(mprevi);
          game.revmovedbj.add(mprevj);

          System.out.println(Integer.toString(game.movedbi.size()));
          tab[mprevi][mprevj].setEnabled(true);
          game.jumps--;
          switch (mprevi) {
            case 0:
              System.out.println("zero");
              switch (mprevj) {
                case 0:
                  System.out.println("zero");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.down);
                  break;
                case 1:
                  System.out.println("one");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.down);
                  break;
                case 2:
                  System.out.println("two");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.down);
                  break;
                case 3:
                  System.out.println("three");//l
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.left);
                  break;
              }
              break;
            case 1:
              System.out.println("one");
              switch (mprevj) {
                case 0:
                  System.out.println("zero");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.right);
                  break;
                case 1:
                  System.out.println("one");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.down);
                  break;
                case 2:
                  System.out.println("two");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.right);
                  break;
                case 3:
                  System.out.println("three");//l
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.left);
                  break;
              }
              break;
            case 2:
              System.out.println("two");
              switch (mprevj) {
                case 0:
                  System.out.println("zero");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.right);
                  break;
                case 1:
                  System.out.println("one");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.right);
                  break;
                case 2:
                  System.out.println("two");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.up);
                  break;
                case 3:
                  System.out.println("three");//l
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.up);
                  break;
              }
              break;
            case 3:
              System.out.println("three");
              switch (mprevj) {
                case 0:
                  System.out.println("zero");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.up);
                  break;
                case 1:
                  System.out.println("one");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.right);
                  break;
                case 2:
                  System.out.println("two");//d
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.up);
                  break;
                case 3:
                  System.out.println("three");//l
                  tab[mprevi][mprevj].setIcon(null);
                  tab[mprevi][mprevj].setText(null);
                  tab[mprevi][mprevj].setIcon(Plansza.left);
                  break;
              }
              break;
          }
        t.setText("Wstecz");
        edit.setEnabled(true);



        System.out.println("Histlist");
        for(int i = 0; i < game.histi.size(); i++) {
            System.out.println("IH: " + game.histi.get(i) + " JH: " + game.histj.get(i));
        }
        System.out.println("\n");
        try{
            System.out.println("histi size -1: "+ (game.histi.size()-1));
            game.histi.remove(game.histi.size()-1);
            game.histj.remove(game.histj.size()-1);
            System.out.println("pHistlist");
            for(int k = 0; k < game.histi.size(); k++) {

                System.out.println("IH: " + game.histi.get(k) + " JH: " + game.histj.get(k));

            }
            System.out.println("\n");
            //game.editmode = true;
            edit.setEnabled(false);
          }
        catch(ArrayIndexOutOfBoundsException ed){t.setText("Błąd edycji.");}




      }catch(ArrayIndexOutOfBoundsException bck1){t.setText("Nie można cofnąć.");}
    }else{
      back.setEnabled(false);
      t.setText("Nie można cofnąć.");
    }
    edit.setEnabled(true);
  }
}
  //Listener do przechodzenia w przód w rozwiązaniu
  class Forward implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        try{
        int fwdi = game.revmovedbi.remove(game.revmovedbi.size()-1);
        int fwdj = game.revmovedbj.remove(game.revmovedbj.size()-1);
        t.setText("Dalej");
        tab[fwdi][fwdj].setEnabled(false);
        game.jumps++;
        game.movedbi.add(fwdi);
        game.movedbj.add(fwdj);

        tab[fwdi][fwdj].setIcon(null);
        tab[fwdi][fwdj].setText(Integer.toString(game.jumps)); //Wypisuje ilość skoków
        //wyłączenie pola
        tab[fwdi][fwdj].setEnabled(false);
      }catch(ArrayIndexOutOfBoundsException fwd1){t.setText("Brak pola.");}

    }
  }
  //Okno pomocy
  class Help implements ActionListener {
      public void actionPerformed(ActionEvent e) {
     JFrame jf=new JFrame("Pomoc");
     jf.setBackground(Color.BLACK);
     jf.setSize(new Dimension(400,400));
     JLabel t = new JLabel("");
     JLabel text = new JLabel();
     text.setText("<html>Autor: Michał Piątek<br><br>Skakanka<br><br>Zaczynając od pola z literą S i skacząc w następnych "+
      "ruchach na kolejne pola, należy<br> obejść wszystkie pola (goszcząc na każdym tylko raz) "+
       "i zakończyć skoki na gwiazdce.<br>Każda strzałka wskazuje kierunek skoku. W rozwiązaniu "+
        "obok strzałek powinny znaleźć się<br>liczby oznaczające kolejne skoki (jedna liczba jest"+
         "ujawniona).<br><br>Edycja<br><br>Żeby edytować ruchy nacisnąć przycisk 'wstecz'"+
         "<br><br></html>");

     jf.add(text);
     jf.setVisible(true);

    }
  }

  //Listener do zapisu stanu gry
  class Save implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        try{
          String filename = sfilename.getText();
          FileOutputStream f = new FileOutputStream(filename);
          ObjectOutputStream save1 = new ObjectOutputStream(f);
          save1.writeObject(game);
          f.close();
          t.setText("Zapisane");
        } catch (IOException ex){System.out.println(ex);}
        sfilename.setText("");
    }
  }

  //Listener do wczytania stanu gry
  class Load implements ActionListener {
      public void actionPerformed(ActionEvent e) {
        boolean checker1 = false;
        try{
             String filename = sfilename.getText();
             ObjectInputStream objload = new ObjectInputStream(
                                             new FileInputStream(filename));
             game = (Gameplay)objload.readObject();
             t.setText("Wczytane");

             checker1 = true;
             objload.close();
         } catch (IOException ex2){t.setText("Błąd wczytywania.");}
             catch (ClassNotFoundException ex2){t.setText("Brak pliku.");}

        if (checker1) { //Odtwarza planszę z stanu gry
          for (int i2=0;i2<4;i2++)
             for (int j2=0;j2<4;j2++){
               tab[i2][j2].setEnabled(true);
             }


             tab[0][0].setText("S | d");
             tab[0][1].setIcon(Plansza.down);
             tab[0][2].setIcon(Plansza.down);
             tab[0][3].setIcon(Plansza.left);
             tab[1][0].setIcon(Plansza.right);
             tab[1][1].setIcon(Plansza.down);
             tab[1][2].setIcon(Plansza.right);
             tab[1][3].setIcon(Plansza.left);
             tab[2][0].setIcon(Plansza.right);
             tab[2][1].setIcon(Plansza.right);
             tab[2][2].setIcon(Plansza.up);
             tab[2][3].setIcon(Plansza.up);
             tab[3][0].setIcon(Plansza.up);
             tab[3][1].setIcon(Plansza.right);
             tab[3][2].setIcon(Plansza.up);
             tab[3][3].setText("*");



          Iterator<Integer> it1 = game.movedbi.iterator();
          Iterator<Integer> it2 = game.movedbj.iterator();

          int jumps2 = 1;
          while (it1.hasNext() && it2.hasNext()) {
              Integer x = it1.next();
              Integer y = it2.next();
              tab[x][y].setIcon(null);
              tab[x][y].setEnabled(false);
              tab[x][y].setText(Integer.toString(jumps2));
              jumps2++;
          }
        }


        sfilename.setText("");
    }
  }

//======================Main====================================================
 public static void main(String[] args) {
    JFrame f = new Plansza();
    f.setSize(600,400);
    f.setLocation(100,100);
    f.setVisible(true);
  }
}
