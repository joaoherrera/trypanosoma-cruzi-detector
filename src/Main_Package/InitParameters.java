/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Main_Package;

import Main_Package.GraphicalUserInterface.GUI;

/**
 * @author João
 * @date 02/12/2013
 */
public class InitParameters{
     
    public static void main(String[] args){
       GUI.Initialize();     
    }
    
    public static String getOS(){
        return System.getProperty("os.name").toLowerCase();
    }
    
    
}
