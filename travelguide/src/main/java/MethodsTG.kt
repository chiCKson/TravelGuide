package com.keliya.chickson.travelguide

import android.content.Context
import com.sdsmdg.tastytoast.TastyToast

class MethodsTG {
    fun TGToastDefault(msg:String,context:Context){
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.DEFAULT)
    }
    fun TGToastWarning(msg:String,context:Context){
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.WARNING)
    }
    fun TGToastInfo(msg:String,context:Context){
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.INFO)
    }
    fun TGToastSuccess(msg:String,context:Context){
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
    }
    fun TGToastError(msg:String,context:Context){
        TastyToast.makeText(context, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR)
    }
}