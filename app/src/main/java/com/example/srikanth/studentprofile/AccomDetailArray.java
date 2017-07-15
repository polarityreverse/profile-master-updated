package com.example.srikanth.studentprofile;

import java.util.ArrayList;

/**
 * Created by skyrider on 5/6/17.
 */

public class AccomDetailArray {

    public static ArrayList<AccomDetails> data=new ArrayList<AccomDetails>();

    public static ArrayList<AccomDetails> getAccomData(){



        return data;
    }

    public static void makeAccomCard(AccomDetails current){


        data.add(current);
        //AccomAdapter.adapterData=data;
        MainActivity.accomadapter.notifyDataSetChanged();

        //MainActivity.accomadapter.notifyItemInserted(data.indexOf(current));


    }
}
