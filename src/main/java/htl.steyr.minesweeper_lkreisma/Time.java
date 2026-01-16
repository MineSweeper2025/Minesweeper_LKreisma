package htl.steyr.minesweeper_lkreisma;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Time {

    private int seconds = 0;


    public String getSeconds() {

        if(this.seconds > 9){
            if(this.seconds > 99){
                return String.valueOf(this.seconds);
            }else{
                return "0" + this.seconds;
            }
        }else{
            return "00" + this.seconds;
        }
    }

    public void oneSecondPassed(){
        this.seconds++;
    }



}
