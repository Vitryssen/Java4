/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labb3;

import labb3.DataStructures.Friend;
import java.util.Comparator;

/**
 *
 * @author André
 */
public class Sortbynick implements Comparator<Friend>{
    public int compare(Friend a, Friend b){
        return a.getNick().compareTo(b.getNick());
    }
}
