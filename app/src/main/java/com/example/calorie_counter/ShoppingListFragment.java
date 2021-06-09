package com.example.calorie_counter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    //    public static List<GroceryList> lists = new LinkedList<>();
    int id;
    ArrayList<String> GroceryLists;
    ArrayList<Integer> listsIds;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        id = userSingleton.getId();
        fillMyLists(rootView);
        for (int i = 0; i < GroceryLists.size(); i++) {
            addNewList(rootView, GroceryLists.get(i), i);
        }
        FloatingActionButton newListBtn = (FloatingActionButton) rootView.findViewById(R.id.newListBtn);
        newListBtn.setImageTintList(ColorStateList.valueOf(0xffffffff));
        newListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(rootView.getContext(), ShoppingItems.class);
                i.putExtra("user_id", id);
                startActivity(i);
            }
        });

        return rootView;
    }

    public void fillMyLists(View rootView) {
        listsIds = new ArrayList<>();
        GroceryLists = new ArrayList<>();
        Database db = new Database(this.getContext());
        List<Pair<Integer, Cursor>> userLists = db.get_user_lists(id);
        for (int i = 0; i < userLists.size(); i++) {
            String res = "";
            Cursor list = userLists.get(i).second;
            listsIds.add(userLists.get(i).first);
            list.moveToFirst();
            while (!list.isAfterLast()) {
                res += list.getString(0) + " ";
                res += list.getDouble(1) + " KGs";
                if (!list.isLast()) {
                    res += "\n";
                }

                list.moveToNext();
            }
            GroceryLists.add(res);
//            listAdapter.add(res);
//            listAdapter.add(addNewList(rootView,res,i));
        }
//        listAdapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addNewList(View rootView, String text, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(10, 10, 10, 10);
        LinearLayout.LayoutParams paramsBtn = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paramsBtn.gravity = Gravity.CENTER_VERTICAL;
        paramsBtn.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        paramsBtn.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        LinearLayout listsLayout = rootView.findViewById(R.id.listsLayout);
        LinearLayout LL = new LinearLayout(rootView.getContext());
//        FloatingActionButton FAB = new FloatingActionButton(rootView.getContext());
        FloatingActionButton FAB =(FloatingActionButton) LayoutInflater.from(rootView.getContext()).inflate(R.layout.typical_button,null);
        TextView listItems = new TextView(rootView.getContext());
        listItems.setId(index + 3000);
        listItems.setWidth(900);
        LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LL.setOrientation(LinearLayout.HORIZONTAL);
        FAB.setId(index);
//        FAB.setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.mipmap.can_icon_foreground));
        FAB.setBackgroundTintList(ColorStateList.valueOf(0xffff0000));
        FAB.setImageTintList(ColorStateList.valueOf(0xffffffff));
        FAB.setRippleColor(ColorStateList.valueOf(0xffffffff));
        FAB.setLayoutParams(paramsBtn);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempBtnId = v.getId();
                int tempTextId = tempBtnId + 3000;
                TextView tempText2 = (TextView) rootView.findViewById(tempTextId);
                listsLayout.removeViewAt(tempBtnId);
                Database db = new Database(rootView.getContext());
                db.delete_list(id, listsIds.get(tempBtnId));
                listsIds.remove(tempBtnId);
                for (int h = tempBtnId; h < listsLayout.getChildCount(); h++) {
                    FloatingActionButton FABTemp = (FloatingActionButton) rootView.findViewById(h + 1);
                    TextView tempText = (TextView) rootView.findViewById(h + 3000 + 1);
                    FABTemp.setId(h);
                    tempText.setId(h + 3000);
                }
            }
        });
        FAB.setEnabled(true);
        listItems.setText(text);
        LL.addView(listItems);
        LL.addView(FAB);
        LL.setLayoutParams(params);
        listsLayout.addView(LL);
    }
}