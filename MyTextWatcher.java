package com.carsonskjerdal.app.scorekeeperplus.MainPage;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.carsonskjerdal.app.scorekeeperplus.MainPage.AddPlayerInterface;

/**
 * Created by Carson on 2018-01-26.
 * This was a great customer listener for editing a textfield. This allowed me to create more textfields
 * in a recycler view based on if there is any text in the edit text. For example: if begin writing a name, it creates
 * a new text field below or deletes if the text is removed. This piece was used in a scorekeeper app I developed.
 * Hopefully one day will apply it as an independent library.
 */

public class MyTextWatcher implements TextWatcher {
    private EditText editText;
    private AddPlayerInterface listener;
    private int size;
    private  int position;


    public MyTextWatcher(EditText editText, AddPlayerInterface pInterface, int listSize, int position) {
        this.editText = editText;
        this.listener = pInterface;
        this.size = listSize;
        this.position = this.position;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //unused method
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //unused method
    }

    @Override
    public void afterTextChanged(Editable s) {
        //listen and modify based on changes to textfield
        String name = s.toString();

        if (editText.getTag() != null) {
            int position = (int) editText.getTag();
            size = listener.getSize();

            //size is declared above
            if (name.length() > 0) {
                //if editing last position
                if (position + 1 == size) {
                    //add an item to the adapter list
                    size += 1;
                    listener.addPlayer();
                    listener.listListener();
                }
                if (position + 1 < size) { 
                    //update string name
                    listener.editPlayer(name, position);
                }
            } else {
                //check if the current position is proven to be one away from the last spot
                if (position + 2 == size) {
                    //if last item is null then delete it
                    listener.deletePlayer();
                    listener.listListener();
                    size -= 1;
                } else {
                    //In the situation of deleting a player higher up this will set it to be blank otherwise it won't update to ""
                    listener.editPlayer(name, position);
                }

            }

        }

    }
}
