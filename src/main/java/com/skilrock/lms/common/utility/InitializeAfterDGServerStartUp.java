package com.skilrock.lms.common.utility;

import javax.servlet.ServletContext;

import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.ServerStartUpData;

public class InitializeAfterDGServerStartUp extends Thread {
    ServletContext servletContext = null;

    public InitializeAfterDGServerStartUp(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

  /*  private Object check() {
        //MBeanServerConnection server;
        try {
            System.out.println("Initializing DG Serivce Data Starts ");
            //ServerStartUpData serverStartUpData = new ServerStartUpData();
            ServerStartUpData.onStartGameData(servletContext);
            return servletContext.getAttribute("GAME_DATA");
           
            //return var;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }*/

    public void run() {
        /*while (check() == null ) {
            try {
                InitializeAfterDGServerStartUp.sleep(10000);
                check();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        // Here add code to call helper

        // Initializing IPE Serivce Data Starts
       
       
        // For setting thr organization data for draw games
        ServerStartUpData.onStartOrganizationData();

        if ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsSLE())) {
            // For setting thr organization data for sports lottery games
            ServerStartUpData.onStartSLEGameData();
            ServerStartUpData.onStartOrgDataForSLE();
        }

        if ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsIW())) {
            ServerStartUpData.onStartIWGameData();
            ServerStartUpData.onStartOrgDataForIW();
        }

        if ("YES".equalsIgnoreCase(LMSFilterDispatcher.getIsVS())) {
            ServerStartUpData.onStartVSGameData();
            ServerStartUpData.onStartOrgDataForVS();
        }

        // For setting advertisement messages.
        ServerStartUpData.onStartAdvMessageData();
        System.out.println("Initializing DG Serivce Data Ends ");
        // Ends
    }
}