package com.bihcomp.bih.testapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reale on 23/11/2016.
 */

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;

    public TitleCreator(Context context, ArrayList<String> titleArray) {
        _titleParents = new ArrayList<>();
        for(int i=0;i<titleArray.size();i++)
        {
            TitleParent title = new TitleParent(titleArray.get(i));
            _titleParents.add(title);
        }
    }

    public static TitleCreator get(Context context)
    {
        ArrayList<String> emptyArray = new ArrayList<String>();
        /*
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context, emptyArray);
        */
        _titleCreator = new TitleCreator(context, emptyArray);
        return _titleCreator;
    }

    public static TitleCreator get(Context context, ArrayList<String> titleArray)
    {
        /*
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context, titleArray);
        */
        _titleCreator = new TitleCreator(context, titleArray);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}