package com.example.codisa_app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class test_selec_multiple extends AppCompatActivity {
         AlertDialog mDialog = null;
        /**
         * This becomes false when "Select All" is selected while deselecting some other item on list.
         */
        boolean selectAll = true;
        /**
         * Number of items in array list and eventually in ListView
         */
        int length;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_test_selec_multiple);
            length = getResources().getStringArray(R.array.items).length;
        }
        public void onDialog (View v) {
            mDialog = onCreateDialog(null);
            mDialog.show();
            // we get the ListView from already shown dialog
            final ListView listView = mDialog.getListView();
            // ListView Item Click Listener that enables "Select all" choice
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean isChecked = listView.isItemChecked(position);
                    if (position == 0) {
                        if(selectAll) {
                            for (int i = 1; i < length; i++) { // we start with first element after "Select all" choice
                                if (isChecked && !listView.isItemChecked(i)  || !isChecked && listView.isItemChecked(i))
                                {
                                    listView.performItemClick(listView, i, 0);
                                }
                            }
                        }
                    } else {
                        if (!isChecked && listView.isItemChecked(0)) {
                            // if other item is unselected while "Select all" is selected, unselect "Select all"
                            // false, performItemClick, true is a must in order for this code to work
                            selectAll = false;
                            listView.performItemClick(listView, 0, 0);
                            selectAll = true;
                        }
                    }
                }
            });
        }
        public AlertDialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(com.example.codisa_app.test_selec_multiple.this);
            // Set the dialog title
            builder.setTitle("SELECCION DE PRODUCTOS")
                    // Specify the list array, the items to be selected by default (null for none),
                    // and the listener is null in order to "Select all" choice to work
                    .setMultiChoiceItems(R.array.items, null, null)
                    // Set the action buttons
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save something here
                        }
                    })
                    .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {}
                    });
            return builder.create();
        }
    }