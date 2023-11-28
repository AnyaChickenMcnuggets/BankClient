package com.example.bankclient.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bankclient.R;
import com.example.bankclient.ui.recycler_view.CheckIEAdapter;
import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.util.interface_helper.ItemClickListener;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.viewmodel.CheckIEViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckLongIncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CheckLongIncomeFragment extends Fragment implements ItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView rv;
    DatabaseHelper db;
    ArrayList<IncomeExpense> longIncomeList;
    CheckIEViewModel viewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckLongIncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LongIncome.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckLongIncomeFragment newInstance(String param1, String param2) {
        CheckLongIncomeFragment fragment = new CheckLongIncomeFragment();
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

        return inflater.inflate(R.layout.fragment_check_long_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CheckIEViewModel.class);

        rv = view.findViewById(R.id.longIncomeList);
        db = new DatabaseHelper(rv.getContext());
        longIncomeList = new ArrayList<>();
        storeDataInArray();
        CheckIEAdapter adapter = new CheckIEAdapter(rv.getContext(), longIncomeList, viewModel, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
    }

    void storeDataInArray(){
        Cursor cursor = db.readAllLongIncome();
        if (cursor.getCount()==0){
            Toast.makeText(rv.getContext(), "No Data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                longIncomeList.add(new IncomeExpense(
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
    public void onItemClick(ArrayList<IncomeExpense> al) {
        this.viewModel.setData(al);
    }
}