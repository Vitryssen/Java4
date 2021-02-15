/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb4.Main;

import labb4.DataStructures.Friend;
import java.util.Comparator;

/**
 *
 * @author André
 */
public class Sortbynick implements Comparator<Friend>{
    public int compare(Friend a, Friend b){
        String alower = a.getNick().toLowerCase();
        String blower = b.getNick().toLowerCase();
        return alower.compareTo(blower); //compare a.getNick() if you wish to sort
    }                                    //uppercase before lowercase
}
