package com.example.bankclient.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.activity.BankProductListActivity;
import com.example.bankclient.activity.EditIEActivity;
import com.example.bankclient.activity.MainActivity;
import com.example.bankclient.adapter.IEAdapter;
import com.example.bankclient.database.DatabaseHelper;
import com.example.bankclient.interface_helper.RecyclerViewInterface;
import com.example.bankclient.model.IncomeExpense;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShortExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShortExpenseFragment extends Fragment implements RecyclerViewInterface {
    RecyclerView rv;
    DatabaseHelper db;
    ArrayList<IncomeExpense> shortExpenseList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShortExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShortExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShortExpenseFragment newInstance(String param1, String param2) {
        ShortExpenseFragment fragment = new ShortExpenseFragment();
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
        return inflater.inflate(R.layout.fragment_short_expense, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.shortExpenseList);
        db = new DatabaseHelper(rv.getContext());
        shortExpenseList = new ArrayList<>();
        storeDataInArray();
        IEAdapter adapter = new IEAdapter(rv.getContext(), shortExpenseList, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
    }

    void storeDataInArray(){
        Cursor cursor = db.readAllShortExpense();
        if (cursor.getCount()==0){
            Toast.makeText(rv.getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                shortExpenseList.add(new IncomeExpense(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Boolean.valueOf(cursor.getString(4)),
                        Boolean.valueOf(cursor.getString(5)),
                        cursor.getString(6)));
            }

        }
    }

    @Override
    public void onItemClick(int position) {
        showPopUp(rv.getContext(), shortExpenseList.get(position));
    }
    private void showPopUp(Context context, IncomeExpense ie) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_dialog_ie);

        LinearLayout editLayout = dialog.findViewById(R.id.layoutEdit);
        LinearLayout deleteLayout = dialog.findViewById(R.id.layoutDelete);

        editLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditIEActivity.class);
            intent.putExtra("id", ie.getId());
            intent.putExtra("title", ie.getTitle());
            intent.putExtra("long", ie.getLong());
            intent.putExtra("income", ie.getIncome());
            intent.putExtra("date", ie.getDate());
            intent.putExtra("period", ie.getPeriod());
            intent.putExtra("sum", ie.getSum());
            startActivity(intent);
        });


        deleteLayout.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(dialog.getContext());
            dialog.dismiss();
            db.deleteIE(ie.getId());
            getActivity().recreate();
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}