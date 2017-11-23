package ai.api.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Enes on 5.03.2017.
 */

public class ClickActionHelper {
    public static void startActivity(String className, Bundle extras, Context context){
        Log.d("className", className);
        Class cls=null;
        try {
            cls = Class.forName(className);
        }catch(ClassNotFoundException e){
            //means you made a wrong input in firebase console
        }
        Intent i = new Intent(context, cls);
        i.putExtras(extras);
        context.startActivity(i);
    }
}
