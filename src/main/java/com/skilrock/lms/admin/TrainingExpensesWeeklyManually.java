package com.skilrock.lms.admin;

import com.skilrock.lms.coreEngine.accMgmt.common.TrainingExpAgentHelper;

public class TrainingExpensesWeeklyManually
{

    public TrainingExpensesWeeklyManually()
    {
    }

    public static void callManuallyTrngExpDaily()
    {
        TrainingExpAgentHelper ts = new TrainingExpAgentHelper();
        System.out.println("Started calculating Daily..............................");
        try
        {
            ts.submitWeeklyTrngExpForAgents();
            ts.submitWeeklyTrngExpInstantWinForAgents();
            System.out.println("Finished calculating Daily.........................");
        }
        catch(Exception e)
        {
            System.out.println("inside exception.................................");
            e.printStackTrace();
        }
    }
}