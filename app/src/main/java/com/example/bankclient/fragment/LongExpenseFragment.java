package com.example.bankclient.fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.adapter.IEAdapter;
import com.example.bankclient.database.DatabaseHelper;
import com.example.bankclient.interface_helper.RecyclerViewInterface;
import com.example.bankclient.model.IncomeExpense;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LongExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LongExpenseFragment extends Fragment implements RecyclerViewInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView rv;
    DatabaseHelper db;
    ArrayList<IncomeExpense> longExpenseList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LongExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LongExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LongExpenseFragment newInstance(String param1, String param2) {
        LongExpenseFragment fragment = new LongExpenseFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.longExpenseList);
        db = new DatabaseHelper(rv.getContext());
        longExpenseList = new ArrayList<>();
        storeDataInArray();
        IEAdapter adapter = new IEAdapter(rv.getContext(), longExpenseList, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_long_expense, container, false);
    }
    void storeDataInArray(){
        Cursor cursor = db.readAllLongExpense();
        if (cursor.getCount()==0){
            Toast.makeText(rv.getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                longExpenseList.add(new IncomeExpense(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        Boolean.valueOf(cursor.getString(4)),
                        Boolean.valueOf(cursor.getString(5))));
            }

        }
    }

    @Override
    public void onItemClick(int position) {

    }
}