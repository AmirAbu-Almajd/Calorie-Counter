package com.example.calorie_counter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static UserLists userLists;
    static ArrayAdapter<String> listAdapter;
    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(String param1, String param2) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_shopping_list, container, false);
        userLists = new UserLists();
        ListView shoppingLists = (ListView)rootView.findViewById(R.id.groceryListView);
        listAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1);
        shoppingLists.setAdapter(listAdapter);
        fillMyLists();
        FloatingActionButton newListBtn = (FloatingActionButton)rootView.findViewById(R.id.newListBtn);
        newListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(rootView.getContext(),ShoppingItems.class);
                startActivity(i);
            }
        });

        return rootView;
    }
    public void fillMyLists(){
        for(int i =0;i<userLists.lists.size();i++){
            GroceryList list = userLists.lists.get(i);
            for(int j = 0 ; j < list.items.size();i++){
                if(list.quantities.get(j)!=0)
                    listAdapter.add(list.items.get(j) + " " + list.quantities.get(j).toString() + " KGs\n");
            }
        }
        listAdapter.notifyDataSetChanged();
    }
}