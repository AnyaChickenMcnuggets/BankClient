package com.example.bankclient.util.solution;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.models.UsedBankProduct;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlotGenerator {

    public static String generatePlot(ArrayList<IncomeExpense> ies,
                                    ArrayList<UsedBankProduct> products,
                                    int startSum,
                                    LocalDate dateNow){
        int[] plotStart = new int[365];

        // ies
        // учет доходов и расходов
        for (IncomeExpense ie :
                ies) {
            // кол-во дней до первого платежа
            int days = (int) ChronoUnit.DAYS.between(dateNow,
                                                    LocalDate.of(Integer.parseInt(ie.getDate().split("\\.")[2]),
                                                                Integer.parseInt(ie.getDate().split("\\.")[1]),
                                                                Integer.parseInt(ie.getDate().split("\\.")[0])));
            // получаем доход или расход
            int value = Integer.parseInt(ie.getSum().split("\\.")[0].replaceAll(",", ""));
            value = ie.getIncome() ? value : -value;

            // если без периода, то ставим в эту дату, если с периодом (месяц), то каждый месяц в это число
            if (ie.getLong()){
                int countMonth = 1;
                while (days<365){
                    if (days>=0)
                        plotStart[days] = plotStart[days] + value;
                    days = (int) ChronoUnit.DAYS.between(dateNow,
                            LocalDate.of( Integer.parseInt(ie.getDate().split("\\.")[1]) + countMonth > 12
                                            ? Integer.parseInt(ie.getDate().split("\\.")[2]) + 1
                                            : Integer.parseInt(ie.getDate().split("\\.")[2]),
                                    Integer.parseInt(ie.getDate().split("\\.")[1]) + countMonth > 12
                                            ? Integer.parseInt(ie.getDate().split("\\.")[1]) + countMonth - 12
                                            : Integer.parseInt(ie.getDate().split("\\.")[1]) + countMonth,
                                    Integer.parseInt(ie.getDate().split("\\.")[0])));
                    countMonth +=1;
                }
            } else {
                plotStart[days] = plotStart[days] + value;
            }
        }

        // products
        // учет используемых продуктов
        for (UsedBankProduct ubp :
                products) {
            // Обработка вклада
            if (ubp.getIncome()){
                int incomeTime = Integer.parseInt(ubp.getTime());
                int incomePercentageRate = Integer.parseInt(ubp.getPercentage().replaceAll("%", ""));
                int incomeSum = Integer.parseInt(ubp.getSum());
                int incomePercentage = incomeSum * incomePercentageRate/100/365 * incomeTime;
                // начало вклада
                int days = (int) ChronoUnit.DAYS.between(dateNow,
                        LocalDate.of(Integer.parseInt(ubp.getStartDate().split("\\.")[2]),
                                Integer.parseInt(ubp.getStartDate().split("\\.")[1]),
                                Integer.parseInt(ubp.getStartDate().split("\\.")[0])));
                if (days >=0 && days<365){
                    plotStart[days] = plotStart[days] - incomeSum;
                }
                // конец вклада
                days = days + incomeTime;
                if (days >=0 && days<365){
                    plotStart[days] = plotStart[days] + incomeSum + incomePercentage;
                }
            } else {
                // TODO рассчет графика выплат по кредиту и применение в определенные дни периода
            }
        }
        StringBuilder writeToDB = new StringBuilder();
        int a = startSum;
        for(int i=0; i<365;i++){
            a = plotStart[i] + a;
            writeToDB.append(a);
            writeToDB.append("|");
        }
        return writeToDB.toString();
    }

    public static LineGraphSeries<DataPoint> getSeriesFromPlot(String plot,String dateNow){
        // нарастающий итог
        DataPoint[] datePoints = new DataPoint[365];
        String[] plotArr = plot.split("\\|");
        for(int i=0; i<365;i++) {
            Date date = Date.from(LocalDate.of(Integer.parseInt(dateNow.split("-")[0]),
                    Integer.parseInt(dateNow.split("-")[1]),
                    Integer.parseInt(dateNow.split("-")[2])).plusDays(1+i).atStartOfDay().atZone(ZoneId.of("Europe/Moscow")).toInstant());
            datePoints[i] = new DataPoint(date, Integer.parseInt(plotArr[i]));
        }
        return new LineGraphSeries<>(datePoints);
    }
    public static int[][] createMatrix(ArrayList<UsedBankProduct> products,
                                       ArrayList<IncomeExpense> ies,
                                       int startSum,
                                       LocalDate dateNow){
        int[][] matrix;
        int n,m;

        String plotString = generatePlot(ies, products, startSum, dateNow);
        n = (int) products.stream().filter(g -> g.getIncome()).count() * 12 + 24 * (int) products.stream().filter(g -> !g.getIncome()).count() + 1;
        m =  products.stream().anyMatch(g -> !g.getIncome()) ? 13 + (int) products.stream().filter(g -> !g.getIncome()).count() * 23 : 13 ;

        matrix = new int[n][m];
        for(int i =0; i<n;i++){
            for(int j =0; j<m;j++){
                matrix[i][j] = 0;
            }
        }
        for (UsedBankProduct product : products) {
            if (product.getIncome()){

            }else {

            }
        }

        int incomeExpenseSum = startSum;

        for (IncomeExpense incomeExpense : ies) {
            if (incomeExpense.getLong()){
                int value = Integer.parseInt(incomeExpense.getSum().split("\\.")[0].replaceAll(",", ""));
                value =  incomeExpense.getIncome() ? value : -value;
                incomeExpenseSum += value;
            }
        }

        // первые 12 по месяцам 100%
        for (int i = 0; i < 12; i++){
            int a = i;
            matrix[0][i] = incomeExpenseSum * (i+1) + ies.stream().filter(g -> !g.getLong() && g.getIncome() && Integer.parseInt(g.getDate().split("\\.")[1]) == a).mapToInt(o -> Integer.valueOf(o.getSum())).sum()
                    + ies.stream().filter(g -> !g.getLong() && !g.getIncome() && Integer.parseInt(g.getDate().split("\\.")[1]) == a).mapToInt(o -> Integer.valueOf(o.getSum())).sum();
        }

        return matrix;
    }

    public static ArrayList<IncomeExpense> getIEFromIds(Context context, String[] incomeExpenseIds){
        DatabaseHelper db = new DatabaseHelper(context);
        ArrayList<IncomeExpense> ies = new ArrayList<>();
        for (String id : incomeExpenseIds) {
            Cursor cursor = db.readIEById(id);
            if (cursor.getCount()==0){
                Toast.makeText(context, "ОШИБКА НЕТ ТАКОГО ID", Toast.LENGTH_SHORT).show();
            }else {
                while (cursor.moveToNext()){
                    ies.add(new IncomeExpense(
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
        return ies;
    }
}
