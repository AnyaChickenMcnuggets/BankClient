package com.example.bankclient.util.solution;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.example.bankclient.repository.DatabaseHelper;
import com.example.bankclient.ui.models.BankProduct;
import com.example.bankclient.ui.models.IncomeExpense;
import com.example.bankclient.ui.models.UsedBankProduct;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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
                    Integer.parseInt(dateNow.split("-")[2])).plusDays(i).atStartOfDay().atZone(ZoneId.of("Europe/Moscow")).toInstant());
            datePoints[i] = new DataPoint(date, Integer.parseInt(plotArr[i]));
        }
        return new LineGraphSeries<>(datePoints);
    }
    public static Double[][] createMatrix(String plot,
                                       LocalDate dateNow){
        Double[][] matrix;
        int n,m;

        // 2 типа вклада и 2 типа кредитных линий
        int k = 4;
        BankProduct[] products = new BankProduct[k];
        products[0] = new BankProduct("0", "Вклад 30 дней", "30", "13%", true, "", "");
        products[1] = new BankProduct("1", "Вклад 180 дней", "180", "14%", true, "", "");
        products[2] = new BankProduct("2", "Кредитная линия 25", "0", "25%", false, "35000", "7%");
        products[3] = new BankProduct("2", "Кредитная линия 27", "0", "27%", false, "50000", "5%");
        // TODO хардкод банковских продуктов для тестов и минимального функционала
        BankProduct[] vs = Arrays.stream(products).filter(g -> g.getIncome()).toArray(BankProduct[]::new);
        BankProduct[] cs = Arrays.stream(products).filter(g -> !g.getIncome()).toArray(BankProduct[]::new);
        int v = vs.length; // вклады
        int c = cs.length; // кредиты
        // m = типы вкладов * 12 + типы кредитных линий * 24 + 1
        m=v*12+c*24+1;
        // n = 13 (ограничения по месяцам и целевая) + кол-во кредитных линий на 23 + кол-во кредитных линий на 12
        n=13 + c*23 + 12*c;


        matrix = new Double[n][m];
        for(int i =0; i<n;i++){
            for(int j =0; j<m;j++){
                matrix[i][j] = 0.0;
            }
        }
        String[] plotArray = plot.split("\\|");
        // вектор B для ограничений по месяцам
        for (int i =0; i<12;i++){
            int days = (int) ChronoUnit.DAYS.between(dateNow, dateNow.plusMonths(i));
            matrix[i][0] = Double.parseDouble(plotArray[days]);
        }

        // Целевая функция
        // для каждого месяца
        for (int i = 1; i<=12; i++){
            for (int j = 0; j<v; j++){
                int s = Integer.parseInt(vs[j].getTime())/30;
                // вклады в целевой функции ВРЕМЯ КРАТНО 30
                if (i + s<=13){
                    matrix[n-1][j*12+i] = -(Double.parseDouble(vs[j].getTime()) * Double.parseDouble(vs[j].getPercentage().replaceAll("%", ""))/100/365);
                }

                // ограничение трат по месяцам
                matrix[i-1][j*12+i] += 1.0;

                // условие что потраченные деньги вернутся в карман после истечения срока вклада
                if (i!=1){
                    for (int f = 1; f<i; f++){
                        if (i-s!=f) {
                            matrix[i-1][j*12+f] += 1.0;
                        }
                    }
                }
            }
        }
        // для каждого месяца
        for (int i = 1; i<=12; i++){
            int credit_index = v*12;
            for (int j = 0; j<c; j++){
                Double multi = (13-i) * Double.parseDouble(cs[j].getPercentage().replaceAll("%", ""))/100/365*30;
                // кредитные линии
                matrix[n-1][credit_index+j*12+i] = multi;
                matrix[n-1][credit_index+12+j*12+i] = -multi;

                // ограничение на разовое снятие: B и множитель
                matrix[11+12*j+i][0] = Double.parseDouble(cs[j].getCreditLimit());
                matrix[11+12*j+i][credit_index+j*12+i] = 1.0;

                // ограничение на сумму задолженности
                matrix[11 + 12*c +12*j+i][0] = Double.parseDouble(cs[j].getCreditLimit());
                for (int f = 1; f<=i; f++){
                    matrix[23+12*j+i][credit_index+j*12+f] = 1.0;
                    matrix[23+12*j+i][credit_index+12+j*12+f] = -1.0;

                    // ограничение трат по месяцам
                    if (f==i){
                        matrix[i-1][credit_index+j*12+f] -= 1.0;
                        matrix[i-1][credit_index+12+j*12+f] += 1.0;
                    }else {
                        matrix[i-1][credit_index+j*12+f] += 1.0;
                        matrix[i-1][credit_index+12+j*12+f] -= 1.0;
                    }

                }

                // ограничение на минимальный платеж
                Double minPercentagePay = Double.parseDouble(cs[j].getMinPercentagePay().replaceAll("%", ""));
                if (i!=1){
                    for (int f = 1; f<=i; f++){
                        if (f!=i) {
                            matrix[11 + 24*c + 11 * j + i-1][credit_index + j * 12 + f] = minPercentagePay;
                            matrix[11 + 24*c + 11 * j + i-1][credit_index + 12 + j * 12 + f] = -minPercentagePay;
                        } else{
                            matrix[11 + 24*c + 11 * j + i-1][credit_index+12+j*12+f] = -1.0;
                        }

                    }
                }
                credit_index +=12;
            }
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
